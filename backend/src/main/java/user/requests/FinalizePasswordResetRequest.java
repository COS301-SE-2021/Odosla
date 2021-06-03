package user.requests;

public class FinalizePasswordResetRequest {
    /* Attributes */
    private String username;
    private String resetCode;
    private String newPassword;
    private String newPasswordConfirm;

    /* Constructor */

    public FinalizePasswordResetRequest(String username, String resetCode, String newPassword, String newPasswordConfirm) {
        this.username = username;
        this.resetCode = resetCode;
        this.newPassword = newPassword;
        this.newPasswordConfirm = newPasswordConfirm;
    }

    /* Getters and Setters */
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getResetCode() {
        return resetCode;
    }

    public void setResetCode(String resetCode) {
        this.resetCode = resetCode;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPasswordConfirm() {
        return newPasswordConfirm;
    }

    public void setNewPasswordConfirm(String newPasswordConfirm) {
        this.newPasswordConfirm = newPasswordConfirm;
    }
}