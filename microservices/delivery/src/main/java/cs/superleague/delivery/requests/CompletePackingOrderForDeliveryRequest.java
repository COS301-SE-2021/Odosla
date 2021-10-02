package cs.superleague.delivery.requests;

import java.util.UUID;

public class CompletePackingOrderForDeliveryRequest {
    private UUID orderID;

    public CompletePackingOrderForDeliveryRequest(UUID orderID) {
        this.orderID = orderID;
    }

    public UUID getOrderID() {
        return orderID;
    }

    public void setOrderID(UUID orderID) {
        this.orderID = orderID;
    }
}
