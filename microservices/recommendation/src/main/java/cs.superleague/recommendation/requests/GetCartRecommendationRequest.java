package cs.superleague.recommendation.requests;

import java.util.List;
import java.util.UUID;

public class GetCartRecommendationRequest {
    private List<String> itemIDs;
    private UUID storeOneID;
    private UUID storeTwoID;
    private UUID storeThreeID;

    public GetCartRecommendationRequest(List<String> itemIDs, UUID storeOneID, UUID storeTwoID, UUID storeThreeID) {
        this.itemIDs = itemIDs;
        this.storeOneID = storeOneID;
        this.storeTwoID = storeTwoID;
        this.storeThreeID = storeThreeID;
    }

    public UUID getStoreOneID() {
        return storeOneID;
    }

    public void setStoreOneID(UUID storeOneID) {
        this.storeOneID = storeOneID;
    }

    public UUID getStoreTwoID() {
        return storeTwoID;
    }

    public void setStoreTwoID(UUID storeTwoID) {
        this.storeTwoID = storeTwoID;
    }

    public UUID getStoreThreeID() {
        return storeThreeID;
    }

    public void setStoreThreeID(UUID storeThreeID) {
        this.storeThreeID = storeThreeID;
    }

    public List<String> getItemIDs() {
        return itemIDs;
    }

    public void setItemIDs(List<String> itemIDs) {
        this.itemIDs = itemIDs;
    }
}
