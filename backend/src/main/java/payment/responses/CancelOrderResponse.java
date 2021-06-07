package payment.responses;

import payment.dataclass.Order;

import java.util.Date;
import java.util.List;

public class CancelOrderResponse {
    /** attributes */
    private List<Order> orders;
    private final boolean success;
    private final Date timestamp;
    private final String message;


    /** constructor
     *
     * @param success //success of the response object - true/false
     * @param timestamp // time that response was created
     * @param message //message corresponding to response object
     * @param orders //list of orders in database
     */
    public CancelOrderResponse(boolean success, Date timestamp, String message,List<Order> orders) {
        this.orders = orders;
        this.success = success;
        this.timestamp = timestamp;
        this.message = message;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public String getMessage() {
        return message;
    }
}
