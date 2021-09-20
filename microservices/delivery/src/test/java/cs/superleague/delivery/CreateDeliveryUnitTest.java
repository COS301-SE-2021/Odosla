package cs.superleague.delivery;

import cs.superleague.delivery.dataclass.Delivery;
import cs.superleague.delivery.dataclass.DeliveryStatus;
import cs.superleague.delivery.exceptions.InvalidRequestException;
import cs.superleague.delivery.repos.DeliveryDetailRepo;
import cs.superleague.delivery.repos.DeliveryRepo;
import cs.superleague.delivery.requests.CreateDeliveryRequest;
import cs.superleague.delivery.responses.CreateDeliveryResponse;
import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.dataclass.OrderStatus;
import cs.superleague.payment.dataclass.OrderType;
import cs.superleague.payment.responses.GetOrderByUUIDResponse;
import cs.superleague.payment.responses.GetOrderResponse;
import cs.superleague.shopping.dataclass.Store;
import cs.superleague.shopping.responses.GetStoreByUUIDResponse;
import cs.superleague.user.dataclass.Customer;
import cs.superleague.user.dataclass.UserType;
import cs.superleague.user.responses.GetCustomerByUUIDResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateDeliveryUnitTest {
    @InjectMocks
    private DeliveryServiceImpl deliveryService;
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
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    DeliveryRepo deliveryRepo;

    UUID deliveryID;
    UUID orderID;
    UUID shopperID;
    Calendar time;
    Delivery delivery;
    GeoPoint invalidDropOffLocation;
    GeoPoint pickUpLocation;
    GeoPoint dropOffLocation;
    UUID customerID;
    UUID storeID;
    DeliveryStatus status;
    double cost;
    Customer customer;
    Store store;
    Order order;

    @BeforeEach
    void setUp(){
        cost = 0.0;
        deliveryID = UUID.randomUUID();
        orderID = UUID.randomUUID();
        time = Calendar.getInstance();
        status = DeliveryStatus.WaitingForShoppers;
        invalidDropOffLocation = new GeoPoint(-91.0, -91.0, "address");
        pickUpLocation = new GeoPoint(0.0, 0.0, "address");
        dropOffLocation = new GeoPoint(1.0, 1.0, "address");
        customerID = UUID.randomUUID();
        storeID = UUID.randomUUID();
        delivery = new Delivery(deliveryID, orderID, pickUpLocation, dropOffLocation, customerID, storeID, status, cost);
        customer = new Customer("Seamus", "Brennan", "u19060468@tuks.co.za", "0743149813", "Hello123$$$", "123", UserType.CUSTOMER,customerID);
        store = new Store(storeID, 1, 2, "Woolworth's", 10, 10, true, "");
        store.setStoreLocation(pickUpLocation);
        order = new Order(orderID, customerID, storeID, shopperID, new Date(), null, 50.0, OrderType.DELIVERY, OrderStatus.PURCHASED, 0.0, dropOffLocation, pickUpLocation, false);
    }

    @AfterEach
    void tearDown(){

    }

    @Test
    @Description("Tests that the request object is created successfully.")
    @DisplayName("Request object creation.")
    void checkRequestObjectCreation_UnitTest(){
        CreateDeliveryRequest request = new CreateDeliveryRequest(orderID, customerID, storeID, time, dropOffLocation);
        assertEquals(request.getCustomerID(), customerID);
        assertEquals(request.getOrderID(), orderID);
        assertEquals(request.getPlaceOfDelivery(), dropOffLocation);
        assertEquals(request.getTimeOfDelivery(), time);
        assertEquals(request.getStoreID(), storeID);
    }

    @Test
    @Description("Tests null request object, should throw an error.")
    @DisplayName("Null request object")
    void nullRequestObject_UnitTest(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.createDelivery(null));
        assertEquals("Null request object.", thrown.getMessage());
    }

    @Test
    @Description("Tests for null parameters, should throw an exceprion.")
    @DisplayName("Tests for null parameters")
    void requestObjectContainsNullParameters_UnitTest(){
        CreateDeliveryRequest request1 = new CreateDeliveryRequest(null, customerID, storeID, time, dropOffLocation);
        Throwable thrown1 = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.createDelivery(request1));
        assertEquals("Missing parameters.", thrown1.getMessage());
        CreateDeliveryRequest request2 = new CreateDeliveryRequest(orderID, null, storeID, time, dropOffLocation);
        Throwable thrown2 = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.createDelivery(request2));
        assertEquals("Missing parameters.", thrown2.getMessage());
        CreateDeliveryRequest request3 = new CreateDeliveryRequest(orderID, customerID, null, time, dropOffLocation);
        Throwable thrown3 = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.createDelivery(request3));
        assertEquals("Missing parameters.", thrown3.getMessage());
        CreateDeliveryRequest request5 = new CreateDeliveryRequest(orderID, customerID, storeID, time, null);
        Throwable thrown5 = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.createDelivery(request5));
        assertEquals("Missing parameters.", thrown5.getMessage());
    }

    @Test
    @Description("Tests for when the customer ID is invalid.")
    @DisplayName("Invalid customer ID")
    void customerIDInRequestObjectNotValid_UnitTest() throws URISyntaxException {
        //when(customerRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(null));
        String uriString = "http://"+userHost+":"+userPort+"/user/getCustomerByUUID";
        URI uri = new URI(uriString);
        Map<String, Object> parts = new HashMap<>();
        parts.put("userID", customerID);
        GetCustomerByUUIDResponse getCustomerByUUIDResponse = new GetCustomerByUUIDResponse(null, null, null);
        ResponseEntity<GetCustomerByUUIDResponse> responseEntity = new ResponseEntity<>(getCustomerByUUIDResponse, HttpStatus.OK);
                when(restTemplate.postForEntity(uri,
                parts, GetCustomerByUUIDResponse.class)).thenReturn(responseEntity);
        CreateDeliveryRequest request1 = new CreateDeliveryRequest(orderID, customerID, storeID, time, dropOffLocation);
        Throwable thrown1 = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.createDelivery(request1));
        assertEquals("Invalid customerID.", thrown1.getMessage());
    }

    @Test
    @Description("Tests for when the store ID is invalid.")
    @DisplayName("Invalid store ID")
    void storeIDInRequestObjectNotValid_UnitTest() throws URISyntaxException {

        //when(customerRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(customer));
        String uriString = "http://"+userHost+":"+userPort+"/user/getCustomerByUUID";
        URI uri = new URI(uriString);
        Map<String, Object> parts = new HashMap<>();
        parts.put("userID", customerID);
        GetCustomerByUUIDResponse getCustomerByUUIDResponse = new GetCustomerByUUIDResponse(customer, new Date(), "null");
        ResponseEntity<GetCustomerByUUIDResponse> responseEntity = new ResponseEntity<>(getCustomerByUUIDResponse, HttpStatus.OK);
        when(restTemplate.postForEntity(uri,
                parts, GetCustomerByUUIDResponse.class)).thenReturn(responseEntity);
        //when(orderRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(order));
        GetOrderByUUIDResponse getOrderResponse = new GetOrderByUUIDResponse(order,  new Date(), "null");
        uriString = "http://"+paymentHost+":"+paymentPort+"/payment/getOrderByUUID";
        uri = new URI(uriString);
        Map<String, Object> orderRequest = new HashMap<>();
        orderRequest.put("orderID", orderID);


        ResponseEntity<GetOrderByUUIDResponse> responseEntityOrder = new ResponseEntity<>(getOrderResponse, HttpStatus.OK);
        when(restTemplate.postForEntity(uri,
                orderRequest, GetOrderByUUIDResponse.class)).thenReturn(responseEntityOrder);
        //when(storeRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(null));
        GetStoreByUUIDResponse getStoreByUUIDResponse = new GetStoreByUUIDResponse(null, null, null);
        uriString = "http://"+shoppingHost+":"+shoppingPort+"/shopping/getStoreByUUID";
        uri = new URI(uriString);
        Map<String, Object> storeRequest = new HashMap<>();
        storeRequest.put("StoreID", storeID);


        ResponseEntity<GetStoreByUUIDResponse> responseEntityStore = new ResponseEntity<>(getStoreByUUIDResponse, HttpStatus.OK);
        when(restTemplate.postForEntity(uri,
                storeRequest, GetStoreByUUIDResponse.class)).thenReturn(responseEntityStore);
        CreateDeliveryRequest request1 = new CreateDeliveryRequest(orderID, customerID, storeID, time, dropOffLocation);
        Throwable thrown1 = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.createDelivery(request1));
        assertEquals("Invalid storeID.", thrown1.getMessage());
    }

    @Test
    @Description("Tests for when the orderID is invalid.")
    @DisplayName("Invalid orderID")
    void invalidOrderIDPassedInRequestObject_UnitTest() throws URISyntaxException {
        String uriString = "http://"+userHost+":"+userPort+"/user/getCustomerByUUID";
        URI uri = new URI(uriString);
        Map<String, Object> parts = new HashMap<>();
        parts.put("userID", customerID);
        GetCustomerByUUIDResponse getCustomerByUUIDResponse = new GetCustomerByUUIDResponse(customer, new Date(), "null");
        ResponseEntity<GetCustomerByUUIDResponse> responseEntity = new ResponseEntity<>(getCustomerByUUIDResponse, HttpStatus.OK);
        when(restTemplate.postForEntity(uri,
                parts, GetCustomerByUUIDResponse.class)).thenReturn(responseEntity);
//        when(orderRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(null));
//        when(customerRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(customer));
        GetOrderByUUIDResponse getOrderResponse = new GetOrderByUUIDResponse(null, new Date(), "null");
        uriString = "http://"+paymentHost+":"+paymentPort+"/payment/getOrderByUUID";
        uri = new URI(uriString);
        Map<String, Object> orderRequest = new HashMap<>();
        orderRequest.put("orderID", orderID);


        ResponseEntity<GetOrderByUUIDResponse> responseEntityOrder = new ResponseEntity<>(getOrderResponse, HttpStatus.OK);
        when(restTemplate.postForEntity(uri,
                orderRequest, GetOrderByUUIDResponse.class)).thenReturn(responseEntityOrder);
        CreateDeliveryRequest request1 = new CreateDeliveryRequest(orderID, customerID, storeID, time, dropOffLocation);
        Throwable thrown1 = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.createDelivery(request1));
        assertEquals("Invalid orderID.", thrown1.getMessage());
    }

    @Test
    @Description("Tests for when the store does not contain a valid location.")
    @DisplayName("Invalid store location")
    void invalidStoreLocationInRequestObject_UnitTest() throws URISyntaxException {
//        when(orderRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(order));
        store.setStoreLocation(null);
//        when(customerRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(customer));
//        when(storeRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(store));
        String uriString = "http://"+userHost+":"+userPort+"/user/getCustomerByUUID";
        URI uri = new URI(uriString);
        Map<String, Object> parts = new HashMap<>();
        parts.put("userID", customerID);
        GetCustomerByUUIDResponse getCustomerByUUIDResponse = new GetCustomerByUUIDResponse(customer, new Date(), "null");
        ResponseEntity<GetCustomerByUUIDResponse> responseEntity = new ResponseEntity<>(getCustomerByUUIDResponse, HttpStatus.OK);
        when(restTemplate.postForEntity(uri,
                parts, GetCustomerByUUIDResponse.class)).thenReturn(responseEntity);

        GetOrderByUUIDResponse getOrderResponse = new GetOrderByUUIDResponse(order, new Date(), "null");
        uriString = "http://"+paymentHost+":"+paymentPort+"/payment/getOrderByUUID";
        uri = new URI(uriString);
        Map<String, Object> orderRequest = new HashMap<>();
        orderRequest.put("orderID", orderID);
        ResponseEntity<GetOrderByUUIDResponse> responseEntityOrder = new ResponseEntity<>(getOrderResponse, HttpStatus.OK);
        when(restTemplate.postForEntity(uri,
                orderRequest, GetOrderByUUIDResponse.class)).thenReturn(responseEntityOrder);

        GetStoreByUUIDResponse getStoreByUUIDResponse = new GetStoreByUUIDResponse(store, new Date(), "null");
        uriString = "http://"+shoppingHost+":"+shoppingPort+"/shopping/getStoreByUUID";
        uri = new URI(uriString);
        Map<String, Object> storeRequest = new HashMap<>();
        storeRequest.put("StoreID", storeID);
        ResponseEntity<GetStoreByUUIDResponse> responseEntityStore = new ResponseEntity<>(getStoreByUUIDResponse, HttpStatus.OK);
        when(restTemplate.postForEntity(uri,
                storeRequest, GetStoreByUUIDResponse.class)).thenReturn(responseEntityStore);
        CreateDeliveryRequest request1 = new CreateDeliveryRequest(orderID, customerID, storeID, time, invalidDropOffLocation);
        Throwable thrown1 = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.createDelivery(request1));
        assertEquals("Store has no location set.", thrown1.getMessage());
    }

    @Test
    @Description("Tests for when the geoPoints are invalid.")
    @DisplayName("Invalid geoPoints")
    void invalidGeoPointsInRequestObject_UnitTest() throws URISyntaxException {
//        when(orderRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(order));
//        when(customerRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(customer));
//        when(storeRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(store));
        String uriString = "http://"+userHost+":"+userPort+"/user/getCustomerByUUID";
        URI uri = new URI(uriString);
        Map<String, Object> parts = new HashMap<>();
        parts.put("userID", customerID);
        GetCustomerByUUIDResponse getCustomerByUUIDResponse = new GetCustomerByUUIDResponse(customer, new Date(), "null");
        ResponseEntity<GetCustomerByUUIDResponse> responseEntity = new ResponseEntity<>(getCustomerByUUIDResponse, HttpStatus.OK);
        when(restTemplate.postForEntity(uri,
                parts, GetCustomerByUUIDResponse.class)).thenReturn(responseEntity);

        GetOrderByUUIDResponse getOrderResponse = new GetOrderByUUIDResponse(order,  new Date(), "null");
        uriString = "http://"+paymentHost+":"+paymentPort+"/payment/getOrderByUUID";
        uri = new URI(uriString);
        Map<String, Object> orderRequest = new HashMap<>();
        orderRequest.put("orderID", orderID);
        ResponseEntity<GetOrderByUUIDResponse> responseEntityOrder = new ResponseEntity<>(getOrderResponse, HttpStatus.OK);
        when(restTemplate.postForEntity(uri,
                orderRequest, GetOrderByUUIDResponse.class)).thenReturn(responseEntityOrder);

        GetStoreByUUIDResponse getStoreByUUIDResponse = new GetStoreByUUIDResponse(store, new Date(), "null");
        uriString = "http://"+shoppingHost+":"+shoppingPort+"/shopping/getStoreByUUID";
        uri = new URI(uriString);
        Map<String, Object> storeRequest = new HashMap<>();
        storeRequest.put("StoreID", storeID);
        ResponseEntity<GetStoreByUUIDResponse> responseEntityStore = new ResponseEntity<>(getStoreByUUIDResponse, HttpStatus.OK);
        when(restTemplate.postForEntity(uri,
                storeRequest, GetStoreByUUIDResponse.class)).thenReturn(responseEntityStore);
        CreateDeliveryRequest request1 = new CreateDeliveryRequest(orderID, customerID, storeID, time, invalidDropOffLocation);
        Throwable thrown1 = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.createDelivery(request1));
        assertEquals("Invalid geoPoints.", thrown1.getMessage());
    }

    @Test
    @Description("Tests for when all the data is valid.")
    @DisplayName("Successful run")
    void deliveryCreatedSuccessfully_UnitTest() throws InvalidRequestException, URISyntaxException {
        String uriString = "http://"+userHost+":"+userPort+"/user/getCustomerByUUID";
        URI uri = new URI(uriString);
        Map<String, Object> parts = new HashMap<>();
        parts.put("userID", customerID);
        GetCustomerByUUIDResponse getCustomerByUUIDResponse = new GetCustomerByUUIDResponse(customer, new Date(), "null");
        ResponseEntity<GetCustomerByUUIDResponse> responseEntity = new ResponseEntity<>(getCustomerByUUIDResponse, HttpStatus.OK);
        when(restTemplate.postForEntity(uri,
                parts, GetCustomerByUUIDResponse.class)).thenReturn(responseEntity);
        //when(orderRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(order));
        GetOrderByUUIDResponse getOrderResponse = new GetOrderByUUIDResponse(order,  new Date(), "null");
        uriString = "http://"+paymentHost+":"+paymentPort+"/payment/getOrderByUUID";
        uri = new URI(uriString);
        Map<String, Object> orderRequest = new HashMap<>();
        orderRequest.put("orderID", orderID);


        ResponseEntity<GetOrderByUUIDResponse> responseEntityOrder = new ResponseEntity<>(getOrderResponse, HttpStatus.OK);
        when(restTemplate.postForEntity(uri,
                orderRequest, GetOrderByUUIDResponse.class)).thenReturn(responseEntityOrder);
//        when(customerRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(customer));
//        when(storeRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(store));
        GetStoreByUUIDResponse getStoreByUUIDResponse = new GetStoreByUUIDResponse(store, new Date(), "null");
        uriString = "http://"+shoppingHost+":"+shoppingPort+"/shopping/getStoreByUUID";
        uri = new URI(uriString);
        Map<String, Object> storeRequest = new HashMap<>();
        storeRequest.put("StoreID", storeID);


        ResponseEntity<GetStoreByUUIDResponse> responseEntityStore = new ResponseEntity<>(getStoreByUUIDResponse, HttpStatus.OK);
        when(restTemplate.postForEntity(uri,
                storeRequest, GetStoreByUUIDResponse.class)).thenReturn(responseEntityStore);
        CreateDeliveryRequest request1 = new CreateDeliveryRequest(orderID, customerID, storeID, time, dropOffLocation);
        CreateDeliveryResponse response = deliveryService.createDelivery(request1);
        assertEquals(response.getMessage(), "Delivery request placed.");
        assertEquals(response.isSuccess(), true);
    }
}
