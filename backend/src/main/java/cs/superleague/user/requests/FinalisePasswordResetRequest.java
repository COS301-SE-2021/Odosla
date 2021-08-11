package cs.superleague.user.requests;

public class FinalisePasswordResetRequest {

    private final String email;
    private final String userType;
    private final String resetCode;
    private final String password;

    public FinalisePasswordResetRequest(String email, String userType, String resetCode, String password) {
        this.email = email;
        this.userType = userType;
        this.resetCode = resetCode;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getUserType() {
        return userType;
    }

    public String getResetCode() {
        return resetCode;
    }
}