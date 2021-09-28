package cs.superleague.payment.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import cs.superleague.payment.dataclass.Order;

import java.util.Date;

public class GetOrderByUUIDResponse {

    private final Order order;
    @JsonFormat(pattern = "E MMM dd HH:mm:ss z yyyy")
    private final Date timestamp;
    private final String message;

    public GetOrderByUUIDResponse() {
        this.order = null;
        this.message = null;
        this.timestamp = null;
    }

    public GetOrderByUUIDResponse(Order order, Date timestamp, String message) {
        this.order = order;
        this.timestamp = timestamp;
        this.message = message;
    }

    public Order getOrder() {
        return order;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }
}
