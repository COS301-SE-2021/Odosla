package cs.superleague.delivery;

import cs.superleague.delivery.dataclass.Delivery;
import cs.superleague.delivery.dataclass.DeliveryDetail;
import cs.superleague.delivery.dataclass.DeliveryStatus;
import cs.superleague.delivery.exceptions.DeliveryDoesNotExistException;
import cs.superleague.delivery.exceptions.DeliveryException;
import cs.superleague.delivery.exceptions.InvalidRequestException;
import cs.superleague.delivery.repos.DeliveryDetailRepo;
import cs.superleague.delivery.repos.DeliveryRepo;
import cs.superleague.delivery.requests.*;
import cs.superleague.delivery.responses.*;
import cs.superleague.delivery.stubs.payment.dataclass.GeoPoint;
import cs.superleague.delivery.stubs.payment.dataclass.Order;
import cs.superleague.delivery.stubs.payment.dataclass.OrderStatus;
import cs.superleague.delivery.stubs.payment.responses.GetOrderResponse;
import cs.superleague.delivery.stubs.shopping.responses.GetStoreByUUIDResponse;
import cs.superleague.delivery.stubs.user.requests.SaveDriverToRepoRequest;
import cs.superleague.delivery.stubs.payment.requests.SaveOrderToRepoRequest;
import cs.superleague.delivery.stubs.shopping.dataclass.Store;
import cs.superleague.delivery.stubs.user.dataclass.*;
import cs.superleague.delivery.stubs.user.responses.GetAdminByEmailResponse;
import cs.superleague.delivery.stubs.user.responses.GetCustomerByUUIDResponse;
import cs.superleague.delivery.stubs.user.responses.GetDriverByEmailResponse;
import cs.superleague.delivery.stubs.user.responses.GetDriverByUUIDResponse;
import cs.superleague.integration.security.CurrentUser;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service("deliveryServiceImpl")
public class DeliveryServiceImpl implements DeliveryService {
    private final DeliveryRepo deliveryRepo;
    private final DeliveryDetailRepo deliveryDetailRepo;
    private final RabbitTemplate rabbitTemplate;
    private final RestTemplate restTemplate;

    @Autowired
    public DeliveryServiceImpl(DeliveryRepo deliveryRepo, DeliveryDetailRepo deliveryDetailRepo,
                               RabbitTemplate rabbitTemplate, RestTemplate restTemplate) {
        this.deliveryRepo = deliveryRepo;
        this.deliveryDetailRepo = deliveryDetailRepo;
        this.rabbitTemplate = rabbitTemplate;
        this.restTemplate = restTemplate;
    }

    @Override
    public AddDeliveryDetailResponse addDeliveryDetail(AddDeliveryDetailRequest request) throws InvalidRequestException {
        if(request == null){
            throw new InvalidRequestException("Null request object.");
        }
        if (request.getDeliveryID() == null || request.getDetail() == null || request.getTimestamp() == null || request.getStatus() == null){
            throw new InvalidRequestException("Null parameters.");
        }
        deliveryRepo.findById(request.getDeliveryID()).orElseThrow(()->new InvalidRequestException("Delivery does not exist in database."));
        DeliveryDetail detail = new DeliveryDetail(request.getDeliveryID(), request.getTimestamp(), request.getStatus(), request.getDetail());
        deliveryDetailRepo.save(detail);
        return new AddDeliveryDetailResponse("Delivery details added successfully.", detail.getId());
    }

