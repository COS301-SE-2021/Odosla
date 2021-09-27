package cs.superleague.user.responses;

public class GetProblemsWithOrderResponse {
    private String currentProductBarcode;
    private String alternativeProductBarcode;
    private boolean problem;
    private String message;

    public GetProblemsWithOrderResponse(String currentProductBarcode, String alternativeProductBarcode, boolean problem, String message) {
        this.currentProductBarcode = currentProductBarcode;
        this.alternativeProductBarcode = alternativeProductBarcode;
        this.problem = problem;
        this.message = message;
    }

    public String getCurrentProductBarcode() {
        return currentProductBarcode;
    }

    public void setCurrentProductBarcode(String currentProductBarcode) {
        this.currentProductBarcode = currentProductBarcode;
    }

    public String getAlternativeProductBarcode() {
        return alternativeProductBarcode;
    }

    public void setAlternativeProductBarcode(String alternativeProductBarcode) {
        this.alternativeProductBarcode = alternativeProductBarcode;
    }

    public boolean isProblem() {
        return problem;
    }

    public void setProblem(boolean problem) {
        this.problem = problem;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
