package cs.superleague.user.stubs.delivery.responses;

import java.util.UUID;

public class CreateDeliveryResponse {
    private boolean isSuccess;
    private String message;
    private UUID deliveryID;

    public CreateDeliveryResponse(boolean isSuccess, String message, UUID deliveryID) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.deliveryID = deliveryID;
    }

    public UUID getDeliveryID() {
        return deliveryID;
    }

    public void setDeliveryID(UUID deliveryID) {
        this.deliveryID = deliveryID;
    }

    public CreateDeliveryResponse() {
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
