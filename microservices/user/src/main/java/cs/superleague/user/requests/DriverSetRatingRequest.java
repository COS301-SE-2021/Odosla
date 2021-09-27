package cs.superleague.user.requests;

import java.util.UUID;

public class DriverSetRatingRequest {

    private UUID driverID;
    private double rating;

    public DriverSetRatingRequest(UUID driverID, double rating) {
        this.driverID = driverID;
        this.rating = rating;
    }

    public UUID getDriverID() {
        return driverID;
    }

    public void setDriverID(UUID driverID) {
        this.driverID = driverID;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
