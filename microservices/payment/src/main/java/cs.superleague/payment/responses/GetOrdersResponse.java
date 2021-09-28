package cs.superleague.payment.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import cs.superleague.payment.dataclass.Order;

import java.util.Date;
import java.util.List;

public class GetOrdersResponse {

    private final List<Order> orders;
    private final boolean success;
    private final String message;
    @JsonFormat(pattern = "E MMM dd HH:mm:ss z yyyy")
    private final Date timestamp;

    public GetOrdersResponse() {
        this.orders = null;
        this.success = false;
        this.message = null;
        this.timestamp = null;
    }

    public GetOrdersResponse(List<Order> orders, boolean success, String message, Date timestamp) {
        this.orders = orders;
        this.success = success;
        this.message = message;
        this.timestamp = timestamp;
    }

    public List<Order> getOrders() {
        return orders;
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
