package cs.superleague.delivery.responses;

public class UpdateDeliveryStatusResponse {
    private String message;

    public UpdateDeliveryStatusResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
