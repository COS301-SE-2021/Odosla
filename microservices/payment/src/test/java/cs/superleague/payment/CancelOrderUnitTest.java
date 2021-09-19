package cs.superleague.payment;

import cs.superleague.integration.security.CurrentUser;
import cs.superleague.integration.security.JwtUtil;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.dataclass.*;
import cs.superleague.payment.exceptions.InvalidRequestException;
import cs.superleague.payment.exceptions.NotAuthorisedException;
import cs.superleague.payment.exceptions.OrderDoesNotExist;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.payment.requests.CancelOrderRequest;
import cs.superleague.payment.responses.CancelOrderResponse;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.user.dataclass.Customer;
import cs.superleague.user.dataclass.Shopper;
import cs.superleague.user.dataclass.UserType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Description;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;

import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CancelOrderUnitTest {
    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Mock
    private OrderRepo orderRepo;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    JwtUtil jwtTokenUtil;

    @Value("${shoppingHost}")
    private String shoppingHost;
    @Value("${shoppingPort}")
    private String shoppingPort;
    @Value("${userHost}")
    private String userHost;
    @Value("${userPort}")
    private String userPort;

    Item I1;
    Item I2;
    CartItem cI1;
    CartItem cI2;
    Order order;
    Order o2;

    UUID userID;
    UUID orderID;
    UUID o2UUID = UUID.randomUUID();
    UUID expectedU1 = UUID.randomUUID();
    UUID expectedS1 = UUID.randomUUID();
    UUID expectedShopper1 = UUID.randomUUID();
    Double expectedDiscount;
    double totalC;
    String expectedMessage;
    Customer customer;
    Shopper shopper;

    OrderStatus expectedStatus;
    OrderType expectedType;
    GeoPoint deliveryAddress = new GeoPoint(2.0, 2.0, "2616 Urban Quarters, Hatfield");
    GeoPoint storeAddress = new GeoPoint(3.0, 3.0, "Woolworth's, Hillcrest Boulevard");

    List<Item> expectedListOfItems = new ArrayList<>();
    List<CartItem> cartItems = new ArrayList<>();
    List<Order> listOfOrders = new ArrayList<>();

    @Value("${jwt.secret}")
    private String SECRET = "uQmMa86HgOi6uweJ1JSftIN7TBHFDa3KVJh6kCyoJ9bwnLBqA0YoCAhMMk";

    @Value("${jwt.header}")
    private String HEADER;

    @BeforeEach
    void setUp() {

        userID = UUID.randomUUID();
        orderID = UUID.randomUUID();

        I1 = new Item("Heinz Tamatoe Sauce","123456","123456",expectedS1,36.99,1,"description","img/");
        cI1 = new CartItem("Heinz Tamatoe Sauce","123456","123456",expectedS1,36.99,1,"description","img/");
        I2 = new Item("Bar one","012345","012345",expectedS1,14.99,3,"description","img/");
        cI2 = new CartItem("Bar one","012345","012345",expectedS1,14.99,3,"description","img/");

        expectedMessage="Order successfully created.";
        expectedDiscount=0.0;
        totalC=66.97;
        expectedStatus = OrderStatus.AWAITING_PAYMENT;
        expectedType= OrderType.DELIVERY;
        expectedListOfItems.add(I1);
        expectedListOfItems.add(I2);

        order = new Order();
        order.setOrderID(orderID);
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

        o2 = new Order();
        o2.setOrderID(o2UUID);
        o2.setUserID(expectedU1);
        o2.setStoreID(expectedS1);
        o2.setShopperID(expectedShopper1);
        o2.setCreateDate(new Date());
        o2.setTotalCost(totalC);
        o2.setType(OrderType.DELIVERY);
        o2.setStatus(OrderStatus.AWAITING_PAYMENT);
        o2.setCartItems(cartItems);
        o2.setDiscount(expectedDiscount);
        o2.setDeliveryAddress(deliveryAddress);
        o2.setStoreAddress(storeAddress);
        o2.setRequiresPharmacy(false);

        listOfOrders.add(order);
        listOfOrders.add(o2);

        customer = new Customer();
        customer.setCustomerID(expectedU1);
        customer.setAccountType(UserType.CUSTOMER);
        customer.setEmail("u14254922@tuks.co.za");

        JwtUtil jwtUtil = new JwtUtil();
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

        shopper = new Shopper();
        shopper.setShopperID(expectedShopper1);
        shopper.setAccountType(UserType.SHOPPER);
        shopper.setEmail("ofentse.mogoatlhe@gmail.com");
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }



    @Test
    @DisplayName("When cancelOrderRequest is null")
    void cancelOrderWhenOrderRequestIsNull() {
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> paymentService.cancelOrder(null));
        assertEquals("request object cannot be null - couldn't cancel order", thrown.getMessage());
    }
    @Test
    @DisplayName("When cancelOrderRequest orderID Parameter is null")
    void cancelOrderWhenOrderRequest_OrderID_IsNull() {
        CancelOrderRequest req=new CancelOrderRequest(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> paymentService.cancelOrder(req));
        assertEquals("OrderID cannot be null in request object - cannot get order.", thrown.getMessage());
    }

    @Test
    @DisplayName("When order does not exist")
    void cancelOrderWhenOrderDoesNotExist(){
        when(orderRepo.findById(Mockito.any())).thenReturn(null);
        CancelOrderRequest req=new CancelOrderRequest(UUID.randomUUID());
        Throwable thrown = Assertions.assertThrows(OrderDoesNotExist.class, ()-> paymentService.cancelOrder(req));
        assertEquals("Order doesn't exist in database - cannot get order.", thrown.getMessage());
    }


    /** Checking request object is created correctly */
    @Test
    @Description("Tests whether the CancelOrderRequest object was created correctly")
    @DisplayName("CancelOrderRequest correctly constructed")
    void UnitTest_CancelOrderRequestConstruction() {
        CancelOrderRequest request=new CancelOrderRequest(orderID);
        assertNotNull(request);
        assertEquals(orderID,request.getOrderID());
    }

    @Test
    @DisplayName("When Order Status is Invalid: Delivered")
    void cancelOrderStatusDelivered() throws InvalidRequestException, OrderDoesNotExist, NotAuthorisedException, URISyntaxException {
        List<Order> orders = listOfOrders;
        Order order = orders.get(0);
        int ordersSize = orders.size();
        this.order.setStatus(OrderStatus.DELIVERED);
        when(orderRepo.findAll()).thenReturn(orders);
        when(orderRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(this.order));
        CancelOrderRequest cancelOrderRequest = new CancelOrderRequest(order.getOrderID());
        CancelOrderResponse cancelOrderResponse = paymentService.cancelOrder(cancelOrderRequest);
        Assertions.assertEquals(cancelOrderResponse.getOrders().size(), ordersSize);
        Assertions.assertEquals("Cannot cancel an order that has been delivered/collected.", cancelOrderResponse.getMessage());
        Assertions.assertFalse(cancelOrderResponse.getSuccess());
        // order size does not change
        Assertions.assertEquals(cancelOrderResponse.getOrders().size(), ordersSize);
    }

    @Test
    @DisplayName("When Order Status is Invalid: DeliveryCollected")
    void cancelOrderStatusDeliveryCollected() throws InvalidRequestException, OrderDoesNotExist, NotAuthorisedException, URISyntaxException {
        List<Order> orders = listOfOrders;
        Order order = orders.get(0);
        int ordersSize = orders.size();
        this.order.setStatus(OrderStatus.DELIVERY_COLLECTED);
        when(orderRepo.findAll()).thenReturn(orders);
        when(orderRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(this.order));
        CancelOrderRequest cancelOrderRequest = new CancelOrderRequest(order.getOrderID());
        CancelOrderResponse cancelOrderResponse = paymentService.cancelOrder(cancelOrderRequest);
        Assertions.assertEquals(cancelOrderResponse.getOrders().size(), ordersSize);
        Assertions.assertEquals("Cannot cancel an order that has been delivered/collected.", cancelOrderResponse.getMessage());
        Assertions.assertFalse(cancelOrderResponse.getSuccess());
        // order size does not change
        Assertions.assertEquals(cancelOrderResponse.getOrders().size(), ordersSize);
    }

    @Test
    @DisplayName("When Order Status is Invalid: CustomerCollected")
    void cancelOrderStatusCustomerCollected() throws InvalidRequestException, OrderDoesNotExist, NotAuthorisedException, URISyntaxException {
        List<Order> orders = listOfOrders;
        Order order = orders.get(0);
        int ordersSize = orders.size();
        this.order.setStatus(OrderStatus.CUSTOMER_COLLECTED);
        when(orderRepo.findAll()).thenReturn(orders);
        when(orderRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(this.order));
        CancelOrderRequest cancelOrderRequest = new CancelOrderRequest(order.getOrderID());
        CancelOrderResponse cancelOrderResponse = paymentService.cancelOrder(cancelOrderRequest);
        Assertions.assertEquals(cancelOrderResponse.getOrders().size(), ordersSize);
        Assertions.assertEquals("Cannot cancel an order that has been delivered/collected.", cancelOrderResponse.getMessage());
        Assertions.assertFalse(cancelOrderResponse.getSuccess());
        // order size does not change
        Assertions.assertEquals(cancelOrderResponse.getOrders().size(), ordersSize);
    }

    @Test
    @DisplayName("When Order Status is Valid: AWAITING_PAYMENT")
    void cancelOrderStatusAwaitingPayment() throws InvalidRequestException, OrderDoesNotExist, NotAuthorisedException, URISyntaxException {
        List<Order> orders = listOfOrders;
        Order order = orders.get(0);
        this.order.setStatus(OrderStatus.AWAITING_PAYMENT);

        when(orderRepo.findAll()).thenReturn(orders);
        when(orderRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(this.order));

        CancelOrderRequest cancelOrderRequest = new CancelOrderRequest(order.getOrderID());
        CancelOrderResponse cancelOrderResponse = paymentService.cancelOrder(cancelOrderRequest);

        Assertions.assertEquals("Order successfully cancelled. Customer has been charged 1000.0", cancelOrderResponse.getMessage());
        Assertions.assertTrue(cancelOrderResponse.getSuccess());
        orders.remove(this.order);
        Assertions.assertEquals(cancelOrderResponse.getOrders(),orders);
    }

    @Test
    @DisplayName("When Order Status is Valid: PURCHASED")
    void cancelOrderStatusPurchased() throws InvalidRequestException, OrderDoesNotExist, NotAuthorisedException, URISyntaxException {
        List<Order> orders = listOfOrders;
        Order order = orders.get(0);
        this.order.setStatus(OrderStatus.PURCHASED);
        when(orderRepo.findAll()).thenReturn(orders);
        when(orderRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(this.order));
        CancelOrderRequest cancelOrderRequest = new CancelOrderRequest(order.getOrderID());
        CancelOrderResponse cancelOrderResponse = paymentService.cancelOrder(cancelOrderRequest);
        Assertions.assertEquals("Order successfully cancelled. Customer has been charged 1000.0", cancelOrderResponse.getMessage());
        Assertions.assertTrue(cancelOrderResponse.getSuccess());
        orders.remove(this.order);
        Assertions.assertEquals(cancelOrderResponse.getOrders(),orders);
    }
}