    @Override
    public AssignDriverToDeliveryResponse assignDriverToDelivery(AssignDriverToDeliveryRequest request) throws InvalidRequestException{
        if (request == null){
            throw new InvalidRequestException("Null request object.");
        }
        if (request.getDeliveryID() == null){
            throw new InvalidRequestException("Null parameters.");
        }
        Delivery delivery = deliveryRepo.findById(request.getDeliveryID()).orElseThrow(()-> new InvalidRequestException("Delivery does not exist in the database."));

        CurrentUser currentUser = new CurrentUser();

        String uri = "http://localhost:8089/user/getDriverByEmail";
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        converters.add(new MappingJackson2HttpMessageConverter());

        restTemplate.setMessageConverters(converters);

        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
        parts.add("email", currentUser.getEmail());


        ResponseEntity<GetDriverByEmailResponse> responseEntity = restTemplate.postForEntity(uri,
                parts, GetDriverByEmailResponse.class);

        if(responseEntity == null || !responseEntity.hasBody()
        || responseEntity.getBody() == null || responseEntity.getBody().getDriver() == null){
            throw new InvalidRequestException("Driver Repository could not be found.");
        }

        Driver driver = responseEntity.getBody().getDriver();

        if (delivery.getDriverId() != null){
            if (delivery.getDriverId().compareTo(driver.getDriverID()) == 0){
                if (delivery.getPickUpLocation() != null && delivery.getDropOffLocation() != null){
                    return new AssignDriverToDeliveryResponse(true, "Driver was already assigned to delivery.", delivery.getPickUpLocation(), delivery.getDropOffLocation(), driver.getDriverID());
                } else{
                    throw new InvalidRequestException("No pick up or drop off location specified with delivery.");
                }
            }
            throw new InvalidRequestException("This delivery has already been taken by another driver.");
        }

        uri = "http://localhost:8086/payment/getOrder";

        MultiValueMap<String, Object> orderRequest = new LinkedMultiValueMap<>();
        orderRequest.add("orderId", delivery.getOrderID());


        ResponseEntity<GetOrderResponse> responseEntityOrder = restTemplate.postForEntity(uri,
                orderRequest, GetOrderResponse.class);

        if(responseEntityOrder == null || !responseEntityOrder.hasBody()
            || responseEntityOrder.getBody() == null){
            throw new InvalidRequestException("Invalid order.");
        }


        Order updateOrder= responseEntityOrder.getBody().getOrder();

        if(updateOrder!=null){
            if (delivery.getPickUpLocation() != null && delivery.getDropOffLocation() != null){
                updateOrder.setDriverID(driver.getDriverID());

                SaveOrderToRepoRequest saveOrderToRepoRequest = new SaveOrderToRepoRequest(updateOrder);

                rabbitTemplate.convertAndSend("PaymentEXCHANGE", "RK_SaveOrderToRepo", saveOrderToRepoRequest);

                //updateOrder.setStatus(OrderStatus.ASSIGNED_DRIVER);

                orderRequest.add("orderId", delivery.getOrderID());

                responseEntityOrder = restTemplate.postForEntity(uri,
                        orderRequest, GetOrderResponse.class);

                if(responseEntityOrder == null || !responseEntityOrder.hasBody()
                        || responseEntityOrder.getBody() == null){
                    throw new InvalidRequestException("Invalid order.");
                }

                Order updateOrder2= responseEntityOrder.getBody().getOrder();
                updateOrder2.setStatus(OrderStatus.ASSIGNED_DRIVER);

                saveOrderToRepoRequest = new SaveOrderToRepoRequest(updateOrder2);

                rabbitTemplate.convertAndSend("PaymentEXCHANGE", "RK_SaveOrderToRepo", saveOrderToRepoRequest);

                delivery.setDriverId(driver.getDriverID());
                deliveryRepo.save(delivery);
                return new AssignDriverToDeliveryResponse(true, "Driver successfully assigned to delivery.", delivery.getPickUpLocation(), delivery.getDropOffLocation(), driver.getDriverID());
            } else{
                throw new InvalidRequestException("No pick up or drop off location specified with delivery.");
            }
        }
        else
        {
            throw new InvalidRequestException("Invalid order.");
        }
    }

