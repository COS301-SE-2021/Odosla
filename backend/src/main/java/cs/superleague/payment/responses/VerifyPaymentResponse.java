package cs.superleague.payment.responses;

import java.util.Calendar;
import java.util.UUID;

public class VerifyPaymentResponse {
    private Calendar timestamp;
    private UUID transactionID;

    public VerifyPaymentResponse(Calendar timestamp, UUID transactionID) {
        this.timestamp = timestamp;
        this.transactionID = transactionID;
    }

    public Calendar getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Calendar timestamp) {
        this.timestamp = timestamp;
    }

    public UUID getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(UUID transactionID) {
        this.transactionID = transactionID;
    }
}
