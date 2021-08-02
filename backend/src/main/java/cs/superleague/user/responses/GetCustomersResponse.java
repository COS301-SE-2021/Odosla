package cs.superleague.user.responses;

import java.util.Date;
import java.util.List;

public class GetCustomersResponse {

    private final List customerList;
    private final boolean success;
    private final Date timestamp;
    private final String message;


    public GetCustomersResponse(List customerList, boolean success, Date timestamp, String message) {
        this.customerList = customerList;
        this.success = success;
        this.timestamp = timestamp;
        this.message = message;
    }

    public List getCustomerList() {
        return customerList;
    }

    public boolean isSuccess() {
        return success;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }
}