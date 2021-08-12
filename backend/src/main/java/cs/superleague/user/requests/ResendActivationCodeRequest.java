package cs.superleague.user.requests;

import cs.superleague.user.dataclass.UserType;

public class ResendActivationCodeRequest {

    private String email;
    private UserType userType;

    public ResendActivationCodeRequest(String email, UserType userType) {
        this.email = email;
        this.userType = userType;
    }

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
}
