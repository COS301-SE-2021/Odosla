package cs.superleague.payment.responses;

import cs.superleague.payment.dataclass.Order;

import java.util.Date;
import java.util.List;

public class GetOrdersResponse {

    private final List<Order> orders;
    private final boolean success;
    private final String message;
    private final Date timestamp;

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
