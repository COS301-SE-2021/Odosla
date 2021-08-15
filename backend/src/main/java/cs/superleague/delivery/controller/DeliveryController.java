package cs.superleague.delivery.controller;

import cs.superleague.api.DeliveryApi;
import cs.superleague.delivery.dataclass.DeliveryDetail;
import cs.superleague.delivery.dataclass.DeliveryStatus;
import cs.superleague.delivery.repos.DeliveryDetailRepo;
import cs.superleague.delivery.repos.DeliveryRepo;
import cs.superleague.delivery.requests.*;
import cs.superleague.delivery.responses.*;
import cs.superleague.integration.ServiceSelector;
import cs.superleague.models.*;
import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.dataclass.OrderStatus;
import cs.superleague.payment.dataclass.OrderType;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.shopping.dataclass.Store;
import cs.superleague.shopping.repos.StoreRepo;
import cs.superleague.user.dataclass.Admin;
import cs.superleague.user.dataclass.Customer;
import cs.superleague.user.dataclass.Driver;
import cs.superleague.user.dataclass.UserType;
import cs.superleague.user.repos.AdminRepo;
import cs.superleague.user.repos.CustomerRepo;
import cs.superleague.user.repos.DriverRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
public class DeliveryController implements DeliveryApi {
    @Autowired
    DeliveryRepo deliveryRepo;
    @Autowired
    DeliveryDetailRepo deliveryDetailRepo;
    @Autowired
    AdminRepo adminRepo;
    @Autowired
    DriverRepo driverRepo;
    @Autowired
    CustomerRepo customerRepo;
    @Autowired
    StoreRepo storeRepo;
    @Autowired
    OrderRepo orderRepo;

    @Override
    public ResponseEntity<DeliveryAddDeliveryDetailResponse> addDeliveryDetail(DeliveryAddDeliveryDetailRequest body) {
        DeliveryAddDeliveryDetailResponse response = new DeliveryAddDeliveryDetailResponse();
        HttpStatus httpStatus = HttpStatus.OK;
        try{
            AddDeliveryDetailRequest request = new AddDeliveryDetailRequest(DeliveryStatus.valueOf(body.getDeliveryStatus()), body.getDetail(), UUID.fromString(body.getDeliveryID()), Calendar.getInstance());
            AddDeliveryDetailResponse addDeliveryDetailResponse = ServiceSelector.getDeliveryService().addDeliveryDetail(request);
            response.setMessage(addDeliveryDetailResponse.getMessage());
            response.setId(addDeliveryDetailResponse.getId());

        }catch (Exception e){
            e.printStackTrace();
            response.setMessage(e.getMessage());
        }
        return new ResponseEntity<>(response, httpStatus);
    }

    @Override
    public ResponseEntity<DeliveryAssignDriverToDeliveryResponse> assignDriverToDelivery(DeliveryAssignDriverToDeliveryRequest body) {
        DeliveryAssignDriverToDeliveryResponse response = new DeliveryAssignDriverToDeliveryResponse();
        HttpStatus httpStatus = HttpStatus.OK;
        try{
            AssignDriverToDeliveryRequest request = new AssignDriverToDeliveryRequest(UUID.fromString(body.getDriverID()), UUID.fromString(body.getDeliveryID()));
            AssignDriverToDeliveryResponse assignDriverToDeliveryResponse = ServiceSelector.getDeliveryService().assignDriverToDelivery(request);
            response.setMessage(assignDriverToDeliveryResponse.getMessage());
            response.setIsAssigned(assignDriverToDeliveryResponse.isAssigned());
        }catch (Exception e){
            e.printStackTrace();
            response.setMessage(e.getMessage());
            response.setIsAssigned(false);
        }
        return new ResponseEntity<>(response, httpStatus);
    }

