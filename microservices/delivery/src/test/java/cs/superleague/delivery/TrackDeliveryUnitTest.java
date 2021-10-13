package cs.superleague.delivery;

import cs.superleague.delivery.dataclass.Delivery;
import cs.superleague.delivery.dataclass.DeliveryDetail;
import cs.superleague.delivery.dataclass.DeliveryStatus;
import cs.superleague.delivery.exceptions.InvalidRequestException;
import cs.superleague.delivery.repos.DeliveryDetailRepo;
import cs.superleague.delivery.repos.DeliveryRepo;
import cs.superleague.delivery.requests.GetDeliveryStatusRequest;
import cs.superleague.delivery.requests.TrackDeliveryRequest;
import cs.superleague.delivery.responses.TrackDeliveryResponse;
import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.shopping.dataclass.Store;
import cs.superleague.user.dataclass.Admin;
import cs.superleague.user.dataclass.Customer;
import cs.superleague.user.dataclass.Driver;
import cs.superleague.user.dataclass.UserType;
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
public class TrackDeliveryUnitTest {
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

    @BeforeEach
    void setUp(){
        cost = 0.0;
        adminID = UUID.randomUUID();
        driverID = UUID.randomUUID();
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
        admin = new Admin("John", "Doe", "u19060468@tuks.co.za", "0743149813", "Hello123", "123", UserType.ADMIN, adminID);
        deliveryDetail1 = new DeliveryDetail(deliveryID, time, status, "detail");
        deliveryDetail2 = new DeliveryDetail(deliveryID, Calendar.getInstance(), DeliveryStatus.CollectedByDriver, "detail");
        driver = new Driver("John", "Doe", "u19060468@tuks.co.za", "0743149813", "Hello123", "123", UserType.DRIVER, driverID);
        driverLocation = new GeoPoint(50.0, 50.0, "address");
    }

    @AfterEach
    void tearDown(){

    }

    @Test
    @Description("Tests that the request object is created successfully.")
    @DisplayName("Request object creation.")
    void checkRequestObjectCreation_UnitTest(){
        TrackDeliveryRequest request = new TrackDeliveryRequest(deliveryID);
        assertEquals(request.getDeliveryID(), deliveryID);
    }

