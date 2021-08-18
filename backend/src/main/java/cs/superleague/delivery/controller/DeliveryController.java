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
            AssignDriverToDeliveryRequest request = new AssignDriverToDeliveryRequest(body.getJwtToken(), UUID.fromString(body.getDeliveryID()));
            AssignDriverToDeliveryResponse assignDriverToDeliveryResponse = ServiceSelector.getDeliveryService().assignDriverToDelivery(request);
            response.setMessage(assignDriverToDeliveryResponse.getMessage());
            response.setIsAssigned(assignDriverToDeliveryResponse.isAssigned());
            response.setPickUpLocation(populateGeoPointObject(assignDriverToDeliveryResponse.getPickUpLocation()));
            response.setDropOffLocation(populateGeoPointObject(assignDriverToDeliveryResponse.getDropOffLocation()));

        }catch (Exception e){
            e.printStackTrace();
            response.setMessage(e.getMessage());
            response.setIsAssigned(false);
            response.setPickUpLocation(null);
            response.setDropOffLocation(null);
        }
        return new ResponseEntity<>(response, httpStatus);
    }

    @Override
    public ResponseEntity<DeliveryCreateDeliveryResponse> createDelivery(DeliveryCreateDeliveryRequest body) {

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

        DeliveryGetNextOrderForDriverResponse response = new DeliveryGetNextOrderForDriverResponse();
        HttpStatus httpStatus = HttpStatus.OK;
        try{
            GeoPoint driversCurrentLocation = new GeoPoint(body.getCurrentLocation().getLatitude().doubleValue(), body.getCurrentLocation().getLongitude().doubleValue(), body.getCurrentLocation().getAddress());
            GetNextOrderForDriverRequest request;
            if (body.getRangeOfDelivery().doubleValue() == 0) {
                request = new GetNextOrderForDriverRequest(UUID.fromString(body.getJwtToken()), driversCurrentLocation);
            }else {
                request = new GetNextOrderForDriverRequest(UUID.fromString(body.getJwtToken()), driversCurrentLocation, body.getRangeOfDelivery().doubleValue());
            }
            GetNextOrderForDriverResponse getNextOrderForDriverResponse = ServiceSelector.getDeliveryService().getNextOrderForDriver(request);

            DeliveryObject deliveryObject = new DeliveryObject();
            deliveryObject.setDeliveryID(getNextOrderForDriverResponse.getDelivery().getDeliveryID().toString());

            GeoPointObject dropOffLocation=new GeoPointObject();
            dropOffLocation.setAddress(getNextOrderForDriverResponse.getDelivery().getDropOffLocation().getAddress());
            dropOffLocation.setLatitude(BigDecimal.valueOf(getNextOrderForDriverResponse.getDelivery().getDropOffLocation().getLatitude()));
            dropOffLocation.setLongitude(BigDecimal.valueOf(getNextOrderForDriverResponse.getDelivery().getDropOffLocation().getLongitude()));
            GeoPointObject pickUpLocation = new GeoPointObject();
            pickUpLocation.setAddress(getNextOrderForDriverResponse.getDelivery().getPickUpLocation().getAddress());
            pickUpLocation.setLatitude(BigDecimal.valueOf(getNextOrderForDriverResponse.getDelivery().getPickUpLocation().getLatitude()));
            pickUpLocation.setLongitude(BigDecimal.valueOf(getNextOrderForDriverResponse.getDelivery().getPickUpLocation().getLongitude()));

            deliveryObject.setDropOffLocation(dropOffLocation);
            deliveryObject.setPickUpLocation(pickUpLocation);
            deliveryObject.setOrderID(getNextOrderForDriverResponse.getDelivery().getOrderID().toString());
            deliveryObject.setCustomerId(getNextOrderForDriverResponse.getDelivery().getCustomerId().toString());
            deliveryObject.setStoreId(getNextOrderForDriverResponse.getDelivery().getStoreId().toString());
            deliveryObject.setDriverId(getNextOrderForDriverResponse.getDelivery().getDriverId().toString());
            deliveryObject.setStatus(getNextOrderForDriverResponse.getDelivery().getStatus().toString());
            deliveryObject.setCost(BigDecimal.valueOf(getNextOrderForDriverResponse.getDelivery().getCost()));
            deliveryObject.setCompleted(getNextOrderForDriverResponse.getDelivery().isCompleted());
            deliveryObject.setDeliveryDetail(populateDeliveryDetails(getNextOrderForDriverResponse.getDelivery().getDeliveryDetail()));

            response.setDelivery(deliveryObject);
            response.setMessage(getNextOrderForDriverResponse.getMessage());

        }catch (Exception e){
            e.printStackTrace();
            response.setMessage(e.getMessage());
            response.setDelivery(null);
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
    public GeoPointObject populateGeoPointObject(GeoPoint location){
        GeoPointObject locationObject = new GeoPointObject();
        locationObject.setAddress(location.getAddress());
        locationObject.setLongitude(BigDecimal.valueOf(location.getLongitude()));
        locationObject.setLatitude(BigDecimal.valueOf(location.getLatitude()));
        return locationObject;
    }

    @Override
    public ResponseEntity<DeliveryRemoveDriverFromDeliveryResponse> removeDriverFromDelivery(DeliveryRemoveDriverFromDeliveryRequest body) {
        return null;
    }
}
