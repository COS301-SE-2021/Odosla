package order;

import order.dataclass.Order;
import order.dataclass.OrderStatus;
import order.mock.CancelOrdersMock;
import order.requests.CancelOrderRequest;
import order.responses.CancelOrderResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

class CancelOrderUnitTest {

    //@InjectMocks
    private PaymentServiceImpl paymentService;

    private List<Order> orders = new CancelOrdersMock().getOrders();

    @Test
    @DisplayName("When cancelOrderRequest is null")
    void cancelOrderWhenOrderRequestIsNull() {
        String message = "request object cannot be null";
        int ordersSize = orders.size();
        CancelOrderResponse cancelOrderResponse = paymentService.cancelOrder(null);
        Assertions.assertEquals(message, cancelOrderResponse.getMessage());
        Assertions.assertEquals(false, cancelOrderResponse.isSuccess());
        // order size does not change
        Assertions.assertEquals(cancelOrderResponse.getOrders().size(), ordersSize);
    }
    @Test
    @DisplayName("When order does not exist")
    void cancelOrderWhenOrderDoesNotExist() {
        UUID orderID = UUID.randomUUID();
        int ordersSize = orders.size();
        CancelOrderRequest cancelOrderRequest = new CancelOrderRequest(orderID);
        CancelOrderResponse cancelOrderResponse = paymentService.cancelOrder(cancelOrderRequest);
        Assertions.assertEquals("An order with id: " + orderID + " does not exist\"", cancelOrderResponse.getMessage());
        Assertions.assertEquals(false, cancelOrderResponse.isSuccess());
        // order size does not change
        Assertions.assertEquals(cancelOrderResponse.getOrders().size(), ordersSize);
    }

    @Test
    @DisplayName("When Order Status is Invalid: Delivered")
    void cancelOrderStatusDelivered() {
        Order order = orders.get(0);
        int ordersSize = orders.size();
        order.setStatus(OrderStatus.DELIVERED);
        CancelOrderRequest cancelOrderRequest = new CancelOrderRequest(order.getOrderID());
        CancelOrderResponse cancelOrderResponse = paymentService.cancelOrder(cancelOrderRequest);
        Assertions.assertEquals(cancelOrderResponse.getOrders().size(), ordersSize);
        Assertions.assertEquals("Cannot cancel an order that has been delivered", cancelOrderResponse.getMessage());
        Assertions.assertEquals(false, cancelOrderResponse.isSuccess());
        // order size does not change
        Assertions.assertEquals(cancelOrderResponse.getOrders().size(), ordersSize);
    }

    @Test
    @DisplayName("When Order Status is Invalid: DeliveryCollected")
    void cancelOrderStatusDeliveryCollected() {
        Order order = orders.get(0);
        order.setStatus(OrderStatus.DELIVERY_COLLECTED);
        CancelOrderRequest cancelOrderRequest = new CancelOrderRequest(order.getOrderID());
        CancelOrderResponse cancelOrderResponse = paymentService.cancelOrder(cancelOrderRequest);
        Assertions.assertEquals("Cannot cancel an order that has been delivered", cancelOrderResponse.getMessage());
        Assertions.assertEquals(false, cancelOrderResponse.isSuccess());
    }

    @Test
    @DisplayName("When Order Status is Invalid: CustomerCollected")
    void cancelOrderStatusCustomerCollected() {
        Order order = orders.get(0);
        int ordersSize = orders.size();
        order.setStatus(OrderStatus.CUSTOMER_COLLECTED);
        CancelOrderRequest cancelOrderRequest = new CancelOrderRequest(order.getOrderID());
        CancelOrderResponse cancelOrderResponse = paymentService.cancelOrder(cancelOrderRequest);
        Assertions.assertEquals("Cannot cancel an order that has been delivered", cancelOrderResponse.getMessage());
        Assertions.assertEquals(false, cancelOrderResponse.isSuccess());
        // the size of orders does not change
        Assertions.assertEquals(cancelOrderResponse.getOrders().size(), ordersSize);
    }

    @Test
    @DisplayName("When Order Status is Valid: AWAITING_PAYMENT")
    void cancelOrderStatusAwaitingPayment() {
        Order order = orders.get(0);
        int ordersSize = orders.size();
        order.setStatus(OrderStatus.AWAITING_PAYMENT);
        CancelOrderRequest cancelOrderRequest = new CancelOrderRequest(order.getOrderID());
        CancelOrderResponse cancelOrderResponse = paymentService.cancelOrder(cancelOrderRequest);
        Assertions.assertEquals("Order has been successfully cancelled", cancelOrderResponse.getMessage());
        Assertions.assertEquals(true, cancelOrderResponse.isSuccess());
        Assertions.assertEquals(cancelOrderResponse.getOrders().size(), ordersSize - 1);
    }

    @Test
    @DisplayName("When Order Status is Valid: PURCHASED")
    void cancelOrderStatusPurchased() {
        Order order = orders.get(0);
        int ordersSize = orders.size();
        order.setStatus(OrderStatus.PURCHASED);
        CancelOrderRequest cancelOrderRequest = new CancelOrderRequest(order.getOrderID());
        CancelOrderResponse cancelOrderResponse = paymentService.cancelOrder(cancelOrderRequest);
        Assertions.assertEquals("Order has been successfully cancelled", cancelOrderResponse.getMessage());
        Assertions.assertEquals(true, cancelOrderResponse.isSuccess());
        Assertions.assertEquals(cancelOrderResponse.getOrders().size(), ordersSize - 1);
    }
}