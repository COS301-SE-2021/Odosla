package cs.superleague.payment.requests;

import cs.superleague.payment.dataclass.Order;

import java.io.Serializable;

public class SaveOrderToRepoRequest implements Serializable {

    private final Order order;

    public SaveOrderToRepoRequest(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }
}
