package cs.superleague.payment.integration;

import cs.superleague.integration.ServiceSelector;
import cs.superleague.payment.PaymentServiceImpl;
import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.dataclass.OrderStatus;
import cs.superleague.payment.dataclass.OrderType;
import cs.superleague.payment.exceptions.InvalidRequestException;
import cs.superleague.payment.exceptions.NotAuthorisedException;
import cs.superleague.payment.exceptions.OrderDoesNotExist;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.payment.requests.CancelOrderRequest;
import cs.superleague.payment.responses.CancelOrderResponse;
import cs.superleague.shopping.ShoppingServiceImpl;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.shopping.dataclass.Store;
import cs.superleague.shopping.exceptions.StoreDoesNotExistException;
import cs.superleague.shopping.repos.ItemRepo;
import cs.superleague.shopping.repos.StoreRepo;
import cs.superleague.shopping.requests.AddShopperRequest;
import cs.superleague.shopping.responses.AddShopperResponse;
import cs.superleague.user.UserServiceImpl;
import cs.superleague.user.dataclass.Shopper;
import cs.superleague.user.exceptions.UserDoesNotExistException;
import cs.superleague.user.repos.ShopperRepo;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CancelOrderIntegrationTest {

    @Autowired
    PaymentServiceImpl paymentService;

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    ItemRepo itemRepo;

    Item item1;
    Item item2;

    Order order;
    Order order2;

    // IDs
    UUID userID;
    UUID orderID;
    UUID storeID;
    UUID shopperID;

    // total cost
    double totalCost;

    // Items List
    List<Item> expectedListOfItems=new ArrayList<>();

    // Order List
    List<Order> listOfOrders=new ArrayList<>();

    // Addresses
    GeoPoint deliveryAddress;
    GeoPoint storeAddress;

    @BeforeEach
    void setUp() {

        // Assigning IDs
        orderID = UUID.randomUUID();
        userID = UUID.randomUUID();
        storeID = UUID.randomUUID();
        shopperID = UUID.randomUUID();

        // Assigning total cost
        totalCost = 66.97;

        // Assigning items
        item1=new Item("Heinz Tamatoe Sauce","123456","123456",storeID,36.99,1,"description","img/");
        item2=new Item("Bar one","012345","012345",storeID,14.99,3,"description","img/");

        itemRepo.save(item1);
        itemRepo.save(item2);

        // Adding items to the items list
        expectedListOfItems.add(item1);
        expectedListOfItems.add(item2);

        // Assigning addresses
        deliveryAddress = new GeoPoint(2.0, 2.0, "2616 Urban Quarters, Hatfield");
        storeAddress = new GeoPoint(3.0, 3.0, "Woolworth's, Hillcrest Boulevard");

        // Assigning order
        order =new Order(orderID, userID, storeID, shopperID, Calendar.getInstance(), null, totalCost, OrderType.DELIVERY, OrderStatus.AWAITING_PAYMENT, expectedListOfItems, 0.0, deliveryAddress, storeAddress, false);
        order2 =new Order(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), Calendar.getInstance(), null, totalCost, OrderType.DELIVERY, OrderStatus.AWAITING_PAYMENT, expectedListOfItems, 0.0, deliveryAddress, storeAddress, false);

        // adding orders to the orders list
        listOfOrders.add(order);
        listOfOrders.add(order2);

        orderRepo.save(order);
        orderRepo.save(order2);
    }

    @AfterEach
    void tearDown() {
        orderRepo.delete(order);
        orderRepo.delete(order2);

        itemRepo.delete(item1);
        itemRepo.delete(item2);
    }

    @Test
    @DisplayName("When cancelOrderRequest is null")
    void cancelOrderWhenOrderRequestIsNull() {
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> paymentService.cancelOrder(null));
        assertEquals("request object cannot be null - couldn't cancel order", thrown.getMessage());
    }

    @Test
    @DisplayName("When cancelOrderRequest orderID Parameter is null")
    void cancelOrderWhenOrderRequest_OrderID_IsNull() {
        CancelOrderRequest req=new CancelOrderRequest(null, userID);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> paymentService.cancelOrder(req));
        assertEquals("OrderID cannot be null in request object - cannot get order.", thrown.getMessage());
    }

    @Test
    @DisplayName("When cancelOrderRequest userID Parameter is null")
    void cancelOrderWhenOrderRequest_UserID_IsNull() {
        CancelOrderRequest req=new CancelOrderRequest(orderID, null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> paymentService.cancelOrder(req));
        assertEquals("UserID cannot be null in request object - order unsuccessfully updated.", thrown.getMessage());
    }

    @Test
    @DisplayName("When order does not exist")
    void cancelOrderWhenOrderDoesNotExist(){
        CancelOrderRequest req=new CancelOrderRequest(UUID.randomUUID(), userID);
        Throwable thrown = Assertions.assertThrows(OrderDoesNotExist.class, ()-> paymentService.cancelOrder(req));
        assertEquals("Order doesn't exist in database - cannot get order.", thrown.getMessage());
    }

    @Test
    @DisplayName("When a user who didn't place the order tries to cancel")
    void cancelOrderWhenOrderRequest_UserID_IsUnauthorised() {
        CancelOrderRequest req=new CancelOrderRequest(orderID, UUID.randomUUID());
        Throwable thrown = Assertions.assertThrows(NotAuthorisedException.class, ()-> paymentService.cancelOrder(req));
        assertEquals("Not Authorised to update an order you did not place.", thrown.getMessage());
    }


    /** Checking request object is created correctly */
    @Test
    @Description("Tests whether the CancelOrderRequest object was created correctly")
    @DisplayName("CancelOrderRequest correctly constructed")
    void UnitTest_CancelOrderRequestConstruction() {
        CancelOrderRequest request=new CancelOrderRequest(orderID, userID);
        assertNotNull(request);
        assertEquals(orderID,request.getOrderID());
        assertEquals(userID, request.getUserID());
    }

    @Test
    @DisplayName("When Order Status is Invalid: Delivered")
    void cancelOrderStatusDelivered() throws InvalidRequestException, OrderDoesNotExist, NotAuthorisedException {
        int ordersSize = listOfOrders.size();
        order.setStatus(OrderStatus.DELIVERED);

        orderRepo.save(order);

        CancelOrderRequest cancelOrderRequest = new CancelOrderRequest(order.getOrderID(), order.getUserID());
        CancelOrderResponse cancelOrderResponse = paymentService.cancelOrder(cancelOrderRequest);
        Assertions.assertEquals(cancelOrderResponse.getOrders().size(), ordersSize);
        Assertions.assertEquals("Cannot cancel an order that has been delivered/collected.", cancelOrderResponse.getMessage());
        Assertions.assertEquals(false, cancelOrderResponse.getSuccess());
        // order size does not change
        Assertions.assertEquals(cancelOrderResponse.getOrders().size(), ordersSize);
    }

    @Test
    @DisplayName("When Order Status is Invalid: DeliveryCollected")
    void cancelOrderStatusDeliveryCollected() throws InvalidRequestException, OrderDoesNotExist, NotAuthorisedException {
        int ordersSize = listOfOrders.size();
        this.order.setStatus(OrderStatus.DELIVERY_COLLECTED);
        orderRepo.save(order);
        CancelOrderRequest cancelOrderRequest = new CancelOrderRequest(order.getOrderID(), order.getUserID());
        CancelOrderResponse cancelOrderResponse = paymentService.cancelOrder(cancelOrderRequest);
        Assertions.assertEquals(cancelOrderResponse.getOrders().size(), ordersSize);
        Assertions.assertEquals("Cannot cancel an order that has been delivered/collected.", cancelOrderResponse.getMessage());
        Assertions.assertEquals(false, cancelOrderResponse.getSuccess());
        // order size does not change
        Assertions.assertEquals(cancelOrderResponse.getOrders().size(), ordersSize);
    }

    @Test
    @DisplayName("When Order Status is Invalid: CustomerCollected")
    void cancelOrderStatusCustomerCollected() throws InvalidRequestException, OrderDoesNotExist, NotAuthorisedException {
        int ordersSize = listOfOrders.size();
        this.order.setStatus(OrderStatus.CUSTOMER_COLLECTED);
        orderRepo.save(order);
        CancelOrderRequest cancelOrderRequest = new CancelOrderRequest(order.getOrderID(), order.getUserID());
        CancelOrderResponse cancelOrderResponse = paymentService.cancelOrder(cancelOrderRequest);
        Assertions.assertEquals(cancelOrderResponse.getOrders().size(), ordersSize);
        Assertions.assertEquals("Cannot cancel an order that has been delivered/collected.", cancelOrderResponse.getMessage());
        Assertions.assertEquals(false, cancelOrderResponse.getSuccess());
        // order size does not change
        Assertions.assertEquals(cancelOrderResponse.getOrders().size(), ordersSize);
    }

    @Test
    @DisplayName("When Order Status is Valid: AWAITING_PAYMENT")
    void cancelOrderStatusAwaitingPayment() throws InvalidRequestException, OrderDoesNotExist, NotAuthorisedException {
        int ordersSize = listOfOrders.size();
        this.order.setStatus(OrderStatus.AWAITING_PAYMENT);
        orderRepo.save(order);
        CancelOrderRequest cancelOrderRequest = new CancelOrderRequest(order.getOrderID(), order.getUserID());
        CancelOrderResponse cancelOrderResponse = paymentService.cancelOrder(cancelOrderRequest);
        Assertions.assertEquals(cancelOrderResponse.getOrders().size(), ordersSize-1);
        Assertions.assertEquals("Order successfully cancelled. Customer has been charged 1000.0", cancelOrderResponse.getMessage());
        Assertions.assertEquals(true, cancelOrderResponse.getSuccess());
        Assertions.assertEquals(cancelOrderResponse.getOrders().size(), ordersSize-1);
        listOfOrders.remove(order);
    }

    @Test
    @DisplayName("When Order Status is Valid: PURCHASED")
    void cancelOrderStatusPurchased() throws InvalidRequestException, OrderDoesNotExist, NotAuthorisedException {

        int ordersSize = listOfOrders.size();
        this.order.setStatus(OrderStatus.PURCHASED);
        orderRepo.save(order);
        CancelOrderRequest cancelOrderRequest = new CancelOrderRequest(order.getOrderID(), order.getUserID());
        CancelOrderResponse cancelOrderResponse = paymentService.cancelOrder(cancelOrderRequest);
        Assertions.assertEquals(cancelOrderResponse.getOrders().size(), ordersSize-1);
        Assertions.assertEquals("Order successfully cancelled. Customer has been charged 1000.0", cancelOrderResponse.getMessage());
        Assertions.assertEquals(true, cancelOrderResponse.getSuccess());
        Assertions.assertEquals(cancelOrderResponse.getOrders().size(), ordersSize-1);
        listOfOrders.remove(order);
    }
}
