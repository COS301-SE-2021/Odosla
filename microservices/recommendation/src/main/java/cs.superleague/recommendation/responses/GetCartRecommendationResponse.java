package cs.superleague.recommendation.responses;

import cs.superleague.payment.dataclass.CartItem;
import cs.superleague.shopping.dataclass.Item;

import java.util.List;

public class GetCartRecommendationResponse {
    private List<CartItem> recommendations;
    private boolean isSuccess;
    private String message;

    public GetCartRecommendationResponse(List<CartItem> recommendations, boolean isSuccess, String message) {
        this.recommendations = recommendations;
        this.isSuccess = isSuccess;
        this.message = message;
    }

    public List<CartItem> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<CartItem> recommendations) {
        this.recommendations = recommendations;
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
