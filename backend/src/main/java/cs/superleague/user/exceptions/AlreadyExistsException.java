package cs.superleague.user.exceptions;

public class AlreadyExistsException extends UserException {
    public AlreadyExistsException(String message){
        super(message);
    }
}
