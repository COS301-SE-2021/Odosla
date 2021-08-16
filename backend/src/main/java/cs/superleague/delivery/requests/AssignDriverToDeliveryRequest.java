package cs.superleague.delivery.requests;

import java.util.UUID;

public class AssignDriverToDeliveryRequest {
    private UUID deliveryID;
    private String jwtToken;

    public AssignDriverToDeliveryRequest(String jwtToken, UUID deliveryID) {
        this.jwtToken = jwtToken;
        this.deliveryID = deliveryID;
    }

    public UUID getDeliveryID() {
        return deliveryID;
    }

    public void setDeliveryID(UUID deliveryID) {
        this.deliveryID = deliveryID;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}
