package cs.superleague.payment;

import cs.superleague.integration.security.JwtUtil;
import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.payment.dataclass.OrderStatus;
import cs.superleague.payment.dataclass.OrderType;
import cs.superleague.payment.exceptions.InvalidRequestException;
import cs.superleague.payment.exceptions.NotAuthorisedException;
import cs.superleague.payment.exceptions.OrderDoesNotExist;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.requests.UpdateOrderRequest;
import cs.superleague.payment.responses.UpdateOrderResponse;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.user.dataclass.Customer;
import cs.superleague.user.dataclass.UserType;
import cs.superleague.user.repos.CustomerRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Description;
import org.mockito.InjectMocks;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateOrderUnitTest {
    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Mock
    private OrderRepo orderRepo;

    @Mock
    private CustomerRepo customerRepo;

    @InjectMocks
    JwtUtil jwtUtil;

    Item I1;
    Item I2;
    Order o;
    Order o2;
    Customer customer;

    UUID o1UUID=UUID.randomUUID();
    UUID o2UUID=UUID.randomUUID();
    UUID expectedU1=UUID.randomUUID();
    UUID expectedS1=UUID.randomUUID();
    UUID expectedShopper1=UUID.randomUUID();
    Double expectedDiscount;
    Double newDiscount;
    double totalC;
    String expectedMessage;
    OrderStatus expectedStatus;
    OrderType expectedType;
    GeoPoint newDeliveryAddress = new GeoPoint(-25.74929765305105, 28.235606061624217, "Hatfield Plaza 1122 Burnett Street &, Grosvenor St, Hatfield, Pretoria, 0083");
    GeoPoint deliveryAddress=new GeoPoint(2.0, 2.0, "2616 Urban Quarters, Hatfield");
    GeoPoint storeAddress=new GeoPoint(3.0, 3.0, "Woolworthes, Hillcrest Boulevard");
    List<Item> expectedListOfItems=new ArrayList<>();
    List<Item> newListOfItems = new ArrayList<>();
    List<Order> listOfOrders=new ArrayList<>();


    private final String SECRET = "uQmMa86HgOi6uweJ1JSftIN7TBHFDa3KVJh6kCyoJ9bwnLBqA0YoCAhMMk";
    @BeforeEach
    void setUp() {
        I1=new Item("Heinz Tamatoe Sauce","123456","123456",expectedS1,36.99,1,"description","img/");
        I2=new Item("Bar one","012345","012345",expectedS1,14.99,3,"description","img/");
        expectedMessage="Order successfully created.";
        expectedDiscount=0.0;
        newDiscount = 13.68;
        totalC=66.97;
        expectedStatus = OrderStatus.AWAITING_PAYMENT;
        expectedType= OrderType.DELIVERY;
        expectedListOfItems.add(I1);
        expectedListOfItems.add(I2);
        newListOfItems.add(I2);
        newListOfItems.add(I1);
        o=new Order(o1UUID, expectedU1, expectedS1, expectedShopper1, Calendar.getInstance(), null, totalC, OrderType.DELIVERY, OrderStatus.AWAITING_PAYMENT, expectedListOfItems, expectedDiscount, deliveryAddress, storeAddress, false);
        o2=new Order(o2UUID,expectedU1, expectedS1, expectedShopper1, Calendar.getInstance(), null, totalC, OrderType.DELIVERY, OrderStatus.AWAITING_PAYMENT, expectedListOfItems, expectedDiscount, deliveryAddress, storeAddress, false);
        listOfOrders.add(o);
        listOfOrders.add(o2);

        customer = new Customer();
        customer.setCustomerID(expectedU1);
        customer.setEmail("superleague@gmail.com");
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
        SecurityContextHolder.clearContext();
    }

    @Test
    @Description("Tests for when an order is updated with a null request object- exception should be thrown")
    @DisplayName("When request object is not specified")
    void UnitTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> paymentService.updateOrder(null));
        assertEquals("Invalid order request received - cannot get order.", thrown.getMessage());
    }

    @Test
    @Description("Tests for when an order is updated with a null orderID in the request object- exception should be thrown")
    @DisplayName("When orderID in the request object is not specified")
    void UnitTest_testingNull_OrderID_Parameter_RequestObject(){
        UpdateOrderRequest request = new UpdateOrderRequest(null, expectedListOfItems, expectedDiscount, expectedType, deliveryAddress);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> paymentService.updateOrder(request));
        assertEquals("OrderID cannot be null in request object - cannot get order.", thrown.getMessage());
    }

    @Test
    @Description("Tests for when an order is updated with an nonexistent orderID in the request object- exception should be thrown")
    @DisplayName("When orderID in the request object does not exist")
    void UnitTest_OrderID_Parameter_RequestObject_Not_In_DB(){
        when(customerRepo.findByEmail(Mockito.any())).thenReturn(Optional.ofNullable(customer));
        UpdateOrderRequest request = new UpdateOrderRequest(UUID.randomUUID(), expectedListOfItems, expectedDiscount, expectedType, deliveryAddress);
        request.setOrderID(UUID.randomUUID());
        Throwable thrown = Assertions.assertThrows(OrderDoesNotExist.class, ()-> paymentService.updateOrder(request));
        assertEquals("Order doesn't exist in database - cannot get order.", thrown.getMessage());
    }

    @Test
    @Description("Tests for when the order status is AWAITING_PAYMENT (order has not been processed yet) - update should be successful for all fields")
    @DisplayName("when the order status is AWAITING_PAYMENT")
    void UnitTest_testingOrderStatus_AWAITING_PAYMENT() throws NotAuthorisedException, InvalidRequestException, OrderDoesNotExist{
        when(customerRepo.findByEmail(Mockito.any())).thenReturn(Optional.ofNullable(customer));
        when(orderRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(o));
        UpdateOrderRequest request = new UpdateOrderRequest(o1UUID, expectedListOfItems, expectedDiscount, OrderType.COLLECTION, newDeliveryAddress);
        request.setOrderID(o1UUID);
        o.setStatus(OrderStatus.AWAITING_PAYMENT);
        UpdateOrderResponse response = paymentService.updateOrder(request);
        assertEquals("Order successfully updated.",response.getMessage());
        assertTrue(response.isSuccess());
        assertEquals(newDeliveryAddress, response.getOrder().getDeliveryAddress());
        assertEquals(OrderType.COLLECTION, response.getOrder().getType());
    }

    @Test
    @Description("Tests for when the order status is PURCHASED (order has not been processed yet) - update should be successful for all fields")
    @DisplayName("When the order status is purchased")
    void UnitTest_testingOrderStatus_PURCHASED() throws NotAuthorisedException, InvalidRequestException, OrderDoesNotExist{
        when(customerRepo.findByEmail(Mockito.any())).thenReturn(Optional.ofNullable(customer));
        when(orderRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(o));
        UpdateOrderRequest request = new UpdateOrderRequest(o1UUID, expectedListOfItems, expectedDiscount, OrderType.COLLECTION, newDeliveryAddress);
        request.setOrderID(o1UUID);
        o.setStatus(OrderStatus.PURCHASED);
        UpdateOrderResponse response = paymentService.updateOrder(request);
        assertEquals("Order successfully updated.",response.getMessage());
        assertTrue(response.isSuccess());
        assertEquals(newDeliveryAddress, response.getOrder().getDeliveryAddress());
        assertEquals(OrderType.COLLECTION, response.getOrder().getType());
    }

    @Test
    @Description("Tests for when the order status is IN_QUEUE (order has not been processed yet) - update should be successful for all fields")
    @DisplayName("When the order status is IN_QUEUE")
    void UnitTest_testingOrderStatus_IN_QUEUE() throws NotAuthorisedException, InvalidRequestException, OrderDoesNotExist{
        when(customerRepo.findByEmail(Mockito.any())).thenReturn(Optional.ofNullable(customer));
        when(orderRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(o));
        UpdateOrderRequest request = new UpdateOrderRequest(o1UUID, expectedListOfItems, expectedDiscount, OrderType.COLLECTION, newDeliveryAddress);
        request.setOrderID(o1UUID);
        o.setStatus(OrderStatus.IN_QUEUE);
        UpdateOrderResponse response = paymentService.updateOrder(request);
        assertEquals("Order successfully updated.",response.getMessage());
        assertTrue(response.isSuccess());
        assertEquals(newDeliveryAddress, response.getOrder().getDeliveryAddress());
        assertEquals(OrderType.COLLECTION, response.getOrder().getType());
    }

    // cannot update order anymore
    @Test
    @Description("Tests for when the order status is PACKING (order has been processed) - All updates should be unsuccessful")
    @DisplayName("When the order status is PACKING")
    void UnitTest_testingOrderStatus_PACKING() throws NotAuthorisedException, InvalidRequestException, OrderDoesNotExist{
        when(customerRepo.findByEmail(Mockito.any())).thenReturn(Optional.ofNullable(customer));
        when(orderRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(o));
        UpdateOrderRequest request = new UpdateOrderRequest(o1UUID, newListOfItems, newDiscount, OrderType.COLLECTION, newDeliveryAddress);
        request.setOrderID(o1UUID);
        o.setStatus(OrderStatus.PACKING);
        UpdateOrderResponse response = paymentService.updateOrder(request);
        assertEquals("Can no longer update the order - UpdateOrder Unsuccessful.",response.getMessage());
        assertFalse(response.isSuccess());
        assertNotEquals(newDeliveryAddress, response.getOrder().getDeliveryAddress());
        assertNotEquals(OrderType.COLLECTION, response.getOrder().getType());
        assertNotEquals(newListOfItems.get(0), response.getOrder().getItems().get(0));
        assertNotEquals(newDiscount, response.getOrder().getDiscount());
    }

    @Test
    @Description("Tests for when the order status is AWAITING_COLLECTION (order has been processed) - All updates should be unsuccessful")
    @DisplayName("When the order status is AWAITING_COLLECTION")
    void UnitTest_testingOrderStatus_AWAITING_COLLECTION() throws NotAuthorisedException, InvalidRequestException, OrderDoesNotExist{

        when(customerRepo.findByEmail(Mockito.any())).thenReturn(Optional.ofNullable(customer));
        when(orderRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(o));
        UpdateOrderRequest request = new UpdateOrderRequest(o1UUID, newListOfItems, newDiscount, OrderType.COLLECTION, newDeliveryAddress);
        request.setOrderID(o1UUID);
        o.setStatus(OrderStatus.AWAITING_COLLECTION);
        UpdateOrderResponse response = paymentService.updateOrder(request);
        assertEquals("Can no longer update the order - UpdateOrder Unsuccessful.",response.getMessage());
        assertFalse(response.isSuccess());
        assertNotEquals(newDeliveryAddress, response.getOrder().getDeliveryAddress());
        assertNotEquals(OrderType.COLLECTION, response.getOrder().getType());
        assertNotEquals(newListOfItems.get(0), response.getOrder().getItems().get(0));
        assertNotEquals(newDiscount, response.getOrder().getDiscount());
    }

    // cannot update order anymore
    @Test
    @Description("Tests for when the order status is DELIVERY_COLLECTED (order has been processed) - All updates should be unsuccessful")
    @DisplayName("When the order status is DELIVERY_COLLECTED")
    void UnitTest_testingOrderStatus_DELIVERY_COLLECTED() throws NotAuthorisedException, InvalidRequestException, OrderDoesNotExist{

        when(customerRepo.findByEmail(Mockito.any())).thenReturn(Optional.ofNullable(customer));
        when(orderRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(o));
        UpdateOrderRequest request = new UpdateOrderRequest(o1UUID, newListOfItems, newDiscount, OrderType.COLLECTION, newDeliveryAddress);
        request.setOrderID(o1UUID);
        o.setStatus(OrderStatus.DELIVERY_COLLECTED);
        UpdateOrderResponse response = paymentService.updateOrder(request);
        assertEquals("Can no longer update the order - UpdateOrder Unsuccessful.",response.getMessage());
        assertFalse(response.isSuccess());
        assertNotEquals(newDeliveryAddress, response.getOrder().getDeliveryAddress());
        assertNotEquals(OrderType.COLLECTION, response.getOrder().getType());
        assertNotEquals(newListOfItems.get(0), response.getOrder().getItems().get(0));
        assertNotEquals(newDiscount, response.getOrder().getDiscount());
    }

    // cannot update order anymore
    @Test
    @Description("Tests for when the order status is CUSTOMER_COLLECTED (order has been processed) - All updates should be unsuccessful")
    @DisplayName("When the order status is CUSTOMER_COLLECTED")
    void UnitTest_testingOrderStatus_CUSTOMER_COLLECTED() throws NotAuthorisedException, InvalidRequestException, OrderDoesNotExist{

        when(customerRepo.findByEmail(Mockito.any())).thenReturn(Optional.ofNullable(customer));
        when(orderRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(o));
        UpdateOrderRequest request = new UpdateOrderRequest(o1UUID, newListOfItems, newDiscount, OrderType.COLLECTION, newDeliveryAddress);
        request.setOrderID(o1UUID);
        o.setStatus(OrderStatus.CUSTOMER_COLLECTED);
        UpdateOrderResponse response = paymentService.updateOrder(request);
        assertEquals("Can no longer update the order - UpdateOrder Unsuccessful.",response.getMessage());
        assertFalse(response.isSuccess());
        assertNotEquals(newDeliveryAddress, response.getOrder().getDeliveryAddress());
        assertNotEquals(OrderType.COLLECTION, response.getOrder().getType());
        assertNotEquals(newListOfItems.get(0), response.getOrder().getItems().get(0));
        assertNotEquals(newDiscount, response.getOrder().getDiscount());
    }

    // cannot update order anymore
    @Test
    @Description("Tests for when the order status is DELIVERED - All updates should be unsuccessful")
    @DisplayName("When the order status is DELIVERED")
    void UnitTest_testingOrderStatus_DELIVERED() throws NotAuthorisedException, InvalidRequestException, OrderDoesNotExist{

        when(customerRepo.findByEmail(Mockito.any())).thenReturn(Optional.ofNullable(customer));
        when(orderRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(o));
        UpdateOrderRequest request = new UpdateOrderRequest(o1UUID, newListOfItems, newDiscount, OrderType.COLLECTION, newDeliveryAddress);
        request.setOrderID(o1UUID);
        o.setStatus(OrderStatus.DELIVERED);
        UpdateOrderResponse response = paymentService.updateOrder(request);
        assertEquals("Can no longer update the order - UpdateOrder Unsuccessful.",response.getMessage());
        assertFalse(response.isSuccess());
        assertNotEquals(newDeliveryAddress, response.getOrder().getDeliveryAddress());
        assertNotEquals(OrderType.COLLECTION, response.getOrder().getType());
        assertNotEquals(newListOfItems.get(0), response.getOrder().getItems().get(0));
        assertNotEquals(newDiscount, response.getOrder().getDiscount());
    }
}
