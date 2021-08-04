package cs.superleague.user.requests;

import java.util.UUID;

public class ClearShoppingCartRequest {

    private final UUID customerID;

    public UUID getCustomerID() {
        return customerID;
    }

    public ClearShoppingCartRequest(UUID customerID) {
        this.customerID = customerID;
    }
}
