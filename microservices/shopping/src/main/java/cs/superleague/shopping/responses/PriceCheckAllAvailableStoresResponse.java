package cs.superleague.shopping.responses;

import cs.superleague.payment.dataclass.CartItem;

import java.util.List;

public class PriceCheckAllAvailableStoresResponse {

    List<CartItem> cartItems;
    String message;
    Boolean success;

    public PriceCheckAllAvailableStoresResponse() {
    }

    public PriceCheckAllAvailableStoresResponse(List<CartItem> items, String message, Boolean success) {
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
