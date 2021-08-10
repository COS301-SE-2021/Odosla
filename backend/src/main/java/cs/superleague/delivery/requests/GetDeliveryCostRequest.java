package cs.superleague.delivery.requests;

import cs.superleague.payment.dataclass.GeoPoint;

public class GetDeliveryCostRequest {
    private GeoPoint dropOffLocation;
    private GeoPoint pickUpLocation;

    public GetDeliveryCostRequest(GeoPoint dropOffLocation, GeoPoint pickUpLocation) {
        this.dropOffLocation = dropOffLocation;
        this.pickUpLocation = pickUpLocation;
    }

    public GeoPoint getDropOffLocation() {
        return dropOffLocation;
    }

    public void setDropOffLocation(GeoPoint dropOffLocation) {
        this.dropOffLocation = dropOffLocation;
    }

    public GeoPoint getPickUpLocation() {
        return pickUpLocation;
    }

    public void setPickUpLocation(GeoPoint pickUpLocation) {
        this.pickUpLocation = pickUpLocation;
    }
}