    @Override
    public CreateDeliveryResponse createDelivery(CreateDeliveryRequest request) throws InvalidRequestException {
        if (request == null){
            throw new InvalidRequestException("Null request object.");
        }
        if(request.getCustomerID() == null || request.getOrderID() == null || request.getStoreID() == null || request.getPlaceOfDelivery() == null){
            throw new InvalidRequestException("Missing parameters.");
        }

        String uri = "http://localhost:8089/user/getCustomerByUUID";
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        converters.add(new MappingJackson2HttpMessageConverter());

        restTemplate.setMessageConverters(converters);

        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
        parts.add("userID", request.getCustomerID());

        ResponseEntity<GetCustomerByUUIDResponse> responseEntity = restTemplate.postForEntity(uri,
                parts, GetCustomerByUUIDResponse.class);

        if(responseEntity == null || !responseEntity.hasBody()
        || responseEntity.getBody() == null){
            throw new InvalidRequestException("Invalid customerID.");
        }

        uri = "http://localhost:8086/payment/getOrder";

        MultiValueMap<String, Object> orderRequest = new LinkedMultiValueMap<>();
        orderRequest.add("orderId", request.getOrderID());


        ResponseEntity<GetOrderResponse> responseEntityOrder = restTemplate.postForEntity(uri,
                orderRequest, GetOrderResponse.class);

        if(responseEntityOrder == null || !responseEntityOrder.hasBody()
                || responseEntityOrder.getBody() == null){
            throw new InvalidRequestException("Invalid orderID.");
        }
        uri = "http://localhost:8088/shopping/getStoreByUUID";

        MultiValueMap<String, Object> storeRequest = new LinkedMultiValueMap<>();
        orderRequest.add("storeId", request.getStoreID());


        ResponseEntity<GetStoreByUUIDResponse> responseEntityStore = restTemplate.postForEntity(uri,
                storeRequest, GetStoreByUUIDResponse.class);

        if(responseEntityStore == null || !responseEntityStore.hasBody()
        || responseEntityStore.getBody() == null || responseEntityStore.getBody().getStore() == null){
            throw new InvalidRequestException("Invalid storeID.");
        }

        Store store = responseEntityStore.getBody().getStore();

        GeoPoint locationOfStore = store.getStoreLocation();
        if (locationOfStore == null){
            throw new InvalidRequestException("Store has no location set.");
        }
        //Adding delivery to database
        UUID deliveryID = UUID.randomUUID();
        while(deliveryRepo.findById(deliveryID).isPresent()){
            deliveryID = UUID.randomUUID();
        }
        boolean longAndLatCheck = checkLongAndLatIfValid(locationOfStore, request.getPlaceOfDelivery());
        if(!longAndLatCheck){
            throw new InvalidRequestException("Invalid geoPoints.");
        }
        GetDeliveryCostRequest getDeliveryCostRequest = new GetDeliveryCostRequest(locationOfStore, request.getPlaceOfDelivery());
        GetDeliveryCostResponse getDeliveryCostResponse = getDeliveryCost(getDeliveryCostRequest);
        Delivery delivery = new Delivery(deliveryID, request.getOrderID(), locationOfStore, request.getPlaceOfDelivery(), request.getCustomerID(), request.getStoreID(), DeliveryStatus.WaitingForShoppers, getDeliveryCostResponse.getCost());
        deliveryRepo.save(delivery);
        return new CreateDeliveryResponse(true, "Delivery request placed.", deliveryID);
    }

    @Override
    public GetDeliveriesResponse getDeliveries(GetDeliveriesRequest request) throws InvalidRequestException {
        if(request == null){
            throw new InvalidRequestException("Null request.");
        }
        return null;
    }

    @Override
    public GetDeliveryCostResponse getDeliveryCost(GetDeliveryCostRequest request) throws InvalidRequestException {
        if(request == null){
            throw new InvalidRequestException("Null request object.");
        }
        if(request.getDropOffLocation() == null || request.getPickUpLocation() == null){
            throw new InvalidRequestException("Null parameters.");
        }
        boolean validGeoPoints = checkLongAndLatIfValid(request.getDropOffLocation(), request.getPickUpLocation());
        if(!validGeoPoints){
            throw new InvalidRequestException("Invalid Co-ordinates.");
        }
        double distance = getDistanceBetweenTwoPoints(request.getDropOffLocation(), request.getPickUpLocation());
        double cost;
        if (distance < 20){
            //price of less than 20km drive
            cost = 20.0;
        }else if (distance < 40){
            cost = 35.0;
        }else{
            cost = 50.0;
        }
        return new GetDeliveryCostResponse(cost);
    }

