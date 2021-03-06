package cs.superleague.payment.requests;

import cs.superleague.payment.dataclass.Order;

import java.io.Serializable;

public class SaveOrderRequest implements Serializable {
    private Order order;

    public SaveOrderRequest(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
