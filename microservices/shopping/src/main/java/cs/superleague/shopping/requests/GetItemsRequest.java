package cs.superleague.shopping.requests;

import java.util.UUID;

public class GetItemsRequest {

    UUID storeID;

    public GetItemsRequest(UUID storeID) {
        this.storeID = storeID;
    }

    public void setStoreID(UUID storeID) {
        this.storeID = storeID;
    }

    public UUID getStoreID() {
        return storeID;
    }
}