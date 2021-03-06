package cs.superleague.delivery.controller;

import cs.superleague.api.DeliveryApi;
import cs.superleague.delivery.DeliveryServiceImpl;
import cs.superleague.delivery.dataclass.DeliveryDetail;
import cs.superleague.delivery.dataclass.DeliveryStatus;
import cs.superleague.delivery.repos.DeliveryDetailRepo;
import cs.superleague.delivery.repos.DeliveryRepo;
import cs.superleague.delivery.requests.*;
import cs.superleague.delivery.responses.*;
import cs.superleague.payment.dataclass.CartItem;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.shopping.dataclass.Store;
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
import java.text.SimpleDateFormat;
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
    public ResponseEntity<DeliveryAddOrderToDeliveryResponse> addOrderToDelivery(DeliveryAddOrderToDeliveryRequest body) {
        DeliveryAddOrderToDeliveryResponse response = new DeliveryAddOrderToDeliveryResponse();
        HttpStatus httpStatus = HttpStatus.OK;
        try{
            Header header = new BasicHeader("Authorization", httpServletRequest.getHeader("Authorization"));
            List<Header> headers = new ArrayList<>();
            headers.add(header);
            CloseableHttpClient httpClient = HttpClients.custom().setDefaultHeaders(headers).build();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));

            AddOrderToDeliveryRequest request = new AddOrderToDeliveryRequest(UUID.fromString(body.getDeliveryID()), UUID.fromString(body.getOrderID()));
            AddOrderToDeliveryResponse addOrderToDeliveryResponse = deliveryService.addOrderToDelivery(request);
            response.setMessage(addOrderToDeliveryResponse.getMessage());
            response.setSuccess(addOrderToDeliveryResponse.isSuccess());

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
            response.setPickUpLocations(populateGeoPointObjects(assignDriverToDeliveryResponse.getPickUpLocations()));
            response.setDropOffLocation(populateGeoPointObject(assignDriverToDeliveryResponse.getDropOffLocation()));
            response.setDriverID(assignDriverToDeliveryResponse.getDriverID().toString());
        }catch (Exception e){
            e.printStackTrace();
            response.setMessage(e.getMessage());
            response.setIsAssigned(false);
            response.setPickUpLocations(null);
            response.setDropOffLocation(null);
        }
        return new ResponseEntity<>(response, httpStatus);
    }

    @Override
    public ResponseEntity<DeliveryCompletePackingOrderForDeliveryResponse> completePackingOrderForDelivery(DeliveryCompletePackingOrderForDeliveryRequest body) {
        DeliveryCompletePackingOrderForDeliveryResponse response = new DeliveryCompletePackingOrderForDeliveryResponse();
        HttpStatus httpStatus = HttpStatus.OK;
        try{

            Header header = new BasicHeader("Authorization", httpServletRequest.getHeader("Authorization"));
            List<Header> headers = new ArrayList<>();
            headers.add(header);
            CloseableHttpClient httpClient = HttpClients.custom().setDefaultHeaders(headers).build();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));

            CompletePackingOrderForDeliveryRequest request = new CompletePackingOrderForDeliveryRequest(UUID.fromString(body.getOrderID()));
            CompletePackingOrderForDeliveryResponse completePackingOrderForDelivery = deliveryService.completePackingOrderForDelivery(request);
            response.setMessage(completePackingOrderForDelivery.getMessage());
            response.setSuccess(completePackingOrderForDelivery.isSuccess());
        }catch (Exception e){
            e.printStackTrace();
            response.setMessage(e.getMessage());
            response.setSuccess(false);
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
    public ResponseEntity<DeliveryGetAdditionalStoresDeliveryCostResponse> getAdditionalStoresDeliveryCost(DeliveryGetAdditionalStoresDeliveryCostRequest body) {
        DeliveryGetAdditionalStoresDeliveryCostResponse response = new DeliveryGetAdditionalStoresDeliveryCostResponse();
        HttpStatus httpStatus = HttpStatus.OK;
        try{
            GetAdditionalStoresDeliveryCostRequest request = new GetAdditionalStoresDeliveryCostRequest(UUID.fromString(body.getDeliveryID()));
            GetAdditionalStoresDeliveryCostResponse getAdditionalStoresDeliveryCostResponse = deliveryService.getAdditionalStoresDeliveryCost(request);
            response.setAdditionalCost(populatePrices(getAdditionalStoresDeliveryCostResponse.getAdditionalCost()));
            response.setStores(populateStores(getAdditionalStoresDeliveryCostResponse.getStores()));
            response.setMessage(getAdditionalStoresDeliveryCostResponse.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            response.setAdditionalCost(null);
            response.setStores(null);
            response.setMessage(e.getMessage());
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
            if (getNextOrderForDriverResponse.getDelivery() == null){
                response.setDelivery(null);
                response.setMessage(getNextOrderForDriverResponse.getMessage());
            }else {
                System.out.println("ASD 1");

                DeliveryObject deliveryObject = new DeliveryObject();


                System.out.println("ASD delv id " + getNextOrderForDriverResponse.getDelivery().toString());
                deliveryObject.setDeliveryID(getNextOrderForDriverResponse.getDelivery().getDeliveryID().toString());

                System.out.println("ASD 1");

                GeoPointObject dropOffLocation = new GeoPointObject();
                dropOffLocation.setAddress(getNextOrderForDriverResponse.getDelivery().getDropOffLocation().getAddress());
                dropOffLocation.setLatitude(BigDecimal.valueOf(getNextOrderForDriverResponse.getDelivery().getDropOffLocation().getLatitude()));
                dropOffLocation.setLongitude(BigDecimal.valueOf(getNextOrderForDriverResponse.getDelivery().getDropOffLocation().getLongitude()));
//            GeoPointObject pickUpLocation = new GeoPointObject();
//            pickUpLocation.setAddress(getNextOrderForDriverResponse.getDelivery().getPickUpLocation().getAddress());
//            pickUpLocation.setLatitude(BigDecimal.valueOf(getNextOrderForDriverResponse.getDelivery().getPickUpLocation().getLatitude()));
//            pickUpLocation.setLongitude(BigDecimal.valueOf(getNextOrderForDriverResponse.getDelivery().getPickUpLocation().getLongitude()));

                deliveryObject.setDropOffLocation(dropOffLocation);
                if (getNextOrderForDriverResponse.getDelivery().getPickUpLocationOne() != null) {
                    deliveryObject.setPickUpLocationOne(populateGeoPointObject(getNextOrderForDriverResponse.getDelivery().getPickUpLocationOne()));
                }
                if (getNextOrderForDriverResponse.getDelivery().getPickUpLocationTwo() != null) {
                    deliveryObject.setPickUpLocationTwo(populateGeoPointObject(getNextOrderForDriverResponse.getDelivery().getPickUpLocationTwo()));
                }
                if (getNextOrderForDriverResponse.getDelivery().getPickUpLocationThree() != null) {
                    deliveryObject.setPickUpLocationThree(populateGeoPointObject(getNextOrderForDriverResponse.getDelivery().getPickUpLocationThree()));
                }
                if (getNextOrderForDriverResponse.getDelivery().getOrderIDOne() != null) {
                    deliveryObject.setOrderIDOne(getNextOrderForDriverResponse.getDelivery().getOrderIDOne().toString());
                }
                if (getNextOrderForDriverResponse.getDelivery().getOrderIDTwo() != null) {
                    deliveryObject.setOrderIDTwo(getNextOrderForDriverResponse.getDelivery().getOrderIDTwo().toString());
                }
                if (getNextOrderForDriverResponse.getDelivery().getOrderIDThree() != null) {
                    deliveryObject.setOrderIDOne(getNextOrderForDriverResponse.getDelivery().getOrderIDThree().toString());
                }
                deliveryObject.setCustomerID(getNextOrderForDriverResponse.getDelivery().getCustomerID().toString());
                if (getNextOrderForDriverResponse.getDelivery().getStoreOneID() != null) {
                    deliveryObject.setStoreOneID(getNextOrderForDriverResponse.getDelivery().getStoreOneID().toString());
                }
                if (getNextOrderForDriverResponse.getDelivery().getStoreTwoID() != null) {
                    deliveryObject.setStoreTwoID(getNextOrderForDriverResponse.getDelivery().getStoreTwoID().toString());
                }
                if (getNextOrderForDriverResponse.getDelivery().getStoreThreeID() != null) {
                    deliveryObject.setStoreThreeID(getNextOrderForDriverResponse.getDelivery().getStoreThreeID().toString());
                }
                if (getNextOrderForDriverResponse.getDelivery().getDriverID() != null) {
                    deliveryObject.setDriverID(getNextOrderForDriverResponse.getDelivery().getDriverID().toString());
                }

                deliveryObject.setStatus(getNextOrderForDriverResponse.getDelivery().getStatus().toString());
                deliveryObject.setCost(BigDecimal.valueOf(getNextOrderForDriverResponse.getDelivery().getCost()));
                deliveryObject.setCompleted(getNextOrderForDriverResponse.getDelivery().isCompleted());
                deliveryObject.setDeliveryDetail(populateDeliveryDetails(getNextOrderForDriverResponse.getDelivery().getDeliveryDetail()));

                response.setDelivery(deliveryObject);
                response.setMessage(getNextOrderForDriverResponse.getMessage());
            }

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
            Header header = new BasicHeader("Authorization", httpServletRequest.getHeader("Authorization"));
            List<Header> headers = new ArrayList<>();
            headers.add(header);
            CloseableHttpClient httpClient = HttpClients.custom().setDefaultHeaders(headers).build();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));

            GetDeliveryByUUIDRequest getDeliveryByUUIDRequest = new GetDeliveryByUUIDRequest(UUID.fromString(body.getDeliveryID()));
            GetDeliveryByUUIDResponse getDeliveryByUUIDResponse = deliveryService.getDeliveryByUUID(getDeliveryByUUIDRequest);

            DeliveryObject deliveryObject = new DeliveryObject();
            deliveryObject.setDeliveryID(getDeliveryByUUIDResponse.getDelivery().getDeliveryID().toString());

            GeoPointObject dropOffLocation=new GeoPointObject();
            dropOffLocation.setAddress(getDeliveryByUUIDResponse.getDelivery().getDropOffLocation().getAddress());
            dropOffLocation.setLatitude(BigDecimal.valueOf(getDeliveryByUUIDResponse.getDelivery().getDropOffLocation().getLatitude()));
            dropOffLocation.setLongitude(BigDecimal.valueOf(getDeliveryByUUIDResponse.getDelivery().getDropOffLocation().getLongitude()));
//            GeoPointObject pickUpLocation = new GeoPointObject();
//            pickUpLocation.setAddress(getDeliveryByUUIDResponse.getDelivery().getPickUpLocation().getAddress());
//            pickUpLocation.setLatitude(BigDecimal.valueOf(getDeliveryByUUIDResponse.getDelivery().getPickUpLocation().getLatitude()));
//            pickUpLocation.setLongitude(BigDecimal.valueOf(getDeliveryByUUIDResponse.getDelivery().getPickUpLocation().getLongitude()));

            deliveryObject.setDropOffLocation(dropOffLocation);
            if (getDeliveryByUUIDResponse.getDelivery().getPickUpLocationOne() != null) {
                deliveryObject.setPickUpLocationOne(populateGeoPointObject(getDeliveryByUUIDResponse.getDelivery().getPickUpLocationOne()));
            }
            if (getDeliveryByUUIDResponse.getDelivery().getPickUpLocationTwo() != null){
                deliveryObject.setPickUpLocationTwo(populateGeoPointObject(getDeliveryByUUIDResponse.getDelivery().getPickUpLocationTwo()));
            }
            if (getDeliveryByUUIDResponse.getDelivery().getPickUpLocationThree() != null){
                deliveryObject.setPickUpLocationThree(populateGeoPointObject(getDeliveryByUUIDResponse.getDelivery().getPickUpLocationThree()));
            }
            if (getDeliveryByUUIDResponse.getDelivery().getOrderIDOne() != null){
                deliveryObject.setOrderIDOne(getDeliveryByUUIDResponse.getDelivery().getOrderIDOne().toString());
            }
            if (getDeliveryByUUIDResponse.getDelivery().getOrderIDTwo() != null){
                deliveryObject.setOrderIDTwo(getDeliveryByUUIDResponse.getDelivery().getOrderIDTwo().toString());
            }
            if (getDeliveryByUUIDResponse.getDelivery().getOrderIDThree() != null){
                deliveryObject.setOrderIDOne(getDeliveryByUUIDResponse.getDelivery().getOrderIDThree().toString());
            }
            deliveryObject.setCustomerID(getDeliveryByUUIDResponse.getDelivery().getCustomerID().toString());
            if (getDeliveryByUUIDResponse.getDelivery().getStoreOneID() != null){
                deliveryObject.setStoreOneID(getDeliveryByUUIDResponse.getDelivery().getStoreOneID().toString());
            }
            if (getDeliveryByUUIDResponse.getDelivery().getStoreTwoID() != null){
                deliveryObject.setStoreTwoID(getDeliveryByUUIDResponse.getDelivery().getStoreTwoID().toString());
            }
            if (getDeliveryByUUIDResponse.getDelivery().getStoreThreeID() != null){
                deliveryObject.setStoreThreeID(getDeliveryByUUIDResponse.getDelivery().getStoreThreeID().toString());
            }
            if(getDeliveryByUUIDResponse.getDelivery().getDriverID() != null)
            {
                deliveryObject.setDriverID(getDeliveryByUUIDResponse.getDelivery().getDriverID().toString());
            }

            deliveryObject.setStatus(getDeliveryByUUIDResponse.getDelivery().getStatus().toString());
            deliveryObject.setCost(BigDecimal.valueOf(getDeliveryByUUIDResponse.getDelivery().getCost()));
            deliveryObject.setCompleted(getDeliveryByUUIDResponse.getDelivery().isCompleted());
            deliveryObject.setDeliveryDetail(populateDeliveryDetails(getDeliveryByUUIDResponse.getDelivery().getDeliveryDetail()));

            response.setDeliveryEntity(deliveryObject);
            response.setMessage(getDeliveryByUUIDResponse.getMessage());
            response.setTimestamp(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(getDeliveryByUUIDResponse.getTimestamp()));

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
    public List<GeoPointObject> populateGeoPointObjects(List<GeoPoint> locations){
        List<GeoPointObject> locationsObject = new ArrayList<>();
        for (GeoPoint location : locations){
            locationsObject.add(populateGeoPointObject(location));
        }
        return locationsObject;
    }
    public List<BigDecimal> populatePrices(List<Double> additionalCosts){
        List<BigDecimal> numberAdditionalCosts = new ArrayList<>();
        for (double cost : additionalCosts){
            numberAdditionalCosts.add(BigDecimal.valueOf(cost));
        }
        return numberAdditionalCosts;
    }
    private List<StoreObject> populateStores(List<Store> responseStores) throws NullPointerException{

        List<StoreObject> responseBody = new ArrayList<>();

        if(responseStores != null)
            for(int i = 0; i < responseStores.size(); i++){

                StoreObject currentStore = new StoreObject();

                if(responseStores.get(i).getStoreID()!=null)
                {
                    currentStore.setStoreID(responseStores.get(i).getStoreID().toString());
                }

                currentStore.setStoreBrand(responseStores.get(i).getStoreBrand());
                currentStore.setOpeningTime(responseStores.get(i).getOpeningTime());
                currentStore.setClosingTime(responseStores.get(i).getClosingTime());
                currentStore.setMaxOrders(responseStores.get(i).getMaxOrders());
                currentStore.setMaxShoppers(responseStores.get(i).getMaxShoppers());
                currentStore.setIsOpen(responseStores.get(i).getOpen());
                currentStore.setImgUrl(responseStores.get(i).getImgUrl());

                if(responseStores.get(i).getStoreLocation()!=null)
                {
                    currentStore.setStoreLocation(populateGeoPointObject(responseStores.get(i).getStoreLocation()));
                }

                responseBody.add(currentStore);

            }

        return responseBody;
    }
    public List<OrderObject> populateOrders(List<Order> orders){
        List<OrderObject> orderObjects = new ArrayList<>();
        for (Order order : orders){
            orderObjects.add(populateOrder(order));
        }
        return orderObjects;
    }
    public OrderObject populateOrder(Order order) {
        OrderObject orderObject = new OrderObject();
        if (order.getOrderID() != null)
            orderObject.setOrderID(order.getOrderID().toString());
        if (order.getUserID() != null)
            orderObject.setUserID(order.getUserID().toString());
        if (order.getStoreID() != null)
            orderObject.setStoreID(order.getStoreID().toString());
        if (order.getShopperID() != null)
            orderObject.setShopperID(order.getShopperID().toString());
        if (order.getCreateDate() != null)
            orderObject.setCreateDate(order.getCreateDate().toString());
        if (order.getProcessDate() != null)
            orderObject.setProcessDate(order.getProcessDate().toString());
        if (order.getTotalCost() != null)
            orderObject.setTotalCost(BigDecimal.valueOf(order.getTotalCost()));
        if (order.getStatus() != null)
            orderObject.setStatus(order.getStatus().toString());
        if (order.getCartItems() != null)
            orderObject.setCartItems(populateCartItems(order.getCartItems()));
        if (order.getDiscount() != null)
            orderObject.setDiscount(BigDecimal.valueOf(order.getDiscount()));
        if (order.getDeliveryAddress() != null)
            orderObject.setDeliveryAddress(populateGeoPointObject(order.getDeliveryAddress()));
        if (order.getStoreAddress() != null)
            orderObject.setStoreAddress(populateGeoPointObject(order.getStoreAddress()));
        if (order.getDriverID() != null)
            orderObject.setDriverID(order.getDriverID().toString());
        orderObject.setRequiresPharmacy(order.isRequiresPharmacy());
        return orderObject;
    }
    private List<CartItemObject> populateCartItems(List<CartItem> responseItems) throws NullPointerException {

        List<CartItemObject> responseBody = new ArrayList<>();

        for (CartItem i : responseItems) {

            CartItemObject item = new CartItemObject();
            if (i.getCartItemNo() != null) {
                item.setCartItemNo(i.getCartItemNo().toString());
            }
            item.setProductID(i.getProductID());
            item.setBarcode(i.getBarcode());
            item.setQuantity(i.getQuantity());
            item.setName(i.getName());
            if (i.getStoreID() != null)
                item.setStoreID(i.getStoreID().toString());
            item.setPrice(BigDecimal.valueOf(i.getPrice()));
            item.setImageUrl(i.getImageUrl());
            item.setBrand(i.getBrand());
            item.setSize(i.getSize());
            item.setItemType(i.getItemType());
            item.setDescription(i.getDescription());
            if (i.getOrderID() != null)
                item.setOrderID(i.getOrderID().toString());
            responseBody.add(item);

        }

        return responseBody;
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
