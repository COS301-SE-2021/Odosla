package cs.superleague.user.requests;

import java.util.UUID;

public class GetShoppingCartRequest {

    private final UUID userID;

    public GetShoppingCartRequest(UUID userID) {
        this.userID = userID;
    }

    public UUID getUserID() {
        return userID;
    }
}
