package cs.superleague.notification;

import cs.superleague.notifications.NotificationServiceImpl;
import cs.superleague.notifications.repos.NotificationRepo;
import cs.superleague.notifications.requests.SendEmailNotificationRequest;
import cs.superleague.user.dataclass.*;
import cs.superleague.user.responses.GetAdminByUUIDResponse;
import cs.superleague.user.responses.GetCustomerByUUIDResponse;
import cs.superleague.user.responses.GetDriverByUUIDResponse;
import cs.superleague.user.responses.GetShopperByUUIDResponse;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Description;
import cs.superleague.notifications.exceptions.InvalidRequestException;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SendEmailNotificationUnitTest {
    @InjectMocks
    private NotificationServiceImpl notificationService;

    @Mock
    private JavaMailSender javaMailSender;

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
    UUID adminID2;
    Admin adminIncorrectEmail;
    Customer customerIncorrectEmail;
    Driver driverIncorrectEmail;
    Shopper shopperIncorrectEmail;
    Admin adminCorrectEmail;

    @BeforeEach
    void setUp() {
        adminID = UUID.randomUUID();
        customerID = UUID.randomUUID();
        driverID = UUID.randomUUID();
        shopperID = UUID.randomUUID();
        adminIncorrectEmail = new Admin("John", "Doe", "u19060468tuks.co.za", "0743149813", "Hello123", "123", UserType.ADMIN, adminID);
        customerIncorrectEmail = new Customer("John", "Doe", "u19060468tuks.co.za", "0743149813", "Hello123", "123", UserType.CUSTOMER, customerID);
        driverIncorrectEmail = new Driver("John", "Doe", "u19060468tuks.co.za", "0743149813", "Hello123", "123", UserType.DRIVER, driverID);
        shopperIncorrectEmail = new Shopper("John", "Doe", "u19060468tuks.co.za", "0743149813", "Hello123", "123", UserType.SHOPPER, shopperID);
        adminCorrectEmail = new Admin("John", "Doe", "u19060468@tuks.co.za", "0743149813", "Hello123", "123", UserType.ADMIN, adminID2);

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Description("Tests that the request object is being created successfully")
    @DisplayName("Object creation")
    void requestObjectCreation_UnitTest(){
        Map<String, String> properties = new HashMap<>();
        properties.put("Type","delivery");
        properties.put("Subject","Odosla");
        properties.put("UserID",adminID.toString());
        properties.put("UserType", UserType.ADMIN.toString());
        SendEmailNotificationRequest request = new SendEmailNotificationRequest("message", properties);
        assertEquals(request.getMessage(), "message");
        assertEquals(request.getSubject(), "Odosla");
        assertEquals(request.getType(), "delivery");
        assertEquals(request.getUserID(), adminID);
        assertEquals(request.getUserType(), UserType.ADMIN);
    }

    @Test
    @Description("Tests when the request object is null that the exception is thrown")
    @DisplayName("Null request object")
    void requestIsNull_UnitTest(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.sendEmailNotification(null));
        assertEquals("Null request object.", thrown.getMessage());
    }

    @Test
    @Description("Tests when the property is null, exception should be thrown.")
    @DisplayName("Null properties")
    void requestObjectHasNullProperties_UnitTest(){
        SendEmailNotificationRequest request = new SendEmailNotificationRequest("message", null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.sendEmailNotification(request));
        assertEquals("Null userID.", thrown.getMessage());
    }

    @Test
    @Description("Tests when the request object has null userID")
    @DisplayName("Null userID in request object")
    void requestObjectHasNullUserID_UnitTest(){
        Map<String, String> properties = new HashMap<>();
        properties.put("Type","delivery");
        properties.put("Subject","Odosla");
        properties.put("UserID",null);
        properties.put("UserType", UserType.ADMIN.toString());
        SendEmailNotificationRequest request = new SendEmailNotificationRequest("message", properties);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.sendEmailNotification(request));
        assertEquals("Null userID.", thrown.getMessage());
    }

    @Test
    @Description("Tests when the request object has empty userID")
    @DisplayName("Empty userID in request object")
    void requestObjectHasEmptyUserID_UnitTest(){
        Map<String, String> properties = new HashMap<>();
        properties.put("Type","delivery");
        properties.put("Subject","Odosla");
        properties.put("UserID","");
        properties.put("UserType", UserType.ADMIN.toString());
        SendEmailNotificationRequest request = new SendEmailNotificationRequest("message", properties);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.sendEmailNotification(request));
        assertEquals("Null userID.", thrown.getMessage());
    }

    @Test
    @Description("Tests when the request object has an invalid UserType")
    @DisplayName("Invalid UserType in request object")
    void requestObjectHasInvalidUserType_UnitTest(){
        Map<String, String> properties = new HashMap<>();
        properties.put("Type","delivery");
        properties.put("Subject","Odosla");
        properties.put("UserID",adminID.toString());
        properties.put("UserType", "");
        SendEmailNotificationRequest request = new SendEmailNotificationRequest("message", properties);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.sendEmailNotification(request));
        assertEquals("Invalid UserType.", thrown.getMessage());
    }

    @Test
    @Description("Tests when the request object has userID not found on the database")
    @DisplayName("Invalid userID in request object")
    void requestObjectInvalidUserID_UnitTest() throws URISyntaxException {
        Map<String, String> properties = new HashMap<>();
        properties.put("Type","delivery");
        properties.put("Subject","Odosla");
        properties.put("UserID",adminID.toString());
        properties.put("UserType", UserType.ADMIN.toString());
        GetAdminByUUIDResponse getAdminByUUIDResponse = new GetAdminByUUIDResponse(null, new Date(), "");
        ResponseEntity<GetAdminByUUIDResponse> responseEntity = new ResponseEntity<>(getAdminByUUIDResponse, HttpStatus.OK);
        String uri = "http://"+userHost+":"+userPort+"/user/getAdminByUUID";
        URI uri1 = new URI(uri);
        Map<String, Object> parts = new HashMap<String, Object>();
        parts.put("userID", adminID.toString());
        when(restTemplate.postForEntity(uri1, parts, GetAdminByUUIDResponse.class)).thenReturn(responseEntity);
        SendEmailNotificationRequest request = new SendEmailNotificationRequest("message", properties);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.sendEmailNotification(request));
        assertEquals("User not found in database.", thrown.getMessage());
    }

    @Test
    @Description("Tests when the request object has userID with an invalid email in admin repo")
    @DisplayName("Invalid email in admin user")
    void requestObjectInvalidEmailAdminRepo_UnitTest() throws URISyntaxException {
        Map<String, String> properties = new HashMap<>();
        properties.put("Type","delivery");
        properties.put("Subject","Odosla");
        properties.put("UserID",adminID.toString());
        properties.put("UserType", UserType.ADMIN.toString());
        //when(adminRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(adminIncorrectEmail));
        GetAdminByUUIDResponse getAdminByUUIDResponse = new GetAdminByUUIDResponse(adminIncorrectEmail, new Date(), "");
        ResponseEntity<GetAdminByUUIDResponse> responseEntity = new ResponseEntity<>(getAdminByUUIDResponse, HttpStatus.OK);
        String uri = "http://"+userHost+":"+userPort+"/user/getAdminByUUID";
        URI uri1 = new URI(uri);
        Map<String, Object> parts = new HashMap<String, Object>();
        parts.put("userID", adminID.toString());
        when(restTemplate.postForEntity(uri1, parts, GetAdminByUUIDResponse.class)).thenReturn(responseEntity);
        SendEmailNotificationRequest request = new SendEmailNotificationRequest("message", properties);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.sendEmailNotification(request));
        assertEquals("Invalid recipient email address.", thrown.getMessage());
    }

    @Test
    @Description("Tests when the request object has userID with an invalid email in customer repo")
    @DisplayName("Invalid email in customer user")
    void requestObjectInvalidEmailCustomerRepo_UnitTest() throws URISyntaxException {
        Map<String, String> properties = new HashMap<>();
        properties.put("Type","delivery");
        properties.put("Subject","Odosla");
        properties.put("UserID",customerID.toString());
        properties.put("UserType", UserType.CUSTOMER.toString());
        //when(customerRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(customerIncorrectEmail));
        GetCustomerByUUIDResponse getCustomerByUUIDResponse = new GetCustomerByUUIDResponse(customerIncorrectEmail, new Date(), "");
        ResponseEntity<GetCustomerByUUIDResponse> responseEntity = new ResponseEntity<>(getCustomerByUUIDResponse, HttpStatus.OK);
        String uri = "http://"+userHost+":"+userPort+"/user/getCustomerByUUID";
        URI uri1 = new URI(uri);
        Map<String, Object> parts = new HashMap<String, Object>();
        parts.put("userID", customerID.toString());
        when(restTemplate.postForEntity(uri1, parts, GetCustomerByUUIDResponse.class)).thenReturn(responseEntity);
        SendEmailNotificationRequest request = new SendEmailNotificationRequest("message", properties);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.sendEmailNotification(request));
        assertEquals("Invalid recipient email address.", thrown.getMessage());
    }

    @Test
    @Description("Tests when the request object has userID with an invalid email in driver repo")
    @DisplayName("Invalid email in driver user")
    void requestObjectInvalidEmailDriverRepo_UnitTest() throws URISyntaxException {
        Map<String, String> properties = new HashMap<>();
        properties.put("Type","delivery");
        properties.put("Subject","Odosla");
        properties.put("UserID",driverID.toString());
        properties.put("UserType", UserType.DRIVER.toString());
        //when(driverRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(driverIncorrectEmail));
        GetDriverByUUIDResponse getDriverByUUIDResponse = new GetDriverByUUIDResponse(driverIncorrectEmail, new Date(), "");
        ResponseEntity<GetDriverByUUIDResponse> responseEntity = new ResponseEntity<>(getDriverByUUIDResponse, HttpStatus.OK);
        String uri = "http://"+userHost+":"+userPort+"/user/getDriverByUUID";
        URI uri1 = new URI(uri);
        Map<String, Object> parts = new HashMap<String, Object>();
        parts.put("userID", driverID.toString());
        when(restTemplate.postForEntity(uri1, parts, GetDriverByUUIDResponse.class)).thenReturn(responseEntity);
        SendEmailNotificationRequest request = new SendEmailNotificationRequest("message", properties);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.sendEmailNotification(request));
        assertEquals("Invalid recipient email address.", thrown.getMessage());
    }

    @Test
    @Description("Tests when the request object has userID with an invalid email in shopper repo")
    @DisplayName("Invalid email in shopper user")
    void requestObjectInvalidEmailShopperRepo_UnitTest() throws URISyntaxException {
        Map<String, String> properties = new HashMap<>();
        properties.put("Type","delivery");
        properties.put("Subject","Odosla");
        properties.put("UserID",shopperID.toString());
        properties.put("UserType", UserType.SHOPPER.toString());
        //when(shopperRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(shopperIncorrectEmail));
        GetShopperByUUIDResponse getShopperByUUIDResponse = new GetShopperByUUIDResponse(shopperIncorrectEmail, new Date(), "");
        ResponseEntity<GetShopperByUUIDResponse> responseEntity = new ResponseEntity<>(getShopperByUUIDResponse, HttpStatus.OK);
        String uri = "http://"+userHost+":"+userPort+"/user/getShopperByUUID";
        URI uri1 = new URI(uri);
        Map<String, Object> parts = new HashMap<String, Object>();
        parts.put("userID", shopperID.toString());
        when(restTemplate.postForEntity(uri1, parts, GetShopperByUUIDResponse.class)).thenReturn(responseEntity);
        SendEmailNotificationRequest request = new SendEmailNotificationRequest("message", properties);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.sendEmailNotification(request));
        assertEquals("Invalid recipient email address.", thrown.getMessage());
    }

    @Test
    @Description("Tests when the subject is empty, should throw an exception.")
    @DisplayName("Empty subject")
    void requestObjectEmptySubject_UnitTest() throws URISyntaxException {
        Map<String, String> properties = new HashMap<>();
        properties.put("Type","delivery");
        properties.put("Subject","");
        properties.put("UserID",adminID.toString());
        properties.put("UserType", UserType.ADMIN.toString());
        //when(adminRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(adminCorrectEmail));
        GetAdminByUUIDResponse getAdminByUUIDResponse = new GetAdminByUUIDResponse(adminCorrectEmail, new Date(), "");
        ResponseEntity<GetAdminByUUIDResponse> responseEntity = new ResponseEntity<>(getAdminByUUIDResponse, HttpStatus.OK);
        String uri = "http://"+userHost+":"+userPort+"/user/getAdminByUUID";
        URI uri1 = new URI(uri);
        Map<String, Object> parts = new HashMap<String, Object>();
        parts.put("userID", adminID.toString());
        when(restTemplate.postForEntity(uri1, parts, GetAdminByUUIDResponse.class)).thenReturn(responseEntity);
        SendEmailNotificationRequest request = new SendEmailNotificationRequest("message", properties);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.sendEmailNotification(request));
        assertEquals("Empty parameters.", thrown.getMessage());
    }

    @Test
    @Description("Tests when the message is empty, should throw an exception.")
    @DisplayName("Empty message")
    void requestObjectEmptyMessage_UnitTest() throws URISyntaxException {
        Map<String, String> properties = new HashMap<>();
        properties.put("Type","delivery");
        properties.put("Subject","odosla");
        properties.put("UserID",adminID.toString());
        properties.put("UserType", UserType.ADMIN.toString());
        //when(adminRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(adminCorrectEmail));
        GetAdminByUUIDResponse getAdminByUUIDResponse = new GetAdminByUUIDResponse(adminCorrectEmail, new Date(), "");
        ResponseEntity<GetAdminByUUIDResponse> responseEntity = new ResponseEntity<>(getAdminByUUIDResponse, HttpStatus.OK);
        String uri = "http://"+userHost+":"+userPort+"/user/getAdminByUUID";
        URI uri1 = new URI(uri);
        Map<String, Object> parts = new HashMap<String, Object>();
        parts.put("userID", adminID.toString());
        when(restTemplate.postForEntity(uri1, parts, GetAdminByUUIDResponse.class)).thenReturn(responseEntity);
        SendEmailNotificationRequest request = new SendEmailNotificationRequest("", properties);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.sendEmailNotification(request));
        assertEquals("Empty parameters.", thrown.getMessage());
    }

    @Test
    @Description("Tests when the type is invalid, should throw an exception from the createNotification function.")
    @DisplayName("Invalid type")
    void requestObjectInvalidType_UnitTest() throws URISyntaxException {
        Map<String, String> properties = new HashMap<>();
        properties.put("Type","hello");
        properties.put("Subject","odosla");
        properties.put("UserID",adminID.toString());
        properties.put("UserType", UserType.ADMIN.toString());
//        when(adminRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(adminCorrectEmail));
        GetAdminByUUIDResponse getAdminByUUIDResponse = new GetAdminByUUIDResponse(adminCorrectEmail, new Date(), "");
        ResponseEntity<GetAdminByUUIDResponse> responseEntity = new ResponseEntity<>(getAdminByUUIDResponse, HttpStatus.OK);
        String uri = "http://"+userHost+":"+userPort+"/user/getAdminByUUID";
        URI uri1 = new URI(uri);
        Map<String, Object> parts = new HashMap<String, Object>();
        parts.put("userID", adminID.toString());
        when(restTemplate.postForEntity(uri1, parts, GetAdminByUUIDResponse.class)).thenReturn(responseEntity);
        SendEmailNotificationRequest request = new SendEmailNotificationRequest("message", properties);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.sendEmailNotification(request));
        assertEquals("Invalid notification type.", thrown.getMessage());
    }
}