    @Override
    public GetDeliveryDetailResponse getDeliveryDetail(GetDeliveryDetailRequest request) throws InvalidRequestException {
        if(request == null){
            throw new InvalidRequestException("Null request object.");
        }
        if (request.getDeliveryID() == null){
            throw new InvalidRequestException("Null parameters.");
        }
        CurrentUser currentUser = new CurrentUser();

        String uri = "http://localhost:8089/user/getAdminByEmail";
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        converters.add(new MappingJackson2HttpMessageConverter());

        restTemplate.setMessageConverters(converters);

        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
        parts.add("email", currentUser.getEmail());


        ResponseEntity<GetAdminByEmailResponse> responseEntity = restTemplate.postForEntity(uri,
                parts, GetAdminByEmailResponse.class);

        if(responseEntity == null || !responseEntity.hasBody()
                || responseEntity.getBody() == null || responseEntity.getBody().getAdmin() == null){
            throw new InvalidRequestException("User is not an admin.");
        }

        List<DeliveryDetail> details = deliveryDetailRepo.findAllByDeliveryID(request.getDeliveryID());
        if (details == null){
            throw new InvalidRequestException("No details found for this delivery.");
        }
        if (details.size() > 0){
            return new GetDeliveryDetailResponse("Details successfully found.",details);
        }else{
            throw new InvalidRequestException("No details found for this delivery.");
        }
    }

    @Override
    public GetDeliveryStatusResponse getDeliveryStatus(GetDeliveryStatusRequest request) throws InvalidRequestException {
        if(request == null){
            throw new InvalidRequestException("Null request object.");
        }
        if(request.getDeliveryID() == null){
            throw new InvalidRequestException("No delivery Id specified.");
        }
        Delivery delivery = deliveryRepo.findById(request.getDeliveryID()).orElseThrow(()->new InvalidRequestException("Delivery not found in database."));
        return new GetDeliveryStatusResponse(delivery.getStatus(), "Delivery Found.");
    }

    @Override
    public GetNextOrderForDriverResponse getNextOrderForDriver(GetNextOrderForDriverRequest request) throws InvalidRequestException {
        if (request==null){
            throw new InvalidRequestException("Null request object.");
        }
        if(request.getCurrentLocation() == null){
            throw new InvalidRequestException("Null parameters.");
        }
        double range = request.getRangeOfDelivery();
        CurrentUser currentUser = new CurrentUser();

        String uri = "http://localhost:8089/user/getDriverByEmail";
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        converters.add(new MappingJackson2HttpMessageConverter());

        restTemplate.setMessageConverters(converters);

        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
        parts.add("email", currentUser.getEmail());


        ResponseEntity<GetDriverByEmailResponse> responseEntity = restTemplate.postForEntity(uri,
                parts, GetDriverByEmailResponse.class);

        if(responseEntity == null || !responseEntity.hasBody()
                || responseEntity.getBody() == null || responseEntity.getBody().getDriver() == null){
            throw new InvalidRequestException("Invalid user.");
        }

        Driver driver = responseEntity.getBody().getDriver();

        if(driver!=null)
        {
            driver.setCurrentAddress(request.getCurrentLocation());
            SaveDriverToRepoRequest saveDriverToRepoRequest = new SaveDriverToRepoRequest(driver);

            rabbitTemplate.convertAndSend("userEXCHANGE", "RK_SaveDriverToRepo", saveDriverToRepoRequest);
        }
        else
        {
            throw new InvalidRequestException("Driver not found in database.");
        }

        List<Delivery> deliveries = deliveryRepo.findAllByDriverIdIsNull();
        if (deliveries == null){
            return new GetNextOrderForDriverResponse("No available deliveries in the database.", null);
        }
        Collections.shuffle(deliveries);
        if (deliveries.size()>0){
            for (Delivery d : deliveries){
                double driverDistanceFromStore = getDistanceBetweenTwoPoints(d.getPickUpLocation(), request.getCurrentLocation());
                if (driverDistanceFromStore > range || d.getDriverId()!=null || d.getStatus().compareTo(DeliveryStatus.WaitingForShoppers)!=0){
                    continue;
                }else{
                    GetNextOrderForDriverResponse response = new GetNextOrderForDriverResponse("Driver can take the following delivery.", d);
                    return response;
                }
            }
            GetNextOrderForDriverResponse response = new GetNextOrderForDriverResponse("No available deliveries in the range specified.", null);
            return response;
        }else{
            GetNextOrderForDriverResponse response = new GetNextOrderForDriverResponse("No available deliveries in the database.", null);
            return response;
        }
    }

