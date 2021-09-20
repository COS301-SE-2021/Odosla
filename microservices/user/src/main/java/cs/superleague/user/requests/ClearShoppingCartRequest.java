package cs.superleague.user.requests;

import java.util.UUID;

public class ClearShoppingCartRequest {

    private final String customerID;

    public String getCustomerID() {
        return customerID;
    }

    public ClearShoppingCartRequest(String customerID) {
        this.customerID = customerID;
    }
}
