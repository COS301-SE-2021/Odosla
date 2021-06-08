package user.requests;

import java.util.UUID;

public class CompletePackagingOrderRequest {
    private UUID orderID;
    private boolean getNext;

    public CompletePackagingOrderRequest(UUID orderID, boolean getNext) {
        this.orderID = orderID;
        this.getNext = getNext;
    }

    public UUID getOrderID() {
        return orderID;
    }

    public boolean isGetNext() {
        return getNext;
    }
}
