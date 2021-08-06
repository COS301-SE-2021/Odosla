package cs.superleague.user.requests;

public class GetCurrentUserRequest {
    private final String JWTToken;

    public GetCurrentUserRequest(String JWTToken) {
        this.JWTToken = JWTToken;
    }

    public String getJWTToken() {
        return JWTToken;
    }
}
