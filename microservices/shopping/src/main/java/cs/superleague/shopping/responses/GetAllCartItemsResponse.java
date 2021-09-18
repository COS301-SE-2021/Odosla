package cs.superleague.shopping.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import cs.superleague.payment.dataclass.CartItem;

import java.util.Date;
import java.util.List;

public class GetAllCartItemsResponse {

    private List<CartItem> items;
    @JsonFormat(pattern="E MMM dd HH:mm:ss z yyyy")
    private Date timestamp;
    private String message;

    public GetAllCartItemsResponse(List<CartItem> items, Date timestamp, String message)
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

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
