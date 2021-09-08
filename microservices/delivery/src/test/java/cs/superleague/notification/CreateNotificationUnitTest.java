//package cs.superleague.notification;
//
//import cs.superleague.notification.exceptions.InvalidRequestException;
//import cs.superleague.notification.repos.NotificationRepo;
//import cs.superleague.notification.requests.CreateNotificationRequest;
//import cs.superleague.notification.responses.CreateNotificationResponse;
//import cs.superleague.user.dataclass.*;
//import cs.superleague.user.repos.AdminRepo;
//import cs.superleague.user.repos.CustomerRepo;
//import cs.superleague.user.repos.DriverRepo;
//import cs.superleague.user.repos.ShopperRepo;
//import org.junit.jupiter.api.*;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.context.annotation.Description;
//
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class CreateNotificationUnitTest {
//    @InjectMocks
//    private NotificationServiceImpl notificationServiceImpl;
//
//    @Mock
//    private NotificationRepo notificationRepo;
//
//    @Mock
//    private AdminRepo adminRepo;
//
//    @Mock
//    private CustomerRepo customerRepo;
//
//    @Mock
//    private DriverRepo driverRepo;
//
//    @Mock
//    private ShopperRepo shopperRepo;
//
//    UUID adminID;
//    UUID customerID;
//    UUID driverID;
//    UUID shopperID;
//    Admin admin;
//    Customer customer;
//    Driver driver;
//    Shopper shopper;
//
//    @BeforeEach
//    void setUp() {
//        adminID = UUID.randomUUID();
//        customerID = UUID.randomUUID();
//        driverID = UUID.randomUUID();
//        shopperID = UUID.randomUUID();
//        admin = new Admin("John", "Doe", "u19060468tuks.co.za", "0743149813", "Hello123", "123", UserType.ADMIN, adminID);
//        customer = new Customer("John", "Doe", "u19060468tuks.co.za", "0743149813", "Hello123", "123", UserType.CUSTOMER, customerID);
//        driver = new Driver("John", "Doe", "u19060468tuks.co.za", "0743149813", "Hello123", "123", UserType.DRIVER, driverID);
//        shopper = new Shopper("John", "Doe", "u19060468tuks.co.za", "0743149813", "Hello123", "123", UserType.SHOPPER, shopperID);
//    }
//
//    @AfterEach
//    void tearDown() {
//
//    }
//
//    @Test
//    @Description("Tests creation of the request object")
//    @DisplayName("Request object creation")
//    void createNotificationRequestObjectCreation_UnitTest(){
//        Map<String, String> properties = new HashMap<>();
//        properties.put("NotificationType", "delivery");
//        properties.put("Subject", "Odosla");
//        properties.put("UserID", adminID.toString());
//        properties.put("UserType", UserType.ADMIN.toString());
//        properties.put("ContactDetails", "u19060468@tuks.co.za");
//        Calendar time = Calendar.getInstance();
//        CreateNotificationRequest request = new CreateNotificationRequest("payLoad", time, properties);
//        assertEquals(request.getProperties(), properties);
//        assertEquals(request.getPayLoad(), "payLoad");
//        assertEquals(request.getCreatedDateTime(), time);
//    }
//
//    @Test
//    @Description("Tests when the request object is null that the exception is thrown")
//    @DisplayName("Null request object")
//    void requestIsNull_UnitTest(){
//        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationServiceImpl.createNotification(null));
//        assertEquals("Null request object.", thrown.getMessage());
//    }
//
//    @Test
//    @Description("Tests for when the payLoad is null, an exception should be thrown")
//    @DisplayName("Null payLoad")
//    void NullPayLoadInRequestObject_UnitTest(){
//        Map<String, String> properties = new HashMap<>();
//        properties.put("NotificationType", "delivery");
//        properties.put("Subject", "Odosla");
//        properties.put("ContactDetails", "u19060468@tuks.co.za");
//        properties.put("UserID", adminID.toString());
//        properties.put("UserType", UserType.ADMIN.toString());
//        Calendar time = Calendar.getInstance();
//        CreateNotificationRequest request = new CreateNotificationRequest(null, time, properties);
//        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationServiceImpl.createNotification(request));
//        assertEquals("Null parameters.", thrown.getMessage());
//    }
//
//    @Test
//    @Description("Tests for when the time is null, an exception should be thrown")
//    @DisplayName("Null date and time")
//    void NullDateAndTimeInRequestObject_UnitTest(){
//        Map<String, String> properties = new HashMap<>();
//        properties.put("NotificationType", "delivery");
//        properties.put("Subject", "Odosla");
//        properties.put("ContactDetails", "u19060468@tuks.co.za");
//        properties.put("UserID", adminID.toString());
//        properties.put("UserType", UserType.ADMIN.toString());
//        Calendar time = Calendar.getInstance();
//        CreateNotificationRequest request = new CreateNotificationRequest("PayLoad", null, properties);
//        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationServiceImpl.createNotification(request));
//        assertEquals("Null parameters.", thrown.getMessage());
//    }
//
//    @Test
//    @Description("Tests for when the properties is null, an exception should be thrown")
//    @DisplayName("Null Properties")
//    void NullPropertiesInRequestObject_UnitTest(){
//        Map<String, String> properties = new HashMap<>();
//        properties.put("NotificationType", "delivery");
//        properties.put("Subject", "Odosla");
//        properties.put("ContactDetails", "u19060468@tuks.co.za");
//        properties.put("UserID", adminID.toString());
//        properties.put("UserType", UserType.ADMIN.toString());
//        Calendar time = Calendar.getInstance();
//        CreateNotificationRequest request = new CreateNotificationRequest("PayLoad", time, null);
//        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationServiceImpl.createNotification(request));
//        assertEquals("Null parameters.", thrown.getMessage());
//    }
//
//    @Test
//    @Description("Tests for when the parameters are empty, an exception should be thrown")
//    @DisplayName("Empty payLoad")
//    void EmptyPayLoadInRequestObject_UnitTest(){
//        Map<String, String> properties = new HashMap<>();
//        properties.put("NotificationType", "delivery");
//        properties.put("Subject", "Odosla");
//        properties.put("ContactDetails", "u19060468@tuks.co.za");
//        properties.put("UserID", adminID.toString());
//        properties.put("UserType", UserType.ADMIN.toString());
//        Calendar time = Calendar.getInstance();
//        CreateNotificationRequest request = new CreateNotificationRequest("", time, properties);
//        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationServiceImpl.createNotification(request));
//        assertEquals("Empty parameters.", thrown.getMessage());
//    }
//
//    @Test
//    @Description("Tests for when the notification type is null, an exception should be thrown")
//    @DisplayName("Null NotificationType")
//    void NullNotificationTypeInRequestObject_UnitTest(){
//        Map<String, String> properties = new HashMap<>();
//        properties.put("Subject", "Odosla");
//        properties.put("ContactDetails", "u19060468@tuks.co.za");
//        properties.put("UserID", adminID.toString());
//        properties.put("UserType", UserType.ADMIN.toString());
//        Calendar time = Calendar.getInstance();
//        CreateNotificationRequest request = new CreateNotificationRequest("payLoad", time, properties);
//        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationServiceImpl.createNotification(request));
//        assertEquals("Null notification type.", thrown.getMessage());
//    }
//
//    @Test
//    @Description("Tests for when the notification type is invalid, an exception should be thrown")
//    @DisplayName("Invalid NotificationType")
//    void InvalidNotificationTypeInRequestObject_UnitTest(){
//        Map<String, String> properties = new HashMap<>();
//        properties.put("NotificationType", "shipping");
//        properties.put("Subject", "Odosla");
//        properties.put("ContactDetails", "u19060468@tuks.co.za");
//        properties.put("UserID", adminID.toString());
//        properties.put("UserType", UserType.ADMIN.toString());
//        Calendar time = Calendar.getInstance();
//        CreateNotificationRequest request = new CreateNotificationRequest("payLoad", time, properties);
//        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationServiceImpl.createNotification(request));
//        assertEquals("Invalid notification type.", thrown.getMessage());
//    }
//
//    @Test
//    @Description("Tests for when the user type is invalid, an exception should be thrown")
//    @DisplayName("Invalid UserType")
//    void InvalidUserTypeInRequestObject_UnitTest(){
//        Map<String, String> properties = new HashMap<>();
//        properties.put("NotificationType", "delivery");
//        properties.put("Subject", "Odosla");
//        properties.put("ContactDetails", "u19060468@tuks.co.za");
//        properties.put("UserID", adminID.toString());
//        properties.put("UserType", "assistant");
//        Calendar time = Calendar.getInstance();
//        CreateNotificationRequest request = new CreateNotificationRequest("payLoad", time, properties);
//        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationServiceImpl.createNotification(request));
//        assertEquals("Invalid UserType.", thrown.getMessage());
//    }
//
//    @Test
//    @Description("Tests for when the userID is null, an exception should be thrown")
//    @DisplayName("Null userID")
//    void NullUserIDInRequestObject_UnitTest(){
//        Map<String, String> properties = new HashMap<>();
//        properties.put("NotificationType", "delivery");
//        properties.put("Subject", "Odosla");
//        properties.put("ContactDetails", "u19060468@tuks.co.za");
//        properties.put("UserType", UserType.ADMIN.toString());
//        Calendar time = Calendar.getInstance();
//        CreateNotificationRequest request = new CreateNotificationRequest("payLoad", time, properties);
//        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationServiceImpl.createNotification(request));
//        assertEquals("Null UserID.", thrown.getMessage());
//    }
//
//    @Test
//    @Description("Tests for when the userID is not in the database, an exception should be thrown")
//    @DisplayName("Invalid userID")
//    void InvalidUserIDInRequestObject_UnitTest(){
//        Map<String, String> properties = new HashMap<>();
//        properties.put("NotificationType", "delivery");
//        properties.put("Subject", "Odosla");
//        properties.put("ContactDetails", "u19060468@tuks.co.za");
//        properties.put("UserID", adminID.toString());
//        properties.put("UserType", UserType.ADMIN.toString());
//        Calendar time = Calendar.getInstance();
//        when(adminRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(null));
//        CreateNotificationRequest request = new CreateNotificationRequest("payLoad", time, properties);
//        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationServiceImpl.createNotification(request));
//        assertEquals("User does not exist in database.", thrown.getMessage());
//    }
//
//    @Test
//    @Description("Successful adding of a notification for an admin")
//    @DisplayName("Successful adding of notification for admin")
//    void SuccessfulAddingOfNotificationForAdmin_UnitTest() throws InvalidRequestException {
//        Map<String, String> properties = new HashMap<>();
//        properties.put("NotificationType", "delivery");
//        properties.put("Subject", "Odosla");
//        properties.put("ContactDetails", "u19060468@tuks.co.za");
//        properties.put("UserID", adminID.toString());
//        properties.put("UserType", UserType.ADMIN.toString());
//        Calendar time = Calendar.getInstance();
//        when(adminRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(admin));
//        CreateNotificationRequest request = new CreateNotificationRequest("payLoad", time, properties);
//        CreateNotificationResponse response =  notificationServiceImpl.createNotification(request);
//        assertEquals(response.getResponseMessage(), "Notification successfully created.");
//    }
//
//    @Test
//    @Description("Successful adding of a notification for a customer")
//    @DisplayName("Successful adding of notification for customer")
//    void SuccessfulAddingOfNotificationForCustomer_UnitTest() throws InvalidRequestException {
//        Map<String, String> properties = new HashMap<>();
//        properties.put("NotificationType", "delivery");
//        properties.put("Subject", "Odosla");
//        properties.put("ContactDetails", "u19060468@tuks.co.za");
//        properties.put("UserID", customerID.toString());
//        properties.put("UserType", UserType.CUSTOMER.toString());
//        Calendar time = Calendar.getInstance();
//        when(customerRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(customer));
//        CreateNotificationRequest request = new CreateNotificationRequest("payLoad", time, properties);
//        CreateNotificationResponse response =  notificationServiceImpl.createNotification(request);
//        assertEquals(response.getResponseMessage(), "Notification successfully created.");
//    }
//
//    @Test
//    @Description("Successful adding of a notification for a driver")
//    @DisplayName("Successful adding of notification for driver")
//    void SuccessfulAddingOfNotificationForDriver_UnitTest() throws InvalidRequestException {
//        Map<String, String> properties = new HashMap<>();
//        properties.put("NotificationType", "delivery");
//        properties.put("Subject", "Odosla");
//        properties.put("ContactDetails", "u19060468@tuks.co.za");
//        properties.put("UserID", driverID.toString());
//        properties.put("UserType", UserType.DRIVER.toString());
//        Calendar time = Calendar.getInstance();
//        when(driverRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(driver));
//        CreateNotificationRequest request = new CreateNotificationRequest("payLoad", time, properties);
//        CreateNotificationResponse response =  notificationServiceImpl.createNotification(request);
//        assertEquals(response.getResponseMessage(), "Notification successfully created.");
//    }
//
//    @Test
//    @Description("Successful adding of a notification for a shopper")
//    @DisplayName("Successful adding of notification for shopper")
//    void SuccessfulAddingOfNotificationForShopper_UnitTest() throws InvalidRequestException {
//        Map<String, String> properties = new HashMap<>();
//        properties.put("NotificationType", "delivery");
//        properties.put("Subject", "Odosla");
//        properties.put("ContactDetails", "u19060468@tuks.co.za");
//        properties.put("UserID", shopperID.toString());
//        properties.put("UserType", UserType.SHOPPER.toString());
//        Calendar time = Calendar.getInstance();
//        when(shopperRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(shopper));
//        CreateNotificationRequest request = new CreateNotificationRequest("payLoad", time, properties);
//        CreateNotificationResponse response =  notificationServiceImpl.createNotification(request);
//        assertEquals(response.getResponseMessage(), "Notification successfully created.");
//    }
//
//}
