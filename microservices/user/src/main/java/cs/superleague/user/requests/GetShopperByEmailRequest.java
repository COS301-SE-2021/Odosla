package cs.superleague.user.requests;

public class GetShopperByEmailRequest {
    private String email;

    public GetShopperByEmailRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
