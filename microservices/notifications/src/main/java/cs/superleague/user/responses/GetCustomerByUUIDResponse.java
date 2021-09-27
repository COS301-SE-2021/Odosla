package cs.superleague.user.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import cs.superleague.user.dataclass.Customer;

import java.util.Date;

public class GetCustomerByUUIDResponse {

    private final Customer customer;
    @JsonFormat(pattern = "E MMM dd HH:mm:ss z yyyy")
    private final Date timestamp;
    private final String message;

    public GetCustomerByUUIDResponse() {
        this.customer = null;
        this.timestamp = null;
        this.message = null;
    }

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
