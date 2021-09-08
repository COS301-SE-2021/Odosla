package cs.superleague.user.requests;

import java.util.List;

public class SetCurrentLocationRequest {

    private final String driverID;
    private final Double longitude;
    private final Double latitude;
    private final String address;

    public SetCurrentLocationRequest(String driverID, Double longitude, Double latitude, String address) {
        this.driverID = driverID;
        this.longitude = longitude;
        this.latitude = latitude;
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public String getDriverID() {
        return driverID;
    }

    public double getLongitude() {
        return longitude;
    }

    public Double getLatitude() {
        return latitude;
    }
}
