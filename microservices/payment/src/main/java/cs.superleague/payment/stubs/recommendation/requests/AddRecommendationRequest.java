package cs.superleague.payment.stubs.recommendation.requests;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class AddRecommendationRequest implements Serializable {
    private UUID orderID;
    private List<String> productID;

    public AddRecommendationRequest(UUID orderID, List<String> productID) {
        this.orderID = orderID;
        this.productID = productID;
    }

    public UUID getOrderID() {
        return orderID;
    }

    public void setOrderID(UUID orderID) {
        this.orderID = orderID;
    }

    public List<String> getProductID() {
        return productID;
    }

    public void setProductID(List<String> productID) {
        this.productID = productID;
    }
}
