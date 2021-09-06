package cs.superleague.delivery.requests;

import java.util.UUID;

public class GetDeliveryDetailRequest {
    private UUID deliveryID;

    public GetDeliveryDetailRequest(UUID deliveryID) {
        this.deliveryID = deliveryID;
    }

    public UUID getDeliveryID() {
        return deliveryID;
    }

    public void setDeliveryID(UUID deliveryID) {
        this.deliveryID = deliveryID;
    }
}
