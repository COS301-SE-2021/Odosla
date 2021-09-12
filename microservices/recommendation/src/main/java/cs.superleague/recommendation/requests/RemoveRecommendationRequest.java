package cs.superleague.recommendation.requests;

import java.io.Serializable;
import java.util.UUID;

public class RemoveRecommendationRequest implements Serializable {
    private UUID orderID;

    public RemoveRecommendationRequest(UUID orderID) {
        this.orderID = orderID;
    }

    public UUID getOrderID() {
        return orderID;
    }

    public void setOrderID(UUID orderID) {
        this.orderID = orderID;
    }
}
