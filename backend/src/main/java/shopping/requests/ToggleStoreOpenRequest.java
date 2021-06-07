package shopping.requests;

import java.util.UUID;

public class ToggleStoreOpenRequest {

    UUID storeID;

    public ToggleStoreOpenRequest(UUID storeID)
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
