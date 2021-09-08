package cs.superleague.payment.requests;

import java.util.UUID;

public class GetStatusRequest {
    /* Attributes */
    private UUID orderID;

    public GetStatusRequest(UUID orderID) {
        this.orderID = orderID;
    }

    public UUID getOrderID() {
        return orderID;
    }

    public void setOrderID(UUID orderID) {
        this.orderID = orderID;
    }
}
