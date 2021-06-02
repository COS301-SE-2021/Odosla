package shopping.exceptions;

import java.util.function.Supplier;

public class StoreDoesNotExistException extends ShoppingException {
    public StoreDoesNotExistException(String message){
        super(message);
    }
}
