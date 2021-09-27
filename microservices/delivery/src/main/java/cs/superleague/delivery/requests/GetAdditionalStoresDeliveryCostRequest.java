package cs.superleague.delivery.requests;

import java.util.UUID;

public class GetAdditionalStoresDeliveryCostRequest {
    private UUID deliveryID;

    public GetAdditionalStoresDeliveryCostRequest(UUID deliveryID) {
        this.deliveryID = deliveryID;
    }

    public UUID getDeliveryID() {
        return deliveryID;
    }

    public void setDeliveryID(UUID deliveryID) {
        this.deliveryID = deliveryID;
    }
}
