package cs.superleague.payment.requests;


import java.util.UUID;

public class GetInvoiceRequest {

    /** ATTRIBUTES */
    private final UUID invoiceID;

    public GetInvoiceRequest(UUID invoiceID){
        this.invoiceID = invoiceID;
    }

    public UUID getInvoiceID() {
        return invoiceID;
    }
}
