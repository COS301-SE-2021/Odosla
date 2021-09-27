package cs.superleague.shopping.responses;

import cs.superleague.payment.dataclass.Order;

import java.util.List;

public class GetQueueResponse {

    private final boolean response;
    private final String message;
    private final List<Order> queueOfOrders;


    public GetQueueResponse(boolean response, String message, List<Order> queueOfOrders) {

        this.response = response;
        this.message = message;
        this.queueOfOrders = queueOfOrders;

    }

    public boolean getResponse() {
        return response;
    }

    public String getMessage() {
        return message;
    }

    public List<Order> getQueueOfOrders() {
        return queueOfOrders;
    }

}
