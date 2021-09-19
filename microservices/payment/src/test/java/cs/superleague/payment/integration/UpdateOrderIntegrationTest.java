package cs.superleague.payment.integration;

import cs.superleague.integration.security.JwtUtil;
import cs.superleague.payment.PaymentServiceImpl;
import cs.superleague.payment.dataclass.*;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.exceptions.InvalidRequestException;
import cs.superleague.payment.exceptions.NotAuthorisedException;
import cs.superleague.payment.exceptions.OrderDoesNotExist;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.payment.requests.UpdateOrderRequest;
import cs.superleague.payment.responses.GetOrderResponse;
import cs.superleague.payment.responses.UpdateOrderResponse;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.shopping.requests.SaveItemToRepoRequest;
import cs.superleague.user.dataclass.Customer;
import cs.superleague.user.dataclass.UserType;
import cs.superleague.user.requests.SaveCustomerToRepoRequest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
public class UpdateOrderIntegrationTest {
    @Autowired
    PaymentServiceImpl paymentService;

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    JwtUtil jwtUtil;

    // Global variables
    double expectedDiscount, expectedtotalCost;

    // Items
    Item I1, I2;
    CartItem cI1, cI2;

    // List of items
    List<CartItem> expectedListOfItems = new ArrayList<>();

    // Orders
    Order O1, O2, O3, O4, O5, O6, O7, O8;

    Customer customer;

    // List of orders
    List<Order> listOfOrders = new ArrayList<>();

    // UUIDs
    UUID expectedOrderId = UUID.randomUUID();
    UUID expectedOrderId2 = UUID.randomUUID();
    UUID expectedOrderId3 = UUID.randomUUID();
    UUID expectedOrderId4 = UUID.randomUUID();
    UUID expectedOrderId5 = UUID.randomUUID();
    UUID expectedOrderId6 = UUID.randomUUID();
    UUID expectedOrderId7 = UUID.randomUUID();
    UUID expectedOrderId8 = UUID.randomUUID();
    UUID expectedStoreId = UUID.fromString("4a0d95d8-8ca3-4224-a6ca-744932b3abba");
    UUID expectedUserId = UUID.fromString("ffd9af91-7573-4a10-a103-85971cea4f6b");
    UUID expectedShopperId = UUID.randomUUID();

    // String
    String expectedMessage_NullRequest;
    String expectedMessage_NullOrderId;
    String expectedMessage_InvalidOrder;
    String expectedMessage_UnauthorisedUser;
    String expectedMessage_AWAITING_PAYMENT;
    String expectedMessage_PACKING;
    String expectedMessage_ValidOrderId;

    // Enums
    OrderType expectedOrderType;

    // request objects
    UpdateOrderRequest updateOrderRequestNull;
    UpdateOrderRequest updateOrderRequestOrderIdNull;
    UpdateOrderRequest updateOrderRequestUserIdNull;
    UpdateOrderRequest updateOrderRequestInvalidOrderId;
    UpdateOrderRequest updateOrderRequestUnauthorisedUser;
    UpdateOrderRequest updateOrderRequestValidOrderId;
    UpdateOrderRequest updateOrderRequest_PURCHASED;
    UpdateOrderRequest updateOrderRequest_IN_QUEUE;
    UpdateOrderRequest updateOrderRequest_PACKING;
    UpdateOrderRequest updateOrderRequest_AWAITING_COLLECTION;
    UpdateOrderRequest updateOrderRequest_DELIVERY_COLLECTED;
    UpdateOrderRequest updateOrderRequest_CUSTOMER_COLLECTED;
    UpdateOrderRequest updateOrderRequest_DELIVERED;


    // Address
    GeoPoint expectedDeliveryAddress;
    // response objects
    GetOrderResponse getOrderResponse;

    /** This function is used to set any variables needed per test (to be reset at each test).
     * This includes:
     * Storing objects in repos
     * Accessing objects in repos
     * Creation and instantiation of variables
     */

    @Value("${env.SECRET}")
    private String SECRET = "stub";

    @Value("${env.HEADER}")
    private String HEADER = "stub";

