package cs.superleague.shopping;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Description;
import cs.superleague.payment.dataclass.OrderStatus;
import cs.superleague.payment.dataclass.OrderType;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.shopping.dataclass.*;
import cs.superleague.shopping.exceptions.InvalidRequestException;
import cs.superleague.shopping.repos.StoreRepo;
import cs.superleague.shopping.requests.AddToQueueRequest;
import cs.superleague.shopping.responses.AddToQueueResponse;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AddToQueueUnitTest {

    @Mock
    private StoreRepo storeRepo;

    @InjectMocks
    private ShoppingServiceImpl shoppingService;

    UUID uuid= UUID.randomUUID();
    Store store;
    Catalogue cat;
    Item i1;
    Item i2;
    List<Item> listOfItems=new ArrayList<>();
    Order order;
    List<Order> listOfOrders = new ArrayList<>();

    @BeforeEach
    void setUp() {
        i1=new Item("Heinz Tomato Sauce","p123456","b123456",uuid,36.99,1,"description","img/");
        i2=new Item("Ice Cream","p012345","b012345",uuid,14.99,3,"description","img/");
        listOfItems.add(i1);
        listOfItems.add(i2);
        cat = new Catalogue(uuid,listOfItems);
        store = new Store(uuid,"Checkers",cat,2,listOfOrders,null,4,true);
        order = new Order(uuid, uuid, uuid, uuid, Calendar.getInstance(), Calendar.getInstance(), 30.0, OrderType.DELIVERY, OrderStatus.PURCHASED, listOfItems, 0.0, null, null, false);
    }
    @AfterEach
    void tearDown() {
        storeRepo.deleteAll();
    }

    //Tests

    @Test
    @Description("Test for checking if a null request object was passed in when calling AddToQueue - exception should be thrown")
    @DisplayName("Null/empty request passed in")
    void UnitTest_nullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> shoppingService.addToQueue(null));
        assertEquals("Invalid request: null value received", thrown.getMessage());
    }


    @Test
    @Description("Test for checking if a null orderID value was passed in the order object when calling AddToQueue - exception should be thrown")
    @DisplayName("No orderID value in passed in request")
    void UnitTest_null_orderID(){
        order.setOrderID(null);
        AddToQueueRequest request=new AddToQueueRequest(order);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> shoppingService.addToQueue(request));
        assertEquals("Invalid request: Missing order ID", thrown.getMessage());
    }

    @Test
    @Description("Test for checking if a null storeID value was passed in the order object when calling AddToQueue - exception should be thrown")
    @DisplayName("No storeID value in passed in request")
    void UnitTest_null_storeID(){
        order.setStoreID(null);
        AddToQueueRequest request=new AddToQueueRequest(order);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> shoppingService.addToQueue(request));
        assertEquals("Invalid request: missing store ID", thrown.getMessage());
    }

    @Test
    @Description("Test for checking if a null userID value was passed in the order object when calling AddToQueue - exception should be thrown")
    @DisplayName("Invalid request: missing cs.superleague.user ID")
    void UnitTest_null_userID(){
        order.setUserID(null);
        AddToQueueRequest request=new AddToQueueRequest(order);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> shoppingService.addToQueue(request));
        assertEquals("Invalid request: missing user ID", thrown.getMessage());
    }

    @Test
    @Description("Test for checking if a null or missing totalCost value was passed in the order object when calling AddToQueue - exception should be thrown")
    @DisplayName("No totalCost value in passed in request")
    void UnitTest_null_orderCost(){
        order.setTotalCost(null);
        AddToQueueRequest request=new AddToQueueRequest(order);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> shoppingService.addToQueue(request));
        assertEquals("Invalid request: missing order cost", thrown.getMessage());
    }

    /** Checking request object is created correctly */
    @Test
    @Description("Tests whether the AddToQueue request object was created correctly")
    @DisplayName("AddToQueueRequest correctly constructed")
    void UnitTest_AddToQueueRequestConstruction() {

        AddToQueueRequest request = new AddToQueueRequest(order);

        assertNotNull(request);
        assertEquals(order, request.getOrder());
    }

    @Test
    @Description("Test for checking if a null or empty item list was passed in the order object when calling AddToQueue - exception should be thrown")
    @DisplayName("No items in passed in request")
    void UnitTest_missing_orderItems(){
        order.setItems(null);
        AddToQueueRequest request=new AddToQueueRequest(order);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> shoppingService.addToQueue(request));
        assertEquals("Invalid request: item list is empty or null", thrown.getMessage());
    }

    @Test
    @Description("Test for correct and valid request object passed in - should pass without an exception")
    @DisplayName("Valid request object")
    void UnitTest_validRequest(){
        AddToQueueRequest request=new AddToQueueRequest(order);
        AddToQueueResponse response;
        try {
            when(storeRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(store));
            response = shoppingService.addToQueue(request);
            assertTrue(response.isSuccess());
        } catch (Exception e){
            System.out.println(e.getMessage());
            fail();
        }

    }


}
