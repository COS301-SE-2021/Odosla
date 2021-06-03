package payment.responses;

import payment.dataclass.Order;
import payment.dataclass.OrderStatus;

import java.util.Date;
import java.util.UUID;

public class SubmitOrderResponse {

    /** attributes */
    private final Order order;
    private final boolean status;
    private final Date timestamp;
    private final String message;

    /** constructor
     * @param order - order object that haas been created
     * @param status - status of response object, whether successful or not
     * @param timestamp - time that the system sends the response object
     * @param message - message returned after response
     */
    public SubmitOrderResponse(Order order, Boolean status, Date timestamp, String message) {
        this.order = order;
        this.status = status;
        this.timestamp = timestamp;
        this.message = message;
    }


    public Order getOrder() {
        return order;
    }

    public Boolean getStatus() {
        return status;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }
}
