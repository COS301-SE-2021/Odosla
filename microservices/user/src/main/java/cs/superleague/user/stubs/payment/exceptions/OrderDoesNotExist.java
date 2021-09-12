package cs.superleague.user.stubs.payment.exceptions;

public class OrderDoesNotExist extends PaymentException {
    public OrderDoesNotExist(String message) {
        super(message);
    }
}
