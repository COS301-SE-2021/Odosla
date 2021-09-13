package cs.superleague.importer.stub.shopping.requests;

import cs.superleague.importer.stub.shopping.dataclass.Item;

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
