package cs.superleague.payment.integration;

import cs.superleague.integration.security.JwtUtil;
import cs.superleague.payment.PaymentServiceImpl;
import cs.superleague.payment.dataclass.*;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.exceptions.InvalidRequestException;
import cs.superleague.payment.exceptions.NotAuthorisedException;
import cs.superleague.payment.exceptions.OrderDoesNotExist;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.payment.requests.CancelOrderRequest;
import cs.superleague.payment.responses.CancelOrderResponse;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.shopping.requests.SaveItemToRepoRequest;
import cs.superleague.user.dataclass.Customer;
import cs.superleague.user.dataclass.UserType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class CancelOrderIntegrationTest {

    @Autowired
    PaymentServiceImpl paymentService;

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @InjectMocks
    JwtUtil jwtTokenUtil;

    Item item1;
    Item item2;
    CartItem cItem1;
    CartItem cItem2;

    Order order;
    Order order2;

    // IDs
    UUID userID;
    UUID orderID;
    UUID storeID;
    UUID shopperID;

    // total cost
    double totalCost;

    // Items List
    List<Item> expectedListOfItems = new ArrayList<>();
    List<CartItem> cartItems = new ArrayList<>();

    // Order List
    List<Order> listOfOrders=new ArrayList<>();

    // Addresses
    GeoPoint deliveryAddress;
    GeoPoint storeAddress;
    Customer customer;

    private String SECRET = "uQmMa86HgOi6uweJ1JSftIN7TBHFDa3KVJh6kCyoJ9bwnLBqA0YoCAhMMk";

    @BeforeEach
    void setUp() {

        // Assigning IDs
        orderID = UUID.randomUUID();
        userID = UUID.randomUUID();
        storeID = UUID.randomUUID();
        shopperID = UUID.randomUUID();

        // Assigning total cost
        totalCost = 66.97;

        // Assigning items
        item1 = new Item("Heinz Tamatoe Sauce","123456","123456",storeID,36.99,1,"description","img/");
        item2 = new Item("Bar one","012345","012345",storeID,14.99,3,"description","img/");
        cItem1 = new CartItem("Heinz Tamatoe Sauce","123456","123456",storeID,36.99,1,"description","img/");
        cItem2 = new CartItem("Bar one","012345","012345",storeID,14.99,3,"description","img/");

        cItem1.setCartItemNo(UUID.randomUUID());
        cItem2.setCartItemNo(UUID.randomUUID());

        // Adding items to the items list
        expectedListOfItems.add(item1);
        expectedListOfItems.add(item2);
        cartItems.add(cItem1);
        cartItems.add(cItem2);

        for (Item item: expectedListOfItems) {
            SaveItemToRepoRequest saveItemToRepo = new SaveItemToRepoRequest(item);
            rabbitTemplate.convertAndSend("ShoppingEXCHANGE", "RK_SaveItemToRepo", saveItemToRepo);
        }

        // Assigning addresses
        deliveryAddress = new GeoPoint(2.0, 2.0, "2616 Urban Quarters, Hatfield");
        storeAddress = new GeoPoint(3.0, 3.0, "Woolworth's, Hillcrest Boulevard");

        // Assigning order

        order = new Order();
        order.setOrderID(orderID);
        order.setUserID(userID);
        order.setStoreID(storeID);
        order.setShopperID(shopperID);
        order.setCreateDate(new Date());
        order.setTotalCost(totalCost);
        order.setType(OrderType.DELIVERY);
        order.setStatus(OrderStatus.AWAITING_PAYMENT);
        order.setCartItems(cartItems);
        order.setDiscount(0.0);
        order.setDeliveryAddress(deliveryAddress);
        order.setStoreAddress(storeAddress);
        order.setRequiresPharmacy(false);

        order2 = new Order();
        order2.setOrderID(UUID.randomUUID());
        order2.setUserID(UUID.randomUUID());
        order2.setStoreID(UUID.randomUUID());
        order2.setShopperID(UUID.randomUUID());
        order2.setCreateDate(new Date());
        order2.setTotalCost(totalCost);
        order2.setType(OrderType.DELIVERY);
        order2.setStatus(OrderStatus.AWAITING_PAYMENT);
        order2.setCartItems(cartItems);
        order2.setDiscount(0.0);
        order2.setDeliveryAddress(deliveryAddress);
        order2.setStoreAddress(storeAddress);
        order2.setRequiresPharmacy(false);

        // adding orders to the orders list
        listOfOrders.add(order);
        listOfOrders.add(order2);

        orderRepo.save(order);
        orderRepo.save(order2);

        customer = new Customer();
        customer.setCustomerID(order.getUserID());
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
    }

    @AfterEach
    void tearDown() {
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
//        order.setStatus(OrderStatus.DELIVERED);
//
//        orderRepo.save(order);
//
//        CancelOrderRequest cancelOrderRequest = new CancelOrderRequest(order.getOrderID());
//        CancelOrderResponse cancelOrderResponse = paymentService.cancelOrder(cancelOrderRequest);
//        Assertions.assertEquals("Cannot cancel an order that has been delivered/collected.", cancelOrderResponse.getMessage());
//        Assertions.assertEquals(false, cancelOrderResponse.getSuccess());
    }

    @Test
    @DisplayName("When Order Status is Invalid: DeliveryCollected")
    void cancelOrderStatusDeliveryCollected() throws InvalidRequestException, OrderDoesNotExist, NotAuthorisedException, URISyntaxException {
//        this.order.setStatus(OrderStatus.DELIVERY_COLLECTED);
//        orderRepo.save(order);
//        CancelOrderRequest cancelOrderRequest = new CancelOrderRequest(order.getOrderID());
//        CancelOrderResponse cancelOrderResponse = paymentService.cancelOrder(cancelOrderRequest);
//        Assertions.assertEquals("Cannot cancel an order that has been delivered/collected.", cancelOrderResponse.getMessage());
//        Assertions.assertEquals(false, cancelOrderResponse.getSuccess());
    }

    @Test
    @DisplayName("When Order Status is Invalid: CustomerCollected")
    void cancelOrderStatusCustomerCollected() throws InvalidRequestException, OrderDoesNotExist, NotAuthorisedException, URISyntaxException {
//        this.order.setStatus(OrderStatus.CUSTOMER_COLLECTED);
//        orderRepo.save(order);
//        CancelOrderRequest cancelOrderRequest = new CancelOrderRequest(order.getOrderID());
//        CancelOrderResponse cancelOrderResponse = paymentService.cancelOrder(cancelOrderRequest);
//        Assertions.assertEquals("Cannot cancel an order that has been delivered/collected.", cancelOrderResponse.getMessage());
//        Assertions.assertEquals(false, cancelOrderResponse.getSuccess());
    }

    @Test
    @DisplayName("When Order Status is Valid: AWAITING_PAYMENT")
    void cancelOrderStatusAwaitingPayment() throws InvalidRequestException, OrderDoesNotExist, NotAuthorisedException, URISyntaxException {
//        this.order.setStatus(OrderStatus.AWAITING_PAYMENT);
//        orderRepo.save(order);
//        CancelOrderRequest cancelOrderRequest = new CancelOrderRequest(order.getOrderID());
//        CancelOrderResponse cancelOrderResponse = paymentService.cancelOrder(cancelOrderRequest);
//        Assertions.assertEquals("Order successfully cancelled. Customer has been charged 1000.0", cancelOrderResponse.getMessage());
//        Assertions.assertEquals(true, cancelOrderResponse.getSuccess());
//        listOfOrders.remove(order);
    }

    @Test
    @DisplayName("When Order Status is Valid: PURCHASED")
    void cancelOrderStatusPurchased() throws InvalidRequestException, OrderDoesNotExist, NotAuthorisedException, URISyntaxException {

//        this.order.setStatus(OrderStatus.PURCHASED);
//        orderRepo.save(order);
//        CancelOrderRequest cancelOrderRequest = new CancelOrderRequest(order.getOrderID());
//        CancelOrderResponse cancelOrderResponse = paymentService.cancelOrder(cancelOrderRequest);
//        Assertions.assertEquals("Order successfully cancelled. Customer has been charged 1000.0", cancelOrderResponse.getMessage());
//        Assertions.assertEquals(true, cancelOrderResponse.getSuccess());
//        listOfOrders.remove(order);
    }
}
