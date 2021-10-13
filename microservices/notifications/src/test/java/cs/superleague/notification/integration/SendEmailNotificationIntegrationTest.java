package cs.superleague.notification.integration;


import cs.superleague.integration.security.JwtUtil;
import cs.superleague.notifications.NotificationServiceImpl;
import cs.superleague.notifications.exceptions.InvalidRequestException;
import cs.superleague.notifications.repos.NotificationRepo;
import cs.superleague.notifications.requests.SendEmailNotificationRequest;
import cs.superleague.notifications.responses.SendEmailNotificationResponse;
import cs.superleague.user.dataclass.Admin;
import cs.superleague.user.dataclass.Customer;
import cs.superleague.user.dataclass.UserType;
import cs.superleague.user.requests.SaveCustomerToRepoRequest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.http.Header;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.junit.jupiter.api.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest
@Transactional
class SendEmailNotificationIntegrationTest {

    @Autowired
    NotificationRepo notificationRepo;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    RestTemplate restTemplate;
    @Value("${jwt.secret}")
    private String SECRET;

    @Autowired
    private NotificationServiceImpl notificationService;


    UUID userID1;
    UUID invalidID;
    Admin adminCorrectEmail;
    String email;

    @BeforeEach
    void setUp() {
        email = "u19060468@tuks.co.za";
//        RegisterAdminRequest request = new RegisterAdminRequest("John", "Doe", email, "0743149813", "H123@@@@@dfsfsdf");
//        RegisterAdminResponse response = userService.registerAdmin(request);
//        System.out.println(response.getMessage());
//        adminCorrectEmail = adminRepo.findAdminByEmail(email);
        userID1 = UUID.fromString("26634e65-21ca-4c6f-932a-ca3c48755123");
        invalidID = UUID.randomUUID();
        Customer customer = new Customer();
        customer.setCustomerID(userID1);
        customer.setEmail(email);
        customer.setAccountType(UserType.CUSTOMER);
        SaveCustomerToRepoRequest saveCustomerToRepoRequest = new SaveCustomerToRepoRequest(customer);
        rabbitTemplate.convertAndSend("UserEXCHANGE", "RK_SaveCustomerToRepoTest", saveCustomerToRepoRequest);
        JwtUtil jwtUtil = new JwtUtil();
        String jwt = jwtUtil.generateJWTTokenCustomer(customer);

        Header header = new BasicHeader("Authorization", jwt);
        List<Header> headers = new ArrayList<>();
        headers.add(header);
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultHeaders(headers).build();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));

        jwt = jwt.replace("Bearer ","");
        Claims claims= Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(jwt).getBody();
        List<String> authorities = (List) claims.get("authorities");
        String userType= (String) claims.get("userType");
        String email = (String) claims.get("email");
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
                authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        HashMap<String, Object> info=new HashMap<String, Object>();
        info.put("userType",userType);
        info.put("email",email);
        auth.setDetails(info);
        SecurityContextHolder.getContext().setAuthentication(auth);

        while (invalidID == userID1){
            invalidID = UUID.randomUUID();
        }
    }

    @AfterEach
    void tearDown(){
        SecurityContextHolder.clearContext();
        notificationRepo.deleteAll();
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
        properties.put("UserType", UserType.CUSTOMER.toString());
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
        properties.put("UserType", UserType.CUSTOMER.toString());
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
        properties.put("UserType", UserType.CUSTOMER.toString());
        SendEmailNotificationRequest request = new SendEmailNotificationRequest("message", properties);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.sendEmailNotification(request));
        assertEquals("Invalid notification type.", thrown.getMessage());
    }

    @Test
    @Description("Tests the sending of an email to an admin account.")
    @DisplayName("Send email successfully")
    void sendSingleEmail_IntegrationTest() throws InvalidRequestException, URISyntaxException {
        Map<String, String> properties = new HashMap<>();
        properties.put("Type","delivery");
        properties.put("Subject","Odosla");
        properties.put("UserID",userID1.toString());
        properties.put("UserType", UserType.CUSTOMER.toString());
        SendEmailNotificationRequest request = new SendEmailNotificationRequest("message", properties);
        SendEmailNotificationResponse response = notificationService.sendEmailNotification(request);
        assertEquals(true, response.getSuccessMessage());
        assertEquals("Email sent to u19060468@tuks.co.za - Subject: Odosla - Content: message", response.getResponseMessage());
    }

}
