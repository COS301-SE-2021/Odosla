package cs.superleague.shopping.requests;

import java.util.UUID;

public class GetShoppersRequest {
    private UUID storeID;

    public GetShoppersRequest(UUID storeID) {
        this.storeID = storeID;
    }

    public UUID getStoreID() {
        return storeID;
    }

    public void setStoreID(UUID storeID) {
        this.storeID = storeID;
    }
}
