package cs.superleague.recommendation.dataclass;

import javax.persistence.*;
import java.util.Calendar;
import java.util.UUID;

@Entity
@Table(name = "recommendationTable")
public class Recommendation {
    @Id
    private UUID recommendationID;
    private String productyID;
    private UUID orderID;
    private Calendar recommendationAddedDate;

    public Recommendation(UUID recommendationID, String productID, UUID orderID, Calendar recommendationAddedDate) {
        this.recommendationID = recommendationID;
        this.productyID = productID;
        this.orderID = orderID;
        this.recommendationAddedDate = recommendationAddedDate;
    }

    public Recommendation() {
    }

    public String getProductID() {
        return productyID;
    }

    public void setProductID(String productID) {
        this.productyID = productID;
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

    public Calendar getRecommendationAddedDate() {
        return recommendationAddedDate;
    }

    public void setRecommendationAddedDate(Calendar recommendationAddedDate) {
        this.recommendationAddedDate = recommendationAddedDate;
    }
}
