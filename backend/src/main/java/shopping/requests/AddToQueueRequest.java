package shopping.requests;

import payment.dataclass.Order;

public class AddToQueueRequest {
    private Order order;

    public AddToQueueRequest(Order order) {
        this.order = order;

    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
