package cs.superleague.shopping.requests;

import java.util.List;

public class GetItemsByUUIDSRequest {

    private List<String> itemIDs;

    public GetItemsByUUIDSRequest(List<String> itemIDs)
    {
        this.itemIDs = itemIDs;
    }

    public List<String> getItemIDs() {
        return itemIDs;
    }

    public void setItemIDs(List<String> itemIDs) {
        this.itemIDs = itemIDs;
    }

}
