package cs.superleague.user.requests;

import java.util.UUID;

public class GetShopperByUUIDRequest {
    private UUID userID;

    public GetShopperByUUIDRequest(UUID userID) {
        this.userID = userID;
    }

    public UUID getUserID() {
        return userID;
    }

}
