package cs.superleague.delivery;

import cs.superleague.delivery.dataclass.Delivery;
import cs.superleague.delivery.dataclass.DeliveryDetail;
import cs.superleague.delivery.dataclass.DeliveryStatus;
import cs.superleague.delivery.exceptions.InvalidRequestException;
import cs.superleague.delivery.repos.DeliveryDetailRepo;
import cs.superleague.delivery.repos.DeliveryRepo;
import cs.superleague.delivery.requests.*;
import cs.superleague.delivery.responses.*;
import cs.superleague.integration.security.JwtUtil;
import cs.superleague.payment.PaymentService;
import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.dataclass.OrderStatus;
import cs.superleague.payment.exceptions.PaymentException;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.payment.requests.SetStatusRequest;
import cs.superleague.shopping.dataclass.Store;
import cs.superleague.shopping.repos.StoreRepo;
import cs.superleague.user.UserService;
import cs.superleague.user.dataclass.Driver;
import cs.superleague.user.dataclass.Shopper;
import cs.superleague.user.repos.AdminRepo;
import cs.superleague.user.repos.CustomerRepo;
import cs.superleague.user.repos.DriverRepo;
import cs.superleague.user.requests.GetCurrentUserRequest;
import cs.superleague.user.responses.GetCurrentUserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("deliveryServiceImpl")
public class DeliveryServiceImpl implements DeliveryService {
    private final DeliveryRepo deliveryRepo;
    private final DeliveryDetailRepo deliveryDetailRepo;
    private final AdminRepo adminRepo;
    private final DriverRepo driverRepo;
    private final CustomerRepo customerRepo;
    private final StoreRepo storeRepo;
    private final OrderRepo orderRepo;
    private final UserService userService;
    private final PaymentService paymentService;

    @Autowired
    public DeliveryServiceImpl(DeliveryRepo deliveryRepo, DeliveryDetailRepo deliveryDetailRepo, AdminRepo adminRepo, DriverRepo driverRepo, CustomerRepo customerRepo, StoreRepo storeRepo, OrderRepo orderRepo, @Lazy UserService userService, @Lazy PaymentService paymentService) {
        this.deliveryRepo = deliveryRepo;
        this.deliveryDetailRepo = deliveryDetailRepo;
        this.adminRepo = adminRepo;
        this.driverRepo = driverRepo;
        this.customerRepo = customerRepo;
        this.storeRepo = storeRepo;
        this.orderRepo = orderRepo;
        this.userService=userService;
        this.paymentService = paymentService;
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
        AddDeliveryDetailResponse response = new AddDeliveryDetailResponse("Delivery details added successfully.", detail.getId());
        return response;
    }

