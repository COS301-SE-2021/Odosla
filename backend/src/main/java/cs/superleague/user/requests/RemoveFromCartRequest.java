package cs.superleague.user.requests;

import java.util.UUID;

public class RemoveFromCartRequest {

    private final UUID customerID;
    private final String barcode;

    public UUID getCustomerID() {
        return customerID;
    }

    public String getBarcode() {
        return barcode;
    }

    public RemoveFromCartRequest(UUID customerID, String barcode) {
        this.customerID = customerID;
        this.barcode = barcode;
    }
}
