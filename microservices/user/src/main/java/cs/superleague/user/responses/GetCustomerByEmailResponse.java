package cs.superleague.user.responses;

import cs.superleague.user.dataclass.Customer;

public class GetCustomerByEmailResponse {
    final Customer customer;

    public GetCustomerByEmailResponse(Customer customer) {
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }
}
