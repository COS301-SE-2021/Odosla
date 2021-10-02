package cs.superleague.recommendation.responses;

import java.util.ArrayList;
import java.util.List;

public class GenerateRecommendationTableResponse {
    private boolean isSuccess;
    private String message;
    private List<List<String>> recommendationTable;

    public GenerateRecommendationTableResponse(boolean isSuccess, String message, List<List<String>> recommendationTable){
        this.isSuccess = isSuccess;
        this.message = message;
        this.recommendationTable = recommendationTable;
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

    public List<List<String>> getRecommendationTable() {
        return recommendationTable;
    }

    public void setRecommendationTable(List<List<String>> recommendationTable) {
        this.recommendationTable = recommendationTable;
    }

}
