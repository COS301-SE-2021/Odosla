package cs.superleague.delivery;

import cs.superleague.delivery.dataclass.Delivery;
import cs.superleague.delivery.dataclass.DeliveryDetail;
import cs.superleague.delivery.dataclass.DeliveryStatus;
import cs.superleague.delivery.exceptions.InvalidRequestException;
import cs.superleague.delivery.repos.DeliveryDetailRepo;
import cs.superleague.delivery.repos.DeliveryRepo;
import cs.superleague.delivery.requests.*;
import cs.superleague.delivery.responses.*;
import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.shopping.dataclass.Store;
import cs.superleague.shopping.repos.StoreRepo;
import cs.superleague.user.dataclass.Driver;
import cs.superleague.user.repos.AdminRepo;
import cs.superleague.user.repos.CustomerRepo;
import cs.superleague.user.repos.DriverRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service("deliveryServiceImpl")
public class DeliveryServiceImpl implements DeliveryService {
    private final DeliveryRepo deliveryRepo;
    private final DeliveryDetailRepo deliveryDetailRepo;
    private final AdminRepo adminRepo;
    private final DriverRepo driverRepo;
    private final CustomerRepo customerRepo;
    private final StoreRepo storeRepo;
    private final OrderRepo orderRepo;

    @Autowired
    public DeliveryServiceImpl(DeliveryRepo deliveryRepo, DeliveryDetailRepo deliveryDetailRepo, AdminRepo adminRepo, DriverRepo driverRepo, CustomerRepo customerRepo, StoreRepo storeRepo, OrderRepo orderRepo) {
        this.deliveryRepo = deliveryRepo;
        this.deliveryDetailRepo = deliveryDetailRepo;
        this.adminRepo = adminRepo;
        this.driverRepo = driverRepo;
        this.customerRepo = customerRepo;
        this.storeRepo = storeRepo;
        this.orderRepo = orderRepo;
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
    public AssignDriverToDeliveryResponse assignDriverToDelivery(AssignDriverToDeliveryRequest request) throws InvalidRequestException {
        if (request == null){
            throw new InvalidRequestException("Null request object.");
        }
        if (request.getDeliveryID() == null || request.getDriverID() == null){
            throw new InvalidRequestException("Null parameters.");
        }
        if(!driverRepo.findById(request.getDriverID()).isPresent()){
            throw new InvalidRequestException("Driver does not exist in the database.");
        }
        Delivery delivery = deliveryRepo.findById(request.getDeliveryID()).orElseThrow(()-> new InvalidRequestException("Delivery does not exist in the database."));

        if (delivery.getDriverId() != null){
            if (delivery.getDriverId().compareTo(request.getDriverID()) == 0){
                AssignDriverToDeliveryResponse response = new AssignDriverToDeliveryResponse(true, "Driver was already assigned to delivery.");
                return response;
            }
            throw new InvalidRequestException("This delivery has already been taken by another driver.");
        }
        delivery.setDriverId(request.getDriverID());
        deliveryRepo.save(delivery);
        AssignDriverToDeliveryResponse response = new AssignDriverToDeliveryResponse(true, "Driver successfully assigned to delivery.");
        return response;
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
        Delivery delivery = new Delivery(deliveryID, request.getOrderID(), locationOfStore, request.getPlaceOfDelivery(), request.getCustomerID(), request.getStoreID(), DeliveryStatus.CollectingFromStore, getDeliveryCostResponse.getCost());
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
    public GetNextOrderForDriverResponse getNextOrderForDriver(GetNextOrderForDriverRequest request) throws InvalidRequestException {
        if (request==null){
            throw new InvalidRequestException("Null request object.");
        }
        if(request.getDriverID() == null || request.getCurrentLocation() == null){
            throw new InvalidRequestException("Null parameters.");
        }
        double range = request.getRangeOfDelivery();
        Driver driver = driverRepo.findById(request.getDriverID()).orElseThrow(()->new InvalidRequestException("Driver not found in database."));
        driver.setCurrentAddress(request.getCurrentLocation());
        driverRepo.save(driver);
        List<Delivery> deliveries = deliveryRepo.findAllByDriverIdIsNull();
        if (deliveries == null){
            GetNextOrderForDriverResponse response = new GetNextOrderForDriverResponse("No available deliveries in the database.", null);
            return response;
        }
        Collections.shuffle(deliveries);
        if (deliveries.size()>0){
            for (Delivery d : deliveries){
                double driverDistanceFromStore = getDistanceBetweenTwoPoints(d.getPickUpLocation(), request.getCurrentLocation());
                if (driverDistanceFromStore > range){
                    continue;
                }else{
                    GetNextOrderForDriverResponse response = new GetNextOrderForDriverResponse("Driver can take the following delivery.", d.getDeliveryID());
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
    public RemoveDriverFromDeliveryResponse removeDriverFromDelivery(RemoveDriverFromDeliveryRequest request) throws InvalidRequestException {
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
    public UpdateDeliveryStatusResponse updateDeliveryStatus(UpdateDeliveryStatusRequest request) throws InvalidRequestException {
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
}
