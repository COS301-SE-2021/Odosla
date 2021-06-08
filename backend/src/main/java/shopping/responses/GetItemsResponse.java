package shopping.responses;

import shopping.dataclass.Item;

import java.util.Date;
import java.util.List;

public class GetItemsResponse {

    List<Item> items;
    Date timestamp;
    String message;

    public GetItemsResponse(List<Item> items, Date timestamp, String message)
    {
        this.items = items;
        this.timestamp= timestamp;
        this.message= message;
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
}
