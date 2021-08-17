package cs.superleague.payment.requests;

public class GetCustomersActiveOrdersRequest {
    private String jwtToken;

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public GetCustomersActiveOrdersRequest(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}