    @Override
    public RemoveDriverFromDeliveryResponse removeDriverFromDelivery(RemoveDriverFromDeliveryRequest request) throws InvalidRequestException{
        if (request == null){
            throw new InvalidRequestException("Null request object.");
        }
        if (request.getDeliveryID() == null){
            throw new InvalidRequestException("Null parameters.");
        }
        Delivery delivery = deliveryRepo.findById(request.getDeliveryID()).orElseThrow(()->new InvalidRequestException("Delivery not found in database."));
        if (delivery.getDriverId() == null){
            throw new InvalidRequestException("No driver is assigned to this delivery.");
        }
        CurrentUser currentUser = new CurrentUser();

        String uri = "http://localhost:8089/user/getDriverByEmail";
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        converters.add(new MappingJackson2HttpMessageConverter());

        restTemplate.setMessageConverters(converters);

        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();
        parts.add("driverEmail", currentUser.getEmail());


        ResponseEntity<GetDriverByEmailResponse> responseEntity = restTemplate.postForEntity(uri,
                parts, GetDriverByEmailResponse.class);

        if(responseEntity == null || !responseEntity.hasBody()
                || responseEntity.getBody() == null || responseEntity.getBody().getDriver() == null){
            throw new InvalidRequestException("Invalid user.");
        }

        Driver driver = responseEntity.getBody().getDriver();

        if (delivery.getDriverId().compareTo(driver.getDriverID()) != 0){
            throw new InvalidRequestException("Driver was not assigned to delivery.");
        }
        delivery.setDriverId(null);
        deliveryRepo.save(delivery);
        UpdateDeliveryStatusRequest request1 = new UpdateDeliveryStatusRequest(DeliveryStatus.WaitingForShoppers, delivery.getDeliveryID(), "Driver has dropped delivery.");
        updateDeliveryStatus(request1);
        RemoveDriverFromDeliveryResponse response = new RemoveDriverFromDeliveryResponse(true, "Driver successfully removed from delivery.");
        return response;
    }

    @Override
    public TrackDeliveryResponse trackDelivery(TrackDeliveryRequest request) throws InvalidRequestException {
        if(request == null){
            throw new InvalidRequestException("Null request object.");
        }
        if (request.getDeliveryID() == null){
            throw new InvalidRequestException("No delivery Id specified.");
        }
        Delivery delivery = deliveryRepo.findById(request.getDeliveryID()).orElseThrow(()->new InvalidRequestException("Delivery not found in database."));
        if (delivery.getDriverId() == null){
            TrackDeliveryResponse response = new TrackDeliveryResponse(delivery.getPickUpLocation(), "No driver has been assigned to this delivery.");
            return response;
        }

        String uri = "http://localhost:8089/user/getDriverByUUID";
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        converters.add(new MappingJackson2HttpMessageConverter());

        restTemplate.setMessageConverters(converters);

        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();
        parts.add("userID", delivery.getDriverId());


        ResponseEntity<GetDriverByUUIDResponse> responseEntity = restTemplate.postForEntity(uri,
                parts, GetDriverByUUIDResponse.class);

        if(responseEntity == null || !responseEntity.hasBody()
                || responseEntity.getBody() == null){
            throw new InvalidRequestException("Invalid user.");
        }

        Driver driver = responseEntity.getBody().getDriver();

        if (driver == null ||driver.getOnShift() == false){
            delivery.setDriverId(null);
            deliveryRepo.save(delivery);
            TrackDeliveryResponse response = new TrackDeliveryResponse(delivery.getPickUpLocation(), "No driver has been assigned to this delivery.");
            return response;
        }
        TrackDeliveryResponse response = new TrackDeliveryResponse(driver.getCurrentAddress(), "Drivers current location.");
        return response;
    }

