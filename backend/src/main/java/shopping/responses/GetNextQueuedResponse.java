package shopping.responses;

import payment.dataclass.Order;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class GetNextQueuedResponse {

    private final Date timeStamp;
    private final boolean response;
    private final String message;
    private final List<Order> queueOfOrders;
    private final Order newCurrentOrder;

    public GetNextQueuedResponse(Date timeStamp, boolean response, String message, List<Order> queueOfOrders,Order newCurrentOrder) {
        this.timeStamp = timeStamp;
        this.response = response;
        this.message = message;
        this.queueOfOrders=queueOfOrders;
        this.newCurrentOrder=newCurrentOrder;
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
