package cs.superleague.shopping.stubs.payment.requests;

import cs.superleague.shopping.stubs.payment.dataclass.Order;

import java.io.Serializable;

public class SaveOrderToRepoRequest implements Serializable {

    private static final long serialVersionUID = 1234567L;
    private Order order;

    public SaveOrderToRepoRequest(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
