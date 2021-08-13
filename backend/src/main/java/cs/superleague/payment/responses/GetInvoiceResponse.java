package cs.superleague.payment.responses;

import java.util.Calendar;
import java.util.UUID;

public class GetInvoiceResponse {

    private final UUID invoiceID;
    private final byte[] PDF;
    private final Calendar timestamp;
    private final String message;


    public GetInvoiceResponse(UUID invoiceID, byte[] PDF, Calendar timestamp, String message) {
        this.invoiceID = invoiceID;
        this.PDF = PDF;
        this.timestamp = timestamp;
        this.message = message;
    }

    public UUID getInvoiceID() {
        return invoiceID;
    }

    public byte[] getPDF() {
        return PDF;
    }

    public Calendar getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }
}
