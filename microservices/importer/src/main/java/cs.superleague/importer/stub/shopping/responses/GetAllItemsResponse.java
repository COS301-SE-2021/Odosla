package cs.superleague.importer.stub.shopping.responses;

import cs.superleague.importer.stub.shopping.dataclass.Item;

import java.util.List;

public class GetAllItemsResponse {

    private final List<Item> items;

    public GetAllItemsResponse() {
        this.items = null;
    }

    public GetAllItemsResponse(List<Item> items) {
        this.items = items;
    }

    public List<Item> getItems() {
        return items;
    }
}
