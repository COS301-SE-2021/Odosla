package cs.superleague.payment.exceptions;

public class OrderDoesNotExist extends PaymentException {
    public OrderDoesNotExist(String message) {
        super(message);
    }
}
