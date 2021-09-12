package cs.superleague.importer.stub.shopping.responses;

import cs.superleague.importer.stub.shopping.dataclass.Item;

import java.util.List;

public class GetAlItemsResponse {

    private final List<Item> items;

    public GetAlItemsResponse() {
        this.items = null;
    }

    public GetAlItemsResponse(List<Item> items) {
        this.items = items;
    }

    public List<Item> getItems() {
        return items;
    }
}
