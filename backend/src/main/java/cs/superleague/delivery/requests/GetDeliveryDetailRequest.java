package cs.superleague.delivery.requests;

import java.util.UUID;

public class GetDeliveryDetailRequest {
    private UUID deliveryID;
    private UUID adminID;

    public GetDeliveryDetailRequest(UUID deliveryID, UUID adminID) {
        this.deliveryID = deliveryID;
        this.adminID = adminID;
    }

    public UUID getAdminID() {
        return adminID;
    }

    public void setAdminID(UUID adminID) {
        this.adminID = adminID;
    }

    public UUID getDeliveryID() {
        return deliveryID;
    }

    public void setDeliveryID(UUID deliveryID) {
        this.deliveryID = deliveryID;
    }
}
