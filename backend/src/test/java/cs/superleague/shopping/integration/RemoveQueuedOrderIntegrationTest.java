package cs.superleague.shopping.integration;

import cs.superleague.integration.ServiceSelector;
import cs.superleague.integration.security.JwtUtil;
import cs.superleague.payment.PaymentServiceImpl;
import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.dataclass.OrderStatus;
import cs.superleague.payment.dataclass.OrderType;
import cs.superleague.payment.exceptions.PaymentException;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.payment.requests.SubmitOrderRequest;
import cs.superleague.payment.responses.SubmitOrderResponse;
import cs.superleague.shopping.ShoppingServiceImpl;
import cs.superleague.shopping.dataclass.Catalogue;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.shopping.dataclass.Store;
import cs.superleague.shopping.exceptions.InvalidRequestException;
import cs.superleague.shopping.exceptions.StoreClosedException;
import cs.superleague.shopping.exceptions.StoreDoesNotExistException;
import cs.superleague.shopping.repos.CatalogueRepo;
import cs.superleague.shopping.repos.ItemRepo;
import cs.superleague.shopping.repos.StoreRepo;
import cs.superleague.shopping.requests.AddToQueueRequest;
import cs.superleague.shopping.requests.RemoveQueuedOrderRequest;
import cs.superleague.shopping.responses.AddToQueueResponse;
import cs.superleague.shopping.responses.RemoveQueuedOrderResponse;
import cs.superleague.user.UserServiceImpl;
import cs.superleague.user.dataclass.Customer;
import cs.superleague.user.dataclass.UserType;
import cs.superleague.user.repos.CustomerRepo;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
public class RemoveQueuedOrderIntegrationTest {

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    StoreRepo storeRepo;

    @Autowired
    CatalogueRepo catalogueRepo;

    @Autowired
    ItemRepo itemRepo;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    CustomerRepo customerRepo;

    /* Requests */
    RemoveQueuedOrderRequest removeQueuedOrderRequest;

    /* User Ids */
    UUID userID=UUID.randomUUID();

    /* Store Ids */
    UUID storeID=UUID.randomUUID();

    /* Order Ids */
    UUID orderID1;
    UUID orderID2;

    /* Orders */
    Order order1;
    Order order2;

    /*Items */
    Item item1,item2,item3,item4,item5;
    List<Item> itemList=new ArrayList<>();

    /*GeoPoints*/
    GeoPoint geoPoint1;
    GeoPoint geoPoint2;

    Catalogue stock;

    /*Store */
    Store store;

    /*Order */
    List<Order> currentOrders=new ArrayList<>();
    List<Order> orderQueue=new ArrayList<>();

    Customer customer;
    String jwtToken;

    @BeforeEach
    void setup() throws PaymentException, StoreClosedException, InvalidRequestException, StoreDoesNotExistException, InterruptedException, cs.superleague.user.exceptions.InvalidRequestException {
        customer=new Customer();
        customer.setCustomerID(userID);
        customer.setEmail("hello@gmail.com");
        customer.setAccountType(UserType.CUSTOMER);
        customerRepo.save(customer);
        jwtToken=jwtUtil.generateJWTTokenCustomer(customer);

        item1=new Item("name1","productID1","barcode1",storeID,10.0,2,"Description1","imageURL1");
        item2=new Item("name2","productID2","barcode2",storeID,30.0,1,"Description2","imageURL2");
        item3=new Item("name3","productID3","barcode3",storeID,27.0,1,"Description3","imageURL3");
        item4=new Item("name4","productID4","barcode4",storeID,22.0,1,"Description4","imageURL4");
        item5=new Item("name5","productID5","barcode5",storeID,22.0,1,"Description5","imageURL5");
        itemList.add(item1);
        itemList.add(item2);
        itemList.add(item3);
        itemList.add(item4);
        itemList.add(item5);
        geoPoint1=new GeoPoint(4.0,5.0,"address1");
        geoPoint2=new GeoPoint(3.0,3.5,"address2");
        stock=new Catalogue(storeID,itemList);

        store=new Store(storeID,"StoreBrand",stock,3,currentOrders,orderQueue,6,true);
        store.setStoreLocation(geoPoint2);

        itemRepo.save(item1);
        itemRepo.save(item2);
        itemRepo.save(item3);
        itemRepo.save(item4);
        itemRepo.save(item5);
        catalogueRepo.save(stock);
        storeRepo.save(store);
        SubmitOrderRequest submitOrderRequest=new SubmitOrderRequest(jwtToken,itemList,3.0,storeID,OrderType.DELIVERY, 3.3, 3.5, "Homer Street");
        SubmitOrderResponse submitOrderResponse= ServiceSelector.getPaymentService().submitOrder(submitOrderRequest);
        orderID1 = submitOrderResponse.getOrder().getOrderID();
        order1 = submitOrderResponse.getOrder();
        order1.setStatus(OrderStatus.PURCHASED);
        orderRepo.save(order1);
        store.getOrderQueue().add(order1);
        storeRepo.save(store);
        submitOrderRequest=new SubmitOrderRequest(jwtToken,itemList,3.0,storeID,OrderType.DELIVERY, 3.3, 3.5, "Homer Street");
        submitOrderResponse= ServiceSelector.getPaymentService().submitOrder(submitOrderRequest);
        orderID2 = submitOrderResponse.getOrder().getOrderID();
        order2 = submitOrderResponse.getOrder();
        order2.setStatus(OrderStatus.PURCHASED);
        orderRepo.save(order2);
        store.getOrderQueue().add(order2);
        storeRepo.save(store);
    }

