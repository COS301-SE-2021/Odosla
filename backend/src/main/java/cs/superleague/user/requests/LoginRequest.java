package cs.superleague.user.requests;

import cs.superleague.user.dataclass.UserType;

public class LoginRequest {

    private String email;
    private String password;
    private UserType userType;


    public LoginRequest(String email, String password,UserType userType) {
        this.email = email;
        this.password = password;
        this.userType=userType;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}