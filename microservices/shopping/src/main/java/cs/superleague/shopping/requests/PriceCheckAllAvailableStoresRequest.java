package cs.superleague.shopping.requests;

import cs.superleague.payment.dataclass.CartItem;

import java.util.List;

public class PriceCheckAllAvailableStoresRequest {

    List<CartItem> cartItems;

    public PriceCheckAllAvailableStoresRequest() {
    }

    public PriceCheckAllAvailableStoresRequest(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }
}
