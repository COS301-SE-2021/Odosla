package cs.superleague.payment.responses;

import java.util.Calendar;
import java.util.UUID;

public class GenerateInvoiceResponse {
    private final UUID invoiceID;
    private final Calendar timestamp;
    private final String message;

    public GenerateInvoiceResponse(UUID invoiceID, Calendar timestamp, String message) {
        this.invoiceID = invoiceID;
        this.timestamp = timestamp;
        this.message = message;
    }

    public UUID getInvoiceID() {
        return invoiceID;
    }

    public Calendar getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }
}