    @Override
    public UpdateDeliveryStatusResponse updateDeliveryStatus(UpdateDeliveryStatusRequest request) throws InvalidRequestException{
        if(request == null){
            throw new InvalidRequestException("Null request object.");
        }
        if(request.getDeliveryID() == null || request.getStatus() == null || request.getDetail() == null){
            throw new InvalidRequestException("Null parameters.");
        }

        Delivery delivery = deliveryRepo.findById(request.getDeliveryID()).orElseThrow(()->new InvalidRequestException("Delivery does not exist in database."));
        AddDeliveryDetailRequest requestAdd = new AddDeliveryDetailRequest(request.getStatus(), request.getDetail(), request.getDeliveryID(), Calendar.getInstance());
        addDeliveryDetail(requestAdd);
        delivery.setStatus(request.getStatus());
        deliveryRepo.save(delivery);
        if (request.getStatus() == DeliveryStatus.Delivered){
            delivery = deliveryRepo.findById(request.getDeliveryID()).orElseThrow(()->new InvalidRequestException("Delivery does not exist in database."));
            delivery.setCompleted(true);
            String uri = "http://localhost:8089/user/getDriverByUUID";
            List<HttpMessageConverter<?>> converters = new ArrayList<>();
            converters.add(new MappingJackson2HttpMessageConverter());

            restTemplate.setMessageConverters(converters);

            MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();
            parts.add("userID", delivery.getDeliveryID());


            ResponseEntity<GetDriverByUUIDResponse> responseEntity = restTemplate.postForEntity(uri,
                    parts, GetDriverByUUIDResponse.class);

            if(responseEntity == null || !responseEntity.hasBody()
                    || responseEntity.getBody() == null){
                throw new InvalidRequestException("Invalid user.");
            }

            Driver driver = responseEntity.getBody().getDriver();

            if(driver!=null)
            {
                driver.setDeliveriesCompleted(driver.getDeliveriesCompleted()+1);
                SaveDriverToRepoRequest saveDriverToRepoRequest = new SaveDriverToRepoRequest(driver);
                rabbitTemplate.convertAndSend("userEXCHANGE", "RK_SaveDriverToRepo", saveDriverToRepoRequest);
            }
            deliveryRepo.save(delivery);

            uri = "http://localhost:8086/payment/getOrder";

            MultiValueMap<String, Object> orderRequest = new LinkedMultiValueMap<String, Object>();
            orderRequest.add("orderId", delivery.getOrderID());

            ResponseEntity<GetOrderResponse> responseEntityOrder = restTemplate.postForEntity(uri,
                    orderRequest, GetOrderResponse.class);

            if(responseEntityOrder == null || !responseEntityOrder.hasBody()
                    || responseEntityOrder.getBody() == null){
                throw new InvalidRequestException("Invalid orderID.");
            }

            Order order = responseEntityOrder.getBody().getOrder();

            order.setStatus(OrderStatus.DELIVERED);
            SaveOrderToRepoRequest saveOrderToRepoRequest = new SaveOrderToRepoRequest(order);

            rabbitTemplate.convertAndSend("PaymentEXCHANGE", "RK_SaveOrderToRepo", saveOrderToRepoRequest);
        }
        UpdateDeliveryStatusResponse response = new UpdateDeliveryStatusResponse("Successful status update.");
        return response;
    }

