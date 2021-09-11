package cs.superleague.recommendation.responses;

public class AddRecommendationResponse {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public AddRecommendationResponse(String message) {
        this.message = message;
    }
}
