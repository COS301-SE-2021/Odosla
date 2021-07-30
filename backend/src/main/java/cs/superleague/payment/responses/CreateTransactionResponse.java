package cs.superleague.payment.responses;

import java.util.Calendar;
import java.util.UUID;

public class CreateTransactionResponse {
    private UUID transactionID;
    private Calendar timestamp;

    public CreateTransactionResponse(UUID transactionID, Calendar timestamp) {
        this.transactionID = transactionID;
        this.timestamp = timestamp;
    }

    public UUID getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(UUID transactionID) {
        this.transactionID = transactionID;
    }

    public Calendar getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Calendar timestamp) {
        this.timestamp = timestamp;
    }
}
