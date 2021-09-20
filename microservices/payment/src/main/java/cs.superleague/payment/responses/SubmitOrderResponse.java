package cs.superleague.payment.responses;

import cs.superleague.payment.dataclass.Order;

import java.util.Date;

public class SubmitOrderResponse {

    /** attributes */
    private final boolean success;
    private final Date timestamp;
    private final String message;
    private final Order order;

    /** constructor
     * @param order - order object that haas been created
     * @param success - success of response object, whether successful or not
     * @param timestamp - time that the system sends the response object
     * @param message - message returned after response
     */
    public SubmitOrderResponse(Order order, Boolean success, Date timestamp, String message) {
        this.order = order;
        this.success = success;
        this.timestamp = timestamp;
        this.message = message;
    }


    /** getters */
    public Order getOrder() {
        return order;
    }

    public Boolean getsuccess() {
        return success;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }
}
