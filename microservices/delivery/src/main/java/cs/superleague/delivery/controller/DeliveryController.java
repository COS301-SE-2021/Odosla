package cs.superleague.delivery.controller;

import cs.superleague.api.DeliveryApi;
import cs.superleague.delivery.DeliveryServiceImpl;
import cs.superleague.delivery.dataclass.DeliveryDetail;
import cs.superleague.delivery.dataclass.DeliveryStatus;
import cs.superleague.delivery.repos.DeliveryDetailRepo;
import cs.superleague.delivery.repos.DeliveryRepo;
import cs.superleague.delivery.requests.*;
import cs.superleague.delivery.responses.*;
import cs.superleague.user.dataclass.Driver;
import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.models.*;
import org.apache.http.Header;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
public class DeliveryController implements DeliveryApi {

    DeliveryRepo deliveryRepo;
    DeliveryDetailRepo deliveryDetailRepo;
    DeliveryServiceImpl deliveryService;
    HttpServletRequest httpServletRequest;
    RestTemplate restTemplate;

    @Autowired
    public DeliveryController(DeliveryRepo deliveryRepo, DeliveryDetailRepo deliveryDetailRepo,
                              DeliveryServiceImpl deliveryService, HttpServletRequest httpServletRequest,
                              RestTemplate restTemplate) {
        this.deliveryRepo = deliveryRepo;
        this.deliveryDetailRepo = deliveryDetailRepo;
        this.deliveryService = deliveryService;
        this.httpServletRequest = httpServletRequest;
        this.restTemplate = restTemplate;
    }

    @Override
    public ResponseEntity<DeliveryAddDeliveryDetailResponse> addDeliveryDetail(DeliveryAddDeliveryDetailRequest body) {
        DeliveryAddDeliveryDetailResponse response = new DeliveryAddDeliveryDetailResponse();
        HttpStatus httpStatus = HttpStatus.OK;
        try{
            AddDeliveryDetailRequest request = new AddDeliveryDetailRequest(DeliveryStatus.valueOf(body.getStatus()), body.getDetail(), UUID.fromString(body.getDeliveryID()), Calendar.getInstance());
            AddDeliveryDetailResponse addDeliveryDetailResponse = deliveryService.addDeliveryDetail(request);
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

            System.out.println("delivery id: " + body.getDeliveryID());

            Header header = new BasicHeader("Authorization", httpServletRequest.getHeader("Authorization"));
            List<Header> headers = new ArrayList<>();
            headers.add(header);
            CloseableHttpClient httpClient = HttpClients.custom().setDefaultHeaders(headers).build();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));

            AssignDriverToDeliveryRequest request = new AssignDriverToDeliveryRequest(UUID.fromString(body.getDeliveryID()));
            AssignDriverToDeliveryResponse assignDriverToDeliveryResponse = deliveryService.assignDriverToDelivery(request);
            response.setMessage(assignDriverToDeliveryResponse.getMessage());
            response.setIsAssigned(assignDriverToDeliveryResponse.isAssigned());
            response.setPickUpLocation(populateGeoPointObject(assignDriverToDeliveryResponse.getPickUpLocation()));
            response.setDropOffLocation(populateGeoPointObject(assignDriverToDeliveryResponse.getDropOffLocation()));
            response.setDriverID(assignDriverToDeliveryResponse.getDriverID().toString());
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

