package cs.superleague.user.requests;

import java.util.UUID;

public class GetCustomerByUUIDRequest {
    private UUID userID;

    public GetCustomerByUUIDRequest(UUID userID) {
        this.userID = userID;
    }

    public UUID getUserID() {
        return userID;
    }
}
