package cs.superleague.user.requests;

import java.util.UUID;

public class CollectOrderRequest {

    private UUID orderID;

    public CollectOrderRequest(UUID orderID) {
        this.orderID = orderID;
    }

    public UUID getOrderID() {
        return orderID;
    }

    public void setOrderID(UUID orderID) {
        this.orderID = orderID;
    }
}
