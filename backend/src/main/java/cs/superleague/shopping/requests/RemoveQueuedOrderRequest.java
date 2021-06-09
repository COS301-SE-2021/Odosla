package cs.superleague.shopping.requests;

import java.util.UUID;

public class RemoveQueuedOrderRequest {
    private UUID orderID;

    public RemoveQueuedOrderRequest(UUID orderID) {
        this.orderID = orderID;
    }

    public UUID getOrderID() {
        return orderID;
    }

    public void setOrderID(UUID orderID) {
        this.orderID = orderID;
    }
}
