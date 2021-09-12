package cs.superleague.user.responses;

import cs.superleague.user.stubs.shopping.dataclass.Item;

import java.util.List;

public class GetShoppingCartResponse {

    private final List<Item> shoppingCart;
    private final String message;
    private final boolean success;

    public GetShoppingCartResponse(List<Item> shoppingCart, String message, boolean success) {
        this.shoppingCart = shoppingCart;
        this.message = message;
        this.success = success;
    }

    public List<Item> getShoppingCart() {
        return shoppingCart;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
}
