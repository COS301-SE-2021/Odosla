package cs.superleague.delivery.responses;

public class CompletePackingOrderForDeliveryResponse {
    private boolean success;
    private String message;

    public CompletePackingOrderForDeliveryResponse() {
        this.success = false;
        this.message = null;
    }

    public CompletePackingOrderForDeliveryResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
