package cs.superleague.shopping.exceptions;

public class ItemDoesNotExistException extends ShoppingException{
    public ItemDoesNotExistException(String message) {
        super(message);
    }
}
