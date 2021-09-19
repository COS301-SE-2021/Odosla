package cs.superleague.notification;

import cs.superleague.notifications.exceptions.InvalidRequestException;
import cs.superleague.notifications.repos.NotificationRepo;
import cs.superleague.notifications.requests.CreateNotificationRequest;
import cs.superleague.notifications.responses.CreateNotificationResponse;
import cs.superleague.notifications.NotificationServiceImpl;
import cs.superleague.user.dataclass.*;
import cs.superleague.user.responses.GetAdminByUUIDResponse;
import cs.superleague.user.responses.GetCustomerByUUIDResponse;
import cs.superleague.user.responses.GetDriverByUUIDResponse;
import cs.superleague.user.responses.GetShopperByUUIDResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
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
public class CreateNotificationUnitTest {
    @InjectMocks
    private NotificationServiceImpl notificationServiceImpl;

    @Mock
    private NotificationRepo notificationRepo;

    @Mock
    private RestTemplate restTemplate;

    @Value("${userHost}")
    private String userHost;
    @Value("${userPort}")
    private String userPort;


    UUID adminID;
    UUID customerID;
    UUID driverID;
    UUID shopperID;
    Admin admin;
    Customer customer;
    Driver driver;
    Shopper shopper;

    @BeforeEach
    void setUp() {
        adminID = UUID.randomUUID();
        customerID = UUID.randomUUID();
        driverID = UUID.randomUUID();
        shopperID = UUID.randomUUID();
        admin = new Admin("John", "Doe", "u19060468tuks.co.za", "0743149813", "Hello123", "123", UserType.ADMIN, adminID);
        customer = new Customer("John", "Doe", "u19060468tuks.co.za", "0743149813", "Hello123", "123", UserType.CUSTOMER, customerID);
        driver = new Driver("John", "Doe", "u19060468tuks.co.za", "0743149813", "Hello123", "123", UserType.DRIVER, driverID);
        shopper = new Shopper("John", "Doe", "u19060468tuks.co.za", "0743149813", "Hello123", "123", UserType.SHOPPER, shopperID);
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    @Description("Tests creation of the request object")
    @DisplayName("Request object creation")
    void createNotificationRequestObjectCreation_UnitTest(){
        Map<String, String> properties = new HashMap<>();
        properties.put("NotificationType", "delivery");
        properties.put("Subject", "Odosla");
        properties.put("UserID", adminID.toString());
        properties.put("UserType", UserType.ADMIN.toString());
        properties.put("ContactDetails", "u19060468@tuks.co.za");
        Calendar time = Calendar.getInstance();
        CreateNotificationRequest request = new CreateNotificationRequest("payLoad", time, properties);
        assertEquals(request.getProperties(), properties);
        assertEquals(request.getPayLoad(), "payLoad");
        assertEquals(request.getCreatedDateTime(), time);
    }

    @Test
    @Description("Tests when the request object is null that the exception is thrown")
    @DisplayName("Null request object")
    void requestIsNull_UnitTest(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationServiceImpl.createNotification(null));
        assertEquals("Null request object.", thrown.getMessage());
    }

