package cs.superleague.payment.responses;

import cs.superleague.payment.dataclass.Order;

import java.util.Date;
import java.util.UUID;

public class SubmitOrderResponse {

    /**
     * attributes
     */
    private final boolean success;
    private final Date timestamp;
    private final String message;
    private final Order orderOne;
    private final Order orderTwo;
    private final Order orderThree;
    private final UUID deliveryID;
    /**
     * constructor
     *
     * @param orderOne  - order object that haas been created
     * @param orderTwo  - order object that haas been created
     * @param orderThree- order object that haas been created
     * @param success   - success of response object, whether successful or not
     * @param timestamp - time that the system sends the response object
     * @param message   - message returned after response
     */
    public SubmitOrderResponse(Order orderOne, Order orderTwo, Order orderThree, Boolean success, Date timestamp, String message, UUID deliveryID) {
        this.orderOne = orderOne;
        this.orderTwo = orderTwo;
        this.orderThree = orderThree;
        this.success = success;
        this.timestamp = timestamp;
        this.message = message;
        this.deliveryID = deliveryID;
    }


    /**
     * getters
     */
    public Order getOrderOne() {
        return orderOne;
    }

    public Order getOrderTwo() {
        return orderTwo;
    }

    public Order getOrderThree() {
        return orderThree;
    }

    public Boolean isSuccess() {
        return success;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public UUID getDeliveryID() {
        return deliveryID;
    }
}
