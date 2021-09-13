package cs.superleague.user.requests;

import java.util.UUID;

public class GetDriverByUUIDRequest {
    private UUID userID;

    public GetDriverByUUIDRequest(UUID userID) {
        this.userID = userID;
    }

    public UUID getUserID() {
        return userID;
    }

    public void setUserID(UUID userID) {
        this.userID = userID;
    }
}
