package cs.superleague.payment.requests;

import cs.superleague.payment.dataclass.CartItem;

import java.util.List;

public class FixOrderProblemRequest {

    private CartItem cartItem;
    private List<CartItem> cartItems;

    public FixOrderProblemRequest() {
    }

    public FixOrderProblemRequest(CartItem cartItem, List<CartItem> cartItems) {
        this.cartItem = cartItem;
        this.cartItems = cartItems;
    }

    public CartItem getCartItem() {
        return cartItem;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }
}
