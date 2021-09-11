package cs.superleague.user.stubs.notifications.responses;

public class SendEmailNotificationResponse {
    private final String responseMessage;
    private final Boolean success;

    public SendEmailNotificationResponse(Boolean bool, String respMesg)
    {
        this.success =bool;
        this.responseMessage = respMesg;
    }

    public String getResponseMessage() {
        return this.responseMessage;
    }
    public Boolean getSuccessMessage() {
        return this.success;
    }
}
