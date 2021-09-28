package cs.superleague.shopping.requests;

import cs.superleague.payment.dataclass.Order;

import java.io.Serializable;

public class AddToQueueRequest implements Serializable {
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
