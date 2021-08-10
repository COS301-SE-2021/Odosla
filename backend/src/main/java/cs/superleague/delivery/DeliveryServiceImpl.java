package cs.superleague.delivery;

import cs.superleague.delivery.dataclass.Delivery;
import cs.superleague.delivery.exceptions.InvalidRequestException;
import cs.superleague.delivery.repos.DeliveryDetailRepo;
import cs.superleague.delivery.repos.DeliveryRepo;
import cs.superleague.delivery.requests.*;
import cs.superleague.delivery.responses.*;
import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.shopping.dataclass.Store;
import cs.superleague.shopping.repos.StoreRepo;
import cs.superleague.user.dataclass.Driver;
import cs.superleague.user.repos.CustomerRepo;
import cs.superleague.user.repos.DriverRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service("deliveryServiceImpl")
public class DeliveryServiceImpl implements DeliveryService {
    private final DeliveryRepo deliveryRepo;
    private final DeliveryDetailRepo deliveryDetailRepo;
    private final DriverRepo driverRepo;
    private final CustomerRepo customerRepo;
    private final StoreRepo storeRepo;

    @Autowired
    public DeliveryServiceImpl(DeliveryRepo deliveryRepo, DeliveryDetailRepo deliveryDetailRepo, DriverRepo driverRepo, CustomerRepo customerRepo, StoreRepo storeRepo) {
        this.deliveryRepo = deliveryRepo;
        this.deliveryDetailRepo = deliveryDetailRepo;
        this.driverRepo = driverRepo;
        this.customerRepo = customerRepo;
        this.storeRepo = storeRepo;
    }

    @Override
    public AddDeliveryDetailResponse addDeliveryDetail(AddDeliveryDetailRequest request) {
        return null;
    }

    @Override
    public CreateDeliveryResponse createDelivery(CreateDeliveryRequest request) throws InvalidRequestException {
        if (request == null){
            throw new InvalidRequestException("Null request.");
        }
        if(request.getCustomerID() == null || request.getOrderID() == null || request.getStoreID() == null || request.getPlaceOfDelivery() == null){
            throw new InvalidRequestException("Missing parameters.");
        }
        if(!customerRepo.findById(request.getCustomerID()).isPresent()){
            throw new InvalidRequestException("Invalid customerID.");
        }

        //Find driver to take delivery
        boolean driverFound = false;
        Driver driver = null;
        Store store = storeRepo.findById(request.getStoreID()).orElse(null);
        if (store == null){
            throw new InvalidRequestException("Invalid storeID.");
        }
        //Get from store
        GeoPoint locationOfStore = new GeoPoint(0.0,0.0, "");

        while(!driverFound){
            List<Driver> drivers = driverRepo.findAll();
            for(Driver d : drivers) {
//                if(d.isActive()){
//                    if(request.getTimeOfDelivery() == null){
//                        continue;
//                    }else{
//                        Check if driver has a booking if so continue otherwise check location
//                    }
//                }
                //Need to get location of store
                GeoPoint locationOfDriver = d.getCurrentAddress();
                double theta = locationOfDriver.getLongitude() - locationOfStore.getLongitude();
                double distance = Math.sin(Math.toRadians(locationOfDriver.getLatitude())) * Math.sin(Math.toRadians(locationOfStore.getLatitude())) + Math.cos(Math.toRadians(locationOfDriver.getLatitude())) * Math.cos(Math.toRadians(locationOfStore.getLatitude())) * Math.cos(Math.toRadians(theta));
                distance = Math.acos(distance);
                distance = Math.toDegrees(distance);
                distance = distance * 60 * 1.1515 * 1.609344;
                if (distance > 20){
                    continue;
                }else{
                    //Ask driver to accept, if they accept then driverFound becomes true otherwise try next driver
                    driverFound = true;
                    driver = d;
                    if (request.getTimeOfDelivery() == null){
//                        driver.setActive();
//                        driverRepo.save(driver);
                    }
                    break;
                }
            }
        }

        //Adding delivery to database
        UUID deliveryID = UUID.randomUUID();
        while(deliveryRepo.findById(deliveryID).isPresent()){
            deliveryID = UUID.randomUUID();
        }
        GetDeliveryCostRequest getDeliveryCostRequest = new GetDeliveryCostRequest(locationOfStore, request.getPlaceOfDelivery());
        Delivery delivery = new Delivery(null, null, null, null, null, null, 0);
        return null;
    }

    @Override
    public GetDeliveriesResponse getDeliveries(GetDeliveriesRequest request) {
        return null;
    }

    @Override
    public GetDeliveryCostResponse getDeliveryCost(GetDeliveryCostRequest request) {
        return null;
    }

    @Override
    public GetDeliveryDetailResponse getDeliveryDetail(GetDeliveryDetailRequest request) {
        return null;
    }

    @Override
    public GetDeliveryStatusResponse getDeliveryStatus(GetDeliveryStatusRequest request) {
        return null;
    }

    @Override
    public TrackDeliveryResponse trackDelivery(TrackDeliveryRequest request) {
        return null;
    }

    @Override
    public UpdateDeliveryStatusResponse updateDeliveryStatus(UpdateDeliveryStatusRequest request) {
        return null;
    }
}
