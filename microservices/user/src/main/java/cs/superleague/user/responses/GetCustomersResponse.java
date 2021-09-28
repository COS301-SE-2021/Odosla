package cs.superleague.user.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import cs.superleague.user.dataclass.Customer;
import cs.superleague.user.dataclass.User;

import java.util.Date;
import java.util.List;

public class GetCustomersResponse {

    private final List<Customer> users;
    private final boolean success;
    private final String message;
    @JsonFormat(pattern = "E MMM dd HH:mm:ss z yyyy")
    private final Date timestamp;

    public GetCustomersResponse(List<Customer> users, boolean success, String message, Date timestamp) {
        this.users = users;
        this.success = success;
        this.message = message;
        this.timestamp = timestamp;
    }

    public List<Customer> getCustomers() {
        return users;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
