package cs.superleague.shopping.requests;

import cs.superleague.payment.dataclass.Order;

public class AddToQueueRequest {
    private Order order;

    public AddToQueueRequest(Order order) {
        this.order = order;

    }

    public cs.superleague.shopping.stubs.payment.dataclass.Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
