package cs.superleague.shopping.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import cs.superleague.shopping.dataclass.Item;

import java.util.Date;
import java.util.List;

public class GetAllItemsResponse {

    private List<Item> items;
    @JsonFormat(pattern="E MMM dd HH:mm:ss z yyyy")
    private Date timestamp;
    private String message;

    public GetAllItemsResponse(List<Item> items, Date timestamp, String message)
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