    @Override
    public AssignDriverToDeliveryResponse assignDriverToDelivery(AssignDriverToDeliveryRequest request) throws InvalidRequestException, cs.superleague.user.exceptions.InvalidRequestException, PaymentException {
        if (request == null){
            throw new InvalidRequestException("Null request object.");
        }
        if (request.getDeliveryID() == null || request.getJwtToken() == null){
            throw new InvalidRequestException("Null parameters.");
        }
        Delivery delivery = deliveryRepo.findById(request.getDeliveryID()).orElseThrow(()-> new InvalidRequestException("Delivery does not exist in the database."));

        JwtUtil jwtUtil = new JwtUtil();
        if(jwtUtil.extractUserType(request.getJwtToken()).equals("DRIVER"))
        {
            GetCurrentUserResponse getCurrentUserResponse = userService.getCurrentUser(new GetCurrentUserRequest(request.getJwtToken()));

            if(driverRepo!=null)
            {
                Driver driver = driverRepo.findDriverByEmail(getCurrentUserResponse.getUser().getEmail());
                if(driver!=null)
                {
                    if (delivery.getDriverId() != null){
                        if (delivery.getDriverId().compareTo(driver.getDriverID()) == 0){
                            if (delivery.getPickUpLocation() != null && delivery.getDropOffLocation() != null){
                                AssignDriverToDeliveryResponse response = new AssignDriverToDeliveryResponse(true, "Driver was already assigned to delivery.", delivery.getPickUpLocation(), delivery.getDropOffLocation(), driver.getDriverID());
                                return response;
                            } else{
                                throw new InvalidRequestException("No pick up or drop off location specified with delivery.");
                            }
                        }
                        throw new InvalidRequestException("This delivery has already been taken by another driver.");
                    }

                    Order updateOrder= orderRepo.findById(delivery.getOrderID()).orElse(null);
                    if(updateOrder!=null){
                        if (delivery.getPickUpLocation() != null && delivery.getDropOffLocation() != null){
                            updateOrder.setDriverID(driver.getDriverID());
                            orderRepo.save(updateOrder);
                            //updateOrder.setStatus(OrderStatus.ASSIGNED_DRIVER);
                            Order updateOrder2= orderRepo.findById(delivery.getOrderID()).orElse(null);
                            SetStatusRequest setStatusRequest= new SetStatusRequest(updateOrder2, OrderStatus.ASSIGNED_DRIVER);
                            paymentService.setStatus(setStatusRequest);

                            delivery.setDriverId(driver.getDriverID());
                            deliveryRepo.save(delivery);
                            AssignDriverToDeliveryResponse response = new AssignDriverToDeliveryResponse(true, "Driver successfully assigned to delivery.", delivery.getPickUpLocation(), delivery.getDropOffLocation(), driver.getDriverID());
                            return response;
                        } else{
                            throw new InvalidRequestException("No pick up or drop off location specified with delivery.");
                        }
                    }
                    else
                    {
                        throw new InvalidRequestException("Invalid order.");
                    }
                }
                else
                {
                    throw new InvalidRequestException("Invalid user.");
                }
            }

        }
        else
        {
            throw new InvalidRequestException("Invalid user.");
        }

        return null;
    }

    @Override
    public CreateDeliveryResponse createDelivery(CreateDeliveryRequest request) throws InvalidRequestException {
        if (request == null){
            throw new InvalidRequestException("Null request object.");
        }
        if(request.getCustomerID() == null || request.getOrderID() == null || request.getStoreID() == null || request.getPlaceOfDelivery() == null){
            throw new InvalidRequestException("Missing parameters.");
        }
        if(!customerRepo.findById(request.getCustomerID()).isPresent()){
            throw new InvalidRequestException("Invalid customerID.");
        }
        if (!orderRepo.findById(request.getOrderID()).isPresent()){
            throw new InvalidRequestException("Invalid orderID.");
        }
        Store store = storeRepo.findById(request.getStoreID()).orElse(null);
        if (store == null){
            throw new InvalidRequestException("Invalid storeID.");
        }
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
        CreateDeliveryResponse response = new CreateDeliveryResponse(true, "Delivery request placed.", deliveryID);
        return response;
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
        GetDeliveryCostResponse response = new GetDeliveryCostResponse(cost);
        return response;
    }

