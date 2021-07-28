package cs.superleague.payment.requests;

import java.util.UUID;

public class GenerateInvoiceRequest {
    private final UUID transactionID;
    private final UUID customerID;

    public GenerateInvoiceRequest(UUID transactionID, UUID customerID) {
        this.transactionID = transactionID;
        this.customerID = customerID;
    }


    public UUID getTransactionID() {
        return transactionID;
    }

    public UUID getCustomerID() {
        return customerID;
    }
}
