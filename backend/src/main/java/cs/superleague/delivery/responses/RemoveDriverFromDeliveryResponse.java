package cs.superleague.delivery.responses;

public class RemoveDriverFromDeliveryResponse {
    private boolean isSuccess;
    private String message;

    public RemoveDriverFromDeliveryResponse(boolean isSuccess, String message) {
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
