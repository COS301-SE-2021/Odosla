package cs.superleague.notifications.responses;

public class SendPDFEmailNotificationResponse {
    private boolean isSuccess;
    private String message;

    public SendPDFEmailNotificationResponse(boolean isSuccess, String message) {
        this.isSuccess = isSuccess;
        this.message = message;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
