package cs.superleague.user.requests;

import cs.superleague.shopping.dataclass.Item;
import cs.superleague.user.dataclass.GroceryList;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MakeGroceryListRequest {

    private final UUID userID;
    private final List<GroceryList> groceryList;


    public MakeGroceryListRequest(UUID userID, List<GroceryList> groceryList) { //if multiple items are parsed in
        this.userID = userID;
        this.groceryList = groceryList;
    }

    public MakeGroceryListRequest(UUID userID, GroceryList item){ // if one item is passed in
        List<GroceryList> groceryList = new ArrayList();
        groceryList.add(item);

        this.groceryList = groceryList;
        this.userID = userID;
    }

    public UUID getUserID() {
        return userID;
    }

    public List<GroceryList> getGroceryList() {
        return groceryList;
    }
}
