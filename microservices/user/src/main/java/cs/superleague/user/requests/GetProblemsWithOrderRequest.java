package cs.superleague.user.requests;

import java.util.UUID;

public class GetProblemsWithOrderRequest {
    private UUID orderID;

    public GetProblemsWithOrderRequest(UUID orderID) {
        this.orderID = orderID;
    }

    public UUID getOrderID() {
        return orderID;
    }

    public void setOrderID(UUID orderID) {
        this.orderID = orderID;
    }
}
