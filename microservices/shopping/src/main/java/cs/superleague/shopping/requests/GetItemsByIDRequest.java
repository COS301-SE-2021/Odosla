package cs.superleague.shopping.requests;

import java.util.List;

public class GetItemsByIDRequest {

    private List<String> itemIDs;

    public GetItemsByIDRequest(List<String> itemIDs) {
        this.itemIDs = itemIDs;
    }

    public List<String> getItemIDs() {
        return itemIDs;
    }

    public void setItemIDs(List<String> itemIDs) {
        this.itemIDs = itemIDs;
    }

}
