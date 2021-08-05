package cs.superleague.user.requests;

import java.util.UUID;

public class GetCustomersRequest {

    private UUID userID;

    public GetCustomersRequest(UUID userID) {
        this.userID = userID;
    }

    public UUID getUserID() {
        return userID;
    }

    public void setUserID(UUID userID) {
        this.userID = userID;
    }
}