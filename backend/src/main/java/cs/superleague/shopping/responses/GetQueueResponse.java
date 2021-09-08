package cs.superleague.shopping.responses;

import cs.superleague.payment.dataclass.Order;

import java.util.List;

public class GetQueueResponse {

    private boolean response;
    private String message;
    private List<Order> queueOfOrders;


    public GetQueueResponse(boolean response, String message, List<Order> queueOfOrders) {

        this.response = response;
        this.message = message;
        this.queueOfOrders=queueOfOrders;

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
