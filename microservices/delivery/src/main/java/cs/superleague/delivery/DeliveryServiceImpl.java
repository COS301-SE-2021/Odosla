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
import cs.superleague.integration.security.CurrentUser;
import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.dataclass.OrderStatus;
import cs.superleague.payment.exceptions.OrderDoesNotExist;
import cs.superleague.payment.requests.SaveOrderToRepoRequest;
import cs.superleague.payment.responses.GetOrderByUUIDResponse;
import cs.superleague.shopping.dataclass.Store;
import cs.superleague.shopping.responses.GetStoreByUUIDResponse;
import cs.superleague.shopping.responses.GetStoresResponse;
import cs.superleague.user.dataclass.Driver;
import cs.superleague.user.requests.SaveDriverToRepoRequest;
import cs.superleague.user.responses.GetAdminByEmailResponse;
import cs.superleague.user.responses.GetCustomerByUUIDResponse;
import cs.superleague.user.responses.GetDriverByEmailResponse;
import cs.superleague.user.responses.GetDriverByUUIDResponse;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@Service("deliveryServiceImpl")
public class DeliveryServiceImpl implements DeliveryService {
    private final DeliveryRepo deliveryRepo;
    private final DeliveryDetailRepo deliveryDetailRepo;
    private final RabbitTemplate rabbitTemplate;
    private final RestTemplate restTemplate;
    @Value("${shoppingHost}")
    private String shoppingHost;
    @Value("${shoppingPort}")
    private String shoppingPort;
    @Value("${paymentHost}")
    private String paymentHost;
    @Value("${paymentPort}")
    private String paymentPort;
    @Value("${userHost}")
    private String userHost;
    @Value("${userPort}")
    private String userPort;

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
        if (request == null) {
            throw new InvalidRequestException("Null request object.");
        }
        if (request.getDeliveryID() == null || request.getDetail() == null || request.getTimestamp() == null || request.getStatus() == null) {
            throw new InvalidRequestException("Null parameters.");
        }
        deliveryRepo.findById(request.getDeliveryID()).orElseThrow(() -> new InvalidRequestException("Delivery does not exist in database."));
        DeliveryDetail detail = new DeliveryDetail(request.getDeliveryID(), request.getTimestamp(), request.getStatus(), request.getDetail());
        deliveryDetailRepo.save(detail);
        return new AddDeliveryDetailResponse("Delivery details added successfully.", detail.getId());
    }

    @Override
    public AssignDriverToDeliveryResponse assignDriverToDelivery(AssignDriverToDeliveryRequest request) throws InvalidRequestException, URISyntaxException {
        if (request == null) {
            throw new InvalidRequestException("Null request object.");
        }
        if (request.getDeliveryID() == null) {
            throw new InvalidRequestException("Null parameters.");
        }
        CurrentUser currentUser = new CurrentUser();

        Delivery delivery = deliveryRepo.findById(request.getDeliveryID()).orElseThrow(() -> new InvalidRequestException("Delivery does not exist in the database."));

        String uriString = "http://" + userHost + ":" + userPort + "/user/getDriverByEmail";
        URI uri = new URI(uriString);
        Map<String, Object> parts = new HashMap<>();
        parts.put("email", currentUser.getEmail());

        ResponseEntity<GetDriverByEmailResponse> responseEntity = restTemplate.postForEntity(uri,
                parts, GetDriverByEmailResponse.class);

        if (responseEntity == null || !responseEntity.hasBody()
                || responseEntity.getBody() == null || responseEntity.getBody().getDriver() == null) {
            throw new InvalidRequestException("Driver Repository could not be found.");
        }

        Driver driver = responseEntity.getBody().getDriver();

        if (delivery.getDriverID() != null) {
            if (delivery.getDriverID().compareTo(driver.getDriverID()) == 0) {
                if (delivery.getPickUpLocationOne() != null && delivery.getDropOffLocation() != null) {
                    List<GeoPoint> pickUpLocations = new ArrayList<>();
                    if (delivery.getPickUpLocationOne() != null){
                        pickUpLocations.add(delivery.getPickUpLocationOne());
                        if (delivery.getPickUpLocationTwo() != null){
                            pickUpLocations.add(delivery.getPickUpLocationTwo());
                            if (delivery.getPickUpLocationThree() != null){
                                pickUpLocations.add(delivery.getPickUpLocationThree());
                            }
                        }
                    }
                    return new AssignDriverToDeliveryResponse(true, "Driver was already assigned to delivery.", pickUpLocations, delivery.getDropOffLocation(), driver.getDriverID());
                } else {
                    throw new InvalidRequestException("No pick up or drop off location specified with delivery.");
                }
            }
            throw new InvalidRequestException("This delivery has already been taken by another driver.");
        }

        Map<String, Object> orderRequest = new HashMap<>();
        orderRequest.put("orderID", delivery.getOrderIDOne());

        uriString = "http://" + paymentHost + ":" + paymentPort + "/payment/getOrderByUUID";
        uri = new URI(uriString);

        ResponseEntity<GetOrderByUUIDResponse> responseEntityOrder = restTemplate.postForEntity(uri,
                orderRequest, GetOrderByUUIDResponse.class);
        Map<String, Object> orderRequestTwo = new HashMap<>();
        ResponseEntity<GetOrderByUUIDResponse> responseEntityOrderTwo = null;
        Map<String, Object> orderRequestThree = new HashMap<>();
        ResponseEntity<GetOrderByUUIDResponse> responseEntityOrderThree = null;
        if (delivery.getOrderIDTwo() != null){
            orderRequestTwo.put("orderID", delivery.getOrderIDTwo());
            uriString = "http://" + paymentHost + ":" + paymentPort + "/payment/getOrderByUUID";
            uri = new URI(uriString);

            responseEntityOrderTwo = restTemplate.postForEntity(uri,
                    orderRequest, GetOrderByUUIDResponse.class);
        }
        if (delivery.getOrderIDThree() != null){
            orderRequestThree.put("orderID", delivery.getOrderIDThree());
            uriString = "http://" + paymentHost + ":" + paymentPort + "/payment/getOrderByUUID";
            uri = new URI(uriString);

            responseEntityOrderThree = restTemplate.postForEntity(uri,
                    orderRequest, GetOrderByUUIDResponse.class);
        }

        Order updateOrder = null;
        Order updateOrderTwo = null;
        Order updateOrderThree = null;

        if (responseEntity.getBody() != null) {
            updateOrder = responseEntityOrder.getBody().getOrder();
        }
        if (responseEntityOrderTwo != null && responseEntityOrderTwo.getBody() != null){
            updateOrderTwo = responseEntityOrderTwo.getBody().getOrder();
        }
        if (responseEntityOrderThree != null && responseEntityOrderThree.getBody() != null){
            updateOrderThree = responseEntityOrderThree.getBody().getOrder();
        }

        if (updateOrder != null) {
            if (delivery.getPickUpLocationOne() != null && delivery.getDropOffLocation() != null) {
                updateOrder.setDriverID(driver.getDriverID());

                SaveOrderToRepoRequest saveOrderToRepoRequest = new SaveOrderToRepoRequest(updateOrder);

                rabbitTemplate.convertAndSend("PaymentEXCHANGE", "RK_SaveOrderToRepo", saveOrderToRepoRequest);

                if (delivery.getOrderIDTwo() != null){
                    updateOrderTwo.setDriverID(driver.getDriverID());
                    updateOrderTwo.setStatus(OrderStatus.ASSIGNED_DRIVER);
                    saveOrderToRepoRequest = new SaveOrderToRepoRequest(updateOrderTwo);

                    rabbitTemplate.convertAndSend("PaymentEXCHANGE", "RK_SaveOrderToRepo", saveOrderToRepoRequest);

                }
                if (delivery.getOrderIDThree() != null){
                    updateOrderThree.setDriverID(driver.getDriverID());
                    updateOrderThree.setStatus(OrderStatus.ASSIGNED_DRIVER);
                    saveOrderToRepoRequest = new SaveOrderToRepoRequest(updateOrderThree);

                    rabbitTemplate.convertAndSend("PaymentEXCHANGE", "RK_SaveOrderToRepo", saveOrderToRepoRequest);

                }
                //updateOrder.setStatus(OrderStatus.ASSIGNED_DRIVER);

                orderRequest.put("orderID", delivery.getOrderIDOne());

                responseEntityOrder = restTemplate.postForEntity(uri,
                        orderRequest, GetOrderByUUIDResponse.class);

                if (responseEntityOrder == null || !responseEntityOrder.hasBody()
                        || responseEntityOrder.getBody() == null) {
                    throw new InvalidRequestException("Invalid order.");
                }

                Order updateOrder2 = responseEntityOrder.getBody().getOrder();
                updateOrder2.setStatus(OrderStatus.ASSIGNED_DRIVER);

                saveOrderToRepoRequest = new SaveOrderToRepoRequest(updateOrder2);

                rabbitTemplate.convertAndSend("PaymentEXCHANGE", "RK_SaveOrderToRepo", saveOrderToRepoRequest);

                delivery.setDriverID(driver.getDriverID());
                deliveryRepo.save(delivery);
            } else {
                throw new InvalidRequestException("No pick up or drop off location specified with delivery.");
            }
        } else {
            throw new InvalidRequestException("Invalid order.");
        }
        List<GeoPoint> pickUpLocations = new ArrayList<>();
        if (delivery.getPickUpLocationOne() != null){
            pickUpLocations.add(delivery.getPickUpLocationOne());
            if (delivery.getPickUpLocationTwo() != null){
                pickUpLocations.add(delivery.getPickUpLocationTwo());
                if (delivery.getPickUpLocationThree() != null){
                    pickUpLocations.add(delivery.getPickUpLocationThree());
                }
            }
        }
        return new AssignDriverToDeliveryResponse(true, "Driver successfully assigned to delivery.", pickUpLocations, delivery.getDropOffLocation(), driver.getDriverID());
    }

    @Override
    public CreateDeliveryResponse createDelivery(CreateDeliveryRequest request) throws InvalidRequestException, URISyntaxException {
        if (request == null) {
            throw new InvalidRequestException("Null request object.");
        }
        if (request.getCustomerID() == null || request.getOrderID() == null || request.getStoreID() == null || request.getPlaceOfDelivery() == null) {
            throw new InvalidRequestException("Missing parameters.");
        }

        String uriString = "http://" + userHost + ":" + userPort + "/user/getCustomerByUUID";
        URI uri = new URI(uriString);
        Map<String, Object> parts = new HashMap<>();
        parts.put("userID", request.getCustomerID());

        ResponseEntity<GetCustomerByUUIDResponse> responseEntity = restTemplate.postForEntity(uri,
                parts, GetCustomerByUUIDResponse.class);

        if (responseEntity == null || !responseEntity.hasBody()
                || responseEntity.getBody() == null || responseEntity.getBody().getCustomer() == null) {
            throw new InvalidRequestException("Invalid customerID.");
        }

        uriString = "http://" + paymentHost + ":" + paymentPort + "/payment/getOrderByUUID";
        uri = new URI(uriString);
        Map<String, Object> orderRequest = new HashMap<>();
        orderRequest.put("orderID", request.getOrderID());


        ResponseEntity<GetOrderByUUIDResponse> responseEntityOrder = restTemplate.postForEntity(uri,
                orderRequest, GetOrderByUUIDResponse.class);

        if (responseEntityOrder == null || !responseEntityOrder.hasBody()
                || responseEntityOrder.getBody() == null || responseEntityOrder.getBody().getOrder() == null) {
            throw new InvalidRequestException("Invalid orderID.");
        }
        Order order = responseEntityOrder.getBody().getOrder();

        uriString = "http://" + shoppingHost + ":" + shoppingPort + "/shopping/getStoreByUUID";
        uri = new URI(uriString);
        Map<String, Object> storeRequest = new HashMap<>();
        storeRequest.put("StoreID", request.getStoreID());


        ResponseEntity<GetStoreByUUIDResponse> responseEntityStore = restTemplate.postForEntity(uri,
                storeRequest, GetStoreByUUIDResponse.class);

        if (responseEntityStore == null || !responseEntityStore.hasBody()
                || responseEntityStore.getBody() == null || responseEntityStore.getBody().getStore() == null) {
            throw new InvalidRequestException("Invalid storeID.");
        }

        Store store = responseEntityStore.getBody().getStore();

        GeoPoint locationOfStore = store.getStoreLocation();
        if (locationOfStore == null) {
            throw new InvalidRequestException("Store has no location set.");
        }
        //Adding delivery to database
        List<Delivery> currentDeliverys = deliveryRepo.findAll();
        for (Delivery delivery : currentDeliverys){
            if (delivery.isCompleted() == false && delivery.getCustomerID().compareTo(request.getCustomerID()) == 0){
                //Add Order to delivery
                GetDeliveryCostRequest getDeliveryCostRequest = new GetDeliveryCostRequest(locationOfStore, request.getPlaceOfDelivery());
                GetDeliveryCostResponse getDeliveryCostResponse = getDeliveryCost(getDeliveryCostRequest);
                double deliveryCost = getDeliveryCostResponse.getCost() + delivery.getCost();
                if (delivery.getOrderIDTwo() == null){
                    delivery.setOrderIDTwo(request.getOrderID());
                    delivery.setPickUpLocationTwo(order.getStoreAddress());
                    delivery.setStoreTwoID(request.getStoreID());
                    delivery.setCost(deliveryCost);
                    return new CreateDeliveryResponse(true, "Delivery request placed.", delivery.getDeliveryID());
                } else if (delivery.getOrderIDThree() == null){
                    delivery.setOrderIDThree(request.getOrderID());
                    delivery.setPickUpLocationThree(order.getStoreAddress());
                    delivery.setStoreThreeID(request.getStoreID());
                    delivery.setCost(deliveryCost);
                    return new CreateDeliveryResponse(true, "Delivery request placed.", delivery.getDeliveryID());
                }

            }
        }
        UUID deliveryID = UUID.randomUUID();
        while (deliveryRepo.findById(deliveryID).isPresent()) {
            deliveryID = UUID.randomUUID();
        }
        boolean longAndLatCheck = checkLongAndLatIfValid(locationOfStore, request.getPlaceOfDelivery());
        if (!longAndLatCheck) {
            throw new InvalidRequestException("Invalid geoPoints.");
        }
        GetDeliveryCostRequest getDeliveryCostRequest = new GetDeliveryCostRequest(locationOfStore, request.getPlaceOfDelivery());
        GetDeliveryCostResponse getDeliveryCostResponse = getDeliveryCost(getDeliveryCostRequest);
        Delivery delivery = new Delivery(deliveryID, order.getOrderID(), order.getStoreAddress(), request.getPlaceOfDelivery(), request.getCustomerID(), request.getStoreID(), DeliveryStatus.WaitingForShoppers, getDeliveryCostResponse.getCost());
        deliveryRepo.save(delivery);
        return new CreateDeliveryResponse(true, "Delivery request placed.", deliveryID);
    }

    @Override
    public GetDeliveriesResponse getDeliveries(GetDeliveriesRequest request) throws InvalidRequestException {
        if (request == null) {
            throw new InvalidRequestException("Null request.");
        }
        return null;
    }

    @Override
    public GetDeliveryCostResponse getDeliveryCost(GetDeliveryCostRequest request) throws InvalidRequestException {
        if (request == null) {
            throw new InvalidRequestException("Null request object.");
        }
        if (request.getDropOffLocation() == null || request.getPickUpLocation() == null) {
            throw new InvalidRequestException("Null parameters.");
        }
        boolean validGeoPoints = checkLongAndLatIfValid(request.getDropOffLocation(), request.getPickUpLocation());
        if (!validGeoPoints) {
            throw new InvalidRequestException("Invalid Co-ordinates.");
        }
        double distance = getDistanceBetweenTwoPoints(request.getDropOffLocation(), request.getPickUpLocation());
        double cost;
        if (distance < 20) {
            //price of less than 20 km drive
            cost = 20.0;
        } else if (distance < 40) {
            cost = 35.0;
        } else {
            cost = 50.0;
        }
        return new GetDeliveryCostResponse(cost);
    }

    @Override
    public GetDeliveryDetailResponse getDeliveryDetail(GetDeliveryDetailRequest request) throws InvalidRequestException, URISyntaxException {
        if (request == null) {
            throw new InvalidRequestException("Null request object.");
        }
        if (request.getDeliveryID() == null) {
            throw new InvalidRequestException("Null parameters.");
        }
        CurrentUser currentUser = new CurrentUser();

        String stringUri = "http://" + userHost + ":" + userPort + "/user/getAdminByEmail";
        URI uri = new URI(stringUri);
        Map<String, Object> parts = new HashMap<>();
        parts.put("email", currentUser.getEmail());


        ResponseEntity<GetAdminByEmailResponse> responseEntity = restTemplate.postForEntity(uri,
                parts, GetAdminByEmailResponse.class);

        if (responseEntity == null || !responseEntity.hasBody()
                || responseEntity.getBody() == null || responseEntity.getBody().getAdmin() == null) {
            throw new InvalidRequestException("User is not an admin.");
        }

        List<DeliveryDetail> details = deliveryDetailRepo.findAllByDeliveryID(request.getDeliveryID());
        if (details == null) {
            throw new InvalidRequestException("No details found for this delivery.");
        }
        if (details.size() > 0) {
            return new GetDeliveryDetailResponse("Details successfully found.", details);
        } else {
            throw new InvalidRequestException("No details found for this delivery.");
        }
    }

    @Override
    public GetDeliveryStatusResponse getDeliveryStatus(GetDeliveryStatusRequest request) throws InvalidRequestException {
        if (request == null) {
            throw new InvalidRequestException("Null request object.");
        }
        if (request.getDeliveryID() == null) {
            throw new InvalidRequestException("No delivery Id specified.");
        }
        Delivery delivery = deliveryRepo.findById(request.getDeliveryID()).orElseThrow(() -> new InvalidRequestException("Delivery not found in database."));
        return new GetDeliveryStatusResponse(delivery.getStatus(), "Delivery Found.");
    }

    @Override
    public GetNextOrderForDriverResponse getNextOrderForDriver(GetNextOrderForDriverRequest request) throws InvalidRequestException, URISyntaxException {
        if (request == null) {
            throw new InvalidRequestException("Null request object.");
        }
        if (request.getCurrentLocation() == null) {
            throw new InvalidRequestException("Null parameters.");
        }

        System.out.println(request.getRangeOfDelivery());
        System.out.println("_REQ_");
        System.out.println(request.getCurrentLocation().getAddress());

        double range = request.getRangeOfDelivery();

        CurrentUser currentUser = new CurrentUser();

        String stringUri = "http://" + userHost + ":" + userPort + "/user/getDriverByEmail";
        URI uri = new URI(stringUri);
        Map<String, Object> parts = new HashMap<>();
        parts.put("email", currentUser.getEmail());


        ResponseEntity<GetDriverByEmailResponse> responseEntity = restTemplate.postForEntity(uri,
                parts, GetDriverByEmailResponse.class);

        System.out.println("_responsex_ " + responseEntity.getBody() + " " + responseEntity.getBody().getDriver().toString());

        if (responseEntity == null || !responseEntity.hasBody()
                || responseEntity.getBody() == null || responseEntity.getBody().getDriver() == null) {
            throw new InvalidRequestException("Invalid user.");
        }

        Driver driver = responseEntity.getBody().getDriver();

        if (driver != null) {
            driver.setCurrentAddress(request.getCurrentLocation());
            SaveDriverToRepoRequest saveDriverToRepoRequest = new SaveDriverToRepoRequest(driver);

            System.out.println("_before rab_ " + saveDriverToRepoRequest.getDriver().toString());

            rabbitTemplate.convertAndSend("UserEXCHANGE", "RK_SaveDriverToRepo", saveDriverToRepoRequest);
        } else {
            throw new InvalidRequestException("Driver not found in database.");
        }

        List<Delivery> deliveries = deliveryRepo.findAllByDriverIDIsNull();
        if (deliveries == null) {
            return new GetNextOrderForDriverResponse("No available deliveries in the database.", null);
        }
        Collections.shuffle(deliveries);
        if (deliveries.size() > 0) {
            for (Delivery d : deliveries) {
                double driverDistanceFromStore = getDistanceBetweenTwoPoints(d.getPickUpLocationOne(), request.getCurrentLocation());
                if (driverDistanceFromStore > range || d.getDriverID() != null || d.getStatus().compareTo(DeliveryStatus.WaitingForShoppers) != 0) {
                    continue;
                } else {
                    if (d.isOrderOnePacked() == true && d.isOrderTwoPacked() == true && d.isOrderThreePacked() == true){
                        return new GetNextOrderForDriverResponse("Driver can take the following delivery.", d);
                    } else{
                        continue;
                    }
                }
            }
            return new GetNextOrderForDriverResponse("No available deliveries in the range specified.", null);
        } else {
            return new GetNextOrderForDriverResponse("No available deliveries in the database.", null);
        }
    }

    @Override
    public RemoveDriverFromDeliveryResponse removeDriverFromDelivery(RemoveDriverFromDeliveryRequest request) throws InvalidRequestException, URISyntaxException {
        if (request == null) {
            throw new InvalidRequestException("Null request object.");
        }
        if (request.getDeliveryID() == null) {
            throw new InvalidRequestException("Null parameters.");
        }
        Delivery delivery = deliveryRepo.findById(request.getDeliveryID()).orElseThrow(() -> new InvalidRequestException("Delivery not found in database."));
        if (delivery.getDriverID() == null) {
            throw new InvalidRequestException("No driver is assigned to this delivery.");
        }
        CurrentUser currentUser = new CurrentUser();

        String stringUri = "http://" + userHost + ":" + userPort + "/user/getDriverByEmail";
        URI uri = new URI(stringUri);
        Map<String, Object> parts = new HashMap<String, Object>();
        parts.put("email", currentUser.getEmail());


        ResponseEntity<GetDriverByEmailResponse> responseEntity = restTemplate.postForEntity(uri,
                parts, GetDriverByEmailResponse.class);

        if (responseEntity == null || !responseEntity.hasBody()
                || responseEntity.getBody() == null || responseEntity.getBody().getDriver() == null) {
            throw new InvalidRequestException("Invalid user.");
        }

        Driver driver = responseEntity.getBody().getDriver();

        if (delivery.getDriverID().compareTo(driver.getDriverID()) != 0) {
            throw new InvalidRequestException("Driver was not assigned to delivery.");
        }
        delivery.setDriverID(null);
        deliveryRepo.save(delivery);
        UpdateDeliveryStatusRequest request1 = new UpdateDeliveryStatusRequest(DeliveryStatus.WaitingForShoppers, delivery.getDeliveryID(), "Driver has dropped delivery.");
        updateDeliveryStatus(request1);
        RemoveDriverFromDeliveryResponse response = new RemoveDriverFromDeliveryResponse(true, "Driver successfully removed from delivery.");
        return response;
    }

    @Override
    public TrackDeliveryResponse trackDelivery(TrackDeliveryRequest request) throws InvalidRequestException, URISyntaxException {
        if (request == null) {
            throw new InvalidRequestException("Null request object.");
        }
        if (request.getDeliveryID() == null) {
            throw new InvalidRequestException("No delivery Id specified.");
        }
        Delivery delivery = deliveryRepo.findById(request.getDeliveryID()).orElseThrow(() -> new InvalidRequestException("Delivery not found in database."));
        if (delivery.getDriverID() == null) {
            TrackDeliveryResponse response = new TrackDeliveryResponse(delivery.getPickUpLocationOne(), "No driver has been assigned to this delivery.");
            return response;
        }

        String stringUri = "http://" + userHost + ":" + userPort + "/user/getDriverByUUID";
        URI uri = new URI(stringUri);
        Map<String, Object> parts = new HashMap<String, Object>();
        parts.put("userID", delivery.getDriverID());


        ResponseEntity<GetDriverByUUIDResponse> responseEntity = restTemplate.postForEntity(uri,
                parts, GetDriverByUUIDResponse.class);

        if (responseEntity == null || !responseEntity.hasBody()
                || responseEntity.getBody() == null) {
            throw new InvalidRequestException("Invalid user.");
        }

        Driver driver = responseEntity.getBody().getDriver();

        if (driver == null || driver.getOnShift() == false) {
            delivery.setDriverID(null);
            deliveryRepo.save(delivery);
            TrackDeliveryResponse response = new TrackDeliveryResponse(delivery.getPickUpLocationOne(), "No driver has been assigned to this delivery.");
            return response;
        }
        return new TrackDeliveryResponse(driver.getCurrentAddress(), "Drivers current location.");
    }

    @Override
    public UpdateDeliveryStatusResponse updateDeliveryStatus(UpdateDeliveryStatusRequest request) throws InvalidRequestException, URISyntaxException {
        if (request == null) {
            throw new InvalidRequestException("Null request object.");
        }
        if (request.getDeliveryID() == null || request.getStatus() == null || request.getDetail() == null) {
            throw new InvalidRequestException("Null parameters.");
        }

        Delivery delivery = deliveryRepo.findById(request.getDeliveryID()).orElseThrow(() -> new InvalidRequestException("Delivery does not exist in database."));
        AddDeliveryDetailRequest requestAdd = new AddDeliveryDetailRequest(request.getStatus(), request.getDetail(), request.getDeliveryID(), Calendar.getInstance());
        addDeliveryDetail(requestAdd);
        delivery.setStatus(request.getStatus());
        deliveryRepo.save(delivery);
        if (request.getStatus() == DeliveryStatus.Delivered) {
            delivery = deliveryRepo.findById(request.getDeliveryID()).orElseThrow(() -> new InvalidRequestException("Delivery does not exist in database."));
            delivery.setCompleted(true);

            String stringUri = "http://" + userHost + ":" + userPort + "/user/getDriverByUUID";
            URI uri = new URI(stringUri);
            Map<String, Object> parts = new HashMap<>();
            parts.put("userID", delivery.getDriverID().toString());

            ResponseEntity<GetDriverByUUIDResponse> responseEntity = restTemplate.postForEntity(uri,
                    parts, GetDriverByUUIDResponse.class);

            Driver driver;

            if (responseEntity == null || responseEntity.getStatusCode() != HttpStatus.OK
                    || !responseEntity.hasBody()
                    || responseEntity.getBody() == null) {
                driver = null;
            } else {
                driver = responseEntity.getBody().getDriver();
            }

            if (driver != null) {
                driver.setDeliveriesCompleted(driver.getDeliveriesCompleted() + 1);
                SaveDriverToRepoRequest saveDriverToRepoRequest = new SaveDriverToRepoRequest(driver);
                rabbitTemplate.convertAndSend("UserEXCHANGE", "RK_SaveDriverToRepo", saveDriverToRepoRequest);
            }
            deliveryRepo.save(delivery);
            if (delivery.getOrderIDOne() != null){
                stringUri = "http://" + paymentHost + ":" + paymentPort + "/payment/getOrderByUUID";
                uri = new URI(stringUri);
                Map<String, Object> orderRequest = new HashMap<String, Object>();
                orderRequest.put("orderID", delivery.getOrderIDOne());

                ResponseEntity<GetOrderByUUIDResponse> responseEntityOrder = restTemplate.postForEntity(uri,
                        orderRequest, GetOrderByUUIDResponse.class);
                Order order;
                if (responseEntityOrder == null || !responseEntityOrder.hasBody()
                        || responseEntityOrder.getBody() == null
                        || responseEntityOrder.getStatusCode() != HttpStatus.OK) {
                    order = null;
                } else {
                    order = responseEntityOrder.getBody().getOrder();
                }

                order.setStatus(OrderStatus.DELIVERED);
                SaveOrderToRepoRequest saveOrderToRepoRequest = new SaveOrderToRepoRequest(order);

                rabbitTemplate.convertAndSend("PaymentEXCHANGE", "RK_SaveOrderToRepo", saveOrderToRepoRequest);
                if (delivery.getOrderIDTwo() != null){
                    stringUri = "http://" + paymentHost + ":" + paymentPort + "/payment/getOrderByUUID";
                    uri = new URI(stringUri);
                    orderRequest = new HashMap<String, Object>();
                    orderRequest.put("orderID", delivery.getOrderIDTwo());

                    responseEntityOrder = restTemplate.postForEntity(uri,
                            orderRequest, GetOrderByUUIDResponse.class);
                    if (responseEntityOrder == null || !responseEntityOrder.hasBody()
                            || responseEntityOrder.getBody() == null
                            || responseEntityOrder.getStatusCode() != HttpStatus.OK) {
                        order = null;
                    } else {
                        order = responseEntityOrder.getBody().getOrder();
                    }

                    order.setStatus(OrderStatus.DELIVERED);
                    saveOrderToRepoRequest = new SaveOrderToRepoRequest(order);

                    rabbitTemplate.convertAndSend("PaymentEXCHANGE", "RK_SaveOrderToRepo", saveOrderToRepoRequest);
                    if (delivery.getOrderIDThree() != null){
                        stringUri = "http://" + paymentHost + ":" + paymentPort + "/payment/getOrderByUUID";
                        uri = new URI(stringUri);
                        orderRequest = new HashMap<String, Object>();
                        orderRequest.put("orderID", delivery.getOrderIDThree());

                        responseEntityOrder = restTemplate.postForEntity(uri,
                                orderRequest, GetOrderByUUIDResponse.class);
                        if (responseEntityOrder == null || !responseEntityOrder.hasBody()
                                || responseEntityOrder.getBody() == null
                                || responseEntityOrder.getStatusCode() != HttpStatus.OK) {
                            order = null;
                        } else {
                            order = responseEntityOrder.getBody().getOrder();
                        }

                        order.setStatus(OrderStatus.DELIVERED);
                        saveOrderToRepoRequest = new SaveOrderToRepoRequest(order);

                        rabbitTemplate.convertAndSend("PaymentEXCHANGE", "RK_SaveOrderToRepo", saveOrderToRepoRequest);
                    }
                }
            }
        }

        return new UpdateDeliveryStatusResponse("Successful status update.");
    }

    @Override
    public GetDeliveryByUUIDResponse getDeliveryByUUID(GetDeliveryByUUIDRequest request) throws DeliveryException {

        Delivery delivery = null;
        String message = "Delivery with given ID successfully returned.";

        if (request == null) {
            throw new InvalidRequestException("GetDeliveryByUUID request is null - could not return delivery entity");
        }

        if (request.getDeliveryID() == null) {
            throw new InvalidRequestException("DeliveryID is null in GetDeliveryByUUID request - could not return delivery entity");
        }

        try {
            delivery = deliveryRepo.findById(request.getDeliveryID()).orElse(null);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        if (delivery == null) {
            throw new DeliveryDoesNotExistException("Delivery with given ID does not exist in the repository - could not return delivery");
        }

        return new GetDeliveryByUUIDResponse(delivery, new Date(), message);
    }

    @Override
    public GetAdditionalStoresDeliveryCostResponse getAdditionalStoresDeliveryCost(GetAdditionalStoresDeliveryCostRequest request) throws InvalidRequestException, DeliveryDoesNotExistException, URISyntaxException {
        if(request == null){
            throw new InvalidRequestException("GetDeliveryByUUID request is null - could not get additional stores.");
        }

        if(request.getDeliveryID() == null){
            throw  new InvalidRequestException("DeliveryID is null in GetDeliveryByUUID request - could not get additional stores.");
        }
        Delivery delivery = null;
        try{
            delivery = deliveryRepo.findById(request.getDeliveryID()).orElse(null);
        }catch(NullPointerException e){
            e.printStackTrace();
        }
        if (delivery == null){
            throw new DeliveryDoesNotExistException("Delivery with given ID does not exist in the repository - could not get additional stores.");
        }
        Map<String, Object> parts = new HashMap<>();

        String stringUri = "http://"+shoppingHost+":"+shoppingPort+"/shopping/getStores";
        URI uri = new URI(stringUri);
        ResponseEntity<GetStoresResponse> responseEntity = restTemplate.postForEntity(
                                        uri, parts, GetStoresResponse.class);

        if(responseEntity == null || !responseEntity.hasBody()
                                || responseEntity.getBody() == null){
            throw new InvalidRequestException("No stores returned from shopper subsystem.");
        }
        List<Store> storeList = responseEntity.getBody().getStores();
        List<Store> storesInRange = new ArrayList<>();
        for (Store store : storeList){
            if (getDistanceBetweenTwoPoints(store.getStoreLocation(), delivery.getPickUpLocationOne()) < 10){
                storesInRange.add(store);
            }
        }
        if (storesInRange.size() == 0){
            GetAdditionalStoresDeliveryCostResponse response = new GetAdditionalStoresDeliveryCostResponse(null, null, "No stores within range of the selected store");
            return response;
        }
        double currentRouteDistance = getDistanceBetweenTwoPoints(delivery.getPickUpLocationOne(), delivery.getDropOffLocation());
        List<Double> additionalCost = new ArrayList<>();
        for (Store store : storesInRange){
            double newDistanceForStore = getDistanceBetweenTwoPoints(delivery.getPickUpLocationOne(), store.getStoreLocation());
            newDistanceForStore = newDistanceForStore + getDistanceBetweenTwoPoints(store.getStoreLocation(), delivery.getDropOffLocation());
            double addedDistance = newDistanceForStore - currentRouteDistance;
            double addedCost = getAddedCostOfDistance(addedDistance);
            additionalCost.add(addedCost);
        }
        GetAdditionalStoresDeliveryCostResponse response = new GetAdditionalStoresDeliveryCostResponse(storesInRange, additionalCost, "Returned the stores that can be added to the order.");
        return response;
    }

    @Override
    public AddOrderToDeliveryResponse addOrderToDelivery(AddOrderToDeliveryRequest request) throws InvalidRequestException, URISyntaxException, DeliveryDoesNotExistException, OrderDoesNotExist {
        if (request == null){
            throw new InvalidRequestException("Null request object.");
        }
        if (request.getDeliveryID() == null || request.getOrderID() == null){
            throw new InvalidRequestException("Null parameters in request object.");
        }
        Delivery delivery = deliveryRepo.findById(request.getDeliveryID()).orElse(null);
        if (delivery == null){
            throw new DeliveryDoesNotExistException("The delivery cannot be found in the database.");
        }
        Order orderEntity = null;
        Map<String, Object> parts = new HashMap<>();
        String strOrderID = request.getOrderID().toString();
        parts.put("orderID", strOrderID);
        String stringUri = "http://" + paymentHost + ":" + paymentPort + "/payment/getOrderByUUID";
        URI uri = new URI(stringUri);
        ResponseEntity<GetOrderByUUIDResponse> responseEntity = restTemplate.postForEntity(
                uri, parts, GetOrderByUUIDResponse.class);

        if (responseEntity.getBody() != null) {
            orderEntity = responseEntity.getBody().getOrder();
        }
        if (orderEntity == null) {
            throw new OrderDoesNotExist("Order with ID does not exist in repository - could not get Order entity");
        }
        if (delivery.getOrderIDOne() != null){
            if (delivery.getOrderIDOne().compareTo(request.getOrderID()) == 0){
                AddOrderToDeliveryResponse response = new AddOrderToDeliveryResponse("Order has already been added to the delivery.", false);
                return response;
            }
        }
        if (delivery.getOrderIDTwo() != null){
            if (delivery.getOrderIDTwo().compareTo(request.getOrderID()) == 0){
                AddOrderToDeliveryResponse response = new AddOrderToDeliveryResponse("Order has already been added to the delivery.", false);
                return response;
            }
        }
        if (delivery.getOrderIDThree() != null){
            if (delivery.getOrderIDThree().compareTo(request.getOrderID()) == 0){
                AddOrderToDeliveryResponse response = new AddOrderToDeliveryResponse("Order has already been added to the delivery.", false);
                return response;
            } else {
                AddOrderToDeliveryResponse response = new AddOrderToDeliveryResponse("No more orders can be added to this delivery.", false);
                return response;
            }
        }
        if (delivery.getOrderIDOne() == null){
            delivery.setOrderIDOne(request.getOrderID());
            delivery.setPickUpLocationOne(orderEntity.getStoreAddress());
            delivery.setStoreOneID(orderEntity.getStoreID());
        } else if (delivery.getOrderIDTwo() == null){
            delivery.setOrderIDTwo(request.getOrderID());
            delivery.setPickUpLocationTwo(orderEntity.getStoreAddress());
            delivery.setStoreTwoID(orderEntity.getStoreID());
        } else {
            delivery.setOrderIDThree(request.getOrderID());
            delivery.setPickUpLocationThree(orderEntity.getStoreAddress());
            delivery.setStoreThreeID(orderEntity.getStoreID());
        }
        deliveryRepo.save(delivery);
        AddOrderToDeliveryResponse response = new AddOrderToDeliveryResponse("Order with orderID "+ orderEntity.getOrderID() +" has been successfully added to the delivery.", true);
        return response;
    }

    @Override
    public CompletePackingOrderForDeliveryResponse completePackingOrderForDelivery(CompletePackingOrderForDeliveryRequest request) throws InvalidRequestException, DeliveryDoesNotExistException {
        if (request == null){
            throw new InvalidRequestException("Null request object");
        }
        if (request.getOrderID() == null){
            throw new InvalidRequestException("Null orderID in request.");
        }
        boolean deliveryIDOne = true;
        boolean deliveryIDTwo = false;
        boolean deliveryIDThree = false;
        Delivery delivery = deliveryRepo.findDeliveryByOrderIDOne(request.getOrderID());
        if (delivery == null){
            deliveryIDTwo = true;
            deliveryIDOne = false;
            delivery = deliveryRepo.findDeliveryByOrderIDTwo(request.getOrderID());
            if (delivery == null){
                deliveryIDTwo = false;
                deliveryIDThree = true;
                delivery = deliveryRepo.findDeliveryByOrderIDThree(request.getOrderID());
            }
        }
        if (delivery == null){
            throw new DeliveryDoesNotExistException("No delivery's found with the orderID specified.");
        }
        if (delivery.getOrderIDTwo() == null && delivery.getOrderIDThree() == null && deliveryIDOne == true){
            delivery.setOrderOnePacked(true);
            delivery.setOrderTwoPacked(true);
            delivery.setOrderThreePacked(true);
            deliveryRepo.save(delivery);
            return new CompletePackingOrderForDeliveryResponse(true, "Delivery is now ready for collection.");
        }
        if (delivery.getOrderIDThree() == null && deliveryIDTwo == true){
            delivery.setOrderTwoPacked(true);
            delivery.setOrderThreePacked(true);
            deliveryRepo.save(delivery);
            return new CompletePackingOrderForDeliveryResponse(true, "Delivery is now ready for collection.");
        }
        if (deliveryIDThree == true){
            delivery.setOrderThreePacked(true);
            deliveryRepo.save(delivery);
            return new CompletePackingOrderForDeliveryResponse(true, "Delivery is now ready for collection.");
        }
        if (deliveryIDOne == true){
            delivery.setOrderOnePacked(true);
            deliveryRepo.save(delivery);
            return new CompletePackingOrderForDeliveryResponse(true, "First Order packed, waiting for others.");
        } else if (deliveryIDTwo == true){
            delivery.setOrderTwoPacked(true);
            deliveryRepo.save(delivery);
            return new CompletePackingOrderForDeliveryResponse(true, "Second Order packed, waiting for others.");
        } else {
            delivery.setOrderThreePacked(true);
            deliveryRepo.save(delivery);
            return new CompletePackingOrderForDeliveryResponse(true, "Third Order packed, waiting for others.");
        }

    }

    //Helpers
    public boolean checkLongAndLatIfValid(GeoPoint point1, GeoPoint point2) {
        return point1.getLongitude() >= -180 && point1.getLongitude() <= 180 &&
                point2.getLongitude() >= -180 && point2.getLongitude() <= 180 &&
                point1.getLatitude() >= -90 && point1.getLatitude() <= 90 &&
                point2.getLatitude() >= -90 && point2.getLatitude() <= 90;
    }

    public double getDistanceBetweenTwoPoints(GeoPoint point1, GeoPoint point2) {
        double theta = point1.getLongitude() - point2.getLongitude();
        double distance = Math.sin(Math.toRadians(point1.getLatitude())) * Math.sin(Math.toRadians(point2.getLatitude())) + Math.cos(Math.toRadians(point1.getLatitude())) * Math.cos(Math.toRadians(point2.getLatitude())) * Math.cos(Math.toRadians(theta));
        distance = Math.acos(distance);
        distance = Math.toDegrees(distance);
        distance = distance * 60 * 1.1515 * 1.609344;
        return distance;
    }

    public double getAddedCostOfDistance(double addedDistance){
        double deliveryCost = addedDistance * 2.0;
        return deliveryCost;
    }

    @Override
    public GetDeliveryDriverByOrderIDResponse getDeliveryDriverByOrderID(GetDeliveryDriverByOrderIDRequest request) throws InvalidRequestException, URISyntaxException {

        if (request == null) {
            throw new InvalidRequestException("Request object is null");
        }

        if (request.getOrderID() == null) {
            throw new InvalidRequestException("Order ID is null. Cannot get Driver.");
        }
        if (deliveryRepo == null) {
            return null;
        }

        List<Delivery> deliveries;
        Optional.of(deliveries = deliveryRepo.findAll()).orElse(null);

        if (deliveries == null) {
            System.out.println("hi");
            return null;
        }

        for (Delivery delivery : deliveries) {
            if (delivery.getDeliveryID().compareTo(request.getOrderID()) == 0) {
                String uriString = "http://" + userHost + ":" + userPort + "/user/getDriverByUUID";
                URI uri = new URI(uriString);
                Map<String, Object> parts = new HashMap<>();
                parts.put("userID", delivery.getDriverID());

                ResponseEntity<GetDriverByUUIDResponse> responseEntity = restTemplate.postForEntity(uri,
                        parts, GetDriverByUUIDResponse.class);

                if (responseEntity == null || !responseEntity.hasBody()
                        || responseEntity.getBody() == null || responseEntity.getBody().getDriver() == null) {
                    throw new InvalidRequestException("Invalid user.");
                }

                Driver driver = responseEntity.getBody().getDriver();
                if (driver != null) {
                    return new GetDeliveryDriverByOrderIDResponse(driver, "Driver successfully retrieved", delivery.getDeliveryID());
                }
            }
        }

        return new GetDeliveryDriverByOrderIDResponse(null, "Driver not found", null);
    }
}
