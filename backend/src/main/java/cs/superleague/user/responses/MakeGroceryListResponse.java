package cs.superleague.user.responses;

import cs.superleague.user.dataclass.GroceryList;

import java.util.List;

public class MakeGroceryListResponse {

    private final GroceryList groceryList;
    private final boolean success;
    private final String message;

    public MakeGroceryListResponse(GroceryList groceryList, boolean success, String message) {
        this.groceryList = groceryList;
        this.success = success;
        this.message = message;
    }

    public GroceryList getGroceryList() {
        return groceryList;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
