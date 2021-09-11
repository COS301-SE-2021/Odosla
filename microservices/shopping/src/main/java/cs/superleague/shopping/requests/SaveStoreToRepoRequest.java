package cs.superleague.shopping.requests;

import java.util.UUID;

public class SaveStoreToRepoRequest {

    private UUID storeID;

    public SaveStoreToRepoRequest(UUID storeID)
    {
        this.storeID=storeID;
    }

    public UUID getStoreID() {
        return storeID;
    }

    public void setStoreID(UUID storeID) {
        this.storeID = storeID;
    }


}
