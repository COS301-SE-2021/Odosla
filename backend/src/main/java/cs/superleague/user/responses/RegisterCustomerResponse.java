package cs.superleague.user.responses;

import cs.superleague.user.dataclass.Customer;

public class RegisterCustomerResponse {
    private final Customer customer;
    private final boolean success;
    private final String message;

    public RegisterCustomerResponse(Customer customer, boolean success, String message) {
        this.customer = customer;
        this.success = success;
        this.message = message;
    }

    public Customer getCustomer() {
        return customer;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
