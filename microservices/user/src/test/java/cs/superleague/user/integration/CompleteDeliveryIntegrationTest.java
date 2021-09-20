package cs.superleague.user.integration;

import cs.superleague.integration.security.JwtUtil;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.dataclass.OrderStatus;
import cs.superleague.payment.exceptions.OrderDoesNotExist;
import cs.superleague.payment.requests.SaveOrderToRepoRequest;
import cs.superleague.user.UserServiceImpl;
import cs.superleague.user.dataclass.Driver;
import cs.superleague.user.exceptions.InvalidRequestException;
import cs.superleague.user.repos.DriverRepo;
import cs.superleague.user.requests.CompleteDeliveryRequest;
import cs.superleague.user.responses.CompleteDeliveryResponse;
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
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class CompleteDeliveryIntegrationTest {

    @Autowired
    DriverRepo driverRepo;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Value("${paymentPort}")
    private String paymentPort;
    @Value("${paymentHost}")
    private String paymentHost;
    @Value("${deliveryPort}")
    private String deliveryPort;
    @Value("${deliveryHost}")
    private String deliveryHost;
    @Value("${shoppingPort}")
    private String shoppingPort;
    @Value("${shoppingHost}")
    private String shoppingHost;
    @Value("${jwt.secret}")
    private String SECRET;

    @Autowired
    private UserServiceImpl userService;

    CompleteDeliveryResponse response;
    CompleteDeliveryRequest request;

    Order order;
    UUID orderId;

    Driver driver;
    UUID driverId;

    @BeforeEach
    void setup(){
        driverId= UUID.fromString("4196e85d-dd74-43ff-9643-606f8c739c54");
        orderId = UUID.fromString("31b06384-ce8d-4c6e-9082-7da75cec27fa");
        request = new CompleteDeliveryRequest(orderId);
        order = new Order();
        driver = new Driver();
        driver.setDriverID(driverId);
        driverRepo.save(driver);
        order.setOrderID(orderId);
        order.setDriverID(driverId);
        order.setStatus(OrderStatus.DELIVERY_COLLECTED);
        System.out.println(order.getOrderID());
        SaveOrderToRepoRequest saveOrderRequest = new SaveOrderToRepoRequest(order);
        rabbitTemplate.convertAndSend("PaymentEXCHANGE", "RK_SaveOrderToRepo", saveOrderRequest);
//        JwtUtil jwtUtil = new JwtUtil();
//        String jwt = jwtUtil.generateJWTTokenDriver(driver);
//
//        Header header = new BasicHeader("Authorization", jwt);
//        List<Header> headers = new ArrayList<>();
//        headers.add(header);
//        CloseableHttpClient httpClient = HttpClients.custom().setDefaultHeaders(headers).build();
//        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));
//
//        jwt = jwt.replace("Bearer ","");
//        Claims claims= Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(jwt).getBody();
//        List<String> authorities = (List) claims.get("authorities");
//        String userType= (String) claims.get("userType");
//        String email = (String) claims.get("email");
//        System.out.println(userType);
//        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
//                authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
//        HashMap<String, Object> info=new HashMap<String, Object>();
//        info.put("userType",userType);
//        info.put("email",email);
//        auth.setDetails(info);
//        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @AfterEach
    void teardown(){
        //SecurityContextHolder.clearContext();
        }

    @Test
    @DisplayName("When request object is not specified")
    void IntegrationTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.completeDelivery(null));
        assertEquals("CompleteDeliveryRequest object is null", thrown.getMessage());
    }

    @Test
    @DisplayName("When orderID parameter is not specified")
    void IntegrationTest_testingNullRequestUserIDParameter(){
        request.setOrderID(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.completeDelivery(request));
        assertEquals("OrderID in CompleteDeliveryRequest object is null", thrown.getMessage());
    }

    @Test
    @DisplayName("When order with OrderID does not exist - OrderDoesNotExistException")
    void IntegrationTest_testingOrderDoesNotExistException(){
        request.setOrderID(UUID.randomUUID());
        Throwable thrown = Assertions.assertThrows(OrderDoesNotExist.class, ()-> userService.completeDelivery(request));
        assertEquals("Order does not exist in database", thrown.getMessage());
    }

    @Test
    @DisplayName("Order correctly collected")
    void IntegrationTest_testingCorrectlyCollected() throws InvalidRequestException, OrderDoesNotExist, URISyntaxException {
        request.setOrderID(orderId);
        response=userService.completeDelivery(request);
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertNotNull(response.getTimestamp());
        assertEquals("Order successfully been delivered and status has been changed",response.getMessage());
    }

}
