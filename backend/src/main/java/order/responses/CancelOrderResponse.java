package order.responses;

import order.dataclass.Order;

import java.util.List;

public class CancelOrderResponse {
    private boolean success;
    private List<Order> orders;

    public CancelOrderResponse(boolean success, List<Order> orders) {
        this.success = success;
        this.orders = orders;
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
}
