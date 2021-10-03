package cs.superleague.shopping.requests;

import cs.superleague.payment.dataclass.GeoPoint;

import java.util.UUID;

public class CalculateOverallDistanceRequest {

    private UUID store1ID;
    private UUID store2ID;
    private UUID store3ID;
    private GeoPoint customerLocation;

    public CalculateOverallDistanceRequest() {
    }

    public CalculateOverallDistanceRequest(UUID store1ID, UUID store2ID, UUID store3ID, GeoPoint customerLocation) {
        this.store1ID = store1ID;
        this.store2ID = store2ID;
        this.store3ID = store3ID;
        this.customerLocation = customerLocation;
    }

    public UUID getStore1ID() {
        return store1ID;
    }

    public UUID getStore2ID() {
        return store2ID;
    }

    public UUID getStore3ID() {
        return store3ID;
    }

    public GeoPoint getCustomerLocation() {
        return customerLocation;
    }
}