            Header header = new BasicHeader("Authorization", httpServletRequest.getHeader("Authorization"));
            List<Header> headers = new ArrayList<>();
            headers.add(header);
            CloseableHttpClient httpClient = HttpClients.custom().setDefaultHeaders(headers).build();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));

            GeoPoint placeOfDelivery = new GeoPoint(body.getPlaceOfDelivery().getLatitude().doubleValue(), body.getPlaceOfDelivery().getLongitude().doubleValue(), body.getPlaceOfDelivery().getAddress());
            CreateDeliveryRequest request = new CreateDeliveryRequest(UUID.fromString(body.getOrderID()), UUID.fromString(body.getCustomerID()), UUID.fromString(body.getStoreID()), Calendar.getInstance(), placeOfDelivery);
            CreateDeliveryResponse createDeliveryResponse = deliveryService.createDelivery(request);
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

            Header header = new BasicHeader("Authorization", httpServletRequest.getHeader("Authorization"));
            List<Header> headers = new ArrayList<>();
            headers.add(header);
            CloseableHttpClient httpClient = HttpClients.custom().setDefaultHeaders(headers).build();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));

            GetDeliveryDetailRequest request = new GetDeliveryDetailRequest(UUID.fromString(body.getDeliveryID()));
            GetDeliveryDetailResponse getDeliveryDetailResponse = deliveryService.getDeliveryDetail(request);
            response.setMessage(getDeliveryDetailResponse.getMessage());

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
            GetDeliveryStatusResponse getDeliveryStatusResponse = deliveryService.getDeliveryStatus(request);
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

            Header header = new BasicHeader("Authorization", httpServletRequest.getHeader("Authorization"));
            List<Header> headers = new ArrayList<>();
            headers.add(header);
            CloseableHttpClient httpClient = HttpClients.custom().setDefaultHeaders(headers).build();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));

            GeoPoint driversCurrentLocation = new GeoPoint(body.getCurrentLocation().getLatitude().doubleValue(), body.getCurrentLocation().getLongitude().doubleValue(), body.getCurrentLocation().getAddress());
            GetNextOrderForDriverRequest request;
            if (body.getRangeOfDelivery().doubleValue() == 0) {
                request = new GetNextOrderForDriverRequest(driversCurrentLocation);
            }else {
                request = new GetNextOrderForDriverRequest(driversCurrentLocation, body.getRangeOfDelivery().doubleValue());
            }
            GetNextOrderForDriverResponse getNextOrderForDriverResponse = deliveryService.getNextOrderForDriver(request);

            System.out.println("ASD 1");

            DeliveryObject deliveryObject = new DeliveryObject();
            

            System.out.println("ASD delv id " + getNextOrderForDriverResponse.getDelivery().toString());
            deliveryObject.setDeliveryID(getNextOrderForDriverResponse.getDelivery().getDeliveryID().toString());

            System.out.println("ASD 1");

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
            if(getNextOrderForDriverResponse.getDelivery().getDriverId()!=null)
            {
                deliveryObject.setDriverId(getNextOrderForDriverResponse.getDelivery().getDriverId().toString());
            }

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

            Header header = new BasicHeader("Authorization", httpServletRequest.getHeader("Authorization"));
            List<Header> headers = new ArrayList<>();
            headers.add(header);
            CloseableHttpClient httpClient = HttpClients.custom().setDefaultHeaders(headers).build();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));

            TrackDeliveryRequest request = new TrackDeliveryRequest(UUID.fromString(body.getDeliveryID()));
            TrackDeliveryResponse trackDeliveryResponse = deliveryService.trackDelivery(request);
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

            Header header = new BasicHeader("Authorization", httpServletRequest.getHeader("Authorization"));
            List<Header> headers = new ArrayList<>();
            headers.add(header);
            CloseableHttpClient httpClient = HttpClients.custom().setDefaultHeaders(headers).build();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));

            UpdateDeliveryStatusRequest request = new UpdateDeliveryStatusRequest(status, UUID.fromString(body.getDeliveryID()), body.getDetail());
            UpdateDeliveryStatusResponse updateDeliveryStatusResponse = deliveryService.updateDeliveryStatus(request);
            response.setMessage(updateDeliveryStatusResponse.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            response.setMessage(e.getMessage());
        }
        return new ResponseEntity<>(response, httpStatus);
    }

    @Override
    public ResponseEntity<DeliveryGetDeliveryByUUIDResponse> getDeliveryByUUID(DeliveryGetDeliveryByUUIDRequest body) {

        DeliveryGetDeliveryByUUIDResponse response = new DeliveryGetDeliveryByUUIDResponse();
        HttpStatus httpStatus = HttpStatus.OK;
        try{
            GetDeliveryByUUIDRequest getDeliveryByUUIDRequest = new GetDeliveryByUUIDRequest(UUID.fromString(body.getDeliveryID()));
            GetDeliveryByUUIDResponse getDeliveryByUUIDResponse = deliveryService.getDeliveryByUUID(getDeliveryByUUIDRequest);

            DeliveryObject deliveryObject = new DeliveryObject();
            deliveryObject.setDeliveryID(getDeliveryByUUIDResponse.getDelivery().getDeliveryID().toString());

            GeoPointObject dropOffLocation=new GeoPointObject();
            dropOffLocation.setAddress(getDeliveryByUUIDResponse.getDelivery().getDropOffLocation().getAddress());
            dropOffLocation.setLatitude(BigDecimal.valueOf(getDeliveryByUUIDResponse.getDelivery().getDropOffLocation().getLatitude()));
            dropOffLocation.setLongitude(BigDecimal.valueOf(getDeliveryByUUIDResponse.getDelivery().getDropOffLocation().getLongitude()));
            GeoPointObject pickUpLocation = new GeoPointObject();
            pickUpLocation.setAddress(getDeliveryByUUIDResponse.getDelivery().getPickUpLocation().getAddress());
            pickUpLocation.setLatitude(BigDecimal.valueOf(getDeliveryByUUIDResponse.getDelivery().getPickUpLocation().getLatitude()));
            pickUpLocation.setLongitude(BigDecimal.valueOf(getDeliveryByUUIDResponse.getDelivery().getPickUpLocation().getLongitude()));

            deliveryObject.setDropOffLocation(dropOffLocation);
            deliveryObject.setPickUpLocation(pickUpLocation);
            deliveryObject.setOrderID(getDeliveryByUUIDResponse.getDelivery().getOrderID().toString());
            deliveryObject.setCustomerId(getDeliveryByUUIDResponse.getDelivery().getCustomerId().toString());
            deliveryObject.setStoreId(getDeliveryByUUIDResponse.getDelivery().getStoreId().toString());
            if(getDeliveryByUUIDResponse.getDelivery().getDriverId() != null)
            {
                deliveryObject.setDriverId(getDeliveryByUUIDResponse.getDelivery().getDriverId().toString());
            }

            deliveryObject.setStatus(getDeliveryByUUIDResponse.getDelivery().getStatus().toString());
            deliveryObject.setCost(BigDecimal.valueOf(getDeliveryByUUIDResponse.getDelivery().getCost()));
            deliveryObject.setCompleted(getDeliveryByUUIDResponse.getDelivery().isCompleted());
            deliveryObject.setDeliveryDetail(populateDeliveryDetails(getDeliveryByUUIDResponse.getDelivery().getDeliveryDetail()));

            response.setDeliveryEntity(deliveryObject);
            response.setMessage(getDeliveryByUUIDResponse.getMessage());
            response.setTimestamp(getDeliveryByUUIDResponse.getTimestamp().toString());

        }catch (Exception e){
            e.printStackTrace();
            response.setMessage(e.getMessage());
            response.setDeliveryEntity(null);
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
            deliveryDetailObject.setStatus(String.valueOf(deliveryDetails.get(i).getStatus()));
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

    @Override
    public ResponseEntity<DeliveryGetDeliveryDriverByOrderIdResponse> getDeliveryDriverByOrderId(DeliveryGetDeliveryDriverByOrderIdRequest body) {
        DeliveryGetDeliveryDriverByOrderIdResponse response = new DeliveryGetDeliveryDriverByOrderIdResponse();
        HttpStatus httpStatus = HttpStatus.OK;
        try{

            Header header = new BasicHeader("Authorization", httpServletRequest.getHeader("Authorization"));
            List<Header> headers = new ArrayList<>();
            headers.add(header);
            CloseableHttpClient httpClient = HttpClients.custom().setDefaultHeaders(headers).build();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));

            GetDeliveryDriverByOrderIDRequest request = new GetDeliveryDriverByOrderIDRequest(UUID.fromString(body.getOrderID()));
            GetDeliveryDriverByOrderIDResponse getDeliveryDriverByOrderIDResponse = deliveryService.getDeliveryDriverByOrderID(request);
            response.setMessage(getDeliveryDriverByOrderIDResponse.getMessage());
            response.setDeliveryID(getDeliveryDriverByOrderIDResponse.getDeliveryID().toString());

            DriverObject driverObject = new DriverObject();
            Driver driver = getDeliveryDriverByOrderIDResponse.getDriver();
            driverObject.setDriverID(driver.getDriverID().toString());
            driverObject.setAccountType(driver.getAccountType().toString());
            driverObject.setName(driver.getName());
            driverObject.setSurname(driver.getSurname());
            driverObject.setPhoneNumber(driver.getPhoneNumber());
            driverObject.setEmail(driver.getEmail());
            driverObject.setRating(BigDecimal.valueOf(driver.getRating()));
            driverObject.setDeliveriesCompleted(BigDecimal.valueOf(driver.getDeliveriesCompleted()));
            driverObject.setCurrentAddress(populateGeoPointObject(driver.getCurrentAddress()));
            driverObject.setOnShift(driver.getOnShift());

            response.setDriver(driverObject);

        }catch (Exception e){
            e.printStackTrace();
            response.setMessage(e.getMessage());
        }
        return new ResponseEntity<>(response, httpStatus);
    }
}
