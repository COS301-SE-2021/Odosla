package cs.superleague.user.responses;

import cs.superleague.user.dataclass.GroceryList;
import cs.superleague.user.dataclass.User;

import java.util.Date;
import java.util.List;

public class GetGroceryListsResponse {

    private final List<GroceryList> groceryLists;
    private final boolean success;
    private final Date timestamp;
    private final String message;

    public GetGroceryListsResponse(List<GroceryList> groceryLists, boolean success, Date timestamp, String message) {
        this.groceryLists = groceryLists;
        this.success = success;
        this.timestamp = timestamp;
        this.message = message;
    }

    public List<GroceryList> getGroceryLists() {
        return groceryLists;
    }

    public boolean isSuccess() {
        return success;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }
}
