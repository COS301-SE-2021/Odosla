package cs.superleague.payment.requests;

import java.util.UUID;

public class CreateTransactionRequest {
    private UUID orderID;

    public UUID getOrderID() {
        return orderID;
    }

    public void setOrderID(UUID orderID) {
        this.orderID = orderID;
    }

    public CreateTransactionRequest(UUID orderID) {
        this.orderID = orderID;
    }
}
