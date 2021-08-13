package cs.superleague.user.requests;

public class ResetPasswordRequest {

    private final String email;
    private final String userType;

    public ResetPasswordRequest(String email, String userType) {
        this.email = email;
        this.userType = userType;
    }

    public String getEmail() {
        return email;
    }

    public String getUserType() {
        return userType;
    }
}