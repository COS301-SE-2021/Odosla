package cs.superleague.payment.stubs.recommendation.dataclass;

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
