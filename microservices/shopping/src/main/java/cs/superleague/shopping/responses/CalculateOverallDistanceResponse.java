package cs.superleague.shopping.responses;

public class CalculateOverallDistanceResponse {

    private double distance;
    private String message;
    private boolean success;

    public CalculateOverallDistanceResponse() {
    }

    public CalculateOverallDistanceResponse(double distance, String message, boolean success) {
        this.distance = distance;
        this.message = message;
        this.success = success;
    }

    public double getDistance() {
        return distance;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
}
