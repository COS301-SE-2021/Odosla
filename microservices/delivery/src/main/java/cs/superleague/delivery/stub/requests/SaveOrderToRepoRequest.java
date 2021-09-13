package cs.superleague.delivery.stub.requests;

import cs.superleague.delivery.stub.dataclass.Order;

import java.io.Serializable;

public class SaveOrderToRepoRequest implements Serializable {

    private final Order order;

    public SaveOrderToRepoRequest() {
        this.order = null;
    }

    public SaveOrderToRepoRequest(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }
}
