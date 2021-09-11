package cs.superleague.payment.responses;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.stubs.Item;

import java.util.Date;
import java.util.List;

public class GetItemsResponse {
    /** attributes */
    private final List<Item> itemList;
    private final boolean success;
    private final Date timestamp;
    private final String message;

    /** CONSTRUCTOR
     * @param itemList - the order items
     * @param success - success status of the operation
     * @param timestamp - time that the system sends the response object
     * @param message - message returned after response
     */
    public GetItemsResponse(List itemList, boolean success, Date timestamp, String message) {
        this.itemList = itemList;
        this.success = success;
        this.timestamp = timestamp;
        this.message = message;
    }

    public List<Item> getItems() {
        return itemList;
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
