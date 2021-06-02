package payment;

import org.mockito.Mock;
import payment.dataclass.Order;
import payment.dataclass.OrderStatus;
import payment.mock.CancelOrdersMock;
import payment.repos.OrderRepo;
import payment.requests.CancelOrderRequest;
import payment.responses.CancelOrderResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class CancelOrderUnitTest {


    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Mock
    private OrderRepo orderRepo;

    @Test
    @DisplayName("When cancelOrderRequest is null")
    void cancelOrderWhenOrderRequestIsNull() {
        String message = "request object cannot be null";
        CancelOrderResponse cancelOrderResponse = paymentService.cancelOrder(null);
        Assertions.assertEquals(message, cancelOrderResponse.getMessage());
        Assertions.assertEquals(false, cancelOrderResponse.isSuccess());
    }
    @Test
    @DisplayName("When order does not exist")
    void cancelOrderWhenOrderDoesNotExist() {
        UUID orderID = UUID.randomUUID();
        List<Order> orders = orderRepo.findAll();
        int ordersSize = orders.size();
        CancelOrderRequest cancelOrderRequest = new CancelOrderRequest(orderID);
        CancelOrderResponse cancelOrderResponse = paymentService.cancelOrder(cancelOrderRequest);
        Assertions.assertEquals("An order with id: " + orderID + " does not exist", cancelOrderResponse.getMessage());
        Assertions.assertEquals(false, cancelOrderResponse.isSuccess());
        // order size does not change
        Assertions.assertEquals(cancelOrderResponse.getOrders().size(), ordersSize);
    }

    @Test
    @DisplayName("When Order Status is Invalid: Delivered")
    void cancelOrderStatusDelivered() {
        List<Order> orders = orderRepo.findAll();
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
        List<Order> orders = orderRepo.findAll();
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
        List<Order> orders = orderRepo.findAll();
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
        List<Order> orders = orderRepo.findAll();
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
        List<Order> orders = orderRepo.findAll();
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