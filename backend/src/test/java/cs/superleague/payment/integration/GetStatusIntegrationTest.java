package cs.superleague.payment.integration;

import cs.superleague.payment.PaymentServiceImpl;
import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.dataclass.OrderStatus;
import cs.superleague.payment.dataclass.OrderType;
import cs.superleague.payment.exceptions.InvalidRequestException;
import cs.superleague.payment.exceptions.OrderDoesNotExist;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.payment.requests.GetOrderRequest;
import cs.superleague.payment.requests.GetStatusRequest;
import cs.superleague.payment.responses.GetOrderResponse;
import cs.superleague.payment.responses.GetStatusResponse;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.shopping.repos.ItemRepo;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
public class GetStatusIntegrationTest {

    // Services
    @Autowired
    PaymentServiceImpl paymentService;

    // Repos
    @Autowired
    OrderRepo orderRepo;

    @Autowired
    ItemRepo itemRepo;

    // Global variables
    double expectedDiscount, expectedtotalCost;

    // Items
    Item I1, I2;

    // List of items
    List<Item> expectedListOfItems = new ArrayList<>();

    // Orders
    Order O1, O2;

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

    // Enums
    OrderStatus expectedOrderStatus;

    // request objects
    GetOrderRequest getOrderRequestNull;
    GetOrderRequest getOrderRequestOrderIdNull;
    GetOrderRequest getOrderRequestInvalidOrderId;
    GetOrderRequest getOrderRequestValidOrderId;

    // response objects
    GetOrderResponse getOrderResponse;

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
        I2 = new Item("Bar one", "012345", "012345", expectedStoreId, 14.99, 3, "description", "img/");

        expectedDiscount = 0.0;
        expectedListOfItems.add(I1);
        expectedListOfItems.add(I2);

        itemRepo.saveAll(expectedListOfItems);

        O1 = new Order(expectedOrderId, expectedUserId, expectedStoreId, expectedShopperId, Calendar.getInstance(), null, expectedtotalCost, OrderType.DELIVERY, OrderStatus.AWAITING_PAYMENT, expectedListOfItems, expectedDiscount, null, null, false);
        listOfOrders.add(O1);
        orderRepo.saveAll(listOfOrders);
    }

    @AfterEach
    void tearDown(){
        orderRepo.delete(O1);

        itemRepo.delete(I1);
        itemRepo.delete(I2);
    }

    @Test
    @DisplayName("When request object is not specified")
    void IntegrationTest_testingNullRequestObject() {
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, () -> paymentService.getStatus(null));
        assertEquals("Invalid getStatusRequest received - could not get status.", thrown.getMessage());
    }

    @Test
    @DisplayName("When orderID in the request object is not specified")
    void IntegrationTest_testingNull_OrderID_Parameter_RequestObject() {
        GetStatusRequest request = new GetStatusRequest(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, () -> paymentService.getStatus(request));
        assertEquals("OrderID cannot be null in request object - could not get status.", thrown.getMessage());
    }

    @Test
    @DisplayName("When orderID in the request object does not exist")
    void IntegrationTest_OrderID_Parameter_RequestObject_Not_In_DB() {
        GetStatusRequest request = new GetStatusRequest(UUID.randomUUID());
        Throwable thrown = Assertions.assertThrows(OrderDoesNotExist.class, () -> paymentService.getStatus(request));
        assertEquals("Order doesn't exist in database - could not get status.", thrown.getMessage());
    }

    // success
    @Test
    @Description("Tests for when order requested is found - should return the object")
    @DisplayName("when the order is found the DB")
    void IntegrationTest_testingOrderExists() throws InvalidRequestException, OrderDoesNotExist{
        GetStatusRequest request = new GetStatusRequest(expectedOrderId);

        try {
            GetStatusResponse response = paymentService.getStatus(request);

            assertTrue(response.isSuccess());
            assertEquals("Status retrieval successful.", response.getMessage());
        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @Description("Tests for when order requested is found - should return the object")
    @DisplayName("when the order is found the DB")
    void IntegrationTest_testingOrderExistsInvalidUserType() throws InvalidRequestException, OrderDoesNotExist{

        O1.setStatus(null);
        orderRepo.save(O1);
        GetStatusRequest request = new GetStatusRequest(expectedOrderId);

        try {
            GetStatusResponse response = paymentService.getStatus(request);
            assertFalse(response.isSuccess());
            assertEquals("Order does not have a valid Status", response.getMessage());
        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }
}