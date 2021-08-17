package cs.superleague.payment.requests;

public class GetOrdersRequest {

    private final String JWTToken;

    public GetOrdersRequest(String JWTToken) {
        this.JWTToken = JWTToken;
    }

    public String getJWTToken() {
        return JWTToken;
    }
}
