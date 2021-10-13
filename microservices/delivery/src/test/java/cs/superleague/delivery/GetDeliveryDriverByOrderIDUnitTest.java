package cs.superleague.delivery;

import cs.superleague.delivery.dataclass.Delivery;
import cs.superleague.delivery.dataclass.DeliveryDetail;
import cs.superleague.delivery.dataclass.DeliveryStatus;
import cs.superleague.delivery.exceptions.InvalidRequestException;
import cs.superleague.delivery.repos.DeliveryDetailRepo;
import cs.superleague.delivery.repos.DeliveryRepo;
import cs.superleague.delivery.requests.AssignDriverToDeliveryRequest;
import cs.superleague.delivery.requests.GetDeliveryDriverByOrderIDRequest;
import cs.superleague.delivery.responses.AssignDriverToDeliveryResponse;
import cs.superleague.delivery.responses.GetDeliveryDriverByOrderIDResponse;
import cs.superleague.integration.security.JwtUtil;
import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.responses.GetOrderByUUIDResponse;
import cs.superleague.payment.responses.GetOrderResponse;
import cs.superleague.shopping.dataclass.Store;
import cs.superleague.user.dataclass.Admin;
import cs.superleague.user.dataclass.Customer;
import cs.superleague.user.dataclass.Driver;
import cs.superleague.user.dataclass.UserType;
import cs.superleague.user.requests.SaveDriverToRepoRequest;
import cs.superleague.user.responses.GetDriverByUUIDResponse;
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
public class GetDeliveryDriverByOrderIDUnitTest {

    @InjectMocks
    private DeliveryServiceImpl deliveryService;

    @Mock
    DeliveryRepo deliveryRepo;
    @Mock
    DeliveryDetailRepo deliveryDetailRepo;
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

    UUID deliveryID;
    UUID orderID;
    UUID adminID;
    UUID driverID;
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
    Admin admin;
    DeliveryDetail deliveryDetail1;
    DeliveryDetail deliveryDetail2;
    Driver driver;
    GeoPoint driverLocation;
    Order order;
    List<Delivery> deliveries = new ArrayList<>();

    @BeforeEach
    void setUp() {
        cost = 0.0;
        adminID = UUID.randomUUID();
        driverID = UUID.randomUUID();
        deliveryID = UUID.randomUUID();
        orderID = UUID.randomUUID();
        time = Calendar.getInstance();
        status = DeliveryStatus.WaitingForShoppers;
        pickUpLocation = new GeoPoint(0.0, 0.0, "address");
        dropOffLocation = new GeoPoint(1.0, 1.0, "address");
        customerID = UUID.randomUUID();
        storeID = UUID.randomUUID();
        order = new Order();
        order.setOrderID(orderID);
        order.setDriverID(driverID);
        delivery = new Delivery(deliveryID, orderID, pickUpLocation, dropOffLocation, customerID, storeID, status, cost);
        delivery.setDriverID(driverID);
        customer = new Customer("Seamus", "Brennan", "u19060468@tuks.co.za", "0743149813", "Hello123$$$", "123", UserType.CUSTOMER, customerID);
        store = new Store(storeID, 1, 2, "Woolworth's", 10, 10, true, "/store/woolworths.png");
        store.setStoreLocation(pickUpLocation);
        admin = new Admin("John", "Doe", "u19060468@tuks.co.za", "0743149813", "Hello123", "123", UserType.ADMIN, adminID);
        deliveryDetail1 = new DeliveryDetail(deliveryID, time, status, "detail");
        deliveryDetail2 = new DeliveryDetail(deliveryID, Calendar.getInstance(), DeliveryStatus.CollectedByDriver, "detail");
        driver = new Driver("John", "Doe", "u19060468@tuks.co.za", "0743149813", "Hello123", "123", UserType.DRIVER, driverID);
        driverLocation = new GeoPoint(50.0, 50.0, "address");
        customer=new Customer();
        customer.setCustomerID(UUID.randomUUID());
        customer.setEmail("hello@gmail.com");
        customer.setAccountType(UserType.CUSTOMER);
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    @Description("Tests that the request object is created successfully.")
    @DisplayName("Request object creation.")
    void checkRequestObjectCreation_UnitTest(){
        GetDeliveryDriverByOrderIDRequest request = new GetDeliveryDriverByOrderIDRequest(orderID);
        assertEquals(request.getOrderID(), orderID);
    }

    @Test
    @Description("Tests null request object, should throw an error.")
    @DisplayName("Null request object")
    void nullRequestObject_UnitTest(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.getDeliveryDriverByOrderID(null));
        assertEquals("Request object is null", thrown.getMessage());
    }

