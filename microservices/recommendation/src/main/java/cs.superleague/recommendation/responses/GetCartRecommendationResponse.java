package cs.superleague.recommendation.responses;

import cs.superleague.shopping.dataclass.Item;

import java.util.List;

public class GetCartRecommendationResponse {
    private List<Item> recommendations;
    private boolean isSuccess;
    private String message;

    public GetCartRecommendationResponse(List<Item> recommendations, boolean isSuccess, String message) {
        this.recommendations = recommendations;
        this.isSuccess = isSuccess;
        this.message = message;
    }

    public List<Item> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<Item> recommendations) {
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
