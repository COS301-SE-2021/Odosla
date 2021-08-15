package cs.superleague.user.requests;

public class GetUsersRequest {

    private final String adminID;

    public GetUsersRequest(String adminID) {
        this.adminID = adminID;
    }

    public String getAdminID() {
        return adminID;
    }
}