    @Test
    @Description("Tests for when a null request object is passed in.")
    @DisplayName("Null request")
    void nullRequestObject_UnitTest(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.trackDelivery(null));
        assertEquals("Null request object.", thrown.getMessage());
    }

    @Test
    @Description("Tests for when there is a null deliveryID in the request object")
    @DisplayName("Null deliveryID")
    void nullDeliveryIDInRequestObject_UnitTest(){
        TrackDeliveryRequest request = new TrackDeliveryRequest(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.trackDelivery(request));
        assertEquals("No delivery Id specified.", thrown.getMessage());
    }

    @Test
    @Description("Tests for when the deliveryID is invalid.")
    @DisplayName("Invalid DeliveryID")
    void invalidDeliveryIDInRequestObject_UnitTest(){
        when(deliveryRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(null));
        TrackDeliveryRequest request = new TrackDeliveryRequest(deliveryID);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.trackDelivery(request));
        assertEquals("Delivery not found in database.", thrown.getMessage());
    }

    @Test
    @Description("Tests for when there is no driver assigned to delivery.")
    @DisplayName("No driver assigned")
    void noDriverCurrentlyAssignedToDelivery_UnitTest() throws InvalidRequestException, URISyntaxException {
        when(deliveryRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(delivery));
        TrackDeliveryRequest request = new TrackDeliveryRequest(deliveryID);
        TrackDeliveryResponse response = deliveryService.trackDelivery(request);
        assertEquals(response.getMessage(), "No driver has been assigned to this delivery.");
        assertEquals(response.getCurrentLocation(), pickUpLocation);
    }

    @Test
    @Description("Tests for when there is a driverID assigned to delivery, but the driver does not exist.")
    @DisplayName("Driver assigned no longer in database")
    void driverCurrentlyAssignedToDeliveryNoLongerInDatabase_UnitTest() throws InvalidRequestException, URISyntaxException {
        delivery.setDriverID(driverID);
        when(deliveryRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(delivery));
        //when(driverRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(null));
        GetDriverByUUIDResponse getDriverByUUIDResponse = new GetDriverByUUIDResponse(null, new Date(), "message");
        String uriString = "http://"+userHost+":"+userPort+"/user/getDriverByUUID";
        URI uri = new URI(uriString);
        Map<String, Object> parts = new HashMap<String, Object>();
        parts.put("userID", delivery.getDriverID());


        ResponseEntity<GetDriverByUUIDResponse> responseEntity = new ResponseEntity<>(getDriverByUUIDResponse, HttpStatus.OK);
        when(restTemplate.postForEntity(uri,
                parts, GetDriverByUUIDResponse.class)).thenReturn(responseEntity);
        TrackDeliveryRequest request = new TrackDeliveryRequest(deliveryID);
        TrackDeliveryResponse response = deliveryService.trackDelivery(request);
        assertEquals(response.getMessage(), "No driver has been assigned to this delivery.");
        assertEquals(response.getCurrentLocation(), pickUpLocation);
    }

    @Test
    @Description("Tests for when there is a driverID assigned to delivery, but the driver is no longer on shift.")
    @DisplayName("Driver assigned no longer on shift")
    void driverCurrentlyAssignedToDeliveryNoLongerOnShift_UnitTest() throws InvalidRequestException, URISyntaxException {
        delivery.setDriverID(driverID);
        driver.setOnShift(false);
        when(deliveryRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(delivery));
        //when(driverRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(driver));
        GetDriverByUUIDResponse getDriverByUUIDResponse = new GetDriverByUUIDResponse(driver, new Date(), "message");
        String uriString = "http://"+userHost+":"+userPort+"/user/getDriverByUUID";
        URI uri = new URI(uriString);
        Map<String, Object> parts = new HashMap<String, Object>();
        parts.put("userID", delivery.getDriverID());


        ResponseEntity<GetDriverByUUIDResponse> responseEntity = new ResponseEntity<>(getDriverByUUIDResponse, HttpStatus.OK);
        when(restTemplate.postForEntity(uri,
                parts, GetDriverByUUIDResponse.class)).thenReturn(responseEntity);
        TrackDeliveryRequest request = new TrackDeliveryRequest(deliveryID);
        TrackDeliveryResponse response = deliveryService.trackDelivery(request);
        assertEquals(response.getMessage(), "No driver has been assigned to this delivery.");
        assertEquals(response.getCurrentLocation(), pickUpLocation);
    }

    @Test
    @Description("Tests for when there is a driverID assigned to delivery and the driver is still on shift.")
    @DisplayName("Gets location of driver")
    void driverCurrentlyAssignedToDeliveryGetsLocationOfDriver_UnitTest() throws InvalidRequestException, URISyntaxException {
        delivery.setDriverID(driverID);
        driver.setOnShift(true);
        driver.setCurrentAddress(driverLocation);
        when(deliveryRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(delivery));
        //when(driverRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(driver));
        GetDriverByUUIDResponse getDriverByUUIDResponse = new GetDriverByUUIDResponse(driver, new Date(), "message");
        String uriString = "http://"+userHost+":"+userPort+"/user/getDriverByUUID";
        URI uri = new URI(uriString);
        Map<String, Object> parts = new HashMap<String, Object>();
        parts.put("userID", delivery.getDriverID());


        ResponseEntity<GetDriverByUUIDResponse> responseEntity = new ResponseEntity<>(getDriverByUUIDResponse, HttpStatus.OK);
        when(restTemplate.postForEntity(uri,
                parts, GetDriverByUUIDResponse.class)).thenReturn(responseEntity);
        TrackDeliveryRequest request = new TrackDeliveryRequest(deliveryID);
        TrackDeliveryResponse response = deliveryService.trackDelivery(request);
        assertEquals(response.getMessage(), "Drivers current location.");
        assertEquals(response.getCurrentLocation(), driverLocation);
    }
}
