package cs.superleague.payment.requests;

import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.dataclass.OrderStatus;
import cs.superleague.payment.dataclass.OrderType;
import cs.superleague.payment.stubs.shopping.dataclass.Item;

import java.util.List;
import java.util.UUID;

public class SetStatusRequest {
    /** attrbiutes */
    private Order order;
    private OrderStatus orderStatus;

    /** constructor
     * @param order - order whose status is to be updated
     * @param orderStatus - the order status that we want the order to be changed to
     */
    public SetStatusRequest(Order order, OrderStatus orderStatus) {
        this.order = order;
        this.orderStatus = orderStatus;
    }

    /* getters */
    public Order getOrder() {
        return order;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
