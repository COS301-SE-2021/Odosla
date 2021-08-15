package cs.superleague.shopping.requests;

import java.util.UUID;

public class GetNextQueuedRequest {
    private UUID storeID;
    private String jwtToken;

    public GetNextQueuedRequest(UUID storeID, String jwtToken) {
        this.storeID = storeID;
        this.jwtToken=jwtToken;
    }

    public UUID getStoreID() {
        return storeID;
    }

    public void setStoreID(UUID storeID) {
        this.storeID = storeID;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}
