package cs.superleague.delivery.requests;

import java.util.UUID;

public class RemoveDriverFromDeliveryRequest {
    private UUID deliveryID;

    public RemoveDriverFromDeliveryRequest(UUID deliveryID) {
        this.deliveryID = deliveryID;
    }

    public UUID getDeliveryID() {
        return deliveryID;
    }

    public void setDeliveryID(UUID deliveryID) {
        this.deliveryID = deliveryID;
    }
}