    @Test
    @Description("Tests for when null parameters are passed in, should throw an exception.")
    @DisplayName("Null parameters")
    void nullParametersInRequestObject_UnitTest(){
        GetDeliveryDriverByOrderIDRequest request1 = new GetDeliveryDriverByOrderIDRequest(null);
        Throwable thrown1 = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.getDeliveryDriverByOrderID(request1));
        assertEquals("Order ID is null. Cannot get Driver.", thrown1.getMessage());
    }

    @Test
    @Description("Tests when there is an invalid orderID passed in, should throw an exception.")
    @DisplayName("Invalid orderID")
    void invalidOrderIDPassedInRequestObject_UnitTest() throws InvalidRequestException, URISyntaxException {
        GetDeliveryDriverByOrderIDRequest request1 = new GetDeliveryDriverByOrderIDRequest(UUID.randomUUID());
        GetDeliveryDriverByOrderIDResponse response = deliveryService.getDeliveryDriverByOrderID(request1);
        assertEquals("Driver not found", response.getMessage());
    }


    @Test
    @Description("Tests for when there is no order corresponding to the delivery.")
    @DisplayName("No order")
    void noOrderAssignedToDelivery_UnitTest() throws InvalidRequestException, URISyntaxException {
        delivery.setOrderIDOne(null);
        //when(orderRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(order));
        GetDeliveryDriverByOrderIDRequest request1 = new GetDeliveryDriverByOrderIDRequest(orderID);
        GetDeliveryDriverByOrderIDResponse response = deliveryService.getDeliveryDriverByOrderID(request1);
        assertEquals("Driver not found", response.getMessage());
    }

    @Test
    @Description("Tests for when driver is successfully found.")
    @DisplayName("Driver found")
    void DriverFoundInDeliveryOrder_UnitTest() throws InvalidRequestException, URISyntaxException {
        delivery.setOrderIDOne(orderID);
        delivery.setDriverID(driver.getDriverID());
        deliveries.add(delivery);

//        when(driverRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(driver));
//        when(orderRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(order));
        GetDriverByUUIDResponse getDriverByUUIDResponse = new GetDriverByUUIDResponse(driver, new Date(), "");
        String uriString = "http://"+userHost+":"+userPort+"/user/getDriverByUUID";
        URI uri = new URI(uriString);
        Map<String, Object> parts = new HashMap<>();
        parts.put("userID", delivery.getDriverID());

        ResponseEntity<GetDriverByUUIDResponse> responseEntity = new ResponseEntity<>(getDriverByUUIDResponse, HttpStatus.OK);
        when(restTemplate.postForEntity(uri,
                parts, GetDriverByUUIDResponse.class)).thenReturn(responseEntity);
        when(deliveryRepo.findAll()).thenReturn(deliveries);

        GetDeliveryDriverByOrderIDRequest request1 = new GetDeliveryDriverByOrderIDRequest(deliveryID);
        GetDeliveryDriverByOrderIDResponse response = deliveryService.getDeliveryDriverByOrderID(request1);
        assertEquals("Driver successfully retrieved", response.getMessage());
    }


}
