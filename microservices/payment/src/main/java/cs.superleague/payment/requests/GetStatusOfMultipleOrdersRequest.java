package cs.superleague.payment.requests;

import java.util.UUID;

public class GetStatusOfMultipleOrdersRequest {
    private UUID deliveryID;

    public GetStatusOfMultipleOrdersRequest(UUID deliveryID) {
        this.deliveryID = deliveryID;
    }

    public UUID getDeliveryID() {
        return deliveryID;
    }

    public void setDeliveryID(UUID deliveryID) {
        this.deliveryID = deliveryID;
    }
}
