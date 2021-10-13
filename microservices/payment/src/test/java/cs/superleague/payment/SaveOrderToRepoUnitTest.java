package cs.superleague.payment;

import cs.superleague.integration.security.JwtUtil;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.dataclass.*;
import cs.superleague.payment.exceptions.InvalidRequestException;
import cs.superleague.payment.exceptions.PaymentException;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.payment.requests.SaveOrderToRepoRequest;
import cs.superleague.payment.requests.SubmitOrderRequest;
import cs.superleague.payment.responses.SubmitOrderResponse;
import cs.superleague.shopping.dataclass.Catalogue;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.shopping.dataclass.Store;
import cs.superleague.shopping.responses.GetStoreByUUIDResponse;
import cs.superleague.user.dataclass.Customer;
import cs.superleague.user.dataclass.UserType;
import cs.superleague.user.responses.GetCustomerByEmailResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/** Testing use cases with JUnit testing and Mockito */
@ExtendWith(MockitoExtension.class)
public class SaveOrderToRepoUnitTest {

    @Value("${shoppingHost}")
    private String shoppingHost;
    @Value("${shoppingPort}")
    private String shoppingPort;

    @Mock
    private OrderRepo orderRepo;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @InjectMocks
    JwtUtil jwtUtil;

    @Mock
    RestTemplate restTemplate;

    @Mock
    RabbitTemplate rabbitTemplate;

    Item I1;
    Item I2;
    CartItem cI1;
    CartItem cI2;
    Order order;

    UUID o1UUID = UUID.randomUUID();
    UUID expectedU1 = UUID.randomUUID();
    UUID expectedS1 = UUID.randomUUID();
    UUID expectedShopper1 = UUID.randomUUID();
    Double expectedDiscount;
    double totalC;
    String expectedMessage;

    OrderStatus expectedStatus;
    OrderType expectedType;
    GeoPoint deliveryAddress = new GeoPoint(2.0, 2.0, "2616 Urban Quarters, Hatfield");
    GeoPoint storeAddress = new GeoPoint(3.0, 3.0, "Woolworthes, Hillcrest Boulevard");

    List<Item> expectedListOfItems = new ArrayList<>();
    List<CartItem> cartItems = new ArrayList<>();
    List<Order> listOfOrders = new ArrayList<>();

    Store expectedStore;
    Store closedStore;
    Catalogue c;
    String jwtToken;
    Customer customer;

    @Value("${env.SECRET}")
    private String SECRET = "uQmMa86HgOi6uweJ1JSftIN7TBHFDa3KVJh6kCyoJ9bwnLBqA0YoCAhMMk";

    @BeforeEach
    void setUp() {
        I1 = new Item("Heinz Tamatoe Sauce","123456","123456",expectedS1,36.99,1,"description","img/");
        cI1 = new CartItem("Heinz Tamatoe Sauce","123456","123456",expectedS1,36.99,1,"description","img/");
        I2 = new Item("Bar one","012345","012345",expectedS1,14.99,3,"description","img/");
        cI2 = new CartItem("Bar one","012345","012345",expectedS1,14.99,3,"description","img/");

        expectedMessage="Order successfully created.";
        expectedDiscount=0.0;
        totalC=81.96;
        expectedStatus = OrderStatus.AWAITING_PAYMENT;
        expectedType= OrderType.DELIVERY;

        expectedListOfItems.add(I1);
        expectedListOfItems.add(I2);
        cartItems.add(cI1);
        cartItems.add(cI2);

        order = new Order();
        order.setOrderID(o1UUID);
        order.setUserID(expectedU1);
        order.setStoreID(expectedS1);
        order.setShopperID(expectedShopper1);
        order.setCreateDate(new Date());
        order.setTotalCost(totalC);
        order.setType(OrderType.DELIVERY);
        order.setStatus(OrderStatus.AWAITING_PAYMENT);
        order.setCartItems(cartItems);
        order.setDiscount(expectedDiscount);
        order.setDeliveryAddress(deliveryAddress);
        order.setStoreAddress(storeAddress);
        order.setRequiresPharmacy(false);

        listOfOrders.add(order);
        c=new Catalogue(expectedS1,expectedListOfItems);
        expectedStore=new Store(expectedS1,"Woolworth's",c,3,listOfOrders,null,4,true);
        expectedStore.setStoreLocation(storeAddress);
        closedStore=new Store(expectedS1,"Woolworth's",c,3,listOfOrders,null,4,false);
        closedStore.setStoreLocation(storeAddress);
        customer=new Customer();
        customer.setCustomerID(expectedU1);
        customer.setEmail("hello@gmail.com");
        customer.setAccountType(UserType.CUSTOMER);

        jwtUtil = new JwtUtil();
        String jwt = jwtUtil.generateJWTTokenCustomer(customer);
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

    }

    @AfterEach
    void tearDown() {
    }

    /** InvalidRequest tests */

    @Test
    @Description("Tests for when SaveToOrderRepoRequest is null")
    @DisplayName("When request object in not specified")
    void UnitTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> paymentService
                .saveOrderToRepo(null));
        assertEquals("Null request object.", thrown.getMessage());
    }

    @Test
    @Description("Tests for when SaveToOrderRepoRequest contains null Order object as parameter")
    @DisplayName("When request object parameter - Order is not specified")
    void UnitTest_testingNull_Order_RequestObject(){
        SaveOrderToRepoRequest request = new SaveOrderToRepoRequest(null);

        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->
                paymentService.saveOrderToRepo(request));
        assertEquals("Null parameters", thrown.getMessage());
    }

    @Test
    @Description("Tests for when Exceptions are thrown - Success")
    @DisplayName("When there are no errors")
    void UnitTest_testingSuccessStoreToRepo(){

        SaveOrderToRepoRequest request = new SaveOrderToRepoRequest(order);

        try {
            paymentService.saveOrderToRepo(request);
        }catch (Exception e){
            e.printStackTrace();
            fail();
        }
    }

}
