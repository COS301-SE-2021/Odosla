package payment.responses;

import payment.dataclass.Order;

import java.util.List;

public class CancelOrderResponse {
    private boolean success;
    private String message;
    private List<Order> orders;

    public CancelOrderResponse(boolean success, List<Order> orders, String message) {
        this.success = success;
        this.orders = orders;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
