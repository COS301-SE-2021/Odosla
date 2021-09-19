package cs.superleague.payment;

import cs.superleague.integration.security.JwtUtil;
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
import cs.superleague.user.responses.GetCustomerByEmailResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class GetCustomersActiveOrdersUnitTest {

    @Value("${userHost}")
    private String userHost;
    @Value("${userPort}")
    private String userPort;

    @Mock
    OrderRepo orderRepo;

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    JwtUtil jwtTokenUtil;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    UUID orderID;
    Order order;
    UUID customerID;
    Customer customer;
    String jwtToken;
    List<Order> orderList;
    GetCustomersActiveOrdersRequest request;
    Order order2;

    @Value("${jwt.secret}")
    private String SECRET = "uQmMa86HgOi6uweJ1JSftIN7TBHFDa3KVJh6kCyoJ9bwnLBqA0YoCAhMMk";

    @BeforeEach
    void setUp() {
        orderID = UUID.randomUUID();
        customerID = UUID.randomUUID();
        customer = new Customer();
        customer.setCustomerID(customerID);
        customer.setEmail("hello@gmail.com");
        customer.setAccountType(UserType.CUSTOMER);

        jwtToken = jwtTokenUtil.generateJWTTokenCustomer(customer);
        jwtToken = jwtToken.replace("Bearer ","");
        Claims claims= Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(jwtToken).getBody();
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

        order = new Order();
        order2 = new Order();
        order.setOrderID(orderID);
        order2.setOrderID(UUID.randomUUID());
        order.setUserID(customerID);
        order2.setUserID(customerID);
        request = new GetCustomersActiveOrdersRequest();
        orderList = new ArrayList<>();
    }

    @AfterEach
    void tearDown(){
        SecurityContextHolder.clearContext();
    }

    @Test
    @Description("Tests for when the request object is not specified.")
    @DisplayName("Null request object")
    void testingNullRequestObject_UnitTest(){
        Throwable thrown = Assertions.assertThrows(PaymentException.class, ()-> paymentService.getCustomersActiveOrders(null));
        assertEquals("Get Customers Active Orders Request cannot be null - Retrieval of Order unsuccessful", thrown.getMessage());
    }


    @Test
    @Description("Tests for when there is no order in the repo for the user.")
    @DisplayName("No orders")
    void noOrdersFoundInDatabaseForCustomer_UnitTest() throws URISyntaxException {
        Mockito.when(orderRepo.findAllByUserID(Mockito.any())).thenReturn(null);

        GetCustomerByEmailResponse getCustomerByEmailResponse = new GetCustomerByEmailResponse(customer, true);

        Map<String, Object> parts = new HashMap<>();
        parts.put("email", customer.getEmail());

        ResponseEntity<GetCustomerByEmailResponse> responseEntity = new ResponseEntity<>(
                getCustomerByEmailResponse, HttpStatus.OK);

        String stringUri = "http://"+userHost+":"+userPort+"/user/getCustomerByEmail";
        URI uri = new URI(stringUri);

        Mockito.when(restTemplate.postForEntity(uri,
                parts, GetCustomerByEmailResponse.class)).thenReturn(responseEntity);

        Throwable thrown = Assertions.assertThrows(OrderDoesNotExist.class, ()-> paymentService.getCustomersActiveOrders(request));
        assertEquals(thrown.getMessage(), "No Orders found for this user in the database.");
    }

    @Test
    @Description("Tests for when there are no active orders in the database.")
    @DisplayName("No active orders")
    void noActiveOrdersInDatabase_UnitTest() throws InvalidRequestException, OrderDoesNotExist, URISyntaxException {
        order.setStatus(OrderStatus.CUSTOMER_COLLECTED);
        orderList.add(order);
        Mockito.when(orderRepo.findAllByUserID(Mockito.any())).thenReturn(orderList);

        GetCustomerByEmailResponse getCustomerByEmailResponse = new GetCustomerByEmailResponse(customer, true);

        Map<String, Object> parts = new HashMap<>();
        parts.put("email", customer.getEmail());

        ResponseEntity<GetCustomerByEmailResponse> responseEntity = new ResponseEntity<>(
                getCustomerByEmailResponse, HttpStatus.OK);

        String stringUri = "http://"+userHost+":"+userPort+"/user/getCustomerByEmail";
        URI uri = new URI(stringUri);

        Mockito.when(restTemplate.postForEntity(uri, parts, GetCustomerByEmailResponse.class)).thenReturn(responseEntity);

        GetCustomersActiveOrdersResponse response = paymentService.getCustomersActiveOrders(request);
        assertEquals(response.getMessage(), "This customer has no active orders.");
        assertFalse(response.isHasActiveOrder());
        assertNull(response.getOrderID());
    }

    @Test
    @Description("Tests for when the user has an active order")
    @DisplayName("Active order returned")
    void activeOrderReturnedFromRequest_UnitTest() throws InvalidRequestException, OrderDoesNotExist, URISyntaxException {
        order.setStatus(OrderStatus.PACKING);
        order2.setStatus(OrderStatus.CUSTOMER_COLLECTED);
        orderList.add(order);
        orderList.add(order2);
        Mockito.when(orderRepo.findAllByUserID(Mockito.any())).thenReturn(orderList);

        GetCustomerByEmailResponse getCustomerByEmailResponse = new GetCustomerByEmailResponse(customer, true);

        Map<String, Object> parts = new HashMap<>();
        parts.put("email", customer.getEmail());

        ResponseEntity<GetCustomerByEmailResponse> responseEntity = new ResponseEntity<>(
                getCustomerByEmailResponse, HttpStatus.OK);

        String stringUri = "http://"+userHost+":"+userPort+"/user/getCustomerByEmail";
        URI uri = new URI(stringUri);

        Mockito.when(restTemplate.postForEntity(uri,
                parts, GetCustomerByEmailResponse.class)).thenReturn(responseEntity);

        GetCustomersActiveOrdersResponse response = paymentService.getCustomersActiveOrders(request);
        assertEquals(response.getMessage(), "Order successfully returned to customer.");
        assertTrue(response.isHasActiveOrder());
        assertEquals(response.getOrderID(), orderID);
    }

}
