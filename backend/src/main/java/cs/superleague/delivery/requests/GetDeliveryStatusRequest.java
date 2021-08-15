package cs.superleague.delivery.requests;

import java.util.UUID;

public class GetDeliveryStatusRequest {
    private UUID deliveryID;

    public GetDeliveryStatusRequest(UUID deliveryID) {
        this.deliveryID = deliveryID;
    }

    public UUID getDeliveryID() {
        return deliveryID;
    }

    public void setDeliveryID(UUID deliveryID) {
        this.deliveryID = deliveryID;
    }
}
