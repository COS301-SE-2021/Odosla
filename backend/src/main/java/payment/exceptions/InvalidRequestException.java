package payment.exceptions;

public class InvalidRequestException extends PaymentException{
    public InvalidRequestException(String message){
        super(message);
    }
}