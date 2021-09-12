package cs.superleague.user.responses;

import cs.superleague.user.stubs.shopping.dataclass.Item;

import java.util.Date;
import java.util.List;

public class RemoveFromCartResponse {

    private final List<Item> cart;
    private final String message;
    private final boolean success;
    private final Date timestamp;

    public RemoveFromCartResponse(List<Item> cart, String message, boolean success, Date timestamp) {
        this.cart = cart;
        this.message = message;
        this.success = success;
        this.timestamp = timestamp;
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

    public List<Item> getCart() {
        return cart;
    }
}
