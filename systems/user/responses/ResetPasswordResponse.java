package cs.superleague.user.responses;

public class ResetPasswordResponse {

    private final String resetCode;
    private final String message;
    private final boolean success;

    public ResetPasswordResponse(String resetCode, String message, boolean success) {
        this.resetCode = resetCode;
        this.message = message;
        this.success = success;
    }

    public String getResetCode() {
        return resetCode;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
}