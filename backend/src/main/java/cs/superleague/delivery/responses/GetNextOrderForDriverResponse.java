package cs.superleague.delivery.responses;

import java.util.UUID;

public class GetNextOrderForDriverResponse {
    private String message;
    private UUID deliveryID;

    public GetNextOrderForDriverResponse(String message, UUID deliveryID) {
        this.message = message;
        this.deliveryID = deliveryID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UUID getDeliveryID() {
        return deliveryID;
    }

    public void setDeliveryID(UUID deliveryID) {
        this.deliveryID = deliveryID;
    }
}
