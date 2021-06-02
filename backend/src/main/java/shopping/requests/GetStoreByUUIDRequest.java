package shopping.requests;

import java.util.UUID;

public class GetStoreByUUIDRequest {
    private UUID StoreID;

    public GetStoreByUUIDRequest(UUID storeID) {
        StoreID = storeID;
    }

    public UUID getStoreID() {
        return StoreID;
    }
}
