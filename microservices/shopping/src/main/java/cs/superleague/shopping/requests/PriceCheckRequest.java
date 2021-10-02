package cs.superleague.shopping.requests;

import cs.superleague.payment.dataclass.CartItem;

import java.util.List;

public class PriceCheckRequest {

    List<CartItem> cartItems;

    public PriceCheckRequest() {
    }

    public PriceCheckRequest(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }
}
