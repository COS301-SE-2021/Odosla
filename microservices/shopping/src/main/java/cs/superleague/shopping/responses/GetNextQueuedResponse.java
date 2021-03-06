package cs.superleague.shopping.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import cs.superleague.payment.dataclass.Order;

import java.util.Date;
import java.util.List;

public class GetNextQueuedResponse {

    @JsonFormat(pattern = "E MMM dd HH:mm:ss z yyyy")
    private final Date timeStamp;
    private final boolean response;
    private final String message;
    private final List<Order> queueOfOrders;
    private final Order newCurrentOrder;

    public GetNextQueuedResponse(Date timeStamp, boolean response, String message, List<Order> queueOfOrders, Order newCurrentOrder) {
        this.timeStamp = timeStamp;
        this.response = response;
        this.message = message;
        this.queueOfOrders = queueOfOrders;
        this.newCurrentOrder = newCurrentOrder;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public boolean isResponse() {
        return response;
    }

    public String getMessage() {
        return message;
    }

    public List<Order> getQueueOfOrders() {
        return queueOfOrders;
    }

    public Order getNewCurrentOrder() {
        return newCurrentOrder;
    }
}
