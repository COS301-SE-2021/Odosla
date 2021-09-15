package cs.superleague.shopping.requests;

import cs.superleague.shopping.dataclass.Item;

import java.io.Serializable;

public class SaveItemToRepoRequest implements Serializable {

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
