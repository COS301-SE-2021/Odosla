package cs.superleague.user.requests;

import cs.superleague.user.dataclass.Customer;

import java.io.Serializable;

public class SaveCustomerToRepoRequest implements Serializable {
    private Customer customer;

    public SaveCustomerToRepoRequest(Customer customer) {
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
