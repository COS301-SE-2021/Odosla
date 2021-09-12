package cs.superleague.delivery.stub.responses;
import cs.superleague.delivery.stub.dataclass.Order;

import java.util.Date;

public class GetOrderResponse {
    /** attributes */
    private final Order order;
    private final boolean success;
    private final Date timestamp;
    private final String message;

    /** CONSTRUCTOR
     * @param order - the order object requested
     * @param success - success status of the operation
     * @param timestamp - time that the system sends the response object
     * @param message - message returned after response
     */
    public GetOrderResponse(Order order, boolean success, Date timestamp, String message) {
        this.order = order;
        this.success = success;
        this.timestamp = timestamp;
        this.message = message;
    }

    public Order getOrder() {
        return order;
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
