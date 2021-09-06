package cs.superleague.recommendation.requests;

import java.util.List;

public class GetCartRecommendationRequest {
    private List<String> itemIDs;

    public GetCartRecommendationRequest(List<String> itemIDs) {
        this.itemIDs = itemIDs;
    }

    public List<String> getItemIDs() {
        return itemIDs;
    }

    public void setItemIDs(List<String> itemIDs) {
        this.itemIDs = itemIDs;
    }
}
