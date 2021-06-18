package user.requests;

import java.util.Date;
import java.util.UUID;

public class GetShopperByUUIDRequest {
    private UUID userID;

    public GetShopperByUUIDRequest(UUID userID) {
        userID = userID;
    }

    public UUID getUserID() {
        return userID;
    }


}
