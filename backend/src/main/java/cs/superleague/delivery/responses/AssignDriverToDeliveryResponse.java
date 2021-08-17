package cs.superleague.delivery.responses;

import cs.superleague.payment.dataclass.GeoPoint;

public class AssignDriverToDeliveryResponse {
    private boolean isAssigned;
    private String message;
    private GeoPoint pickUpLocation;
    private GeoPoint dropOffLocation;

    public AssignDriverToDeliveryResponse(boolean isAssigned, String message, GeoPoint pickUpLocation, GeoPoint dropOffLocation) {
        this.isAssigned = isAssigned;
        this.message = message;
        this.pickUpLocation = pickUpLocation;
        this.dropOffLocation = dropOffLocation;
    }

    public GeoPoint getPickUpLocation() {
        return pickUpLocation;
    }

    public void setPickUpLocation(GeoPoint pickUpLocation) {
        this.pickUpLocation = pickUpLocation;
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
}
