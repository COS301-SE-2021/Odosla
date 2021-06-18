package cs.superleague.user.requests;

public class AccountVerifyRequest {
    /* Attributes */
    private String username;
    private String activationCode;

    /* Constructor */
    public AccountVerifyRequest(String username, String activationCode) {
        this.username = username;
        this.activationCode = activationCode;
    }

    /* Getters and Setters */
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }
}