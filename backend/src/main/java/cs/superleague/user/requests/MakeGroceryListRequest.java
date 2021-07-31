package cs.superleague.user.requests;

import cs.superleague.shopping.dataclass.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MakeGroceryListRequest {

    private final UUID userID;
    private final List<Item> items;
    private final String name;


    public MakeGroceryListRequest(UUID userID, List<Item> items, String name) { //if multiple items are parsed in
        this.userID = userID;
        this.items = items;
        this.name = name;
    }

    public MakeGroceryListRequest(UUID userID, Item item, String message){ // if one item is passed in
        List<Item> items = new ArrayList();
        items.add(item);

        this.items = items;
        this.userID = userID;
        this.name = message;
    }

    public UUID getUserID() {
        return userID;
    }

    public List<Item> getItems() {
        return items;
    }

    public String getName() {
        return name;
    }
}
