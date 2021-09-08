package cs.superleague.shopping.requests;

import java.util.UUID;

public class GetNextQueuedRequest {
    private UUID storeID;

    public GetNextQueuedRequest(UUID storeID) {
        this.storeID = storeID;
    }

    public UUID getStoreID() {
        return storeID;
    }

    public void setStoreID(UUID storeID) {
        this.storeID = storeID;
    }

}
