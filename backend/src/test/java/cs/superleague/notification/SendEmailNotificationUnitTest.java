package cs.superleague.notification;

import cs.superleague.notification.repos.NotificationRepo;
import cs.superleague.notification.requests.SendEmailNotificationRequest;
import cs.superleague.notification.responses.SendEmailNotificationResponse;
import cs.superleague.user.dataclass.*;
import cs.superleague.user.repos.AdminRepo;
import cs.superleague.user.repos.CustomerRepo;
import cs.superleague.user.repos.DriverRepo;
import cs.superleague.user.repos.ShopperRepo;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.context.annotation.Description;
import cs.superleague.notification.exceptions.InvalidRequestException;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
    private AdminRepo adminRepo;

    @Mock
    private CustomerRepo customerRepo;

    @Mock
    private DriverRepo driverRepo;

    @Mock
    private ShopperRepo shopperRepo;

    UUID adminID;
    UUID customerID;
    UUID driverID;
    UUID shopperID;
    Admin adminIncorrectEmail;
    Customer customerIncorrectEmail;
    Driver driverIncorrectEmail;
    Shopper shopperIncorrectEmail;

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
        properties.put("userID",adminID.toString());
        SendEmailNotificationRequest request = new SendEmailNotificationRequest("message", properties);
        assertEquals(request.getMessage(), "message");
        assertEquals(request.getSubject(), "Odosla");
        assertEquals(request.getType(), "delivery");
        assertEquals(request.getUserID(), adminID);
    }

    @Test
    @Description("Tests when the request object is null that the exception is thrown")
    @DisplayName("Null request object")
    void requestIsNull_UnitTest(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.sendEmailNotification(null));
        assertEquals("Null request object.", thrown.getMessage());
    }

    @Test
    @Description("Tests when the request object has null userID")
    @DisplayName("Null userID in request object")
    void requestObjectHasNullUserID_UnitTest(){
        Map<String, String> properties = new HashMap<>();
        properties.put("Type","delivery");
        properties.put("Subject","Odosla");
        properties.put("userID",null);
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
        properties.put("userID","");
        SendEmailNotificationRequest request = new SendEmailNotificationRequest("message", properties);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.sendEmailNotification(request));
        assertEquals("Null userID.", thrown.getMessage());
    }

    @Test
    @Description("Tests when the request object has userID not found on the database")
    @DisplayName("Invalid userID in request object")
    void requestObjectInvalidUserID_UnitTest(){
        Map<String, String> properties = new HashMap<>();
        properties.put("Type","delivery");
        properties.put("Subject","Odosla");
        properties.put("userID",adminID.toString());
        SendEmailNotificationRequest request = new SendEmailNotificationRequest("message", properties);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.sendEmailNotification(request));
        assertEquals("Invalid userID, does not match any in the database.", thrown.getMessage());
    }

    @Test
    @Description("Tests when the request object has userID with an invalid email in admin repo")
    @DisplayName("Invalid email in admin user")
    void requestObjectInvalidEmailAdminRepo_UnitTest(){
        Map<String, String> properties = new HashMap<>();
        properties.put("Type","delivery");
        properties.put("Subject","Odosla");
        properties.put("userID",adminID.toString());
        when(adminRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(adminIncorrectEmail));
        SendEmailNotificationRequest request = new SendEmailNotificationRequest("message", properties);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.sendEmailNotification(request));
        assertEquals("Invalid recipient email address.", thrown.getMessage());
    }

    @Test
    @Description("Tests when the request object has userID with an invalid email in customer repo")
    @DisplayName("Invalid email in customer user")
    void requestObjectInvalidEmailCustomerRepo_UnitTest(){
        Map<String, String> properties = new HashMap<>();
        properties.put("Type","delivery");
        properties.put("Subject","Odosla");
        properties.put("userID",customerID.toString());
        when(customerRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(customerIncorrectEmail));
        SendEmailNotificationRequest request = new SendEmailNotificationRequest("message", properties);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.sendEmailNotification(request));
        assertEquals("Invalid recipient email address.", thrown.getMessage());
    }

    @Test
    @Description("Tests when the request object has userID with an invalid email in driver repo")
    @DisplayName("Invalid email in driver user")
    void requestObjectInvalidEmailDriverRepo_UnitTest(){
        Map<String, String> properties = new HashMap<>();
        properties.put("Type","delivery");
        properties.put("Subject","Odosla");
        properties.put("userID",driverID.toString());
        when(driverRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(driverIncorrectEmail));
        SendEmailNotificationRequest request = new SendEmailNotificationRequest("message", properties);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.sendEmailNotification(request));
        assertEquals("Invalid recipient email address.", thrown.getMessage());
    }

    @Test
    @Description("Tests when the request object has userID with an invalid email in shopper repo")
    @DisplayName("Invalid email in shopper user")
    void requestObjectInvalidEmailShopperRepo_UnitTest(){
        Map<String, String> properties = new HashMap<>();
        properties.put("Type","delivery");
        properties.put("Subject","Odosla");
        properties.put("userID",shopperID.toString());
        when(shopperRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(shopperIncorrectEmail));
        SendEmailNotificationRequest request = new SendEmailNotificationRequest("message", properties);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.sendEmailNotification(request));
        assertEquals("Invalid recipient email address.", thrown.getMessage());
    }
}
