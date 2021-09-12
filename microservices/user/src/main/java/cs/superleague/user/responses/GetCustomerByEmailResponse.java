package cs.superleague.user.responses;

import cs.superleague.user.dataclass.Customer;

public class GetCustomerByEmailResponse {

    final Customer customer;
    private final boolean success;
    public GetCustomerByEmailResponse(Customer customer, boolean success) {
        this.customer = customer;
        this.success = success;
    }

    public Customer getCustomer() {
        return customer;
    }

    public boolean isSuccess() {
        return success;
    }
}
