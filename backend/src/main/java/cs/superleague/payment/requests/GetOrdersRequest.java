package cs.superleague.payment.requests;

public class GetOrdersRequest {

    private final String adminID;

    public GetOrdersRequest(String adminID) {
        this.adminID = adminID;
    }

    public String getAdminID() {
        return adminID;
    }
}
