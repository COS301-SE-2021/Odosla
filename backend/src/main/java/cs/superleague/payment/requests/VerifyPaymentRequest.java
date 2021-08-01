package cs.superleague.payment.requests;

import java.util.UUID;

public class VerifyPaymentRequest {
    private UUID transactionID;

    public VerifyPaymentRequest(UUID transactionID) {
        this.transactionID = transactionID;
    }

    public UUID getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(UUID transactionID) {
        this.transactionID = transactionID;
    }
}
