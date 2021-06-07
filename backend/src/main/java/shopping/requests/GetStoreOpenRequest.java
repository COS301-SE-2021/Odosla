package shopping.requests;

import java.util.UUID;

public class GetStoreOpenRequest {
    UUID storeID;

    public GetStoreOpenRequest(UUID storeID)
    {
        this.storeID=storeID;
    }

    public void setStoreID(UUID storeID) {
        this.storeID = storeID;
    }

    public UUID getStoreID() {
        return storeID;
    }
}