    @Override
    public ResponseEntity<DeliveryCreateDeliveryResponse> createDelivery(DeliveryCreateDeliveryRequest body) {
        UUID storeID = UUID.fromString("e326b4a8-3b9f-41c3-a7f1-c46101fe0c84");
        UUID customerID = UUID.fromString("da99d98c-b6f8-437e-8631-6aa547ddf59a");
        UUID orderID = UUID.fromString("cca80500-96a1-4336-96b7-b5f4e1442f4e");
        UUID shopperID = UUID.randomUUID();
        GeoPoint dropOffLocation = new GeoPoint(1.0, 1.0, "");
        Order order = new Order(orderID, customerID, storeID, shopperID, Calendar.getInstance(), null, 50.0, OrderType.DELIVERY, OrderStatus.PURCHASED, null, 0.0, dropOffLocation, null, false);
        Customer customer = new Customer("Seamus", "Brennan", "u19060468@tuks.co.za", "0743149813", "Hello123$$$", "123", UserType.CUSTOMER,customerID);
        Store store = new Store(storeID, 1, 2, "Woolworth's", 10, 10, true, "");
        GeoPoint pickUpLocation = new GeoPoint(0.0, 0.0, "address");
        store.setStoreLocation(pickUpLocation);
        customerRepo.save(customer);
        storeRepo.save(store);
        orderRepo.save(order);
        DeliveryCreateDeliveryResponse response = new DeliveryCreateDeliveryResponse();
        HttpStatus httpStatus = HttpStatus.OK;
        try{
            System.out.printf(String.valueOf(body.getPlaceOfDelivery()));
            GeoPoint placeOfDelivery = new GeoPoint(body.getPlaceOfDelivery().getLatitude().doubleValue(), body.getPlaceOfDelivery().getLongitude().doubleValue(), body.getPlaceOfDelivery().getAddress());
            CreateDeliveryRequest request = new CreateDeliveryRequest(UUID.fromString(body.getOrderID()), UUID.fromString(body.getCustomerID()), UUID.fromString(body.getStoreID()), Calendar.getInstance(), placeOfDelivery);
            CreateDeliveryResponse createDeliveryResponse = ServiceSelector.getDeliveryService().createDelivery(request);
            response.setDeliveryID(String.valueOf(createDeliveryResponse.getDeliveryID()));
            response.setMessage(createDeliveryResponse.getMessage());
            response.setIsSuccess(createDeliveryResponse.isSuccess());
        }catch (Exception e){
            e.printStackTrace();
            response.setMessage(e.getMessage());
            response.setDeliveryID(null);
            response.setIsSuccess(false);
        }
        return new ResponseEntity<>(response, httpStatus);
    }

    @Override
    public ResponseEntity<DeliveryGetDeliveryDetailResponse> getDeliveryDetail(DeliveryGetDeliveryDetailRequest body) {
        Admin admin = new Admin();
        admin.setAdminID(UUID.fromString("015e8ddf-25f6-4bcf-99d8-13272f042c4a"));
        adminRepo.save(admin);
        DeliveryGetDeliveryDetailResponse response = new DeliveryGetDeliveryDetailResponse();
        HttpStatus httpStatus = HttpStatus.OK;
        try{
            GetDeliveryDetailRequest request = new GetDeliveryDetailRequest(UUID.fromString(body.getDeliveryID()), UUID.fromString(body.getAdminID()));
            GetDeliveryDetailResponse getDeliveryDetailResponse = ServiceSelector.getDeliveryService().getDeliveryDetail(request);
            response.setMessage(getDeliveryDetailResponse.getMessage());
            System.out.println(getDeliveryDetailResponse.getDetail());
            response.setDetail(populateDeliveryDetails(getDeliveryDetailResponse.getDetail()));
        }catch (Exception e){
            e.printStackTrace();
            response.setMessage(e.getMessage());
        }
        return new ResponseEntity<>(response, httpStatus);
    }

    @Override
    public ResponseEntity<DeliveryGetDeliveryStatusResponse> getDeliveryStatus(DeliveryGetDeliveryStatusRequest body) {
        DeliveryGetDeliveryStatusResponse response = new DeliveryGetDeliveryStatusResponse();
        HttpStatus httpStatus = HttpStatus.OK;
        try{
            GetDeliveryStatusRequest request = new GetDeliveryStatusRequest(UUID.fromString(body.getDeliveryID()));
            GetDeliveryStatusResponse getDeliveryStatusResponse = ServiceSelector.getDeliveryService().getDeliveryStatus(request);
            response.setMessage(getDeliveryStatusResponse.getMessage());
            response.setStatus(String.valueOf(getDeliveryStatusResponse.getStatus()));
        }catch (Exception e){
            e.printStackTrace();
            response.setMessage(e.getMessage());
            response.setStatus(null);
        }
        return new ResponseEntity<>(response, httpStatus);
    }

