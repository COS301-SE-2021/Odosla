package cs.superleague.delivery.requests;

import java.util.UUID;

public class GetDeliveryDriverByOrderIDRequest {

    private UUID orderID;

    public GetDeliveryDriverByOrderIDRequest(UUID orderID)
    {
        this.orderID= orderID;
    }

    public UUID getOrderID() {
        return orderID;
    }

    public void setOrderID(UUID orderID) {
        this.orderID = orderID;
    }
}
