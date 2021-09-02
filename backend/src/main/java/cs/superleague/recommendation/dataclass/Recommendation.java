package cs.superleague.recommendation.dataclass;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "recommendationTable")
public class Recommendation {
    @Id
    private UUID recommendationID;
    private String productID;
    private UUID orderID;

    public Recommendation(UUID recommendationID, String productID, UUID orderID) {
        this.recommendationID = recommendationID;
        this.productID = productID;
        this.orderID = orderID;
    }

    public Recommendation() {
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public UUID getRecommendationID() {
        return recommendationID;
    }

    public void setRecommendationID(UUID recommendationID) {
        this.recommendationID = recommendationID;
    }

    public UUID getOrderID() {
        return orderID;
    }

    public void setOrderID(UUID orderID) {
        this.orderID = orderID;
    }
}
