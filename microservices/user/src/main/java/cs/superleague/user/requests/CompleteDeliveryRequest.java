package cs.superleague.user.requests;

import java.util.UUID;

public class CompleteDeliveryRequest {

    private UUID orderID;

    public CompleteDeliveryRequest(UUID orderID) {
        this.orderID = orderID;
    }

    public UUID getOrderID() {
        return orderID;
    }

    public void setOrderID(UUID orderID) {
        this.orderID = orderID;
    }
}
