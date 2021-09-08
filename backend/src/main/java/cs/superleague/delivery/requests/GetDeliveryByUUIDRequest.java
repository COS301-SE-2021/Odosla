package cs.superleague.delivery.requests;

import java.util.UUID;

public class GetDeliveryByUUIDRequest {
    private UUID deliveryID;

    public GetDeliveryByUUIDRequest(UUID storeID) {
        deliveryID = storeID;
    }

    public UUID getDeliveryID() {
        return deliveryID;
    }
}
