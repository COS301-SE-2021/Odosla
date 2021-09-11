package cs.superleague.shopping.requests;

import cs.superleague.shopping.dataclass.Item;

public class SaveItemToRepoRequest {

    private Item item;

    public SaveItemToRepoRequest(Item item){
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
