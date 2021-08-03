package cs.superleague.notification.responses;

public class CreateNotificationResponse {
    private String responseMessage;

    public CreateNotificationResponse(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }
}
