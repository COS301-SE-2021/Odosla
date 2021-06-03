package shopping;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Description;
import payment.dataclass.GeoPoint;
import payment.dataclass.Order;
import payment.dataclass.OrderStatus;
import payment.dataclass.OrderType;
import shopping.dataclass.Catalogue;
import shopping.dataclass.Item;
import shopping.dataclass.Store;
import shopping.exceptions.InvalidRequestException;
import shopping.exceptions.StoreDoesNotExistException;
import shopping.repos.StoreRepo;
import shopping.requests.GetCatalogueRequest;
import shopping.requests.GetNextQueuedRequest;
import shopping.requests.GetStoreByUUIDRequest;
import shopping.responses.GetNextQueuedResponse;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetNextQueuedUnitTest {

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
    String expectedMessage;
    OrderStatus expectedStatus;
    OrderType expectedType;
    GeoPoint deliveryAddress=new GeoPoint(2.0, 2.0, "2616 Urban Quarters, Hatfield");
    GeoPoint storeAddress=new GeoPoint(3.0, 3.0, "Woolworthes, Hillcrest Boulevard");
    List<Item> expectedListOfItems=new ArrayList<>();

    @BeforeEach
    void setUp() {
        i1=new Item("Heinz Tamatoe Sauce","123456","123456",storeUUID1,36.99,1,"description","img/");
        i2=new Item("Bar one","012345","012345",storeUUID1,14.99,3,"description","img/");
        listOfItems.add(i1);
        listOfItems.add(i2);
        c=new Catalogue(listOfItems);
        s=new Store(storeUUID1,"Woolworthes",c,2,null,null,4);
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
    @Description("Tests for when getNextQueued is submited with a null request object- exception should be thrown")
    @DisplayName("When request object is not specificed")
    void UnitTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> shoppingService.getNextQueued(null));
        assertEquals("Request object for GetNextQueuedRequest can't be null - can't get next queued", thrown.getMessage());
    }

    @Test
    @Description("Tests for when storeID in request object is null- exception should be thrown")
    @DisplayName("When request object parameter -storeID - is not specificed")
    void UnitTest_testingNull_storeID_Parameter_RequestObject(){
        GetNextQueuedRequest request=new GetNextQueuedRequest(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> shoppingService.getNextQueued(request));
        assertEquals("Store ID paramter in request can't be null - can't get next queued", thrown.getMessage());
    }

    @Test
    @Description("Test for when Store with storeID does not exist in database - StoreDoesNotExist Exception should be thrown")
    @DisplayName("When Store with ID doesn't exist")
    void UnitTest_Store_doesnt_exist(){
        GetNextQueuedRequest request=new GetNextQueuedRequest(storeUUID1);
        when(storeRepo.findById(Mockito.any())).thenReturn(null);
        Throwable thrown = Assertions.assertThrows(StoreDoesNotExistException.class, ()-> shoppingService.getNextQueued(request));
        assertEquals("Store with ID does not exist in repository - could not get next queued entity", thrown.getMessage());
    }

    @Test
    @Description("Test for when Store with storeID does not have any current orders in order queue")
    @DisplayName("Order queue is empty")
    void UnitTest_Store_no_orders_in_orderQueue() throws InvalidRequestException, StoreDoesNotExistException {
        GetNextQueuedRequest request=new GetNextQueuedRequest(storeUUID1);
        List<Order> listOfOrders2=new ArrayList<>();
        s.setOrderQueue(listOfOrders2);
        when(storeRepo.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(s));
        GetNextQueuedResponse response=shoppingService.getNextQueued(request);
        assertNotNull(response);
        assertEquals("The order queue of shop is empty", response.getMessage());
        assertEquals(false,response.isResponse());
    }

    @Test
    @Description("Test for when order and removes previous order from order queue")
    @DisplayName("Removes and returns correct order")
    void UnitTest_order_is_correctly_removed() throws InvalidRequestException, StoreDoesNotExistException {

        GetNextQueuedRequest request=new GetNextQueuedRequest(storeUUID1);
        s.setOrderQueue(listOfOrders);
        when(storeRepo.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(s));
        GetNextQueuedResponse response=shoppingService.getNextQueued(request);
        listOfOrders.remove(o2);

        assertNotNull(response);
        assertEquals(true,response.isResponse());
        assertEquals(listOfOrders.size(),response.getQueueOfOrders().size());
        assertEquals("Queue was successfully updated for store",response.getMessage());
        assertEquals(listOfOrders,response.getQueueOfOrders());
        assertEquals(o,response.getNewCurrentOrder());

    }
}