    @BeforeEach
    void setUp(){
        // Assigning Item objects
        I1 = new Item("Heinz Tamatoe Sauce", "123456", "123456", expectedStoreId, 36.99, 1, "description", "img/");
        I2 = new Item("Bar one", "012345", "012345", expectedStoreId, 14.99, 3, "description", "img/");

        SaveItemToRepoRequest saveItemToRepo = new SaveItemToRepoRequest(I1);
        rabbitTemplate.convertAndSend("ShoppingEXCHANGE", "RK_SaveItemToRepo", saveItemToRepo);

        saveItemToRepo = new SaveItemToRepoRequest(I2);
        rabbitTemplate.convertAndSend("ShoppingEXCHANGE", "RK_SaveItemToRepo", saveItemToRepo);
        // Assigning Order objects
        expectedListOfItems.add(cI1);
        expectedListOfItems.add(cI2);

        O1 = new Order(expectedOrderId, expectedUserId, expectedStoreId, expectedShopperId,
                new Date(), null, expectedtotalCost, OrderType.DELIVERY, OrderStatus.AWAITING_PAYMENT, expectedDiscount, null, null, false);
        O2 = new Order(expectedOrderId2, expectedUserId, expectedStoreId, expectedShopperId, new Date(), null, expectedtotalCost, OrderType.DELIVERY, OrderStatus.PURCHASED, expectedDiscount, null, null, false);
        O3 = new Order(expectedOrderId3, expectedUserId, expectedStoreId, expectedShopperId, new Date(), null, expectedtotalCost, OrderType.DELIVERY, OrderStatus.IN_QUEUE, expectedDiscount, null, null, false);
        O4 = new Order(expectedOrderId4, expectedUserId, expectedStoreId, expectedShopperId, new Date(), null, expectedtotalCost, OrderType.DELIVERY, OrderStatus.PACKING, expectedDiscount, null, null, false);
        O5 = new Order(expectedOrderId5, expectedUserId, expectedStoreId, expectedShopperId, new Date(), null, expectedtotalCost, OrderType.DELIVERY, OrderStatus.AWAITING_COLLECTION, expectedDiscount, null, null, false);
        O6 = new Order(expectedOrderId6, expectedUserId, expectedStoreId, expectedShopperId, new Date(), null, expectedtotalCost, OrderType.DELIVERY, OrderStatus.DELIVERY_COLLECTED, expectedDiscount, null, null, false);
        O7 = new Order(expectedOrderId7, expectedUserId, expectedStoreId, expectedShopperId, new Date(), null, expectedtotalCost, OrderType.DELIVERY, OrderStatus.CUSTOMER_COLLECTED, expectedDiscount, null, null, false);
        O8 = new Order(expectedOrderId8, expectedUserId, expectedStoreId, expectedShopperId, new Date(), null, expectedtotalCost, OrderType.DELIVERY, OrderStatus.DELIVERED, expectedDiscount, null, null, false);


        expectedDiscount = 0.0;
        expectedOrderType = OrderType.COLLECTION;
        expectedDeliveryAddress = new GeoPoint(2.0, 2.0, "2616 Urban Quarters, Hatfield");;

        // Assigning request objects
        updateOrderRequestNull = null;
        updateOrderRequestOrderIdNull = new UpdateOrderRequest(null, expectedListOfItems, expectedDiscount, expectedOrderType, expectedDeliveryAddress);
        updateOrderRequestUserIdNull = new UpdateOrderRequest(expectedOrderId, expectedListOfItems, expectedDiscount, expectedOrderType, expectedDeliveryAddress);
        updateOrderRequestInvalidOrderId = new UpdateOrderRequest(UUID.randomUUID(), expectedListOfItems, expectedDiscount, expectedOrderType, expectedDeliveryAddress);
        updateOrderRequestUnauthorisedUser = new UpdateOrderRequest(expectedOrderId, expectedListOfItems, expectedDiscount, expectedOrderType, expectedDeliveryAddress);
        updateOrderRequestValidOrderId = new UpdateOrderRequest(expectedOrderId, expectedListOfItems, expectedDiscount, expectedOrderType, expectedDeliveryAddress);
        updateOrderRequest_PURCHASED = new UpdateOrderRequest(expectedOrderId2, expectedListOfItems, expectedDiscount, expectedOrderType, expectedDeliveryAddress);
        updateOrderRequest_IN_QUEUE = new UpdateOrderRequest(expectedOrderId3, expectedListOfItems, expectedDiscount, expectedOrderType, expectedDeliveryAddress);
        updateOrderRequest_PACKING = new UpdateOrderRequest(expectedOrderId4, expectedListOfItems, expectedDiscount, expectedOrderType, expectedDeliveryAddress);
        updateOrderRequest_AWAITING_COLLECTION = new UpdateOrderRequest(expectedOrderId5, expectedListOfItems, expectedDiscount, expectedOrderType, expectedDeliveryAddress);
        updateOrderRequest_CUSTOMER_COLLECTED = new UpdateOrderRequest(expectedOrderId7, expectedListOfItems, expectedDiscount, expectedOrderType, expectedDeliveryAddress);
        updateOrderRequest_DELIVERY_COLLECTED = new UpdateOrderRequest(expectedOrderId6, expectedListOfItems, expectedDiscount, expectedOrderType, expectedDeliveryAddress);
        updateOrderRequest_DELIVERED = new UpdateOrderRequest(expectedOrderId8, expectedListOfItems, expectedDiscount, expectedOrderType, expectedDeliveryAddress);


        // Assigning Exception/Return messages
        expectedMessage_NullRequest = "Invalid order request received - cannot get order.";
        expectedMessage_NullOrderId = "OrderID cannot be null in request object - cannot get order.";
        expectedMessage_ValidOrderId = "Order successfully updated.";
        expectedMessage_UnauthorisedUser = "Not Authorised to update an order you did not place.";
        expectedMessage_AWAITING_PAYMENT = "Order successfully updated.";
        expectedMessage_InvalidOrder = "Order doesn't exist in database - cannot get order.";
        expectedMessage_PACKING = "Can no longer update the order - UpdateOrder Unsuccessful.";

        listOfOrders.add(O1);
        listOfOrders.add(O2);
        listOfOrders.add(O3);
        listOfOrders.add(O4);
        listOfOrders.add(O5);
        listOfOrders.add(O6);
        listOfOrders.add(O7);
        listOfOrders.add(O8);

        customer = new Customer();
        customer.setCustomerID(expectedUserId);
        customer.setEmail("superleague@gmail.com");
        customer.setAccountType(UserType.CUSTOMER);

        String jwt = jwtUtil.generateJWTTokenCustomer(customer);
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

        SaveCustomerToRepoRequest saveCustomerToRepoRequest = new SaveCustomerToRepoRequest(customer);
        rabbitTemplate.convertAndSend("UserEXCHANGE", "RK_SaveCustomerToRepoTest", saveCustomerToRepoRequest);

        orderRepo.saveAll(listOfOrders);
    }

