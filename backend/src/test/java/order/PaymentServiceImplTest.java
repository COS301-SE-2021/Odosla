package order;

import order.dataclass.Order;
import order.dataclass.OrderStatus;
import order.mock.OrdersMock;
import order.requests.CancelOrderRequest;
import order.responses.CancelOrderResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PaymentServiceImplTest {

    //@InjectMocks
    private PaymentServiceImpl paymentService;

    @Test
    @DisplayName("When cancelOrderRequest is null")
    void cancelOrderWhenOrderRequestIsNull() {
        String message = "request object cannot be null";
        Assertions.assertEquals(message, paymentService.cancelOrder(null));
        Assertions.assertEquals(false, paymentService.cancelOrder(null).isSuccess());
    }
    @Test
    @DisplayName("When order does not exist")
    void cancelOrderWhenOrderDoesNotExist() {
        UUID orderID = UUID.randomUUID();
        CancelOrderRequest cancelOrderRequest = new CancelOrderRequest(orderID);
        Assertions.assertEquals("An order with id: " + orderID + " does not exist\"", paymentService.cancelOrder(cancelOrderRequest));
        Assertions.assertEquals(false, paymentService.cancelOrder(cancelOrderRequest).isSuccess());
    }

    @Test
    @DisplayName("When Order Status is Invalid: Delivered")
    void cancelOrderStatusDelivered() {
        Order order = new OrdersMock().getOrders().get(0);
        order.setStatus(OrderStatus.DELIVERED);
        CancelOrderRequest cancelOrderRequest = new CancelOrderRequest(order.getOrderID());
        CancelOrderResponse cancelOrderResponse = paymentService.cancelOrder(cancelOrderRequest);
        Assertions.assertEquals("Cannot cancel an order that has been delivered", cancelOrderResponse.getMessage());
        Assertions.assertEquals(false, cancelOrderResponse.isSuccess());
    }

    @Test
    @DisplayName("When Order Status is Invalid: DeliveryCollected")
    void cancelOrderStatusDeliveryCollected() {
        Order order = new OrdersMock().getOrders().get(0);
        order.setStatus(OrderStatus.DELIVERY_COLLECTED);
        CancelOrderRequest cancelOrderRequest = new CancelOrderRequest(order.getOrderID());
        CancelOrderResponse cancelOrderResponse = paymentService.cancelOrder(cancelOrderRequest);
        Assertions.assertEquals("Cannot cancel an order that has been delivered", cancelOrderResponse.getMessage());
        Assertions.assertEquals(false, cancelOrderResponse.isSuccess());
    }

    @Test
    @DisplayName("When Order Status is Invalid: CustomerCollected")
    void cancelOrderStatusCustomerCollected() {
        Order order = new OrdersMock().getOrders().get(0);
        order.setStatus(OrderStatus.CUSTOMER_COLLECTED);
        CancelOrderRequest cancelOrderRequest = new CancelOrderRequest(order.getOrderID());
        CancelOrderResponse cancelOrderResponse = paymentService.cancelOrder(cancelOrderRequest);
        Assertions.assertEquals("Cannot cancel an order that has been delivered", cancelOrderResponse.getMessage());
        Assertions.assertEquals(false, cancelOrderResponse.isSuccess());
    }

    @Test
    @DisplayName("When Order Status is Valid: AWAITING_PAYMENT")
    void cancelOrderStatusAwaitingPayment() {
        Order order = new OrdersMock().getOrders().get(0);
        order.setStatus(OrderStatus.AWAITING_PAYMENT);
        CancelOrderRequest cancelOrderRequest = new CancelOrderRequest(order.getOrderID());
        CancelOrderResponse cancelOrderResponse = paymentService.cancelOrder(cancelOrderRequest);
        Assertions.assertEquals("Order has been successfully cancelled", cancelOrderResponse.getMessage());
        Assertions.assertEquals(true, cancelOrderResponse.isSuccess());
    }

    @Test
    @DisplayName("When Order Status is Valid: PURCHASED")
    void cancelOrderStatusPurchased() {
        Order order = new OrdersMock().getOrders().get(0);
        order.setStatus(OrderStatus.PURCHASED);
        CancelOrderRequest cancelOrderRequest = new CancelOrderRequest(order.getOrderID());
        CancelOrderResponse cancelOrderResponse = paymentService.cancelOrder(cancelOrderRequest);
        Assertions.assertEquals("Order has been successfully cancelled", cancelOrderResponse.getMessage());
        Assertions.assertEquals(true, cancelOrderResponse.isSuccess());
    }
}