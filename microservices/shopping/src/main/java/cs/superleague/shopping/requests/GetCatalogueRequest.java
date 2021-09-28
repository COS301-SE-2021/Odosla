package cs.superleague.shopping.requests;

import java.util.UUID;

public class GetCatalogueRequest {

    UUID storeID;

    public GetCatalogueRequest(UUID storeID) {
        this.storeID = storeID;
    }

    /*
     * setStoreID accepts a UUID object which is used to set the storeID
     * */
    public void setStoreID(UUID storeID) {
        this.storeID = storeID;
    }

    /*
     * getStoreID returns the storeID variable
     * */
    public UUID getStoreID() {
        return storeID;
    }
}
