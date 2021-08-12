package cs.superleague.payment.requests;

import java.util.UUID;

public class GetItemsRequest {
    /* Attributes */
    private UUID orderID;

    public GetItemsRequest(UUID orderID) {
        this.orderID = orderID;
    }

    public UUID getOrderID() {
        return orderID;
    }

    public void setOrderID(UUID orderID) {
        this.orderID = orderID;
    }
}
