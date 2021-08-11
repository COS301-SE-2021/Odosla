package cs.superleague.delivery.requests;

import cs.superleague.payment.dataclass.GeoPoint;

import java.util.UUID;

public class GetNextOrderForDriverRequest {
    private UUID driverID;
    private GeoPoint currentLocation;
    private double rangeOfDelivery=0;

    public GetNextOrderForDriverRequest(UUID driverID, GeoPoint currentLocation, double rangeOfDeliverys) {
        this.driverID = driverID;
        this.currentLocation = currentLocation;
        this.rangeOfDelivery = rangeOfDeliverys;
    }

    public double getRangeOfDelivery() {
        return rangeOfDelivery;
    }

    public void setRangeOfDelivery(double rangeOfDelivery) {
        this.rangeOfDelivery = rangeOfDelivery;
    }

    public UUID getDriverID() {
        return driverID;
    }

    public void setDriverID(UUID driverID) {
        this.driverID = driverID;
    }

    public GeoPoint getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(GeoPoint currentLocation) {
        this.currentLocation = currentLocation;
    }
}
