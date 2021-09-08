package cs.superleague.user.requests;

import java.util.UUID;

public class GetShoppingCartRequest {

    private final String userID;

    public GetShoppingCartRequest(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }
}
