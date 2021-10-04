package cs.superleague.notifications.responses;

public class SendPDFEmailResponse {
    private boolean success;
    private String message;

    public SendPDFEmailResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
