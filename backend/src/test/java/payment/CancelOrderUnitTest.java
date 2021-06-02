package payment;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import payment.dataclass.GeoPoint;
import payment.dataclass.Order;
import payment.dataclass.OrderStatus;
import payment.dataclass.OrderType;
import payment.exceptions.InvalidRequestException;
import payment.exceptions.OrderDoesNotExist;
import payment.mock.CancelOrdersMock;
import payment.repos.OrderRepo;
import payment.requests.CancelOrderRequest;
import payment.responses.CancelOrderResponse;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import shopping.dataclass.Item;
import shopping.exceptions.StoreDoesNotExistException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CancelOrderUnitTest {
    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Mock
    private OrderRepo orderRepo;

    Item I1;
    Item I2;
    Order o;
    Order o2;
    UUID o1UUID=UUID.randomUUID();
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
        I1=new Item("Heinz Tamatoe Sauce","123456","123456",expectedS1,36.99,1,"description","img/");
        I2=new Item("Bar one","012345","012345",expectedS1,14.99,3,"description","img/");
        expectedMessage="Order successfully created.";
        expectedDiscount=0.0;
        totalC=66.97;
        expectedStatus = OrderStatus.AWAITING_PAYMENT;
        expectedType= OrderType.DELIVERY;
        expectedListOfItems.add(I1);
        expectedListOfItems.add(I2);
        o=new Order(o1UUID, expectedU1, expectedS1, expectedShopper1, Calendar.getInstance(), null, totalC, OrderType.DELIVERY, OrderStatus.AWAITING_PAYMENT, expectedListOfItems, expectedDiscount, deliveryAddress, storeAddress, false);
        o2=new Order(o2UUID,expectedU1, expectedS1, expectedShopper1, Calendar.getInstance(), null, totalC, OrderType.DELIVERY, OrderStatus.AWAITING_PAYMENT, expectedListOfItems, expectedDiscount, deliveryAddress, storeAddress, false);
        listOfOrders.add(o);
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
    @DisplayName("When cancelOrderRequest orderID Paramter is null")
    void cancelOrderWhenOrderRequest_OrderID_IsNull() {
        CancelOrderRequest req=new CancelOrderRequest(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> paymentService.cancelOrder(req));
        assertEquals("orderID in request object can't be null - couldn't cancel order ", thrown.getMessage());
    }
    @Test
    @DisplayName("When order does not exist")
    void cancelOrderWhenOrderDoesNotExist() {
        when(orderRepo.findById(Mockito.any())).thenReturn(null);
        CancelOrderRequest req=new CancelOrderRequest(UUID.randomUUID());
        Throwable thrown = Assertions.assertThrows(OrderDoesNotExist.class, ()-> paymentService.cancelOrder(req));
        assertEquals("Order doesn't exist in database - can't cancel order", thrown.getMessage());
    }

    @Test
    @DisplayName("When Order Status is Invalid: Delivered")
    void cancelOrderStatusDelivered() throws InvalidRequestException, OrderDoesNotExist {
        List<Order> orders = listOfOrders;
        System.out.println(orders.size());
        Order order = orders.get(0);
        int ordersSize = orders.size();
        o.setStatus(OrderStatus.DELIVERED);
        when(orderRepo.findAll()).thenReturn(orders);
        when(orderRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(o));
        CancelOrderRequest cancelOrderRequest = new CancelOrderRequest(order.getOrderID());
        CancelOrderResponse cancelOrderResponse = paymentService.cancelOrder(cancelOrderRequest);
        Assertions.assertEquals(cancelOrderResponse.getOrders().size(), ordersSize);
        Assertions.assertEquals("Cannot cancel an order that has been delivered/collected.", cancelOrderResponse.getMessage());
        Assertions.assertEquals(false, cancelOrderResponse.isSuccess());
        // order size does not change
        Assertions.assertEquals(cancelOrderResponse.getOrders().size(), ordersSize);
    }

    @Test
    @DisplayName("When Order Status is Invalid: DeliveryCollected")
    void cancelOrderStatusDeliveryCollected() throws InvalidRequestException, OrderDoesNotExist {
        List<Order> orders = listOfOrders;
        System.out.println(orders.size());
        Order order = orders.get(0);
        int ordersSize = orders.size();
        o.setStatus(OrderStatus.DELIVERY_COLLECTED);
        when(orderRepo.findAll()).thenReturn(orders);
        when(orderRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(o));
        CancelOrderRequest cancelOrderRequest = new CancelOrderRequest(order.getOrderID());
        CancelOrderResponse cancelOrderResponse = paymentService.cancelOrder(cancelOrderRequest);
        Assertions.assertEquals(cancelOrderResponse.getOrders().size(), ordersSize);
        Assertions.assertEquals("Cannot cancel an order that has been delivered/collected.", cancelOrderResponse.getMessage());
        Assertions.assertEquals(false, cancelOrderResponse.isSuccess());
        // order size does not change
        Assertions.assertEquals(cancelOrderResponse.getOrders().size(), ordersSize);
    }

    @Test
    @DisplayName("When Order Status is Invalid: CustomerCollected")
    void cancelOrderStatusCustomerCollected() throws InvalidRequestException, OrderDoesNotExist {
        List<Order> orders = listOfOrders;
        System.out.println(orders.size());
        Order order = orders.get(0);
        int ordersSize = orders.size();
        o.setStatus(OrderStatus.CUSTOMER_COLLECTED);
        when(orderRepo.findAll()).thenReturn(orders);
        when(orderRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(o));
        CancelOrderRequest cancelOrderRequest = new CancelOrderRequest(order.getOrderID());
        CancelOrderResponse cancelOrderResponse = paymentService.cancelOrder(cancelOrderRequest);
        Assertions.assertEquals(cancelOrderResponse.getOrders().size(), ordersSize);
        Assertions.assertEquals("Cannot cancel an order that has been delivered/collected.", cancelOrderResponse.getMessage());
        Assertions.assertEquals(false, cancelOrderResponse.isSuccess());
        // order size does not change
        Assertions.assertEquals(cancelOrderResponse.getOrders().size(), ordersSize);
    }

    @Test
    @DisplayName("When Order Status is Valid: AWAITING_PAYMENT")
    void cancelOrderStatusAwaitingPayment() throws InvalidRequestException, OrderDoesNotExist {
        List<Order> orders = listOfOrders;
        System.out.println(orders.size());
        Order order = orders.get(0);
        int ordersSize = orders.size();
        o.setStatus(OrderStatus.AWAITING_PAYMENT);
        when(orderRepo.findAll()).thenReturn(orders);
        when(orderRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(o));
        CancelOrderRequest cancelOrderRequest = new CancelOrderRequest(order.getOrderID());
        CancelOrderResponse cancelOrderResponse = paymentService.cancelOrder(cancelOrderRequest);
        Assertions.assertEquals(cancelOrderResponse.getOrders().size(), ordersSize-1);
        Assertions.assertEquals("Order successfully cancelled. Customer has been charged 1000.0", cancelOrderResponse.getMessage());
        Assertions.assertEquals(true, cancelOrderResponse.isSuccess());
        Assertions.assertEquals(cancelOrderResponse.getOrders().size(), ordersSize-1);
        orders.remove(o);
        Assertions.assertEquals(cancelOrderResponse.getOrders(),orders);
    }

    @Test
    @DisplayName("When Order Status is Valid: PURCHASED")
    void cancelOrderStatusPurchased() throws InvalidRequestException, OrderDoesNotExist {
        List<Order> orders = listOfOrders;
        System.out.println(orders.size());
        Order order = orders.get(0);
        int ordersSize = orders.size();
        o.setStatus(OrderStatus.PURCHASED);
        when(orderRepo.findAll()).thenReturn(orders);
        when(orderRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(o));
        CancelOrderRequest cancelOrderRequest = new CancelOrderRequest(order.getOrderID());
        CancelOrderResponse cancelOrderResponse = paymentService.cancelOrder(cancelOrderRequest);
        Assertions.assertEquals(cancelOrderResponse.getOrders().size(), ordersSize-1);
        Assertions.assertEquals("Order successfully cancelled. Customer has been charged 1000.0", cancelOrderResponse.getMessage());
        Assertions.assertEquals(true, cancelOrderResponse.isSuccess());
        Assertions.assertEquals(cancelOrderResponse.getOrders().size(), ordersSize-1);
        orders.remove(o);
        Assertions.assertEquals(cancelOrderResponse.getOrders(),orders);
    }
}