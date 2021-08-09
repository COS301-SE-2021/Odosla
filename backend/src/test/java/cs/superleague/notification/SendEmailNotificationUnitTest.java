package cs.superleague.notification;

import cs.superleague.notification.repos.NotificationRepo;
import cs.superleague.notification.requests.SendEmailNotificationRequest;
import cs.superleague.notification.responses.SendEmailNotificationResponse;
import cs.superleague.user.dataclass.Admin;
import cs.superleague.user.dataclass.UserType;
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

    UUID userID1;
    UUID userID2;
    Admin adminIncorrectEmail;
    Admin adminCorrectEmail;

    @BeforeEach
    void setUp() {
        userID1 = UUID.randomUUID();
        userID2 = UUID.randomUUID();
        adminIncorrectEmail = new Admin("John", "Doe", "u19060468tuks.co.za", "0743149813", "Hello123", "123", UserType.ADMIN, userID1);
        adminCorrectEmail = new Admin("John", "Doe", "u19060468@tuks.co.za", "0743149813", "Hello123", "123", UserType.ADMIN, userID2);
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
        properties.put("userID",userID1.toString());
        SendEmailNotificationRequest request = new SendEmailNotificationRequest("message", properties);
        assertEquals(request.getMessage(), "message");
        assertEquals(request.getSubject(), "Odosla");
        assertEquals(request.getType(), "delivery");
        assertEquals(request.getUserID(), userID1);
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
        properties.put("userID",userID1.toString());
        SendEmailNotificationRequest request = new SendEmailNotificationRequest("message", properties);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.sendEmailNotification(request));
        assertEquals("Invalid userID, does not match any in the database.", thrown.getMessage());
    }

    @Test
    @Description("Tests when the request object has userID with an invalid email")
    @DisplayName("Invalid email in user")
    void requestObjectInvalidEmail_UnitTest(){
        Map<String, String> properties = new HashMap<>();
        properties.put("Type","delivery");
        properties.put("Subject","Odosla");
        properties.put("userID",userID1.toString());
        when(adminRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(adminIncorrectEmail));
        SendEmailNotificationRequest request = new SendEmailNotificationRequest("message", properties);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.sendEmailNotification(request));
        assertEquals("Invalid recipient email address.", thrown.getMessage());
    }

    @Test
    @Description("Tests when the request object has userID with an invalid email")
    @DisplayName("valid email in user")
    void validEmailSent_UnitTest() throws InvalidRequestException {
        Map<String, String> properties = new HashMap<>();
        properties.put("Type","delivery");
        properties.put("Subject","Odosla");
        properties.put("userID",userID2.toString());
        when(adminRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(adminCorrectEmail));
        SendEmailNotificationRequest request = new SendEmailNotificationRequest("message", properties);
        SendEmailNotificationResponse response = notificationService.sendEmailNotification(request);
        assertEquals(true, response.getSuccessMessage());
    }
}
