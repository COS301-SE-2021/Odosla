package cs.superleague.shopping.responses;

import cs.superleague.shopping.dataclass.Item;
import cs.superleague.shopping.dataclass.Item;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class GetItemsResponse {

    List<Item> items;
    Date timestamp;
    String message;
    UUID storeID;

    public GetItemsResponse(UUID storeID, List<Item> items, Date timestamp, String message)
    {
        this.items = items;
        this.timestamp= timestamp;
        this.message= message;
        this.storeID= storeID;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public UUID getStoreID() {
        return storeID;
    }

    public void setStoreID(UUID storeID) {
        this.storeID = storeID;
    }
}
