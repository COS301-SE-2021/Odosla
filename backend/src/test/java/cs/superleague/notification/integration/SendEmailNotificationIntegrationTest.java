package cs.superleague.notification.integration;


import cs.superleague.notification.NotificationServiceImpl;
import cs.superleague.notification.exceptions.InvalidRequestException;
import cs.superleague.notification.repos.NotificationRepo;
import cs.superleague.notification.requests.SendEmailNotificationRequest;
import cs.superleague.notification.responses.SendEmailNotificationResponse;
import cs.superleague.user.UserServiceImpl;
import cs.superleague.user.dataclass.Admin;
import cs.superleague.user.dataclass.UserType;
import cs.superleague.user.repos.*;
import cs.superleague.user.requests.RegisterAdminRequest;
import cs.superleague.user.responses.RegisterAdminResponse;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SpringBootTest
@Transactional
class SendEmailNotificationIntegrationTest {

    @Autowired
    NotificationRepo notificationRepo;

    @Autowired
    AdminRepo adminRepo;

    @Autowired
    ShopperRepo shopperRepo;

    @Autowired
    DriverRepo driverRepo;

    @Autowired
    CustomerRepo customerRepo;

    @Autowired
    private NotificationServiceImpl notificationService;

    @Autowired
    private UserServiceImpl userService;

    UUID userID1;
    UUID invalidID;
    Admin adminCorrectEmail;
    String email;

    @BeforeEach
    void setUp() throws cs.superleague.user.exceptions.InvalidRequestException {
        email = "u19060468@tuks.co.za";
        RegisterAdminRequest request = new RegisterAdminRequest("John", "Doe", email, "0743149813", "H123@@@@@dfsfsdf");
        RegisterAdminResponse response = userService.registerAdmin(request);
        System.out.println(response.getMessage());
        adminCorrectEmail = adminRepo.findAdminByEmail(email);
        userID1 = adminCorrectEmail.getAdminID();
        invalidID = UUID.randomUUID();
        while (invalidID == userID1){
            invalidID = UUID.randomUUID();
        }
    }

    @AfterEach
    void tearDown(){
        notificationRepo.deleteAll();
        adminRepo.deleteAll();
    }

    @Test
    @Description("Tests that the request object is being created successfully")
    @DisplayName("Object creation")
    void requestObjectCreation_IntegrationTest(){
        Map<String, String> properties = new HashMap<>();
        properties.put("Type","delivery");
        properties.put("Subject","Odosla");
        properties.put("UserID",userID1.toString());
        properties.put("UserType", UserType.ADMIN.toString());
        SendEmailNotificationRequest request = new SendEmailNotificationRequest("message", properties);
        assertEquals(request.getMessage(), "message");
        assertEquals(request.getSubject(), "Odosla");
        assertEquals(request.getType(), "delivery");
        assertEquals(request.getUserID(), userID1);
        assertEquals(request.getUserType(), UserType.ADMIN);
    }

    @Test
    @Description("Tests when the request object is null that the exception is thrown")
    @DisplayName("Null request object")
    void requestIsNull_IntegrationTest(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.sendEmailNotification(null));
        assertEquals("Null request object.", thrown.getMessage());
    }

    @Test
    @Description("Tests when the request object has empty userID")
    @DisplayName("Empty userID in request object")
    void requestObjectHasEmptyUserID_IntegrationTest(){
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
    void requestObjectHasInvalidUserType_IntegrationTest(){
        Map<String, String> properties = new HashMap<>();
        properties.put("Type","delivery");
        properties.put("Subject","Odosla");
        properties.put("UserID",userID1.toString());
        properties.put("UserType", "");
        SendEmailNotificationRequest request = new SendEmailNotificationRequest("message", properties);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.sendEmailNotification(request));
        assertEquals("Invalid UserType.", thrown.getMessage());
    }

    @Test
    @Description("Tests when the request object has userID not found on the database")
    @DisplayName("Invalid userID in request object")
    void requestObjectInvalidUserID_IntegrationTest(){
        Map<String, String> properties = new HashMap<>();
        properties.put("Type","delivery");
        properties.put("Subject","Odosla");
        properties.put("UserID",invalidID.toString());
        properties.put("UserType", UserType.ADMIN.toString());
        SendEmailNotificationRequest request = new SendEmailNotificationRequest("message", properties);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.sendEmailNotification(request));
        assertEquals("User not found in database.", thrown.getMessage());
    }

    @Test
    @Description("Tests when the request object has null userID")
    @DisplayName("Null userID in request object")
    void requestObjectHasNullUserID_IntegrationTest(){
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
    @Description("Tests when the subject is empty, should throw an exception.")
    @DisplayName("Empty subject")
    void requestObjectEmptySubject_IntegrationTest(){
        Map<String, String> properties = new HashMap<>();
        properties.put("Type","delivery");
        properties.put("Subject","");
        properties.put("UserID",userID1.toString());
        properties.put("UserType", UserType.ADMIN.toString());
        SendEmailNotificationRequest request = new SendEmailNotificationRequest("message", properties);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.sendEmailNotification(request));
        assertEquals("Empty parameters.", thrown.getMessage());
    }

    @Test
    @Description("Tests when the message is empty, should throw an exception.")
    @DisplayName("Empty message")
    void requestObjectEmptyMessage_IntegrationTest(){
        Map<String, String> properties = new HashMap<>();
        properties.put("Type","delivery");
        properties.put("Subject","odosla");
        properties.put("UserID",userID1.toString());
        properties.put("UserType", UserType.ADMIN.toString());
        SendEmailNotificationRequest request = new SendEmailNotificationRequest("", properties);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.sendEmailNotification(request));
        assertEquals("Empty parameters.", thrown.getMessage());
    }

    @Test
    @Description("Tests when the type is invalid, should throw an exception from the createNotification function.")
    @DisplayName("Invalid type")
    void requestObjectInvalidType_IntegrationTest(){
        Map<String, String> properties = new HashMap<>();
        properties.put("Type","hello");
        properties.put("Subject","odosla");
        properties.put("UserID",userID1.toString());
        properties.put("UserType", UserType.ADMIN.toString());
        SendEmailNotificationRequest request = new SendEmailNotificationRequest("message", properties);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.sendEmailNotification(request));
        assertEquals("Invalid notification type.", thrown.getMessage());
    }

    @Test
    @Description("Tests the sending of an email to an admin account.")
    @DisplayName("Send email successfully")
    void sendSingleEmail_IntegrationTest() throws InvalidRequestException {
        Map<String, String> properties = new HashMap<>();
        properties.put("Type","delivery");
        properties.put("Subject","Odosla");
        properties.put("UserID",userID1.toString());
        properties.put("UserType", UserType.ADMIN.toString());
        SendEmailNotificationRequest request = new SendEmailNotificationRequest("message", properties);
        SendEmailNotificationResponse response = notificationService.sendEmailNotification(request);
        assertEquals(true, response.getSuccessMessage());
        assertEquals("Email sent to u19060468@tuks.co.za - Subject: Odosla - Content: message", response.getResponseMessage());
    }

}
