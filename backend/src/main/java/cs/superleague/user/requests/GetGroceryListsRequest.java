package cs.superleague.user.requests;

public class GetGroceryListsRequest {

    private final String JWTToken;

    public GetGroceryListsRequest(String JWTToken) {
        this.JWTToken = JWTToken;
    }

    public String getJWTToken() {
        return JWTToken;
    }
}