    @Override
    public GetDeliveryByUUIDResponse getDeliveryByUUID(GetDeliveryByUUIDRequest request) throws DeliveryException{

        Delivery delivery = null;
        String message = "Delivery with given ID successfully returned.";

        if(request == null){
            throw new InvalidRequestException("GetDeliveryByUUID request is null - could not return delivery entity");
        }

        if(request.getDeliveryID() == null){
            throw  new InvalidRequestException("DeliveryID is null in GetDeliveryByUUID request - could not return delivery entity");
        }

        try{
            delivery = deliveryRepo.findById(request.getDeliveryID()).orElse(null);
        }catch(NullPointerException e){
            e.printStackTrace();
        }

        if(delivery == null){
            throw new DeliveryDoesNotExistException("Delivery with given ID does not exist in the repository - could not return delivery");
        }

        return new GetDeliveryByUUIDResponse(delivery, new Date(), message);
    }

    //Helpers
    public boolean checkLongAndLatIfValid(GeoPoint point1, GeoPoint point2){
        if(point1.getLongitude() < -180 || point1.getLongitude() > 180 ||
           point2.getLongitude() < -180 || point2.getLongitude() > 180 ||
           point1.getLatitude() < -90 || point1.getLatitude() > 90 ||
           point2.getLatitude() < -90 || point2.getLatitude() > 90){
            return false;
        }
        return true;
    }

    public double getDistanceBetweenTwoPoints(GeoPoint point1, GeoPoint point2){
        double theta = point1.getLongitude() - point2.getLongitude();
        double distance = Math.sin(Math.toRadians(point1.getLatitude())) * Math.sin(Math.toRadians(point2.getLatitude())) + Math.cos(Math.toRadians(point1.getLatitude())) * Math.cos(Math.toRadians(point2.getLatitude())) * Math.cos(Math.toRadians(theta));
        distance = Math.acos(distance);
        distance = Math.toDegrees(distance);
        distance = distance * 60 * 1.1515 * 1.609344;
        return distance;
    }

    @Override
    public GetDeliveryDriverByOrderIDResponse getDeliveryDriverByOrderID(GetDeliveryDriverByOrderIDRequest request) throws InvalidRequestException {

        if(request == null)
        {
            throw new InvalidRequestException("Request object is null");
        }

        if(request.getOrderID() == null)
        {
            throw new InvalidRequestException("Order ID is null. Cannot get Driver.");
        }

        String uri = "http://localhost:8086/payment/getOrder";

        MultiValueMap<String, Object> orderRequest = new LinkedMultiValueMap<String, Object>();
        orderRequest.add("orderId", request.getOrderID());


        ResponseEntity<GetOrderResponse> responseEntityOrder = restTemplate.postForEntity(uri,
                orderRequest, GetOrderResponse.class);

        if(responseEntityOrder == null || !responseEntityOrder.hasBody()
                || responseEntityOrder.getBody() == null || responseEntityOrder.getBody().getOrder() == null){
            throw new InvalidRequestException("Order does not exist");
        }

        if(deliveryRepo==null) {
            return null;
        }

        List<Delivery> deliveries;
        Optional.of(deliveries = deliveryRepo.findAll()).orElse(null);

        if(deliveries!=null)
        {
            return null;
        }

        for(int k=0; k<deliveries.size(); k++)
        {
            if(deliveries.get(k).getOrderID().compareTo(request.getOrderID()) == 0)
            {
                uri = "http://localhost:8089/user/findDriverById";

                MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();
                parts.add("driverID", deliveries.get(k).getDriverId());


                ResponseEntity<FindDriverByIdResponse> responseEntity = restTemplate.postForEntity(uri,
                        parts, FindDriverByIdResponse.class);

                if(responseEntity == null || !responseEntity.hasBody()
                        || responseEntity.getBody() == null){
                    throw new InvalidRequestException("Invalid user.");
                }

                Driver driver = responseEntity.getBody().getDriver();
                if(driver!=null)
                {
                    return new GetDeliveryDriverByOrderIDResponse(driver, "Driver successfully retrieved", deliveries.get(k).getDeliveryID());
                }
            }
        }

        return new GetDeliveryDriverByOrderIDResponse(null, "Driver not found", null);
    }
}