    @AfterEach
    void tearDown() {
        storeRepo.deleteAll();
        orderRepo.deleteAll();
        catalogueRepo.deleteAll();
        itemRepo.deleteAll();

    }

    @Test
    @Description("Tests for when removeQueuedOrder is submitted with a null request object- exception should be thrown")
    @DisplayName("When request object is not specified")
    void IntegrationTest_RemoveQueuedOrderNullRequestObject(){
        removeQueuedOrderRequest = null;

        assertThrows(cs.superleague.shopping.exceptions.InvalidRequestException.class, ()-> {
            RemoveQueuedOrderResponse removeQueuedOrderResponse = ServiceSelector.getShoppingService().removeQueuedOrder(removeQueuedOrderRequest);
        });
    }

    @Test
    @Description("Tests for when any of the parameters of the request object for the removeQueuedOrder function are null")
    @DisplayName("When any parameter is null")
    void IntegrationTest_RemoveQueuedOrderNullParameterRequest(){
        removeQueuedOrderRequest=new RemoveQueuedOrderRequest(orderID1, null);
        assertThrows(cs.superleague.shopping.exceptions.InvalidRequestException.class, ()-> {
            RemoveQueuedOrderResponse removeQueuedOrderResponse = ServiceSelector.getShoppingService().removeQueuedOrder(removeQueuedOrderRequest);
        });
        removeQueuedOrderRequest=new RemoveQueuedOrderRequest(null, storeID);
        assertThrows(cs.superleague.shopping.exceptions.InvalidRequestException.class, ()-> {
            RemoveQueuedOrderResponse removeQueuedOrderResponse = ServiceSelector.getShoppingService().removeQueuedOrder(removeQueuedOrderRequest);
        });
        removeQueuedOrderRequest=new RemoveQueuedOrderRequest(null, null);
        assertThrows(cs.superleague.shopping.exceptions.InvalidRequestException.class, ()-> {
            RemoveQueuedOrderResponse removeQueuedOrderResponse = ServiceSelector.getShoppingService().removeQueuedOrder(removeQueuedOrderRequest);
        });
    }
    @Test
    @Description("Tests for if the store does not exist")
    @DisplayName("When Store with ID doesn't exist")
    void IntegrationTest_StoreDoesNotExist(){
        UUID storeID2=UUID.randomUUID();
        removeQueuedOrderRequest=new RemoveQueuedOrderRequest(orderID1, storeID2);
        assertThrows(StoreDoesNotExistException.class, ()-> {
            RemoveQueuedOrderResponse removeQueuedOrderResponse = ServiceSelector.getShoppingService().removeQueuedOrder(removeQueuedOrderRequest);
        });
    }

    @Test
    @Description("Tests whether the RemoveQueuedOrder request object was created correctly")
    @DisplayName("RemoveQueuedOrderRequest correctly constructed")
    void IntegrationTest_AddToQueueRequestConstruction() {
        RemoveQueuedOrderRequest request = new RemoveQueuedOrderRequest(orderID1, storeID);
        assertNotNull(request);
        assertEquals(storeID, request.getStoreID());
        assertEquals(orderID1, request.getOrderID());
    }

    @Test
    @Description("Test for when order is correctly removed from the order queue")
    @DisplayName("Removes and returns correct orderID")
    void IntegrationTest_order_is_correctly_removed() throws InvalidRequestException, StoreDoesNotExistException {

        RemoveQueuedOrderRequest request=new RemoveQueuedOrderRequest(orderID2, storeID);
        RemoveQueuedOrderResponse response=ServiceSelector.getShoppingService().removeQueuedOrder(request);

        assertNotNull(response);
        assertEquals(true,response.isRemoved());
        assertEquals("Order successfully removed from the queue",response.getMessage());
        assertEquals(response.getOrderID(), orderID2);

        Store store = null;
        if(storeRepo.findById(storeID).isPresent()){
            store = storeRepo.findById(storeID).orElse(null);
        }
        assertNotNull(store);
        assertEquals(store.getOrderQueue().size(), 1);
        assertEquals(store.getOrderQueue().get(0).getOrderID(), orderID1);
    }

    @Test
    @Description("Test for when order is not in stores queue")
    @DisplayName("OrderID not in queue")
    void IntegrationTest_order_is_not_in_queue() throws InvalidRequestException, StoreDoesNotExistException {
        UUID randomID = UUID.randomUUID();
        RemoveQueuedOrderRequest request=new RemoveQueuedOrderRequest(randomID, storeID);
        RemoveQueuedOrderResponse response=ServiceSelector.getShoppingService().removeQueuedOrder(request);

        assertNotNull(response);
        assertEquals(false,response.isRemoved());
        assertEquals("Order not found in shop queue",response.getMessage());
        assertEquals(response.getOrderID(), null);

        Store store = null;
        if(storeRepo.findById(storeID).isPresent()){
            store = storeRepo.findById(storeID).orElse(null);
        }
        assertNotNull(store);
        assertEquals(store.getOrderQueue().size(), 2);
        assertEquals(store.getOrderQueue().get(0).getOrderID(), orderID1);
        assertEquals(store.getOrderQueue().get(1).getOrderID(), orderID2);
    }
}
