package cs.superleague.payment.requests;

import java.util.UUID;

public class GetOrderRequest {
    /* Attributes */
    private UUID orderID;

    public GetOrderRequest(UUID orderID) {
        this.orderID = orderID;
    }

    public UUID getOrderID() {
        return orderID;
    }

    public void setOrderID(UUID orderID) {
        this.orderID = orderID;
    }
}
