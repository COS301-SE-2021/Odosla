package cs.superleague.shopping.integration;

import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.dataclass.OrderStatus;
import cs.superleague.payment.dataclass.OrderType;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.shopping.ShoppingServiceImpl;
import cs.superleague.shopping.dataclass.Catalogue;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.shopping.dataclass.Store;
import cs.superleague.shopping.exceptions.InvalidRequestException;
import cs.superleague.shopping.repos.ItemRepo;
import cs.superleague.shopping.repos.StoreRepo;
import cs.superleague.shopping.requests.AddToQueueRequest;
import cs.superleague.shopping.responses.AddToQueueResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class AddToQueueIntegrationTest {

    @Autowired
    private ShoppingServiceImpl shoppingService;

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    StoreRepo storeRepo;

    @Autowired
    ItemRepo itemRepo;

    // Global variables
    double expectedDiscount, expectedtotalCost;

    // Items
    Item I1, I2;

    // List of items
    List<Item> expectedListOfItems = new ArrayList<>();

    // Orders
    Order order;

    // Stores
    Store store;

    // Catalogue
    Catalogue cat;

    // UUIDs
    UUID expectedOrderId = UUID.randomUUID();
    UUID expectedStoreId = UUID.randomUUID();
    UUID expectedUserId = UUID.randomUUID();
    UUID expectedShopperId = UUID.randomUUID();

    // Enums
    OrderType expectedOrderType;


    // Address
    GeoPoint expectedDeliveryAddress;

    /** This function is used to set any variables needed per test (to be reset at each test).
     * This includes:
     * Storing objects in repos
     * Accessing objects in repos
     * Creation and instantiation of variables
     */

    @BeforeEach
    void setUp(){
        // Assigning Item objects
        I1 = new Item("Heinz Tamatoe Sauce", "123456", "123456", expectedStoreId, 36.99, 1, "description", "img/");
        I2 = new Item("Bar one", "012345", "012345", expectedStoreId, 14.99, 3, "description", "img/");

        itemRepo.save(I1);
        itemRepo.save(I2);

        // Assigning Order objects
        expectedListOfItems.add(I1);
        expectedListOfItems.add(I2);

        cat = new Catalogue(UUID.randomUUID(),expectedListOfItems);
        order = new Order(expectedOrderId, expectedUserId, expectedStoreId, expectedShopperId, Calendar.getInstance(), null, expectedtotalCost, OrderType.DELIVERY, OrderStatus.PURCHASED, expectedListOfItems, expectedDiscount, null, null, false);
        store = new Store(expectedStoreId,"Checkers",cat,2,null,null,4,true);
        expectedDiscount = 0.0;
        expectedOrderType = OrderType.COLLECTION;
        expectedDeliveryAddress = new GeoPoint(2.0, 2.0, "2616 Urban Quarters, Hatfield");

        storeRepo.save(store);
    }

    @AfterEach
    void tearDown(){
        storeRepo.delete(store);
    }

    // Integration Tests
    @Test
    @Description("Test for checking if a null request object was passed in when calling AddToQueue - exception should be thrown")
    @DisplayName("Null/empty request passed in")
    void IntegrationTest_nullRequestObject(){
        Throwable thrown = Assertions.assertThrows(cs.superleague.shopping.exceptions.InvalidRequestException.class, ()-> shoppingService.addToQueue(null));
        assertEquals("Invalid request: null value received", thrown.getMessage());
    }

    @Test
    @Description("Test for checking if a null orderID value was passed in the order object when calling AddToQueue - exception should be thrown")
    @DisplayName("No orderID value in passed in request")
    void IntegrationTest_null_orderID(){
        order.setOrderID(null);
        AddToQueueRequest request=new AddToQueueRequest(order);
        Throwable thrown = Assertions.assertThrows(cs.superleague.shopping.exceptions.InvalidRequestException.class, ()-> shoppingService.addToQueue(request));
        assertEquals("Invalid request: Missing order ID", thrown.getMessage());
    }

    @Test
    @Description("Test for checking if a null storeID value was passed in the order object when calling AddToQueue - exception should be thrown")
    @DisplayName("No storeID value in passed in request")
    void IntegrationTest_null_storeID(){
        order.setStoreID(null);
        AddToQueueRequest request=new AddToQueueRequest(order);
        Throwable thrown = Assertions.assertThrows(cs.superleague.shopping.exceptions.InvalidRequestException.class, ()-> shoppingService.addToQueue(request));
        assertEquals("Invalid request: missing store ID", thrown.getMessage());
    }

    @Test
    @Description("Test for checking if a null userID value was passed in the order object when calling AddToQueue - exception should be thrown")
    @DisplayName("Invalid request: missing cs.superleague.user ID")
    void IntegrationTest_null_userID(){
        order.setUserID(null);
        AddToQueueRequest request=new AddToQueueRequest(order);
        Throwable thrown = Assertions.assertThrows(cs.superleague.shopping.exceptions.InvalidRequestException.class, ()-> shoppingService.addToQueue(request));
        assertEquals("Invalid request: missing user ID", thrown.getMessage());
    }

    @Test
    @Description("Test for checking if a null or missing totalCost value was passed in the order object when calling AddToQueue - exception should be thrown")
    @DisplayName("No totalCost value in passed in request")
    void IntegrationTest_null_orderCost(){
        order.setTotalCost(null);
        AddToQueueRequest request=new AddToQueueRequest(order);
        Throwable thrown = Assertions.assertThrows(cs.superleague.shopping.exceptions.InvalidRequestException.class, ()-> shoppingService.addToQueue(request));
        assertEquals("Invalid request: missing order cost", thrown.getMessage());
    }

    /** Checking request object is created correctly */
    @Test
    @Description("Tests whether the AddToQueue request object was created correctly")
    @DisplayName("AddToQueueRequest correctly constructed")
    void IntegrationTest_AddToQueueRequestConstruction() {

        AddToQueueRequest request = new AddToQueueRequest(order);

        assertNotNull(request);
        assertEquals(order, request.getOrder());
    }

    @Test
    @Description("Test for checking if correct status value was passed in the order object when calling AddToQueue - exception should be thrown")
    @DisplayName("Incorrect OrderStatus object value in passed in request")
    void IntegrationTest_incorrect_orderStatus(){
        order.setStatus(OrderStatus.DELIVERED);
        AddToQueueRequest request=new AddToQueueRequest(order);
        Throwable thrown = Assertions.assertThrows(cs.superleague.shopping.exceptions.InvalidRequestException.class, ()-> shoppingService.addToQueue(request));
        assertEquals("Invalid request: order has incompatible status", thrown.getMessage());
    }

    @Test
    @Description("Test for checking if a null or empty item list was passed in the order object when calling AddToQueue - exception should be thrown")
    @DisplayName("No items in passed in request")
    void IntegrationTest_missing_orderItems(){
        order.setItems(null);
        AddToQueueRequest request=new AddToQueueRequest(order);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> shoppingService.addToQueue(request));
        assertEquals("Invalid request: item list is empty or null", thrown.getMessage());
    }

    @Test
    @Description("Test for correct and valid request object passed in - should pass without an exception")
    @DisplayName("Valid request object")
    void IntegrationTest_validRequest(){
        AddToQueueRequest request=new AddToQueueRequest(order);
        AddToQueueResponse response;
        try {
            response = shoppingService.addToQueue(request);
            assertTrue(response.isSuccess());
        } catch (Exception e){
            fail();
        }

    }

}
