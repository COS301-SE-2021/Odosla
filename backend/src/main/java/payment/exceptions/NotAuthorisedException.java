package payment.exceptions;

public class NotAuthorisedException extends PaymentException{
    public NotAuthorisedException(String message){
        super(message);
    }
}