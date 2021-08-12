package cs.superleague.notification.responses;

public class SendDirectEmailNotificationResponse {
    private final String responseMessage;
    private final boolean isSuccess;

    public SendDirectEmailNotificationResponse(boolean isSuccess, String responseMessage) {
        this.isSuccess = isSuccess;
        this.responseMessage = responseMessage;

    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public boolean isSuccess() {
        return isSuccess;
    }
}
