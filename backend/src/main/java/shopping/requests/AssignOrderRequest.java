package shopping.requests;

import order.dataclass.Order;

public class AssignOrderRequest {
    private Order order;

    public AssignOrderRequest(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
