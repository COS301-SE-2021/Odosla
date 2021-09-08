package cs.superleague.user.requests;

import cs.superleague.user.dataclass.UserType;

public class AccountVerifyRequest {

    /* Attributes */
    private String email;
    private String activationCode;
    private UserType userType;

    /* Constructor */
    public AccountVerifyRequest(String email, String activationCode, UserType userType) {
        this.email = email;
        this.activationCode = activationCode;
        this.userType = userType;
    }

    /* Getters and Setters */

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }
}