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
import cs.superleague.shopping.requests.GetQueueRequest;
import cs.superleague.shopping.requests.UpdateStoreRequest;
import cs.superleague.shopping.responses.GetQueueResponse;
import cs.superleague.shopping.responses.UpdateStoreResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class GetQueueIntegrationTest {

    @Autowired
    StoreRepo storeRepo;

    @Autowired
    CatalogueRepo catalogueRepo;

    @Autowired
    ItemRepo itemRepo;

    @Autowired
    OrderRepo orderRepo;

    /* Requests */
    GetQueueRequest getQueueRequest;

    /* StoreID */
    UUID storeUUID1= UUID.randomUUID();
    UUID storeUUID2= UUID.randomUUID();

    Store s;
    Store s2;

    Catalogue c;
    Catalogue c2;

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
    Item i3;
    Item i4;
    List<Item> listOfItems=new ArrayList<>();
    List<Item> listOfItems2=new ArrayList<>();

    List<Order> listOfOrders=new ArrayList<>();
    String expectedMessage;
    OrderStatus expectedStatus;
    OrderType expectedType;
    GeoPoint deliveryAddress=new GeoPoint(2.0, 2.0, "2616 Urban Quarters, Hatfield");
    GeoPoint storeAddress=new GeoPoint(3.0, 3.0, "Woolworths, Hillcrest Boulevard");
    List<Item> expectedListOfItems=new ArrayList<>();

    @BeforeEach
    void setUp() {
        i1=new Item("Heinz Tomato Sauce","123456","123456",storeUUID1,36.99,1,"description","img/");
        i2=new Item("Bar one","012345","012345",storeUUID1,14.99,3,"description","img/");
        i3=new Item("Milk","901234","901234",storeUUID2,30.00,1,"description","img/");
        i4=new Item("Bread","890123","890123",storeUUID2,36.99,1,"description","img/");
        itemRepo.save(i1);
        itemRepo.save(i2);
        itemRepo.save(i3);
        itemRepo.save(i4);

        listOfItems.add(i1);
        listOfItems.add(i2);
        listOfItems2.add(i3);
        listOfItems2.add(i4);

        c=new Catalogue(storeUUID1,listOfItems);
        c2= new Catalogue(storeUUID2, listOfItems2);
        catalogueRepo.save(c);
        catalogueRepo.save(c2);

        Date d1=new Date(2021,06,1,14,30);
        Date d2=new Date(2021,06,1,14,23);
        totalC=133.99;
        expectedDiscount=0.0;

        Calendar c1=Calendar.getInstance();
        Calendar cal2=Calendar.getInstance();

        c1.setTime(d1);
        cal2.setTime(d2);

        o=new Order(o1UUID, expectedU1, expectedS1, expectedShopper1, Calendar.getInstance(), c1, totalC, OrderType.DELIVERY, OrderStatus.AWAITING_PAYMENT, expectedListOfItems, expectedDiscount, deliveryAddress, storeAddress, false);
        o2=new Order(o2UUID,expectedU1, expectedS1, expectedShopper1, Calendar.getInstance(), cal2, totalC, OrderType.DELIVERY, OrderStatus.AWAITING_PAYMENT, expectedListOfItems, expectedDiscount, deliveryAddress, storeAddress, false);
        orderRepo.save(o);
        orderRepo.save(o2);
        listOfOrders.add(o);
        listOfOrders.add(o2);

        s=new Store(storeUUID1,"Woolworths",c,2,null,listOfOrders,4,true);
        s2= new Store(storeUUID2,"PnP",c2,2,null,null,4,true);

        storeRepo.save(s);
        storeRepo.save(s2);
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    @Description("Tests for when getQueue is submitted with a null request object- exception should be thrown")
    @DisplayName("When request object is not specified")
    void IntegrationTest_GetQueueNullRequestObject(){
        getQueueRequest = null;

        assertThrows(cs.superleague.shopping.exceptions.InvalidRequestException.class, ()-> {
            GetQueueResponse getQueueResponse = ServiceSelector.getShoppingService().getQueue(getQueueRequest);
        });
    }

    @Test
    @Description("Tests for whether a request is submitted with a null parameter for store in request object- exception should be thrown")
    @DisplayName("When request object parameter -store - is not specified")
    void IntegrationTest_testingNull_store_Parameter_RequestObject() {
        getQueueRequest = new GetQueueRequest(null);
        assertThrows(cs.superleague.shopping.exceptions.InvalidRequestException.class, ()-> {
            GetQueueResponse getQueueResponse = ServiceSelector.getShoppingService().getQueue(getQueueRequest);
        });
    }

    @Test
    @Description("Test for when Store with storeID does not exist in database - StoreDoesNotExist Exception should be thrown")
    @DisplayName("When Store with ID doesn't exist")
    void IntegrationTest_Store_doesnt_exist() {

        getQueueRequest = new GetQueueRequest(UUID.randomUUID());
        assertThrows(cs.superleague.shopping.exceptions.StoreDoesNotExistException.class, ()-> {
            GetQueueResponse getQueueResponse = ServiceSelector.getShoppingService().getQueue(getQueueRequest);
        });
    }

    @Test
    @Description("Test for when Store with storeID does not have any current orders in order queue")
    @DisplayName("Order queue is empty")
    void IntegrationTest_Store_no_orders_in_orderQueue() throws InvalidRequestException, StoreDoesNotExistException {

        getQueueRequest = new GetQueueRequest(storeUUID2);
        GetQueueResponse getQueueResponse = ServiceSelector.getShoppingService().getQueue(getQueueRequest);
        assertNotNull(getQueueResponse);
        assertEquals("The order queue of shop is empty", getQueueResponse.getMessage());
        assertFalse(getQueueResponse.getResponse());
        assertEquals(null,getQueueResponse.getQueueOfOrders());

    }

    @Test
    @Description("Test for when Store with storeID does have orders in order queue")
    @DisplayName("Order queue is returned")
    void IntegrationTest_Store_orders_in_orderQueue() throws InvalidRequestException, StoreDoesNotExistException {

        getQueueRequest = new GetQueueRequest(storeUUID1);
        GetQueueResponse getQueueResponse = ServiceSelector.getShoppingService().getQueue(getQueueRequest);
        assertNotNull(getQueueResponse);
        assertEquals("The order queue was successfully returned", getQueueResponse.getMessage());
        assertTrue(getQueueResponse.getResponse());
        assertEquals(listOfOrders.get(0).getOrderID(),getQueueResponse.getQueueOfOrders().get(0).getOrderID());
        assertEquals(listOfOrders.get(1).getOrderID(),getQueueResponse.getQueueOfOrders().get(1).getOrderID());
    }

    /** Checking request object is created correctly */
    @Test
    @Description("Tests whether the GetQueue request object was created correctly")
    @DisplayName("GetQueueRequest correctly constructed")
    void IntegrationTest_GetQueueRequestConstruction() {

        GetQueueRequest request = new GetQueueRequest(storeUUID1);
        assertNotNull(request);
        assertEquals(storeUUID1, request.getStoreID());
    }

}
