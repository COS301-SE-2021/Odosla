package cs.superleague.user.exceptions;

public class CustomerDoesNotExistException extends UserException {
    public CustomerDoesNotExistException(String message) {
        super(message);
    }
}
