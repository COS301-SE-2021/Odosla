package cs.superleague.payment;

import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.dataclass.OrderStatus;
import cs.superleague.payment.dataclass.OrderType;
import cs.superleague.payment.exceptions.PaymentException;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.payment.requests.SetStatusRequest;
import cs.superleague.payment.responses.SetStatusResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class SetStatusUnitTest {

    @Mock
    OrderRepo orderRepo;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    // Order
    Order order;

    UUID orderID;
    UUID userID;
    UUID storeID;
    UUID shopperID;

    // Order statuses
    OrderStatus awaitingPayment;
    OrderStatus purchased;
    OrderStatus inQueue;
    OrderStatus packing;
    OrderStatus awaitingCollection;
    OrderStatus deliveryCollected;
    OrderStatus customerCollected;
    OrderStatus delivered;

    @BeforeEach
    void setUp() {
        orderID = UUID.randomUUID();
        userID = UUID.randomUUID();
        storeID = UUID.randomUUID();
        shopperID = UUID.randomUUID();

        awaitingPayment = OrderStatus.AWAITING_PAYMENT;
        purchased = OrderStatus.PURCHASED;
        inQueue = OrderStatus.IN_QUEUE;
        packing = OrderStatus.PACKING;
        awaitingCollection = OrderStatus.AWAITING_COLLECTION;
        deliveryCollected = OrderStatus.DELIVERY_COLLECTED;
        customerCollected = OrderStatus.CUSTOMER_COLLECTED;
        delivered = OrderStatus.DELIVERED;

        order = new Order();
        order.setOrderID(orderID);
        order.setUserID(userID);
        order.setStoreID(storeID);
        order.setShopperID(storeID);
        order.setCreateDate(new Date());
        order.setTotalCost(0.0);
        order.setType(OrderType.DELIVERY);
        order.setStatus(OrderStatus.AWAITING_PAYMENT);
        order.setRequiresPharmacy(false);
    }

    @AfterEach
    void tearDown(){
    }

    @Test
    @DisplayName("When request object is not specified")
    void UnitTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(PaymentException.class, ()-> paymentService.setStatus(null));
        assertEquals("Invalid request received - request cannot be null", thrown.getMessage());
    }

    @Test
    @DisplayName("When request object has null orderStatus parameter")
    void UnitTest_OrderStatus_inRequest_NullRequestObject(){
        SetStatusRequest request = new SetStatusRequest(order, null);
        Throwable thrown = Assertions.assertThrows(PaymentException.class, ()-> paymentService.setStatus(request));
        assertEquals("Invalid request received - order status cannot be null", thrown.getMessage());
    }

    @Test
    @DisplayName("When request object has null order parameter")
    void UnitTest_Order_inRequest_NullRequestObject(){
        SetStatusRequest request = new SetStatusRequest(null, awaitingCollection);
        Throwable thrown = Assertions.assertThrows(PaymentException.class, ()-> paymentService.setStatus(request));
        assertEquals("Invalid request received - order object cannot be null", thrown.getMessage());
    }

    @Test
    @DisplayName("When request object has null order and orderStatus parameters")
    void UnitTest_OrderAndOrderStatus_inRequest_NullRequestObject(){
        SetStatusRequest request = new SetStatusRequest(null, null);
        Throwable thrown = Assertions.assertThrows(PaymentException.class, ()-> paymentService.setStatus(request));
        assertEquals("Invalid request received - order status cannot be null", thrown.getMessage());
    }

    @Test
    @DisplayName("The order status successfully updates - AWAITING_PAYMENT")
    void UnitTest_OrderStatus_Success_AWATING_PAYMENT() {
        SetStatusRequest request = new SetStatusRequest(order, awaitingPayment);

        try {
            SetStatusResponse response = paymentService.setStatus(request);
            assertEquals(awaitingPayment, response.getOrder().getStatus());
        }catch(Exception e){
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("The order status successfully updates - PURCHASED")
    void UnitTest_OrderStatus_Success_PURCHASED() {
        SetStatusRequest request = new SetStatusRequest(order, purchased);

        assertEquals(order.getStatus(), awaitingPayment); // before
        try {
            SetStatusResponse response = paymentService.setStatus(request);
            assertEquals(purchased, response.getOrder().getStatus()); // after
        }catch(Exception e){
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("The order status successfully updates - IN_QUEUE")
    void UnitTest_OrderStatus_Success_IN_QUEUE() {
        SetStatusRequest request = new SetStatusRequest(order, inQueue);

        assertEquals(order.getStatus(), awaitingPayment); // before
        try {
            SetStatusResponse response = paymentService.setStatus(request);
            assertEquals(inQueue, response.getOrder().getStatus()); // after
        }catch(Exception e){
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("The order status successfully updates - PACKING")
    void UnitTest_OrderStatus_Success_PACKING() {
        SetStatusRequest request = new SetStatusRequest(order, packing);

        assertEquals(order.getStatus(), awaitingPayment); // before
        try {
            SetStatusResponse response = paymentService.setStatus(request);
            assertEquals(packing, response.getOrder().getStatus()); // after
        }catch(Exception e){
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("The order status successfully updates - AWAITING_COLLECTION")
    void UnitTest_OrderStatus_Success_AWAITING_COLLECTION() {
        SetStatusRequest request = new SetStatusRequest(order, awaitingCollection);

        assertEquals(order.getStatus(), awaitingPayment); // before
        try {
            SetStatusResponse response = paymentService.setStatus(request);
            assertEquals(awaitingCollection, response.getOrder().getStatus()); // after
        }catch(Exception e){
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("The order status successfully updates - DELIVERY_COLLECTED")
    void UnitTest_OrderStatus_Success_DELIVERY_COLLECTED() {
        SetStatusRequest request = new SetStatusRequest(order, deliveryCollected);

        assertEquals(order.getStatus(), awaitingPayment); // before
        try {
            SetStatusResponse response = paymentService.setStatus(request);
            assertEquals(deliveryCollected, response.getOrder().getStatus()); // after
        }catch(Exception e){
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("The order status successfully updates - CUSTOMER_COLLECTED")
    void UnitTest_OrderStatus_Success_CUSTOMER_COLLECTED() {
        SetStatusRequest request = new SetStatusRequest(order, customerCollected);

        assertEquals(order.getStatus(), awaitingPayment); // before
        try {
            SetStatusResponse response = paymentService.setStatus(request);
            assertEquals(customerCollected, response.getOrder().getStatus()); // after
        }catch(Exception e){
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("The order status successfully updates - DELIVERED")
    void UnitTest_OrderStatus_DELIVERED() {
        SetStatusRequest request = new SetStatusRequest(order, delivered);

        assertEquals(order.getStatus(), awaitingPayment); // before
        try {
            SetStatusResponse response = paymentService.setStatus(request);
            assertEquals(delivered, response.getOrder().getStatus()); // after
        }catch(Exception e){
            Assertions.fail();
        }
    }
}
