package cs.superleague.payment.requests;


import java.util.UUID;

public class GetInvoiceRequest {

    /**
     * ATTRIBUTES
     */
    private final UUID invoiceID;
    private final UUID userID;

    public GetInvoiceRequest(UUID invoiceID, UUID userID) {
        this.invoiceID = invoiceID;
        this.userID = userID;
    }

    public UUID getInvoiceID() {
        return invoiceID;
    }

    public UUID getUserID() {
        return userID;
    }
}
