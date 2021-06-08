package cs.superleague.shopping.requests;

import java.util.UUID;

public class GetShoppersRequest {
    private UUID userID;

    public UUID getUserID() {
        return userID;
    }

    public void setUserID(UUID userID) {
        this.userID = userID;
    }
}
