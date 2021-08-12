package cs.superleague.notification.responses;

public class SendDirectEmailNotificationResponse {
    private final boolean isSuccess;
    private final String responseMessage;

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
