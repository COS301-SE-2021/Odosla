package cs.superleague.recommendation.requests;

import java.util.UUID;

public class RemoveRecommendationRequest {
    private String productID;
    private UUID orderID;

    public RemoveRecommendationRequest(String productID, UUID orderID) {
        this.productID = productID;
        this.orderID = orderID;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public UUID getOrderID() {
        return orderID;
    }

    public void setOrderID(UUID orderID) {
        this.orderID = orderID;
    }
}
