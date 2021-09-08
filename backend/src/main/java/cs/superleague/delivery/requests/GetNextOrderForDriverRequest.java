package cs.superleague.delivery.requests;

import cs.superleague.payment.dataclass.GeoPoint;

import java.util.UUID;

public class GetNextOrderForDriverRequest {
    private GeoPoint currentLocation;
    private double rangeOfDelivery;

    public GetNextOrderForDriverRequest(GeoPoint currentLocation, double rangeOfDeliverys) {
        this.currentLocation = currentLocation;
        this.rangeOfDelivery = rangeOfDeliverys;
    }

    public GetNextOrderForDriverRequest(GeoPoint currentLocation) {
        this.currentLocation = currentLocation;
        this.rangeOfDelivery = 10;
    }

    public double getRangeOfDelivery() {
        return rangeOfDelivery;
    }

    public void setRangeOfDelivery(double rangeOfDelivery) {
        this.rangeOfDelivery = rangeOfDelivery;
    }

    public GeoPoint getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(GeoPoint currentLocation) {
        this.currentLocation = currentLocation;
    }

}
