package cs.superleague.user.requests;

import cs.superleague.shopping.dataclass.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SetCartRequest {

    private final UUID customerID;
    private final List<String> barcodes;

    public SetCartRequest(UUID customerID, List<String> barcodes) {
        this.customerID = customerID;
        this.barcodes = barcodes;
    }

    public UUID getCustomerID() {
        return customerID;
    }

    public List<String> getBarcodes() {
        return barcodes;
    }
}
