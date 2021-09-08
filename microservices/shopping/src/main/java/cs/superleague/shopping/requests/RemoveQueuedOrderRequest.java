package cs.superleague.shopping.requests;

import java.util.UUID;

public class RemoveQueuedOrderRequest {
    private UUID orderID;
    private UUID storeID;

    public RemoveQueuedOrderRequest(UUID orderID, UUID storeID) {
        this.orderID = orderID;
        this.storeID = storeID;
    }

    public UUID getOrderID() {
        return orderID;
    }

    public void setOrderID(UUID orderID) {
        this.orderID = orderID;
    }

    public UUID getStoreID() {
        return storeID;
    }

    public void setStoreID(UUID storeID) {
        this.storeID = storeID;
    }
}
