package cs.superleague.recommendation.responses;

public class GenerateRecommendationTablePDFResponse {

    private boolean isSuccess;
    private String message;

    public GenerateRecommendationTablePDFResponse() {
    }

    public GenerateRecommendationTablePDFResponse(boolean isSuccess, String message) {
        this.isSuccess = isSuccess;
        this.message = message;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public String getMessage() {
        return message;
    }
}
