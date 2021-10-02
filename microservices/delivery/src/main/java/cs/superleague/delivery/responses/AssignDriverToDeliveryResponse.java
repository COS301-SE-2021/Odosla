package cs.superleague.delivery.responses;

import cs.superleague.payment.dataclass.GeoPoint;

import java.util.List;
import java.util.UUID;

public class AssignDriverToDeliveryResponse {
    private boolean isAssigned;
    private String message;
    private List<GeoPoint> pickUpLocations;
    private GeoPoint dropOffLocation;
    private UUID driverID;

    public AssignDriverToDeliveryResponse(boolean isAssigned, String message, List<GeoPoint> pickUpLocations, GeoPoint dropOffLocation, UUID driverID) {
        this.isAssigned = isAssigned;
        this.message = message;
        this.pickUpLocations = pickUpLocations;
        this.dropOffLocation = dropOffLocation;
        this.driverID= driverID;
    }

    public List<GeoPoint> getPickUpLocations() {
        return pickUpLocations;
    }

    public void setPickUpLocations(List<GeoPoint> pickUpLocations) {
        this.pickUpLocations = pickUpLocations;
    }

    public GeoPoint getDropOffLocation() {
        return dropOffLocation;
    }

    public void setDropOffLocation(GeoPoint dropOffLocation) {
        this.dropOffLocation = dropOffLocation;
    }

    public boolean isAssigned() {
        return isAssigned;
    }

    public void setAssigned(boolean assigned) {
        isAssigned = assigned;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UUID getDriverID() {
        return driverID;
    }

    public void setDriverID(UUID driverID) {
        this.driverID = driverID;
    }
}
