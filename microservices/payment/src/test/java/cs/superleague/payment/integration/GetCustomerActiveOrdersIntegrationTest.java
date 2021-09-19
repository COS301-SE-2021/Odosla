package cs.superleague.payment.integration;

import cs.superleague.integration.security.JwtUtil;
import cs.superleague.payment.PaymentServiceImpl;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.dataclass.OrderStatus;
import cs.superleague.payment.exceptions.InvalidRequestException;
import cs.superleague.payment.exceptions.OrderDoesNotExist;
import cs.superleague.payment.exceptions.PaymentException;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.payment.requests.GetCustomersActiveOrdersRequest;
import cs.superleague.payment.responses.GetCustomersActiveOrdersResponse;
import cs.superleague.user.dataclass.Customer;
import cs.superleague.user.dataclass.UserType;
import cs.superleague.user.exceptions.UserDoesNotExistException;
import cs.superleague.user.requests.SaveCustomerToRepoRequest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class GetCustomerActiveOrdersIntegrationTest {
    @Autowired
    private PaymentServiceImpl paymentService;

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    JwtUtil jwtTokenUtil;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    RestTemplate restTemplate;

    UUID orderID;
    Order order;
    UUID customerID;
    Customer customer;
    String jwtToken;
    List<Order> orderList;
    GetCustomersActiveOrdersRequest request;
    Order order2;
    String invalidJWTToken;

    @Value("${env.SECRET}")
    private String SECRET;

    @Value("${env.HEADER}")
    private String HEADER;


    @BeforeEach
    void setUp() {
        customerID = UUID.fromString("ffd9af91-7573-4a10-a103-85971cea4f6b");
        Customer customer2 = new Customer();
        customer2.setCustomerID(UUID.randomUUID());
        customer2.setEmail("jj@gmail.com");
        invalidJWTToken = jwtTokenUtil.generateJWTTokenCustomer(customer2);
        orderID = UUID.randomUUID();
        customer = new Customer();
        customer.setCustomerID(customerID);
        customer.setEmail("hello@gmail.com");
        customer.setAccountType(UserType.CUSTOMER);
        jwtToken = jwtTokenUtil.generateJWTTokenCustomer(customer);
        order = new Order();
        order2 = new Order();
        order.setOrderID(orderID);
        order2.setOrderID(UUID.randomUUID());
        order.setUserID(customerID);
        order2.setUserID(customerID);
        request = new GetCustomersActiveOrdersRequest();
        orderList = new ArrayList<>();
        orderRepo.save(order);
        orderRepo.save(order2);


        String jwt = jwtTokenUtil.generateJWTTokenCustomer(customer);
        jwt = jwt.replace(HEADER,"");
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

//        rabbitTemplate.setChannelTransacted(true);
        SaveCustomerToRepoRequest saveCustomerToRepoRequest = new SaveCustomerToRepoRequest(customer);
        rabbitTemplate.convertAndSend("UserEXCHANGE", "RK_SaveCustomerToRepoTest", saveCustomerToRepoRequest);
    }

    @AfterEach
    void tearDown(){
        SecurityContextHolder.clearContext();
    }

    @Test
    @Description("Test for when there is a null request object.")
    @DisplayName("Null request")
    void nullRequestObjectPassedIn_IntegrationTest(){
        Throwable thrown = Assertions.assertThrows(PaymentException.class, ()-> paymentService.getCustomersActiveOrders(null));
        assertEquals("Get Customers Active Orders Request cannot be null - Retrieval of Order unsuccessful", thrown.getMessage());
    }
}
