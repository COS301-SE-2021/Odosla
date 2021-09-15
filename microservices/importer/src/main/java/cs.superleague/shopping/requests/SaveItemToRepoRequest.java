package cs.superleague.shopping.requests;

import cs.superleague.shopping.dataclass.Item;

public class SaveItemToRepoRequest {

    private final Item item;

    public SaveItemToRepoRequest(){
        item = null;
    }

    public SaveItemToRepoRequest(Item item) {
        this.item = item;
    }

    public Item getItem() {
        return item;
    }
}
