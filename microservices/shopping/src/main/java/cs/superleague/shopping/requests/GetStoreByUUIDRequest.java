package cs.superleague.shopping.requests;

import java.util.UUID;

public class GetStoreByUUIDRequest {
    private final UUID StoreID;

    public GetStoreByUUIDRequest(UUID storeID) {
        StoreID = storeID;
    }

    public UUID getStoreID() {
        return StoreID;
    }
}