    @AfterEach
    void tearDown(){
        SecurityContextHolder.clearContext();
    }

    // Integration Tests
    @Test
    @DisplayName("When request object is not specified")
    void IntegrationTest_RequestObjectIsNull() {
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, () -> paymentService.updateOrder(null));
        assertEquals(expectedMessage_NullRequest, thrown.getMessage());
    }

    @Test
    @DisplayName("When orderID in the request object is not specified")
    void IntegrationTest_OrderIDIsNull() {
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, () -> paymentService.updateOrder(updateOrderRequestOrderIdNull));
        assertEquals(expectedMessage_NullOrderId, thrown.getMessage());
    }

    @Test
    @DisplayName("When orderID in the request object does not exist")
    void IntegrationTest_OrderID_Not_In_DB() {
//        Throwable thrown = Assertions.assertThrows(OrderDoesNotExist.class, () -> paymentService.updateOrder(updateOrderRequestInvalidOrderId));
//        assertEquals(expectedMessage_InvalidOrder, thrown.getMessage());
    }

    @Test
    @DisplayName("when the order status is AWAITING_PAYMENT")
    void IntegrationTest_testingOrderStatus_AWAITING_PAYMENT() throws NotAuthorisedException, InvalidRequestException, OrderDoesNotExist, URISyntaxException {
//        UpdateOrderResponse response = paymentService.updateOrder(updateOrderRequestValidOrderId);
//        assertEquals(expectedMessage_AWAITING_PAYMENT,response.getMessage());
//        assertTrue(response.isSuccess());
//        assertEquals(expectedDeliveryAddress.getAddress(), response.getOrder().getDeliveryAddress().getAddress());
//        assertEquals(expectedDeliveryAddress.getLatitude(), response.getOrder().getDeliveryAddress().getLatitude());
//        assertEquals(expectedDeliveryAddress.getLongitude(), response.getOrder().getDeliveryAddress().getLongitude());
//        assertEquals(OrderType.COLLECTION, response.getOrder().getType());
    }

    @Test
    @DisplayName("when the order status is PURCHASED")
    void IntegrationTest_testingOrderStatus_PURCHASED() throws NotAuthorisedException, InvalidRequestException, OrderDoesNotExist, URISyntaxException {
//        UpdateOrderResponse response = paymentService.updateOrder(updateOrderRequest_PURCHASED);
//        assertEquals(expectedMessage_AWAITING_PAYMENT,response.getMessage()); // same message as awaiting payment
//        assertTrue(response.isSuccess());
//        assertEquals(expectedDeliveryAddress.getAddress(), response.getOrder().getDeliveryAddress().getAddress());
//        assertEquals(expectedDeliveryAddress.getLatitude(), response.getOrder().getDeliveryAddress().getLatitude());
//        assertEquals(expectedDeliveryAddress.getLongitude(), response.getOrder().getDeliveryAddress().getLongitude());
//        assertEquals(OrderType.COLLECTION, response.getOrder().getType());
    }

    @Test
    @DisplayName("when the order status is IN_QUEUE")
    void IntegrationTest_testingOrderStatus_INQUEUE() throws NotAuthorisedException, InvalidRequestException, OrderDoesNotExist, URISyntaxException {
//        UpdateOrderResponse response = paymentService.updateOrder(updateOrderRequest_IN_QUEUE);
//        assertEquals(expectedMessage_AWAITING_PAYMENT,response.getMessage()); // same message as awaiting payment
//        assertTrue(response.isSuccess());
//        assertEquals(expectedDeliveryAddress.getAddress(), response.getOrder().getDeliveryAddress().getAddress());
//        assertEquals(expectedDeliveryAddress.getLatitude(), response.getOrder().getDeliveryAddress().getLatitude());
//        assertEquals(expectedDeliveryAddress.getLongitude(), response.getOrder().getDeliveryAddress().getLongitude());
//        assertEquals(OrderType.COLLECTION, response.getOrder().getType());
    }

    @Test
    @DisplayName("when the order status is IN_PACKING")
    void IntegrationTest_testingOrderStatus_PACKING() throws NotAuthorisedException, InvalidRequestException, OrderDoesNotExist, URISyntaxException {
//        UpdateOrderResponse response = paymentService.updateOrder(updateOrderRequest_PACKING);
//        assertEquals(expectedMessage_PACKING,response.getMessage());
//        assertFalse(response.isSuccess());
//        Assertions.assertNotEquals(expectedDeliveryAddress, response.getOrder().getDeliveryAddress());
//        assertNotEquals(OrderType.COLLECTION, response.getOrder().getType());
    }

    @Test
    @DisplayName("when the order status is AWAITING_COLLECTION")
    void IntegrationTest_testingOrderStatus_AWAITING_COLLECTION() throws NotAuthorisedException, InvalidRequestException, OrderDoesNotExist, URISyntaxException {
//        UpdateOrderResponse response = paymentService.updateOrder(updateOrderRequest_AWAITING_COLLECTION);
//        assertEquals(expectedMessage_PACKING,response.getMessage()); // same message as packing
//        assertFalse(response.isSuccess());
//        Assertions.assertNotEquals(expectedDeliveryAddress, response.getOrder().getDeliveryAddress());
//        assertNotEquals(OrderType.COLLECTION, response.getOrder().getType());
    }

    @Test
    @DisplayName("when the order status is DELIVERY_COLLECTED")
    void IntegrationTest_testingOrderStatus_DELIVERY_COLLECTED() throws NotAuthorisedException, InvalidRequestException, OrderDoesNotExist, URISyntaxException {
//        UpdateOrderResponse response = paymentService.updateOrder(updateOrderRequest_DELIVERY_COLLECTED);
//        assertEquals(expectedMessage_PACKING,response.getMessage()); // same message as packing
//        assertFalse(response.isSuccess());
//        Assertions.assertNotEquals(expectedDeliveryAddress, response.getOrder().getDeliveryAddress());
//        assertNotEquals(OrderType.COLLECTION, response.getOrder().getType());
    }

    @Test
    @DisplayName("when the order status is CUSTOMER_COLLECTED")
    void IntegrationTest_testingOrderStatus_CUSTOMER_COLLECTED() throws NotAuthorisedException, InvalidRequestException, OrderDoesNotExist, URISyntaxException {
//        UpdateOrderResponse response = paymentService.updateOrder(updateOrderRequest_CUSTOMER_COLLECTED);
//        assertEquals(expectedMessage_PACKING,response.getMessage()); // same message as packing
//        assertFalse(response.isSuccess());
//        Assertions.assertNotEquals(expectedDeliveryAddress, response.getOrder().getDeliveryAddress());
//        assertNotEquals(OrderType.COLLECTION, response.getOrder().getType());
    }

    @Test
    @DisplayName("when the order status is DELIVERED")
    void IntegrationTest_testingOrderStatus_DELIVERED() throws NotAuthorisedException, InvalidRequestException, OrderDoesNotExist, URISyntaxException {
//        UpdateOrderResponse response = paymentService.updateOrder(updateOrderRequest_DELIVERED);
//        assertEquals(expectedMessage_PACKING,response.getMessage()); // same message as packing
//        assertFalse(response.isSuccess());
//        Assertions.assertNotEquals(expectedDeliveryAddress, response.getOrder().getDeliveryAddress());
//        assertNotEquals(OrderType.COLLECTION, response.getOrder().getType());
    }

}
