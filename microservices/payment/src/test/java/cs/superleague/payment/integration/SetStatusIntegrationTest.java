package cs.superleague.payment.integration;

import cs.superleague.payment.PaymentServiceImpl;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.dataclass.OrderStatus;
import cs.superleague.payment.dataclass.OrderType;
import cs.superleague.payment.exceptions.PaymentException;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.payment.requests.SetStatusRequest;
import cs.superleague.payment.responses.SetStatusResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class SetStatusIntegrationTest {

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    PaymentServiceImpl paymentService;

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
        order.setShopperID(shopperID);
        order.setCreateDate(new Date());
        order.setType(OrderType.DELIVERY);
        order.setStatus(awaitingPayment);
        order.setRequiresPharmacy(false);
    }

    @AfterEach
    void tearDown() {
        orderRepo.delete(order);
    }

    @Test
    @DisplayName("When request object is not specified")
    void IntegrationTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(PaymentException.class, ()-> paymentService.setStatus(null));
        assertEquals("Invalid request received - request cannot be null", thrown.getMessage());
    }

    @Test
    @DisplayName("When request object has null orderStatus parameter")
    void IntegrationTest_OrderStatus_inRequest_NullRequestObject(){
        SetStatusRequest request = new SetStatusRequest(order, null);
        Throwable thrown = Assertions.assertThrows(PaymentException.class, ()-> paymentService.setStatus(request));
        assertEquals("Invalid request received - order status cannot be null", thrown.getMessage());
    }

    @Test
    @DisplayName("When request object has null order parameter")
    void IntegrationTest_Order_inRequest_NullRequestObject(){
        SetStatusRequest request = new SetStatusRequest(null, awaitingCollection);
        Throwable thrown = Assertions.assertThrows(PaymentException.class, ()-> paymentService.setStatus(request));
        assertEquals("Invalid request received - order object cannot be null", thrown.getMessage());
    }

    @Test
    @DisplayName("When request object has null order and orderStatus parameters")
    void IntegrationTest_OrderAndOrderStatus_inRequest_NullRequestObject(){
        SetStatusRequest request = new SetStatusRequest(null, null);
        Throwable thrown = Assertions.assertThrows(PaymentException.class, ()-> paymentService.setStatus(request));
        assertEquals("Invalid request received - order status cannot be null", thrown.getMessage());
    }

    @Test
    @DisplayName("The order status successfully updates - AWAITING_PAYMENT")
    void IntegrationTest_OrderStatus_Success_AWATING_PAYMENT() {
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
    void IntegrationTest_OrderStatus_Success_PURCHASED() {
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
    void IntegrationTest_OrderStatus_Success_IN_QUEUE() {
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
    void IntegrationTest_OrderStatus_Success_PACKING() {
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
    void IntegrationTest_OrderStatus_Success_AWAITING_COLLECTION() {
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
    void IntegrationTest_OrderStatus_Success_DELIVERY_COLLECTED() {
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
    void IntegrationTest_OrderStatus_Success_CUSTOMER_COLLECTED() {
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
    void IntegrationTest_OrderStatus_DELIVERED() {
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