    @Test
    @Description("Tests for when the payLoad is null, an exception should be thrown")
    @DisplayName("Null payLoad")
    void NullPayLoadInRequestObject_UnitTest(){
        Map<String, String> properties = new HashMap<>();
        properties.put("NotificationType", "delivery");
        properties.put("Subject", "Odosla");
        properties.put("ContactDetails", "u19060468@tuks.co.za");
        properties.put("UserID", adminID.toString());
        properties.put("UserType", UserType.ADMIN.toString());
        Calendar time = Calendar.getInstance();
        CreateNotificationRequest request = new CreateNotificationRequest(null, time, properties);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationServiceImpl.createNotification(request));
        assertEquals("Null parameters.", thrown.getMessage());
    }

    @Test
    @Description("Tests for when the time is null, an exception should be thrown")
    @DisplayName("Null date and time")
    void NullDateAndTimeInRequestObject_UnitTest(){
        Map<String, String> properties = new HashMap<>();
        properties.put("NotificationType", "delivery");
        properties.put("Subject", "Odosla");
        properties.put("ContactDetails", "u19060468@tuks.co.za");
        properties.put("UserID", adminID.toString());
        properties.put("UserType", UserType.ADMIN.toString());
        Calendar time = Calendar.getInstance();
        CreateNotificationRequest request = new CreateNotificationRequest("PayLoad", null, properties);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationServiceImpl.createNotification(request));
        assertEquals("Null parameters.", thrown.getMessage());
    }

    @Test
    @Description("Tests for when the properties is null, an exception should be thrown")
    @DisplayName("Null Properties")
    void NullPropertiesInRequestObject_UnitTest(){
        Map<String, String> properties = new HashMap<>();
        properties.put("NotificationType", "delivery");
        properties.put("Subject", "Odosla");
        properties.put("ContactDetails", "u19060468@tuks.co.za");
        properties.put("UserID", adminID.toString());
        properties.put("UserType", UserType.ADMIN.toString());
        Calendar time = Calendar.getInstance();
        CreateNotificationRequest request = new CreateNotificationRequest("PayLoad", time, null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationServiceImpl.createNotification(request));
        assertEquals("Null parameters.", thrown.getMessage());
    }

    @Test
    @Description("Tests for when the parameters are empty, an exception should be thrown")
    @DisplayName("Empty payLoad")
    void EmptyPayLoadInRequestObject_UnitTest(){
        Map<String, String> properties = new HashMap<>();
        properties.put("NotificationType", "delivery");
        properties.put("Subject", "Odosla");
        properties.put("ContactDetails", "u19060468@tuks.co.za");
        properties.put("UserID", adminID.toString());
        properties.put("UserType", UserType.ADMIN.toString());
        Calendar time = Calendar.getInstance();
        CreateNotificationRequest request = new CreateNotificationRequest("", time, properties);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationServiceImpl.createNotification(request));
        assertEquals("Empty parameters.", thrown.getMessage());
    }

    @Test
    @Description("Tests for when the notification type is null, an exception should be thrown")
    @DisplayName("Null NotificationType")
    void NullNotificationTypeInRequestObject_UnitTest(){
        Map<String, String> properties = new HashMap<>();
        properties.put("Subject", "Odosla");
        properties.put("ContactDetails", "u19060468@tuks.co.za");
        properties.put("UserID", adminID.toString());
        properties.put("UserType", UserType.ADMIN.toString());
        Calendar time = Calendar.getInstance();
        CreateNotificationRequest request = new CreateNotificationRequest("payLoad", time, properties);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationServiceImpl.createNotification(request));
        assertEquals("Null notification type.", thrown.getMessage());
    }

    @Test
    @Description("Tests for when the notification type is invalid, an exception should be thrown")
    @DisplayName("Invalid NotificationType")
    void InvalidNotificationTypeInRequestObject_UnitTest(){
        Map<String, String> properties = new HashMap<>();
        properties.put("NotificationType", "shipping");
        properties.put("Subject", "Odosla");
        properties.put("ContactDetails", "u19060468@tuks.co.za");
        properties.put("UserID", adminID.toString());
        properties.put("UserType", UserType.ADMIN.toString());
        Calendar time = Calendar.getInstance();
        CreateNotificationRequest request = new CreateNotificationRequest("payLoad", time, properties);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationServiceImpl.createNotification(request));
        assertEquals("Invalid notification type.", thrown.getMessage());
    }

    @Test
    @Description("Tests for when the user type is invalid, an exception should be thrown")
    @DisplayName("Invalid UserType")
    void InvalidUserTypeInRequestObject_UnitTest(){
        Map<String, String> properties = new HashMap<>();
        properties.put("NotificationType", "delivery");
        properties.put("Subject", "Odosla");
        properties.put("ContactDetails", "u19060468@tuks.co.za");
        properties.put("UserID", adminID.toString());
        properties.put("UserType", "assistant");
        Calendar time = Calendar.getInstance();
        CreateNotificationRequest request = new CreateNotificationRequest("payLoad", time, properties);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationServiceImpl.createNotification(request));
        assertEquals("Invalid UserType.", thrown.getMessage());
    }

    @Test
    @Description("Tests for when the userID is null, an exception should be thrown")
    @DisplayName("Null userID")
    void NullUserIDInRequestObject_UnitTest(){
        Map<String, String> properties = new HashMap<>();
        properties.put("NotificationType", "delivery");
        properties.put("Subject", "Odosla");
        properties.put("ContactDetails", "u19060468@tuks.co.za");
        properties.put("UserType", UserType.ADMIN.toString());
        Calendar time = Calendar.getInstance();
        CreateNotificationRequest request = new CreateNotificationRequest("payLoad", time, properties);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationServiceImpl.createNotification(request));
        assertEquals("Null UserID.", thrown.getMessage());
    }

    @Test
    @Description("Tests for when the userID is not in the database, an exception should be thrown")
    @DisplayName("Invalid userID")
    void InvalidUserIDInRequestObject_UnitTest() throws URISyntaxException {
        Map<String, String> properties = new HashMap<>();
        properties.put("NotificationType", "delivery");
        properties.put("Subject", "Odosla");
        properties.put("ContactDetails", "u19060468@tuks.co.za");
        properties.put("UserID", adminID.toString());
        properties.put("UserType", UserType.ADMIN.toString());
        Calendar time = Calendar.getInstance();
        //when(adminRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(null));
        GetAdminByUUIDResponse getAdminByUUIDResponse = new GetAdminByUUIDResponse(null, new Date(), "");
        ResponseEntity<GetAdminByUUIDResponse> responseEntity = new ResponseEntity<>(getAdminByUUIDResponse, HttpStatus.OK);
        String uri = "http://"+userHost+":"+userPort+"/user/getAdminByUUID";
        URI uri1 = new URI(uri);
        Map<String, Object> parts = new HashMap<String, Object>();
        parts.put("userID", adminID.toString());
        when(restTemplate.postForEntity(uri1, parts, GetAdminByUUIDResponse.class)).thenReturn(responseEntity);
        CreateNotificationRequest request = new CreateNotificationRequest("payLoad", time, properties);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationServiceImpl.createNotification(request));
        assertEquals("User does not exist in database.", thrown.getMessage());
    }

    @Test
    @Description("Successful adding of a notification for an admin")
    @DisplayName("Successful adding of notification for admin")
    void SuccessfulAddingOfNotificationForAdmin_UnitTest() throws InvalidRequestException, URISyntaxException {
        Map<String, String> properties = new HashMap<>();
        properties.put("NotificationType", "delivery");
        properties.put("Subject", "Odosla");
        properties.put("ContactDetails", "u19060468@tuks.co.za");
        properties.put("UserID", adminID.toString());
        properties.put("UserType", UserType.ADMIN.toString());
        Calendar time = Calendar.getInstance();
        //when(adminRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(admin));
        GetAdminByUUIDResponse getAdminByUUIDResponse = new GetAdminByUUIDResponse(admin, new Date(), "");
        ResponseEntity<GetAdminByUUIDResponse> responseEntity = new ResponseEntity<>(getAdminByUUIDResponse, HttpStatus.OK);
        String uri = "http://"+userHost+":"+userPort+"/user/getAdminByUUID";
        URI uri1 = new URI(uri);
        Map<String, Object> parts = new HashMap<String, Object>();
        parts.put("userID", adminID.toString());
        when(restTemplate.postForEntity(uri1, parts, GetAdminByUUIDResponse.class)).thenReturn(responseEntity);
        CreateNotificationRequest request = new CreateNotificationRequest("payLoad", time, properties);
        CreateNotificationResponse response =  notificationServiceImpl.createNotification(request);
        assertEquals(response.getResponseMessage(), "Notification successfully created.");
    }

    @Test
    @Description("Successful adding of a notification for a customer")
    @DisplayName("Successful adding of notification for customer")
    void SuccessfulAddingOfNotificationForCustomer_UnitTest() throws InvalidRequestException, URISyntaxException {
        Map<String, String> properties = new HashMap<>();
        properties.put("NotificationType", "delivery");
        properties.put("Subject", "Odosla");
        properties.put("ContactDetails", "u19060468@tuks.co.za");
        properties.put("UserID", customerID.toString());
        properties.put("UserType", UserType.CUSTOMER.toString());
        Calendar time = Calendar.getInstance();
        //when(customerRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(customer));
        GetCustomerByUUIDResponse getCustomerByUUIDResponse = new GetCustomerByUUIDResponse(customer, new Date(), "");
        ResponseEntity<GetCustomerByUUIDResponse> responseEntity = new ResponseEntity<>(getCustomerByUUIDResponse, HttpStatus.OK);
        String uri = "http://"+userHost+":"+userPort+"/user/getCustomerByUUID";
        URI uri1 = new URI(uri);
        Map<String, Object> parts = new HashMap<String, Object>();
        parts.put("userID", customerID.toString());
        when(restTemplate.postForEntity(uri1, parts, GetCustomerByUUIDResponse.class)).thenReturn(responseEntity);
        CreateNotificationRequest request = new CreateNotificationRequest("payLoad", time, properties);
        CreateNotificationResponse response =  notificationServiceImpl.createNotification(request);
        assertEquals(response.getResponseMessage(), "Notification successfully created.");
    }

    @Test
    @Description("Successful adding of a notification for a driver")
    @DisplayName("Successful adding of notification for driver")
    void SuccessfulAddingOfNotificationForDriver_UnitTest() throws InvalidRequestException, URISyntaxException {
        Map<String, String> properties = new HashMap<>();
        properties.put("NotificationType", "delivery");
        properties.put("Subject", "Odosla");
        properties.put("ContactDetails", "u19060468@tuks.co.za");
        properties.put("UserID", driverID.toString());
        properties.put("UserType", UserType.DRIVER.toString());
        Calendar time = Calendar.getInstance();
        //when(driverRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(driver));
        GetDriverByUUIDResponse getDriverByUUIDResponse = new GetDriverByUUIDResponse(driver, new Date(), "");
        ResponseEntity<GetDriverByUUIDResponse> responseEntity = new ResponseEntity<>(getDriverByUUIDResponse, HttpStatus.OK);
        String uri = "http://"+userHost+":"+userPort+"/user/getDriverByUUID";
        URI uri1 = new URI(uri);
        Map<String, Object> parts = new HashMap<String, Object>();
        parts.put("userID", driverID.toString());
        when(restTemplate.postForEntity(uri1, parts, GetDriverByUUIDResponse.class)).thenReturn(responseEntity);
        CreateNotificationRequest request = new CreateNotificationRequest("payLoad", time, properties);
        CreateNotificationResponse response =  notificationServiceImpl.createNotification(request);
        assertEquals(response.getResponseMessage(), "Notification successfully created.");
    }

    @Test
    @Description("Successful adding of a notification for a shopper")
    @DisplayName("Successful adding of notification for shopper")
    void SuccessfulAddingOfNotificationForShopper_UnitTest() throws InvalidRequestException, URISyntaxException {
        Map<String, String> properties = new HashMap<>();
        properties.put("NotificationType", "delivery");
        properties.put("Subject", "Odosla");
        properties.put("ContactDetails", "u19060468@tuks.co.za");
        properties.put("UserID", shopperID.toString());
        properties.put("UserType", UserType.SHOPPER.toString());
        Calendar time = Calendar.getInstance();
        //when(shopperRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(shopper));
        GetShopperByUUIDResponse getShopperByUUIDResponse = new GetShopperByUUIDResponse(shopper, new Date(), "");
        ResponseEntity<GetShopperByUUIDResponse> responseEntity = new ResponseEntity<>(getShopperByUUIDResponse, HttpStatus.OK);
        String uri = "http://"+userHost+":"+userPort+"/user/getShopperByUUID";
        URI uri1 = new URI(uri);
        Map<String, Object> parts = new HashMap<String, Object>();
        parts.put("userID", shopperID.toString());
        when(restTemplate.postForEntity(uri1, parts, GetShopperByUUIDResponse.class)).thenReturn(responseEntity);
        CreateNotificationRequest request = new CreateNotificationRequest("payLoad", time, properties);
        CreateNotificationResponse response =  notificationServiceImpl.createNotification(request);
        assertEquals(response.getResponseMessage(), "Notification successfully created.");
    }

}
