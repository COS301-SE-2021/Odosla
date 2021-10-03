package cs.superleague.recommendation.requests;

public class GenerateRecommendationTablePDFRequest {

    private String email;

    public GenerateRecommendationTablePDFRequest() {
    }

    public GenerateRecommendationTablePDFRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
