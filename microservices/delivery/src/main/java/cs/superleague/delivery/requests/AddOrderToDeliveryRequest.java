package cs.superleague.delivery.requests;

import java.util.UUID;

public class AddOrderToDeliveryRequest {
    private UUID deliveryID;
    private UUID orderID;

    public AddOrderToDeliveryRequest(UUID deliveryID, UUID orderID) {
        this.deliveryID = deliveryID;
        this.orderID = orderID;
    }

    public UUID getDeliveryID() {
        return deliveryID;
    }

    public void setDeliveryID(UUID deliveryID) {
        this.deliveryID = deliveryID;
    }

    public UUID getOrderID() {
        return orderID;
    }

    public void setOrderID(UUID orderID) {
        this.orderID = orderID;
    }
}
