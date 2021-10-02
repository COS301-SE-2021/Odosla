package cs.superleague.delivery.responses;

import java.util.UUID;

public class CreateDeliveryResponse {
    private boolean isSuccess;
    private String message;
    private UUID deliveryID;
    private double costOfDelivery;



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
        this.isSuccess = false;
        this.message = null;
        this.deliveryID = null;
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
