package cs.superleague.shopping.requests;

import cs.superleague.payment.dataclass.GeoPoint;

import java.util.UUID;

public class GetCloseEnoughStoresRequest {
    private UUID storeID;
    private GeoPoint customerLocation;

    public GetCloseEnoughStoresRequest(UUID storeID, GeoPoint customerLocation) {
        this.storeID = storeID;
        this.customerLocation = customerLocation;
    }

    public GeoPoint getCustomerLocation() {
        return customerLocation;
    }

    public void setCustomerLocation(GeoPoint customerLocation) {
        this.customerLocation = customerLocation;
    }

    public UUID getStoreID() {
        return storeID;
    }

    public void setStoreID(UUID storeID) {
        this.storeID = storeID;
    }
}
