package cs.superleague.payment.requests;

import java.util.UUID;

public class VerifyPaymentRequest {
    private UUID orderID;

    public UUID getOrderID() {
        return orderID;
    }

    public void setOrderID(UUID orderID) {
        this.orderID = orderID;
    }

    public VerifyPaymentRequest(UUID orderID) {
        this.orderID = orderID;
    }
}
