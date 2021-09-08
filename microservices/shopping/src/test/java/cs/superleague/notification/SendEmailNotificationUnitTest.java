package cs.superleague.notification;//package cs.superleague.notification;
//
//import cs.superleague.notification.repos.NotificationRepo;
//import cs.superleague.notification.requests.SendEmailNotificationRequest;
//import cs.superleague.user.dataclass.*;
//import cs.superleague.user.repos.AdminRepo;
//import cs.superleague.user.repos.CustomerRepo;
//import cs.superleague.user.repos.DriverRepo;
//import cs.superleague.user.repos.ShopperRepo;
//import org.junit.jupiter.api.*;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.springframework.context.annotation.Description;
//import cs.superleague.notification.exceptions.InvalidRequestException;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.mail.javamail.JavaMailSender;
//
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class SendEmailNotificationUnitTest {
//    @InjectMocks
//    private NotificationServiceImpl notificationService;
//
//    @Mock
//    private JavaMailSender javaMailSender;
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
//    UUID adminID2;
//    Admin adminIncorrectEmail;
//    Customer customerIncorrectEmail;
//    Driver driverIncorrectEmail;
//    Shopper shopperIncorrectEmail;
//    Admin adminCorrectEmail;
//
//    @BeforeEach
//    void setUp() {
//        adminID = UUID.randomUUID();
//        customerID = UUID.randomUUID();
//        driverID = UUID.randomUUID();
//        shopperID = UUID.randomUUID();
//        adminIncorrectEmail = new Admin("John", "Doe", "u19060468tuks.co.za", "0743149813", "Hello123", "123", UserType.ADMIN, adminID);
//        customerIncorrectEmail = new Customer("John", "Doe", "u19060468tuks.co.za", "0743149813", "Hello123", "123", UserType.CUSTOMER, customerID);
//        driverIncorrectEmail = new Driver("John", "Doe", "u19060468tuks.co.za", "0743149813", "Hello123", "123", UserType.DRIVER, driverID);
//        shopperIncorrectEmail = new Shopper("John", "Doe", "u19060468tuks.co.za", "0743149813", "Hello123", "123", UserType.SHOPPER, shopperID);
//        adminCorrectEmail = new Admin("John", "Doe", "u19060468@tuks.co.za", "0743149813", "Hello123", "123", UserType.ADMIN, adminID2);
//
//    }
//
//    @AfterEach
//    void tearDown() {
//    }
//
//    @Test
//    @Description("Tests that the request object is being created successfully")
//    @DisplayName("Object creation")
//    void requestObjectCreation_UnitTest(){
//        Map<String, String> properties = new HashMap<>();
//        properties.put("Type","delivery");
//        properties.put("Subject","Odosla");
//        properties.put("UserID",adminID.toString());
//        properties.put("UserType", UserType.ADMIN.toString());
//        SendEmailNotificationRequest request = new SendEmailNotificationRequest("message", properties);
//        assertEquals(request.getMessage(), "message");
//        assertEquals(request.getSubject(), "Odosla");
//        assertEquals(request.getType(), "delivery");
//        assertEquals(request.getUserID(), adminID);
//        assertEquals(request.getUserType(), UserType.ADMIN);
//    }
//
//    @Test
//    @Description("Tests when the request object is null that the exception is thrown")
//    @DisplayName("Null request object")
//    void requestIsNull_UnitTest(){
//        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.sendEmailNotification(null));
//        assertEquals("Null request object.", thrown.getMessage());
//    }
//
//    @Test
//    @Description("Tests when the property is null, exception should be thrown.")
//    @DisplayName("Null properties")
//    void requestObjectHasNullProperties_UnitTest(){
//        SendEmailNotificationRequest request = new SendEmailNotificationRequest("message", null);
//        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.sendEmailNotification(request));
//        assertEquals("Null userID.", thrown.getMessage());
//    }
//
//    @Test
//    @Description("Tests when the request object has null userID")
//    @DisplayName("Null userID in request object")
//    void requestObjectHasNullUserID_UnitTest(){
//        Map<String, String> properties = new HashMap<>();
//        properties.put("Type","delivery");
//        properties.put("Subject","Odosla");
//        properties.put("UserID",null);
//        properties.put("UserType", UserType.ADMIN.toString());
//        SendEmailNotificationRequest request = new SendEmailNotificationRequest("message", properties);
//        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.sendEmailNotification(request));
//        assertEquals("Null userID.", thrown.getMessage());
//    }
//
//    @Test
//    @Description("Tests when the request object has empty userID")
//    @DisplayName("Empty userID in request object")
//    void requestObjectHasEmptyUserID_UnitTest(){
//        Map<String, String> properties = new HashMap<>();
//        properties.put("Type","delivery");
//        properties.put("Subject","Odosla");
//        properties.put("UserID","");
//        properties.put("UserType", UserType.ADMIN.toString());
//        SendEmailNotificationRequest request = new SendEmailNotificationRequest("message", properties);
//        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.sendEmailNotification(request));
//        assertEquals("Null userID.", thrown.getMessage());
//    }
//
//    @Test
//    @Description("Tests when the request object has an invalid UserType")
//    @DisplayName("Invalid UserType in request object")
//    void requestObjectHasInvalidUserType_UnitTest(){
//        Map<String, String> properties = new HashMap<>();
//        properties.put("Type","delivery");
//        properties.put("Subject","Odosla");
//        properties.put("UserID",adminID.toString());
//        properties.put("UserType", "");
//        SendEmailNotificationRequest request = new SendEmailNotificationRequest("message", properties);
//        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.sendEmailNotification(request));
//        assertEquals("Invalid UserType.", thrown.getMessage());
//    }
//
//    @Test
//    @Description("Tests when the request object has userID not found on the database")
//    @DisplayName("Invalid userID in request object")
//    void requestObjectInvalidUserID_UnitTest(){
//        Map<String, String> properties = new HashMap<>();
//        properties.put("Type","delivery");
//        properties.put("Subject","Odosla");
//        properties.put("UserID",adminID.toString());
//        properties.put("UserType", UserType.ADMIN.toString());
//        SendEmailNotificationRequest request = new SendEmailNotificationRequest("message", properties);
//        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.sendEmailNotification(request));
//        assertEquals("User not found in database.", thrown.getMessage());
//    }
//
//    @Test
//    @Description("Tests when the request object has userID with an invalid email in admin repo")
//    @DisplayName("Invalid email in admin user")
//    void requestObjectInvalidEmailAdminRepo_UnitTest(){
//        Map<String, String> properties = new HashMap<>();
//        properties.put("Type","delivery");
//        properties.put("Subject","Odosla");
//        properties.put("UserID",adminID.toString());
//        properties.put("UserType", UserType.ADMIN.toString());
//        when(adminRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(adminIncorrectEmail));
//        SendEmailNotificationRequest request = new SendEmailNotificationRequest("message", properties);
//        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.sendEmailNotification(request));
//        assertEquals("Invalid recipient email address.", thrown.getMessage());
//    }
//
//    @Test
//    @Description("Tests when the request object has userID with an invalid email in customer repo")
//    @DisplayName("Invalid email in customer user")
//    void requestObjectInvalidEmailCustomerRepo_UnitTest(){
//        Map<String, String> properties = new HashMap<>();
//        properties.put("Type","delivery");
//        properties.put("Subject","Odosla");
//        properties.put("UserID",customerID.toString());
//        properties.put("UserType", UserType.CUSTOMER.toString());
//        when(customerRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(customerIncorrectEmail));
//        SendEmailNotificationRequest request = new SendEmailNotificationRequest("message", properties);
//        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.sendEmailNotification(request));
//        assertEquals("Invalid recipient email address.", thrown.getMessage());
//    }
//
//    @Test
//    @Description("Tests when the request object has userID with an invalid email in driver repo")
//    @DisplayName("Invalid email in driver user")
//    void requestObjectInvalidEmailDriverRepo_UnitTest(){
//        Map<String, String> properties = new HashMap<>();
//        properties.put("Type","delivery");
//        properties.put("Subject","Odosla");
//        properties.put("UserID",driverID.toString());
//        properties.put("UserType", UserType.DRIVER.toString());
//        when(driverRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(driverIncorrectEmail));
//        SendEmailNotificationRequest request = new SendEmailNotificationRequest("message", properties);
//        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.sendEmailNotification(request));
//        assertEquals("Invalid recipient email address.", thrown.getMessage());
//    }
//
//    @Test
//    @Description("Tests when the request object has userID with an invalid email in shopper repo")
//    @DisplayName("Invalid email in shopper user")
//    void requestObjectInvalidEmailShopperRepo_UnitTest(){
//        Map<String, String> properties = new HashMap<>();
//        properties.put("Type","delivery");
//        properties.put("Subject","Odosla");
//        properties.put("UserID",shopperID.toString());
//        properties.put("UserType", UserType.SHOPPER.toString());
//        when(shopperRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(shopperIncorrectEmail));
//        SendEmailNotificationRequest request = new SendEmailNotificationRequest("message", properties);
//        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.sendEmailNotification(request));
//        assertEquals("Invalid recipient email address.", thrown.getMessage());
//    }
//
//    @Test
//    @Description("Tests when the subject is empty, should throw an exception.")
//    @DisplayName("Empty subject")
//    void requestObjectEmptySubject_UnitTest(){
//        Map<String, String> properties = new HashMap<>();
//        properties.put("Type","delivery");
//        properties.put("Subject","");
//        properties.put("UserID",adminID.toString());
//        properties.put("UserType", UserType.ADMIN.toString());
//        when(adminRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(adminCorrectEmail));
//        SendEmailNotificationRequest request = new SendEmailNotificationRequest("message", properties);
//        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.sendEmailNotification(request));
//        assertEquals("Empty parameters.", thrown.getMessage());
//    }
//
//    @Test
//    @Description("Tests when the message is empty, should throw an exception.")
//    @DisplayName("Empty message")
//    void requestObjectEmptyMessage_UnitTest(){
//        Map<String, String> properties = new HashMap<>();
//        properties.put("Type","delivery");
//        properties.put("Subject","odosla");
//        properties.put("UserID",adminID.toString());
//        properties.put("UserType", UserType.ADMIN.toString());
//        when(adminRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(adminCorrectEmail));
//        SendEmailNotificationRequest request = new SendEmailNotificationRequest("", properties);
//        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.sendEmailNotification(request));
//        assertEquals("Empty parameters.", thrown.getMessage());
//    }
//
//    @Test
//    @Description("Tests when the type is invalid, should throw an exception from the createNotification function.")
//    @DisplayName("Invalid type")
//    void requestObjectInvalidType_UnitTest(){
//        Map<String, String> properties = new HashMap<>();
//        properties.put("Type","hello");
//        properties.put("Subject","odosla");
//        properties.put("UserID",adminID.toString());
//        properties.put("UserType", UserType.ADMIN.toString());
//        when(adminRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(adminCorrectEmail));
//        SendEmailNotificationRequest request = new SendEmailNotificationRequest("message", properties);
//        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.sendEmailNotification(request));
//        assertEquals("Invalid notification type.", thrown.getMessage());
//    }
//}
