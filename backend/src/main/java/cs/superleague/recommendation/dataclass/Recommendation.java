package cs.superleague.recommendation.dataclass;

import cs.superleague.payment.dataclass.Order;
import cs.superleague.shopping.dataclass.Item;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "recommendationTable")
public class Recommendation {
    @Id
    private UUID recommendationID;
    private String productID;
    private UUID orderID;
    private Calendar recommendationAddedDate;

    public Recommendation(UUID recommendationID, String productID, UUID orderID, Calendar recommendationAddedDate) {
        this.recommendationID = recommendationID;
        this.productID = productID;
        this.orderID = orderID;
        this.recommendationAddedDate = recommendationAddedDate;
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

    public Calendar getRecommendationAddedDate() {
        return recommendationAddedDate;
    }

    public void setRecommendationAddedDate(Calendar recommendationAddedDate) {
        this.recommendationAddedDate = recommendationAddedDate;
    }
}
