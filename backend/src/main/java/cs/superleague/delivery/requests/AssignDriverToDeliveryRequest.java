package cs.superleague.delivery.requests;

import java.util.UUID;

public class AssignDriverToDeliveryRequest {
    private UUID deliveryID;

    public AssignDriverToDeliveryRequest(UUID deliveryID) {
        this.deliveryID = deliveryID;
    }

    public UUID getDeliveryID() {
        return deliveryID;
    }

    public void setDeliveryID(UUID deliveryID) {
        this.deliveryID = deliveryID;
    }
}
