package cs.superleague.shopping.responses;

import java.util.UUID;

public class RemoveQueuedOrderResponse {
    private boolean isRemoved;
    private String message;
    private UUID orderID;

    public RemoveQueuedOrderResponse(boolean isRemoved, String message, UUID orderID) {
        this.isRemoved = isRemoved;
        this.message = message;
        this.orderID = orderID;
    }

    public boolean isRemoved() {
        return isRemoved;
    }

    public void setRemoved(boolean removed) {
        isRemoved = removed;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UUID getOrderID() {
        return orderID;
    }

    public void setOrderID(UUID orderID) {
        this.orderID = orderID;
    }
}
