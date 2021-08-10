package cs.superleague.delivery.requests;

import java.util.UUID;

public class TrackDeliveryRequest {
    private UUID deliveryID;

    public TrackDeliveryRequest(UUID deliveryID) {
        this.deliveryID = deliveryID;
    }

    public UUID getDeliveryID() {
        return deliveryID;
    }

    public void setDeliveryID(UUID deliveryId) {
        this.deliveryID = deliveryId;
    }
}
