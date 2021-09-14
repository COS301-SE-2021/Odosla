package cs.superleague.delivery.stubs.user.responses;

import cs.superleague.delivery.stubs.user.dataclass.Customer;

import java.util.Date;

public class GetCustomerByUUIDResponse {

    private final Customer customer;
    private final Date timestamp;
    private final String message;

    public GetCustomerByUUIDResponse(Customer customer, Date timestamp, String message) {
        this.customer = customer;
        this.timestamp = timestamp;
        this.message = message;
    }


    public Customer getCustomer() {
        return customer;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }
}
