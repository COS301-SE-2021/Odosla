package cs.superleague.payment;

import cs.superleague.payment.exceptions.NotAuthorisedException;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.context.annotation.Description;
import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.dataclass.OrderStatus;
import cs.superleague.payment.dataclass.OrderType;
import cs.superleague.payment.exceptions.InvalidRequestException;
import cs.superleague.payment.exceptions.OrderDoesNotExist;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.payment.requests.CancelOrderRequest;
import cs.superleague.payment.responses.CancelOrderResponse;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import cs.superleague.shopping.dataclass.Item;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CancelOrderUnitTest {
    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Mock
    private OrderRepo orderRepo;

    Item I1;
    Item I2;
    Order order;
    Order o2;

    UUID userID;
    UUID orderID;
    UUID o2UUID=UUID.randomUUID();
    UUID expectedU1=UUID.randomUUID();
    UUID expectedS1=UUID.randomUUID();
    UUID expectedShopper1=UUID.randomUUID();
    Double expectedDiscount;
    double totalC;
    String expectedMessage;
    OrderStatus expectedStatus;
    OrderType expectedType;
    GeoPoint deliveryAddress=new GeoPoint(2.0, 2.0, "2616 Urban Quarters, Hatfield");
    GeoPoint storeAddress=new GeoPoint(3.0, 3.0, "Woolworthes, Hillcrest Boulevard");
    List<Item> expectedListOfItems=new ArrayList<>();
    List<Order> listOfOrders=new ArrayList<>();

    @BeforeEach
    void setUp() {
        userID = UUID.randomUUID();
        orderID = UUID.randomUUID();

        I1=new Item("Heinz Tamatoe Sauce","123456","123456",expectedS1,36.99,1,"description","img/");
        I2=new Item("Bar one","012345","012345",expectedS1,14.99,3,"description","img/");
        expectedMessage="Order successfully created.";
        expectedDiscount=0.0;
        totalC=66.97;
        expectedStatus = OrderStatus.AWAITING_PAYMENT;
        expectedType= OrderType.DELIVERY;
        expectedListOfItems.add(I1);
        expectedListOfItems.add(I2);
        order =new Order(orderID, expectedU1, expectedS1, expectedShopper1, Calendar.getInstance(), null, totalC, OrderType.DELIVERY, OrderStatus.AWAITING_PAYMENT, expectedListOfItems, expectedDiscount, deliveryAddress, storeAddress, false);
        o2=new Order(o2UUID,expectedU1, expectedS1, expectedShopper1, Calendar.getInstance(), null, totalC, OrderType.DELIVERY, OrderStatus.AWAITING_PAYMENT, expectedListOfItems, expectedDiscount, deliveryAddress, storeAddress, false);
        listOfOrders.add(order);
        listOfOrders.add(o2);
    }

    @AfterEach
    void tearDown() {
       orderRepo.deleteAll();
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
        when(orderRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(null));
        CancelOrderRequest req=new CancelOrderRequest(UUID.randomUUID(), userID);
        Throwable thrown = Assertions.assertThrows(OrderDoesNotExist.class, ()-> paymentService.cancelOrder(req));
        assertEquals("Order doesn't exist in database - cannot get order.", thrown.getMessage());
    }

    @Test
    @DisplayName("When a user who didn't place the order tries to cancel")
    void cancelOrderWhenOrderRequest_UserID_IsUnauthorised() {
        when(orderRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(order));
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
        List<Order> orders = listOfOrders;
        Order order = orders.get(0);
        int ordersSize = orders.size();
        this.order.setStatus(OrderStatus.DELIVERED);
        when(orderRepo.findAll()).thenReturn(orders);
        when(orderRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(this.order));
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
        List<Order> orders = listOfOrders;
        Order order = orders.get(0);
        int ordersSize = orders.size();
        this.order.setStatus(OrderStatus.DELIVERY_COLLECTED);
        when(orderRepo.findAll()).thenReturn(orders);
        when(orderRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(this.order));
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
        List<Order> orders = listOfOrders;
        Order order = orders.get(0);
        int ordersSize = orders.size();
        this.order.setStatus(OrderStatus.CUSTOMER_COLLECTED);
        when(orderRepo.findAll()).thenReturn(orders);
        when(orderRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(this.order));
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
        List<Order> orders = listOfOrders;
        Order order = orders.get(0);
        int ordersSize = orders.size();
        this.order.setStatus(OrderStatus.AWAITING_PAYMENT);
        when(orderRepo.findAll()).thenReturn(orders);
        when(orderRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(this.order));
        CancelOrderRequest cancelOrderRequest = new CancelOrderRequest(order.getOrderID(), order.getUserID());
        CancelOrderResponse cancelOrderResponse = paymentService.cancelOrder(cancelOrderRequest);
        Assertions.assertEquals(cancelOrderResponse.getOrders().size(), ordersSize-1);
        Assertions.assertEquals("Order successfully cancelled. Customer has been charged 1000.0", cancelOrderResponse.getMessage());
        Assertions.assertEquals(true, cancelOrderResponse.getSuccess());
        Assertions.assertEquals(cancelOrderResponse.getOrders().size(), ordersSize-1);
        orders.remove(this.order);
        Assertions.assertEquals(cancelOrderResponse.getOrders(),orders);
    }

    @Test
    @DisplayName("When Order Status is Valid: PURCHASED")
    void cancelOrderStatusPurchased() throws InvalidRequestException, OrderDoesNotExist, NotAuthorisedException {
        List<Order> orders = listOfOrders;
        Order order = orders.get(0);
        int ordersSize = orders.size();
        this.order.setStatus(OrderStatus.PURCHASED);
        when(orderRepo.findAll()).thenReturn(orders);
        when(orderRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(this.order));
        CancelOrderRequest cancelOrderRequest = new CancelOrderRequest(order.getOrderID(), order.getUserID());
        CancelOrderResponse cancelOrderResponse = paymentService.cancelOrder(cancelOrderRequest);
        Assertions.assertEquals(cancelOrderResponse.getOrders().size(), ordersSize-1);
        Assertions.assertEquals("Order successfully cancelled. Customer has been charged 1000.0", cancelOrderResponse.getMessage());
        Assertions.assertEquals(true, cancelOrderResponse.getSuccess());
        Assertions.assertEquals(cancelOrderResponse.getOrders().size(), ordersSize-1);
        orders.remove(this.order);
        Assertions.assertEquals(cancelOrderResponse.getOrders(),orders);
    }
}