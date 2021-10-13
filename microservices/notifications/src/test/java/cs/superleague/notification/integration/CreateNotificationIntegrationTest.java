package cs.superleague.notification.integration;

import cs.superleague.integration.security.JwtUtil;
import cs.superleague.notifications.NotificationServiceImpl;
import cs.superleague.notifications.dataclass.Notification;
import cs.superleague.notifications.exceptions.InvalidRequestException;
import cs.superleague.notifications.repos.NotificationRepo;
import cs.superleague.notifications.requests.CreateNotificationRequest;
import cs.superleague.notifications.responses.CreateNotificationResponse;
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
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class CreateNotificationIntegrationTest {

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
    Admin admin;
    String email;

    @BeforeEach
    void setUp() throws InvalidRequestException {
        email = "u19060468@tuks.co.za";
        //Need to add user with ID like below
        userID1 = UUID.fromString("26634e65-21ca-4c6f-932a-ca3c48755123");
        Customer customer = new Customer();
        customer.setCustomerID(userID1);
        customer.setEmail(email);
        customer.setAccountType(UserType.CUSTOMER);
        System.out.println("9" + customer.getAccountType());
        SaveCustomerToRepoRequest saveCustomerToRepoRequest = new SaveCustomerToRepoRequest(customer);
        rabbitTemplate.convertAndSend("UserEXCHANGE", "RK_SaveCustomerToRepoTest", saveCustomerToRepoRequest);
        invalidID = UUID.randomUUID();
        JwtUtil jwtUtil = new JwtUtil();
        String jwt = jwtUtil.generateJWTTokenCustomer(customer);

        org.apache.http.Header header = new BasicHeader("Authorization", jwt);
        java.util.List<Header> headers = new ArrayList<>();
        headers.add(header);
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultHeaders(headers).build();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));

        jwt = jwt.replace("Bearer ","");
        Claims claims= Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(jwt).getBody();
        java.util.List<String> authorities = (List) claims.get("authorities");
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
    @Description("Tests creation of the request object")
    @DisplayName("Request object creation")
    void createNotificationRequestObjectCreation_IntegrationTest(){
        Map<String, String> properties = new HashMap<>();
        properties.put("NotificationType", "delivery");
        properties.put("Subject", "Odosla");
        properties.put("UserID", userID1.toString());
        properties.put("UserType", UserType.CUSTOMER.toString());
        properties.put("ContactDetails", "u19060468@tuks.co.za");
        Calendar time = Calendar.getInstance();
        CreateNotificationRequest request = new CreateNotificationRequest("payLoad", time, properties);
        assertEquals(request.getProperties(), properties);
        assertEquals(request.getPayLoad(), "payLoad");
        assertEquals(request.getCreatedDateTime(), time);
    }

    @Test
    @Description("Tests for when the UserID is for a different database")
    @DisplayName("Valid UserID but incorrect type")
    void WrongPairingOfTypeAndUserID_IntegrationTest() throws InvalidRequestException {
        Map<String, String> properties = new HashMap<>();
        properties.put("NotificationType", "delivery");
        properties.put("Subject", "Odosla");
        properties.put("UserID", userID1.toString());
        properties.put("UserType", UserType.ADMIN.toString());
        properties.put("ContactDetails", "u19060468@tuks.co.za");
        Calendar time = Calendar.getInstance();
        CreateNotificationRequest request = new CreateNotificationRequest("payLoad", time, properties);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.createNotification(request));
        assertEquals("User does not exist in database.", thrown.getMessage());
    }

    @Test
    @Description("Tests the successful adding of a notification for a customer")
    @DisplayName("Customer notification added successfully")
    void addNotificationSuccessfullyCustomer_IntegrationTest() throws InvalidRequestException, URISyntaxException {
        Map<String, String> properties = new HashMap<>();
        properties.put("NotificationType", "delivery");
        properties.put("Subject", "Odosla");
        properties.put("UserID", userID1.toString());
        properties.put("UserType", UserType.CUSTOMER.toString());
        properties.put("ContactDetails", "u19060468@tuks.co.za");
        Calendar time = Calendar.getInstance();
        CreateNotificationRequest request = new CreateNotificationRequest("payLoad", time, properties);
        CreateNotificationResponse response = notificationService.createNotification(request);
        assertEquals(response.getResponseMessage(), "Notification successfully created.");
    }

}
