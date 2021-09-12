package cs.superleague.user.requests;

public class GetDriverByEmailRequest {

    private String email;

    public GetDriverByEmailRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
