package cs.superleague.delivery.responses;

import cs.superleague.delivery.stub.dataclass.GeoPoint;

public class TrackDeliveryResponse {
    private GeoPoint currentLocation;
    private String message;

    public TrackDeliveryResponse(GeoPoint currentLocation, String message) {
        this.currentLocation = currentLocation;
        this.message = message;
    }

    public GeoPoint getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(GeoPoint currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
