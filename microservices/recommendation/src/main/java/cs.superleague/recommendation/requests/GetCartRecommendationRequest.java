package cs.superleague.recommendation.requests;

import java.util.List;
import java.util.UUID;

public class GetCartRecommendationRequest {
    private List<String> itemIDs;
    private UUID storeID;

    public GetCartRecommendationRequest(List<String> itemIDs, UUID storeID) {
        this.itemIDs = itemIDs;
        this.storeID = storeID;
    }

    public UUID getStoreID() {
        return storeID;
    }

    public void setStoreID(UUID storeID) {
        this.storeID = storeID;
    }

    public List<String> getItemIDs() {
        return itemIDs;
    }

    public void setItemIDs(List<String> itemIDs) {
        this.itemIDs = itemIDs;
    }
}
