package cs.superleague.shopping;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Description;
import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.dataclass.OrderStatus;
import cs.superleague.payment.dataclass.OrderType;
import cs.superleague.shopping.dataclass.Catalogue;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.shopping.dataclass.Store;
import cs.superleague.shopping.exceptions.InvalidRequestException;
import cs.superleague.shopping.exceptions.StoreDoesNotExistException;
import cs.superleague.shopping.repos.StoreRepo;
import cs.superleague.shopping.requests.RemoveQueuedOrderRequest;
import cs.superleague.shopping.responses.RemoveQueuedOrderResponse;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RemoveQueuedOrderUnitTest {

    @Mock
    private StoreRepo storeRepo;

    @InjectMocks
    private ShoppingServiceImpl shoppingService;

    UUID storeUUID1= UUID.randomUUID();
    Store s;
    Catalogue c;
    Order o;
    Order o2;
    UUID o1UUID=UUID.randomUUID();
    UUID o2UUID=UUID.randomUUID();
    UUID expectedU1=UUID.randomUUID();
    UUID expectedS1=UUID.randomUUID();
    UUID expectedShopper1=UUID.randomUUID();
    Double expectedDiscount;
    Double totalC;
    Item i1;
    Item i2;
    List<Item> listOfItems=new ArrayList<>();
    List<Order> listOfOrders=new ArrayList<>();
    GeoPoint deliveryAddress=new GeoPoint(2.0, 2.0, "2616 Urban Quarters, Hatfield");
    GeoPoint storeAddress=new GeoPoint(3.0, 3.0, "Woolworthes, Hillcrest Boulevard");
    List<Item> expectedListOfItems=new ArrayList<>();

    @BeforeEach
    void setUp() {
        i1=new Item("Heinz Tomato Sauce","123456","123456",storeUUID1,36.99,1,"description","img/");
        i2=new Item("Bar one","012345","012345",storeUUID1,14.99,3,"description","img/");
        listOfItems.add(i1);
        listOfItems.add(i2);
        c=new Catalogue(storeUUID1, listOfItems);
        s=new Store(storeUUID1,"Woolworthes",c,2,null,null,4,true);
        Date d1=new Date(2021,06,1,14,30);
        Date d2=new Date(2021,06,1,14,23);
        totalC=133.99;
        expectedDiscount=0.0;

        Calendar c1=Calendar.getInstance();
        Calendar c2=Calendar.getInstance();

        c1.setTime(d1);
        c2.setTime(d2);

        o=new Order(o1UUID, expectedU1, expectedS1, expectedShopper1, Calendar.getInstance(), c1, totalC, OrderType.DELIVERY, OrderStatus.AWAITING_PAYMENT, expectedListOfItems, expectedDiscount, deliveryAddress, storeAddress, false);
        o2=new Order(o2UUID,expectedU1, expectedS1, expectedShopper1, Calendar.getInstance(), c2, totalC, OrderType.DELIVERY, OrderStatus.AWAITING_PAYMENT, expectedListOfItems, expectedDiscount, deliveryAddress, storeAddress, false);
        listOfOrders.add(o);
        listOfOrders.add(o2);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Description("Tests for when removeQueuedOrder is submitted with a null request object- exception should be thrown")
    @DisplayName("When request object is not specified")
    void UnitTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> shoppingService.removeQueuedOrder(null));
        assertEquals("Request object for RemoveQueuedOrderRequest can't be null - can't get next queued", thrown.getMessage());
    }

    @Test
    @Description("Tests for when storeID in request object is null- exception should be thrown")
    @DisplayName("When request object parameter -storeID - is not specified")
    void UnitTest_testingNull_storeID_Parameter_RequestObject(){
        RemoveQueuedOrderRequest request=new RemoveQueuedOrderRequest(o1UUID, null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> shoppingService.removeQueuedOrder(request));
        assertEquals("Store ID parameter in request can't be null - can't remove from queue", thrown.getMessage());
    }

    @Test
    @Description("Tests for when orderID in request object is null- exception should be thrown")
    @DisplayName("When request object parameter -orderID - is not specified")
    void UnitTest_testingNull_orderID_Parameter_RequestObject(){
        RemoveQueuedOrderRequest request=new RemoveQueuedOrderRequest(null, storeUUID1);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> shoppingService.removeQueuedOrder(request));
        assertEquals("Order ID parameter in request can't be null - can't remove from queue", thrown.getMessage());
    }

    @Test
    @Description("Test for when Store with storeID does not exist in database - StoreDoesNotExist Exception should be thrown")
    @DisplayName("When Store with ID doesn't exist")
    void UnitTest_Store_doesnt_exist(){
        RemoveQueuedOrderRequest request=new RemoveQueuedOrderRequest(o1UUID, storeUUID1);
        when(storeRepo.findById(Mockito.any())).thenReturn(null);
        Throwable thrown = Assertions.assertThrows(StoreDoesNotExistException.class, ()-> shoppingService.removeQueuedOrder(request));
        assertEquals("Store with ID does not exist in repository - could not get next queued entity", thrown.getMessage());
    }

    @Test
    @Description("Tests whether the RemoveQueuedOrder request object was created correctly")
    @DisplayName("RemoveQueuedOrderRequest correctly constructed")
    void UnitTest_AddToQueueRequestConstruction() {
        RemoveQueuedOrderRequest request = new RemoveQueuedOrderRequest(o1UUID, storeUUID1);
        assertNotNull(request);
        assertEquals(storeUUID1, request.getStoreID());
        assertEquals(o1UUID, request.getOrderID());
    }

    @Test
    @Description("Test for when Store with storeID does not have any current orders in order queue")
    @DisplayName("Order queue is empty")
    void UnitTest_Store_no_orders_in_orderQueue() throws InvalidRequestException, StoreDoesNotExistException {
        RemoveQueuedOrderRequest request=new RemoveQueuedOrderRequest(o1UUID, storeUUID1);
        List<Order> listOfOrders2=new ArrayList<>();
        s.setOrderQueue(listOfOrders2);
        when(storeRepo.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(s));
        RemoveQueuedOrderResponse response=shoppingService.removeQueuedOrder(request);
        assertNotNull(response);
        assertEquals("The order queue of shop is empty", response.getMessage());
        assertEquals(false,response.isRemoved());
        assertEquals(response.getOrderID(), null);
    }

    @Test
    @Description("Test for when order is correctly removed from the order queue")
    @DisplayName("Removes and returns correct orderID")
    void UnitTest_order_is_correctly_removed() throws InvalidRequestException, StoreDoesNotExistException {

        RemoveQueuedOrderRequest request=new RemoveQueuedOrderRequest(o2UUID, storeUUID1);
        s.setOrderQueue(listOfOrders);
        assertEquals(listOfOrders.size(),2);
        when(storeRepo.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(s));
        RemoveQueuedOrderResponse response=shoppingService.removeQueuedOrder(request);

        assertNotNull(response);
        assertEquals(true,response.isRemoved());
        assertEquals(listOfOrders.size(),1);
        assertEquals("Order successfully removed from the queue",response.getMessage());
        assertEquals(response.getOrderID(), o2UUID);
    }

    @Test
    @Description("Test for when order is not in stores queue")
    @DisplayName("OrderID not in queue")
    void UnitTest_order_is_not_in_queue() throws InvalidRequestException, StoreDoesNotExistException {
        UUID orderNotInQueue = UUID.randomUUID();
        RemoveQueuedOrderRequest request=new RemoveQueuedOrderRequest(orderNotInQueue, storeUUID1);
        s.setOrderQueue(listOfOrders);
        assertEquals(listOfOrders.size(),2);
        when(storeRepo.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(s));
        RemoveQueuedOrderResponse response=shoppingService.removeQueuedOrder(request);

        assertNotNull(response);
        assertEquals(false,response.isRemoved());
        assertEquals(listOfOrders.size(),2);
        assertEquals("Order not found in shop queue",response.getMessage());
        assertEquals(response.getOrderID(), null);
    }
}
