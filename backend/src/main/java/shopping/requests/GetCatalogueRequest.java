package shopping.requests;

import java.util.UUID;

public class GetCatalogueRequest {

    UUID storeID;

    public GetCatalogueRequest(UUID storeID)
    {
        this.storeID= storeID;
    }

    public void setStoreID(UUID storeID) {
        this.storeID = storeID;
    }

    public UUID getStoreID() {
        return storeID;
    }
}
