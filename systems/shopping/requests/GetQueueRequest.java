package cs.superleague.shopping.requests;

import java.util.UUID;

public class GetQueueRequest {

    private UUID storeID;

    public GetQueueRequest(UUID storeID) {
        this.storeID = storeID;
    }

    public UUID getStoreID() {
        return storeID;
    }

    public void setStoreID(UUID storeID) {
        this.storeID = storeID;
    }
}
