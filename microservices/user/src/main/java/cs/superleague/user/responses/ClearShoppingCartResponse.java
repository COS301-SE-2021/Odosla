package cs.superleague.user.responses;

import cs.superleague.shopping.dataclass.Item;

import java.util.Date;
import java.util.List;

public class ClearShoppingCartResponse {

    private final List<Item> items;
    private final String message;
    private final boolean success;
    private final Date timestamp;

    public List<Item> getItems() {
        return items;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public ClearShoppingCartResponse(List<Item> items, String message, boolean success, Date timestamp) {
        this.items = items;
        this.message = message;
        this.success = success;
        this.timestamp = timestamp;
    }
}
