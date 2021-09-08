//package cs.superleague.notification.integration;
//
//import cs.superleague.notification.NotificationServiceImpl;
//import cs.superleague.notification.dataclass.Notification;
//import cs.superleague.notification.repos.NotificationRepo;
//import cs.superleague.notification.requests.CreateNotificationRequest;
//import cs.superleague.notification.responses.CreateNotificationResponse;
//import cs.superleague.user.UserServiceImpl;
//import cs.superleague.user.dataclass.Admin;
//import cs.superleague.user.dataclass.UserType;
//import cs.superleague.user.exceptions.InvalidRequestException;
//import cs.superleague.user.repos.AdminRepo;
//import cs.superleague.user.repos.CustomerRepo;
//import cs.superleague.user.repos.DriverRepo;
//import cs.superleague.user.repos.ShopperRepo;
//import cs.superleague.user.requests.RegisterAdminRequest;
//import cs.superleague.user.responses.RegisterAdminResponse;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Description;
//
//import javax.transaction.Transactional;
//import java.util.Calendar;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@SpringBootTest
//@Transactional
//public class CreateNotificationIntegrationTest {
//
//    @Autowired
//    NotificationRepo notificationRepo;
//
//    @Autowired
//    AdminRepo adminRepo;
//
//    @Autowired
//    ShopperRepo shopperRepo;
//
//    @Autowired
//    DriverRepo driverRepo;
//
//    @Autowired
//    CustomerRepo customerRepo;
//
//    @Autowired
//    private NotificationServiceImpl notificationService;
//
//    @Autowired
//    private UserServiceImpl userService;
//
//    UUID userID1;
//    UUID invalidID;
//    Admin admin;
//    String email;
//
//    @BeforeEach
//    void setUp() throws InvalidRequestException {
//        email = "u19060468@tuks.co.za";
//        RegisterAdminRequest request = new RegisterAdminRequest("John", "Doe", email, "0743149813", "H123@@@@@dfsfsdf");
//        RegisterAdminResponse response = userService.registerAdmin(request);
//        System.out.println(response.getMessage());
//        admin = adminRepo.findAdminByEmail(email);
//        userID1 = admin.getAdminID();
//        invalidID = UUID.randomUUID();
//        while (invalidID == userID1){
//            invalidID = UUID.randomUUID();
//        }
//    }
//
//    @AfterEach
//    void tearDown(){
//        notificationRepo.deleteAll();
//        adminRepo.deleteAll();
//    }
//
//    @Test
//    @Description("Tests creation of the request object")
//    @DisplayName("Request object creation")
//    void createNotificationRequestObjectCreation_IntegrationTest(){
//        Map<String, String> properties = new HashMap<>();
//        properties.put("NotificationType", "delivery");
//        properties.put("Subject", "Odosla");
//        properties.put("UserID", userID1.toString());
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
//    @Description("Tests for when the UserID is for a different database")
//    @DisplayName("Valid UserID but incorrect type")
//    void WrongPairingOfTypeAndUserID_IntegrationTest() throws cs.superleague.notification.exceptions.InvalidRequestException {
//        Map<String, String> properties = new HashMap<>();
//        properties.put("NotificationType", "delivery");
//        properties.put("Subject", "Odosla");
//        properties.put("UserID", userID1.toString());
//        properties.put("UserType", UserType.CUSTOMER.toString());
//        properties.put("ContactDetails", "u19060468@tuks.co.za");
//        Calendar time = Calendar.getInstance();
//        CreateNotificationRequest request = new CreateNotificationRequest("payLoad", time, properties);
//        Throwable thrown = Assertions.assertThrows(cs.superleague.notification.exceptions.InvalidRequestException.class, ()->notificationService.createNotification(request));
//        assertEquals("User does not exist in database.", thrown.getMessage());
//    }
//
//    @Test
//    @Description("Tests the successful adding of a notification for an admin")
//    @DisplayName("Admin notification added successfully")
//    void addNotificationSuccessfullyAdmin_IntegrationTest() throws cs.superleague.notification.exceptions.InvalidRequestException {
//        Map<String, String> properties = new HashMap<>();
//        properties.put("NotificationType", "delivery");
//        properties.put("Subject", "Odosla");
//        properties.put("UserID", userID1.toString());
//        properties.put("UserType", UserType.ADMIN.toString());
//        properties.put("ContactDetails", "u19060468@tuks.co.za");
//        Calendar time = Calendar.getInstance();
//        CreateNotificationRequest request = new CreateNotificationRequest("payLoad", time, properties);
//        CreateNotificationResponse response = notificationService.createNotification(request);
//        assertEquals(response.getResponseMessage(), "Notification successfully created.");
//    }
//
//}
