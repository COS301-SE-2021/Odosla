package cs.superleague.payment.stubs.shopping.requests;

import cs.superleague.payment.dataclass.Order;

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
