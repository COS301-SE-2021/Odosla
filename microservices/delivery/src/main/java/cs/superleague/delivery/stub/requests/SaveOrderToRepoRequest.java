package cs.superleague.delivery.stub.requests;

import cs.superleague.delivery.stub.payment.dataclass.Order;

public class SaveOrderToRepoRequest {

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
