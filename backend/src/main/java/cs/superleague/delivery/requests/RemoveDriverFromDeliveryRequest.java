package cs.superleague.delivery.requests;

import java.util.UUID;

public class RemoveDriverFromDeliveryRequest {
    private UUID driverID;
    private UUID deliveryID;

    public RemoveDriverFromDeliveryRequest(UUID driverID, UUID deliveryID) {
        this.driverID = driverID;
        this.deliveryID = deliveryID;
    }

    public UUID getDriverID() {
        return driverID;
    }

    public void setDriverID(UUID driverID) {
        this.driverID = driverID;
    }

    public UUID getDeliveryID() {
        return deliveryID;
    }

    public void setDeliveryID(UUID deliveryID) {
        this.deliveryID = deliveryID;
    }
}
