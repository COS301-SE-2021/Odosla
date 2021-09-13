package cs.superleague.user.requests;

import java.util.UUID;

public class GetAdminByUUIDRequest {
    private UUID userID;

    public GetAdminByUUIDRequest(UUID userID) {
        this.userID = userID;
    }

    public UUID getUserID() {
        return userID;
    }

    public void setUserID(UUID userID) {
        this.userID = userID;
    }
}
