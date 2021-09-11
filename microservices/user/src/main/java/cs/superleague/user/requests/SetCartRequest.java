package cs.superleague.user.requests;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SetCartRequest {

    private final String customerID;
    private final List<String> barcodes;

    public SetCartRequest(String customerID, List<String> barcodes) {
        this.customerID = customerID;
        this.barcodes = barcodes;
    }

    public String getCustomerID() {
        return customerID;
    }

    public List<String> getBarcodes() {
        return barcodes;
    }
}
