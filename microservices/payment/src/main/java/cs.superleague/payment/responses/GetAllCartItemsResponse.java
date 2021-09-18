package cs.superleague.payment.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import cs.superleague.payment.dataclass.CartItem;

import java.util.Date;
import java.util.List;

public class GetAllCartItemsResponse {

    private List<CartItem> cartItems;
    @JsonFormat(pattern="E MMM dd HH:mm:ss z yyyy")
    private Date timestamp;
    private String message;

    public GetAllCartItemsResponse(List<CartItem> cartItems, Date timestamp, String message)
    {
        this.cartItems = cartItems;
        this.timestamp= timestamp;
        this.message= message;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
