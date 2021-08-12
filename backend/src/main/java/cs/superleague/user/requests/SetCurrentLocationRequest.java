package cs.superleague.user.requests;

import java.util.List;

public class SetCurrentLocationRequest {

    private final String driverID;
    private final double longitude;
    private final double latitude;
    private final String address;

    public SetCurrentLocationRequest(String driverID, double longitude, double latitude, String address) {
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

    public double getLatitude() {
        return latitude;
    }
}
