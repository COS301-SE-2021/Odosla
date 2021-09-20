package cs.superleague.notification.integration;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import cs.superleague.integration.security.JwtUtil;
import cs.superleague.notifications.NotificationServiceImpl;
import cs.superleague.notifications.exceptions.InvalidRequestException;
import cs.superleague.notifications.responses.SendPDFEmailNotificationResponse;
import cs.superleague.notifications.requests.SendPDFEmailNotificationRequest;
import cs.superleague.shopping.dataclass.Item;
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
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class SendPDFEmailNotificationIntegrationTest {

    @Autowired
    private NotificationServiceImpl notificationService;

    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    RestTemplate restTemplate;
    @Value("${jwt.secret}")
    private String SECRET;

    Customer customer;
    UUID customerID;
    byte[] pdfInput;

    @BeforeEach
    void setUp() throws DocumentException {
        customerID = UUID.fromString("26634e65-21ca-4c6f-932a-ca3c48755123");
        Customer customer = new Customer();
        customer.setCustomerID(customerID);
        customer.setEmail("u19060468@tuks.co.za");
        customer.setAccountType(UserType.CUSTOMER);
        SaveCustomerToRepoRequest saveCustomerToRepoRequest = new SaveCustomerToRepoRequest(customer);
        rabbitTemplate.convertAndSend("UserEXCHANGE", "RK_SaveCustomerToRepoTest", saveCustomerToRepoRequest);
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
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);
        document.open();
        Paragraph paragraph = new Paragraph();
        paragraph.add(new Chunk("Hi this is an Odosla test"));
        document.add(paragraph);
        document.close();
        pdfInput = outputStream.toByteArray();
    }

    @AfterEach
    void tearDown(){
        SecurityContextHolder.clearContext();
    }

    @Test
    @Description("Tests for when the userID is not found in the database")
    @DisplayName("No user found")
    void noUserFoundWithAssociatedID_IntegrationTest(){
        SendPDFEmailNotificationRequest request = new SendPDFEmailNotificationRequest(pdfInput, UUID.randomUUID(), "Hi", "message", UserType.SHOPPER);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.sendPDFEmailNotification(request));
        assertEquals("User not found in database.", thrown.getMessage());
    }

    @Test
    @Description("Tests for valid PDF being sent")
    @DisplayName("Valid pdf")
    void validPDFBeingSentOverEmail_IntegrationTest() throws InvalidRequestException, URISyntaxException {
        SendPDFEmailNotificationRequest request = new SendPDFEmailNotificationRequest(pdfInput, customerID, "Hi", "message", UserType.CUSTOMER);
        SendPDFEmailNotificationResponse response = notificationService.sendPDFEmailNotification(request);
        assertEquals(true, response.isSuccess());
        assertEquals("Email sent successfully", response.getMessage());
    }

}
