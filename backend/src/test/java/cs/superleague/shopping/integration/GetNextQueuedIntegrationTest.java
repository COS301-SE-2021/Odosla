package cs.superleague.shopping.integration;

import cs.superleague.integration.ServiceSelector;
import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.dataclass.OrderStatus;
import cs.superleague.payment.dataclass.OrderType;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.shopping.dataclass.Catalogue;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.shopping.dataclass.Store;
import cs.superleague.shopping.exceptions.InvalidRequestException;
import cs.superleague.shopping.exceptions.StoreDoesNotExistException;
import cs.superleague.shopping.repos.CatalogueRepo;
import cs.superleague.shopping.repos.ItemRepo;
import cs.superleague.shopping.repos.StoreRepo;
import cs.superleague.shopping.requests.GetNextQueuedRequest;
import cs.superleague.shopping.responses.GetNextQueuedResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class GetNextQueuedIntegrationTest {
    @Autowired
    StoreRepo storeRepo;

    @Autowired
    CatalogueRepo catalogueRepo;

    @Autowired
    ItemRepo itemRepo;

    @Autowired
    OrderRepo orderRepo;

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
        i1=new Item("Heinz Tamatoe Sauce","123456","123456",storeUUID1,36.99,1,"description","img/");
        i2=new Item("Bar one","012345","012345",storeUUID1,14.99,3,"description","img/");
        itemRepo.save(i1);
        itemRepo.save(i2);
        listOfItems.add(i1);
        listOfItems.add(i2);

        c=new Catalogue(storeUUID1,listOfItems);
        catalogueRepo.save(c);

        s=new Store(storeUUID1,"Woolworthes",c,2,null,null,4,true);
        storeRepo.save(s);

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
        orderRepo.save(o);
        orderRepo.save(o2);

        listOfOrders.add(o);
        listOfOrders.add(o2);
    }

    @AfterEach
    void tearDown() {
        storeRepo.deleteAll();
        catalogueRepo.deleteAll();
        itemRepo.deleteAll();
        orderRepo.deleteAll();
    }

    @Test
    @Description("Tests for when getNextQueued is submitted with a null request object- exception should be thrown")
    @DisplayName("When request object is not specified")
    void IntegrationTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> ServiceSelector.getShoppingService().getNextQueued(null));
        assertEquals("Request object for GetNextQueuedRequest can't be null - can't get next queued", thrown.getMessage());
    }

    @Test
    @Description("Tests for when storeID in request object is null- exception should be thrown")
    @DisplayName("When request object parameter -storeID - is not specificed")
    void IntegrationTest_testingNull_storeID_Parameter_RequestObject(){
        GetNextQueuedRequest request=new GetNextQueuedRequest(null, "hfasjfhalj");
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> ServiceSelector.getShoppingService().getNextQueued(request));
        assertEquals("Store ID parameter in request can't be null - can't get next queued", thrown.getMessage());
    }

    @Test
    @Description("Test for when Store with storeID does not exist in database - StoreDoesNotExist Exception should be thrown")
    @DisplayName("When Store with ID doesn't exist")
    void IntegrationTest_Store_doesnt_exist(){
        UUID invalidStoreID = UUID.randomUUID();
        while(invalidStoreID == storeUUID1){
            invalidStoreID = UUID.randomUUID();
        }
        GetNextQueuedRequest request=new GetNextQueuedRequest(invalidStoreID, "hfasjfhalj");
        Throwable thrown = Assertions.assertThrows(StoreDoesNotExistException.class, ()-> ServiceSelector.getShoppingService().getNextQueued(request));
        assertEquals("Store with ID does not exist in repository - could not get next queued entity", thrown.getMessage());
    }

    @Test
    @Description("Test for when Store with storeID does not have any current orders in order queue")
    @DisplayName("Order queue is empty")
    void IntegrationTest_Store_no_orders_in_orderQueue() throws InvalidRequestException, StoreDoesNotExistException, cs.superleague.user.exceptions.InvalidRequestException {
        GetNextQueuedRequest request=new GetNextQueuedRequest(storeUUID1, "hfasjfhalj");
        Store updateStore = storeRepo.findById(storeUUID1).orElse(null);
        updateStore.setOrderQueue(null);
        storeRepo.save(updateStore);
        GetNextQueuedResponse response=ServiceSelector.getShoppingService().getNextQueued(request);
        assertNotNull(response);
        assertEquals("The order queue of shop is empty", response.getMessage());
        assertEquals(false,response.isResponse());
    }

    @Test
    @Description("Test for when order and removes previous order from order queue")
    @DisplayName("Removes and returns correct order")
    void IntegrationTest_order_is_correctly_removed() throws InvalidRequestException, StoreDoesNotExistException, cs.superleague.user.exceptions.InvalidRequestException {
        GetNextQueuedRequest request=new GetNextQueuedRequest(storeUUID1, "hfasjfhalj");
        s.setOrderQueue(listOfOrders);
        storeRepo.save(s);
        GetNextQueuedResponse response=ServiceSelector.getShoppingService().getNextQueued(request);
        listOfOrders.remove(o2);
        System.out.println(response.getMessage());
        assertNotNull(response);
        assertEquals(true,response.isResponse());
        assertEquals(listOfOrders.size(),response.getQueueOfOrders().size());
        assertEquals("Queue was successfully updated for store",response.getMessage());
        assertEquals(listOfOrders.get(0).getOrderID(),response.getQueueOfOrders().get(0).getOrderID());
        assertEquals(o2.getOrderID(),response.getNewCurrentOrder().getOrderID());
    }

    @Test
    @Description("Tests whether the GetNextQueued request object was created correctly")
    @DisplayName("GetNextQueueRequest correctly constructed")
    void IntegrationTest_AddToQueueRequestConstruction() {
        GetNextQueuedRequest request = new GetNextQueuedRequest(storeUUID1, "hfasjfhalj");
        assertNotNull(request);
        assertEquals(storeUUID1, request.getStoreID());
    }
}
