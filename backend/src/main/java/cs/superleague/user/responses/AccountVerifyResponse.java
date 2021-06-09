package cs.superleague.user.responses;

public class AccountVerifyResponse {
    /* Attributes */
    private String token;

    /* Constructor */
    public AccountVerifyResponse(String token) {
        this.token = token;
    }

    /* Getters and Setters */
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}