    @Override
    public ResponseEntity<DeliveryGetNextOrderForDriverResponse> getNextOrderForDriver(DeliveryGetNextOrderForDriverRequest body) {
        UUID driverID = UUID.fromString("4a7f8c6c-1a7d-4eab-a23b-a4d0aa0d4062");
        Driver driver = new Driver();
        driver.setDriverID(driverID);
        driver.setOnShift(true);
        driverRepo.save(driver);
        DeliveryGetNextOrderForDriverResponse response = new DeliveryGetNextOrderForDriverResponse();
        HttpStatus httpStatus = HttpStatus.OK;
        try{
            GeoPoint driversCurrentLocation = new GeoPoint(body.getCurrentLocation().getLatitude().doubleValue(), body.getCurrentLocation().getLongitude().doubleValue(), body.getCurrentLocation().getAddress());
            GetNextOrderForDriverRequest request;
            if (body.getRangeOfDelivery().doubleValue() == 0) {
                request = new GetNextOrderForDriverRequest(UUID.fromString(body.getDriverID()), driversCurrentLocation);
            }else {
                request = new GetNextOrderForDriverRequest(UUID.fromString(body.getDriverID()), driversCurrentLocation, body.getRangeOfDelivery().doubleValue());
            }
            GetNextOrderForDriverResponse getNextOrderForDriverResponse = ServiceSelector.getDeliveryService().getNextOrderForDriver(request);
            response.setDeliveryID(String.valueOf(getNextOrderForDriverResponse.getDeliveryID()));
            response.setMessage(getNextOrderForDriverResponse.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            response.setMessage(e.getMessage());
            response.setDeliveryID(null);
        }
        return new ResponseEntity<>(response, httpStatus);
    }

    @Override
    public ResponseEntity<DeliveryRemoveDriverFromDeliveryResponse> removeDriverFromDelivery(DeliveryRemoveDriverFromDeliveryRequest body) {
        DeliveryRemoveDriverFromDeliveryResponse response = new DeliveryRemoveDriverFromDeliveryResponse();
        HttpStatus httpStatus = HttpStatus.OK;
        try{
            RemoveDriverFromDeliveryRequest request = new RemoveDriverFromDeliveryRequest(UUID.fromString(body.getDriverID()), UUID.fromString(body.getDeliveryID()));
            RemoveDriverFromDeliveryResponse removeDriverFromDeliveryResponse = ServiceSelector.getDeliveryService().removeDriverFromDelivery(request);
            response.setIsSuccess(removeDriverFromDeliveryResponse.isSuccess());
            response.setMessage(removeDriverFromDeliveryResponse.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            response.setMessage(e.getMessage());
            response.setIsSuccess(false);
        }
        return new ResponseEntity<>(response, httpStatus);
    }

    @Override
    public ResponseEntity<DeliveryTrackDeliveryResponse> trackDelivery(DeliveryTrackDeliveryRequest body) {
        DeliveryTrackDeliveryResponse response = new DeliveryTrackDeliveryResponse();
        HttpStatus httpStatus = HttpStatus.OK;
        try{
            TrackDeliveryRequest request = new TrackDeliveryRequest(UUID.fromString(body.getDeliveryID()));
            TrackDeliveryResponse trackDeliveryResponse = ServiceSelector.getDeliveryService().trackDelivery(request);
            GeoPointObject currentLocation = new GeoPointObject();
            currentLocation.setLatitude(BigDecimal.valueOf(trackDeliveryResponse.getCurrentLocation().getLatitude()));
            currentLocation.setLongitude(BigDecimal.valueOf(trackDeliveryResponse.getCurrentLocation().getLongitude()));
            currentLocation.setAddress(trackDeliveryResponse.getCurrentLocation().getAddress());
            response.setCurrentLocation(currentLocation);
            response.setMessage(trackDeliveryResponse.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            response.setMessage(e.getMessage());
            response.setCurrentLocation(null);
        }
        return new ResponseEntity<>(response, httpStatus);
    }

    @Override
    public ResponseEntity<DeliveryUpdateDeliveryStatusResponse> updateDeliveryStatus(DeliveryUpdateDeliveryStatusRequest body) {
        DeliveryUpdateDeliveryStatusResponse response = new DeliveryUpdateDeliveryStatusResponse();
        HttpStatus httpStatus = HttpStatus.OK;
        try{
            DeliveryStatus status = null;
            if (body.getStatus().equals("WaitingForShoppers")){
                status = DeliveryStatus.WaitingForShoppers;
            }else if(body.getStatus().equals("CollectingFromStore")){
                status = DeliveryStatus.CollectingFromStore;
            }else if(body.getStatus().equals("CollectedByDriver")){
                status = DeliveryStatus.CollectedByDriver;
            }else if(body.getStatus().equals("DeliveringToCustomer")){
                status = DeliveryStatus.DeliveringToCustomer;
            }else if(body.getStatus().equals("CollectedByCustomer")){
                status = DeliveryStatus.CollectedByCustomer;
            }else if(body.getStatus().equals("Delivered")){
                status = DeliveryStatus.Delivered;
            }
            UpdateDeliveryStatusRequest request = new UpdateDeliveryStatusRequest(status, UUID.fromString(body.getDeliveryID()), body.getDetail());
            UpdateDeliveryStatusResponse updateDeliveryStatusResponse = ServiceSelector.getDeliveryService().updateDeliveryStatus(request);
            response.setMessage(updateDeliveryStatusResponse.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            response.setMessage(e.getMessage());
        }
        return new ResponseEntity<>(response, httpStatus);
    }

    //Helper functions

    public List<DeliveryDetailObject> populateDeliveryDetails(List<DeliveryDetail> deliveryDetails){
        List<DeliveryDetailObject> deliveryDetailObjects = new ArrayList<>();
        if (deliveryDetails == null){
            return null;
        }
        for (int i = 0; i < deliveryDetails.size(); i++){
            DeliveryDetailObject deliveryDetailObject = new DeliveryDetailObject();
            deliveryDetailObject.setId(deliveryDetails.get(i).getId());
            deliveryDetailObject.setDeliveryID(String.valueOf(deliveryDetails.get(i).getDeliveryID()));
            deliveryDetailObject.setTime(String.valueOf(deliveryDetails.get(i).getTime().getTime()));
            deliveryDetailObject.setDeliveryStatus(String.valueOf(deliveryDetails.get(i).getStatus()));
            deliveryDetailObject.setDetail(deliveryDetails.get(i).getDetail());
            deliveryDetailObjects.add(deliveryDetailObject);
        }
        return deliveryDetailObjects;
    }
}
