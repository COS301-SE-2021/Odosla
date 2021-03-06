package cs.superleague.payment.requests;

import java.util.UUID;

public class GetOrderByUUIDRequest {

    private final UUID orderID;

    public GetOrderByUUIDRequest(UUID orderID) {
        this.orderID = orderID;
    }

    public UUID getOrderID() {
        return orderID;
    }
}