    @Override
    public GetDeliveryDetailResponse getDeliveryDetail(GetDeliveryDetailRequest request) throws InvalidRequestException {
        if(request == null){
            throw new InvalidRequestException("Null request object.");
        }
        if (request.getDeliveryID() == null || request.getAdminID() == null){
            throw new InvalidRequestException("Null parameters.");
        }
        if (!adminRepo.findById(request.getAdminID()).isPresent()){
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
        GetDeliveryStatusResponse response = new GetDeliveryStatusResponse(delivery.getStatus(), "Delivery Found.");
        return response;
    }

    @Override
    public GetNextOrderForDriverResponse getNextOrderForDriver(GetNextOrderForDriverRequest request) throws InvalidRequestException, cs.superleague.user.exceptions.InvalidRequestException {
        if (request==null){
            throw new InvalidRequestException("Null request object.");
        }
        if(request.getJwtToken()==null || request.getCurrentLocation() == null){
            throw new InvalidRequestException("Null parameters.");
        }
        double range = request.getRangeOfDelivery();

        JwtUtil jwtUtil = new JwtUtil();
        if(jwtUtil.extractUserType(request.getJwtToken()).equals("DRIVER"))
        {
            GetCurrentUserResponse getCurrentUserResponse = userService.getCurrentUser(new GetCurrentUserRequest(request.getJwtToken()));

            if(driverRepo!=null)
            {
                Driver driver= null;
                try{
                    driver = driverRepo.findDriverByEmail(getCurrentUserResponse.getUser().getEmail());
                }
                catch(NullPointerException e)
                {
                    driver=null;
                }

                if(driver!=null)
                {
                    driver.setCurrentAddress(request.getCurrentLocation());
                    driverRepo.save(driver);
                }
                else
                {
                    throw new InvalidRequestException("Driver not found in database.");
                }
            }
            else
            {
                throw new InvalidRequestException("Driver not found in database.");
            }

        }

        List<Delivery> deliveries = deliveryRepo.findAll();
        if (deliveries == null){
            GetNextOrderForDriverResponse response = new GetNextOrderForDriverResponse("No available deliveries in the database.", null);
            return response;
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
    public RemoveDriverFromDeliveryResponse removeDriverFromDelivery(RemoveDriverFromDeliveryRequest request) throws InvalidRequestException, PaymentException {
        if (request == null){
            throw new InvalidRequestException("Null request object.");
        }
        if (request.getDeliveryID() == null || request.getDriverID() == null){
            throw new InvalidRequestException("Null parameters.");
        }
        Delivery delivery = deliveryRepo.findById(request.getDeliveryID()).orElseThrow(()->new InvalidRequestException("Delivery not found in database."));
        if (delivery.getDriverId() == null){
            throw new InvalidRequestException("No driver is assigned to this delivery.");
        }
        if (delivery.getDriverId().compareTo(request.getDriverID()) != 0){
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
        Driver driver = driverRepo.findById(delivery.getDriverId()).orElse(null);
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
    public UpdateDeliveryStatusResponse updateDeliveryStatus(UpdateDeliveryStatusRequest request) throws InvalidRequestException, PaymentException {
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
            Driver driver= driverRepo.findById(delivery.getDriverId()).orElse(null);
            if(driver!=null)
            {
                driver.setDeliveriesCompleted(driver.getDeliveriesCompleted()+1);
                driverRepo.save(driver);
            }
            deliveryRepo.save(delivery);
            Order order = orderRepo.findById(delivery.getOrderID()).orElse(null);
            SetStatusRequest setStatusRequest = new SetStatusRequest(order, OrderStatus.DELIVERED);
            paymentService.setStatus(setStatusRequest);
        }
        UpdateDeliveryStatusResponse response = new UpdateDeliveryStatusResponse("Successful status update.");
        return response;
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

        if(request!=null)
        {
            if(request.getOrderID()!=null)
            {
                Order order = orderRepo.findById(request.getOrderID()).orElse(null);

                if(order!=null)
                {
                    if(deliveryRepo!=null)
                    {
                        List<Delivery> deliveries;
                        Optional.of(deliveries = deliveryRepo.findAll()).orElse(null);

                        if(deliveries!=null)
                        {
                            for(int k=0; k<deliveries.size(); k++)
                            {
                                if(deliveries.get(k).getOrderID().equals(request.getOrderID()))
                                {
                                    Driver driver = driverRepo.findById(deliveries.get(k).getDriverId()).orElse(null);
                                    if(driver!=null)
                                    {
                                        GetDeliveryDriverByOrderIDResponse response= new GetDeliveryDriverByOrderIDResponse(driver, "Driver successfully retrieved", deliveries.get(k).getDeliveryID());
                                        return response;
                                    }
                                }
                            }
                            GetDeliveryDriverByOrderIDResponse response= new GetDeliveryDriverByOrderIDResponse(null, "Driver not found", null);
                            return response;
                        }

                    }
                }
                else
                {
                    throw new InvalidRequestException("Order does not exist");
                }

            }
            else
            {
                throw new InvalidRequestException("Order ID is null. Cannot get Driver.");
            }
        }else
        {
            throw new InvalidRequestException("Request object is null");
        }

        return null;
    }
}
