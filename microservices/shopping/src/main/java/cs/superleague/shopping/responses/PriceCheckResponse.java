package cs.superleague.shopping.responses;

import cs.superleague.payment.dataclass.CartItem;
import cs.superleague.shopping.dataclass.Item;

import java.util.List;

public class PriceCheckResponse {

    List<CartItem> cartItems;
    String message;
    Boolean success;

    public PriceCheckResponse() {
    }

    public PriceCheckResponse(List<CartItem> items, String message, Boolean success) {
        this.cartItems = items;
        this.message = message;
        this.success = success;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public String getMessage() {
        return message;
    }

    public Boolean getSuccess() {
        return success;
    }
}
