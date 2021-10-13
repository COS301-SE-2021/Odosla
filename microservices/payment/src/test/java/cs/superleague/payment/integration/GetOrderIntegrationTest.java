package cs.superleague.payment.integration;

import cs.superleague.payment.PaymentServiceImpl;
import cs.superleague.payment.dataclass.CartItem;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.dataclass.OrderStatus;
import cs.superleague.payment.dataclass.OrderType;
import cs.superleague.payment.exceptions.InvalidRequestException;
import cs.superleague.payment.exceptions.OrderDoesNotExist;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.payment.requests.GetOrderRequest;
import cs.superleague.payment.responses.GetOrderResponse;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.shopping.requests.SaveItemToRepoRequest;
import org.junit.jupiter.api.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
public class GetOrderIntegrationTest {

    // Services
    @Autowired
    PaymentServiceImpl paymentService;

    // Repos
    @Autowired
    OrderRepo orderRepo;

    @Autowired
    RabbitTemplate rabbitTemplate;

    // Global variables
    double expectedDiscount, expectedTotalCost;

    // Items
    Item I1, I2;
    CartItem cI1, cI2;

    // List of items
    List<Item> expectedListOfItems = new ArrayList<>();
    List<CartItem> cartItems = new ArrayList<>();

    // Orders
    Order order;

    // List of orders
    List<Order> listOfOrders = new ArrayList<>();

    // UUIDs
    UUID expectedOrderId = UUID.randomUUID();
    UUID expectedStoreId = UUID.randomUUID();
    UUID expectedUserId = UUID.randomUUID();
    UUID expectedShopperId = UUID.randomUUID();

    // String
    String expectedMessage_NullRequest;
    String expectedMessage_NullOrderId;
    String expectedMessage_InvalidOrder;
    String expectedMessage_ValidOrderId;

    // request objects
    GetOrderRequest getOrderRequestNull;
    GetOrderRequest getOrderRequestOrderIdNull;
    GetOrderRequest getOrderRequestInvalidOrderId;
    GetOrderRequest getOrderRequestValidOrderId;

    /** This function is used to set any variables needed per test (to be reset at each test).
     * This includes:
     * Storing objects in repos
     * Accessing objects in repos
     * Creation and instantiation of variables
     */

    @BeforeEach
    void setUp(){
        // Assigning request objects
        getOrderRequestNull = null;
        getOrderRequestOrderIdNull = new GetOrderRequest(null);
        getOrderRequestInvalidOrderId = new GetOrderRequest(UUID.randomUUID());
        getOrderRequestValidOrderId = new GetOrderRequest(expectedOrderId);

        // Assigning Exception/Return messages
        expectedMessage_NullRequest = "Invalid order request received - cannot get order.";
        expectedMessage_NullOrderId = "OrderID cannot be null in request object - cannot get order.";
        expectedMessage_ValidOrderId = "Order retrieval successful.";
        expectedMessage_InvalidOrder = "Order doesn't exist in database - cannot get order.";

        // Assigning Item objects
        I1 = new Item("Heinz Tamatoe Sauce", "123456", "123456", expectedStoreId, 36.99, 1, "description", "img/");
        cI1 = new CartItem("Heinz Tamatoe Sauce", "123456", "123456", expectedStoreId, 36.99, 1, "description", "img/");
        I2 = new Item("Bar one", "012345", "012345", expectedStoreId, 14.99, 3, "description", "img/");
        cI2 = new CartItem("Bar one", "012345", "012345", expectedStoreId, 14.99, 3, "description", "img/");
        cI1.setCartItemNo(UUID.randomUUID());
        cI2.setCartItemNo(UUID.randomUUID());

        expectedDiscount = 0.0;
        expectedListOfItems.add(I1);
        expectedListOfItems.add(I2);
        cartItems.add(cI1);
        cartItems.add(cI2);

        for (Item item: expectedListOfItems) {
            SaveItemToRepoRequest saveItemToRepo = new SaveItemToRepoRequest(item);
            rabbitTemplate.convertAndSend("ShoppingEXCHANGE", "RK_SaveItemToRepo", saveItemToRepo);
        }

        order = new Order();
        order.setOrderID(expectedOrderId);
        order.setUserID(expectedUserId);
        order.setStoreID(expectedStoreId);
        order.setShopperID(expectedShopperId);
        order.setCreateDate(new Date());
        order.setTotalCost(expectedTotalCost);
        order.setType(OrderType.DELIVERY);
        order.setStatus(OrderStatus.AWAITING_PAYMENT);
        order.setCartItems(cartItems);
        order.setDiscount(expectedDiscount);
        order.setRequiresPharmacy(false);

        listOfOrders.add(order);
        orderRepo.saveAll(listOfOrders);
    }

    @AfterEach
    void tearDown(){
    }

    // Integration Tests
    @Test
    @DisplayName("When request object is not specified")
    void IntegrationTest_RequestObjectIsNull() {
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, () -> paymentService.getOrder(getOrderRequestNull));
        assertEquals(expectedMessage_NullRequest, thrown.getMessage());
    }

    @Test
    @DisplayName("When orderID in the request object is not specified")
    void IntegrationTest_OrderIDIsNull() {
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, () -> paymentService.getOrder(getOrderRequestOrderIdNull));
        assertEquals(expectedMessage_NullOrderId, thrown.getMessage());
    }

    @Test
    @DisplayName("When orderID in the request object does not exist")
    void IntegrationTest_OrderID_Not_In_DB() {
        Throwable thrown = Assertions.assertThrows(OrderDoesNotExist.class, () -> paymentService.getOrder(getOrderRequestInvalidOrderId));
        assertEquals(expectedMessage_InvalidOrder, thrown.getMessage());
    }

    @Test
    @DisplayName("when the order is found the DB")
    void IntegrationTest_testingOrderExists() throws InvalidRequestException, OrderDoesNotExist{
        GetOrderResponse response = paymentService.getOrder(getOrderRequestValidOrderId);
        assertEquals(expectedMessage_ValidOrderId,response.getMessage());
        assertTrue(response.isSuccess());
        assertEquals(expectedOrderId, response.getOrder().getOrderID());
    }
}
