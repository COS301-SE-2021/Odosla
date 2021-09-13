package cs.superleague.user.requests;

public class GetAdminByEmailRequest {
    private String email;

    public GetAdminByEmailRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
