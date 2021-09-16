package cs.superleague.payment.requests;

import java.util.UUID;

public class CancelOrderRequest {

    /** attributes */
    private UUID orderID;

    /** constructor
     *
     * @param orderID - orderId that needs to be cancelled
     */
    public CancelOrderRequest(UUID orderID){
        this.orderID = orderID;
    }

    /** getters */
    public UUID getOrderID() {
        return orderID;
    }

    public void setOrderID(UUID orderID) {
        this.orderID = orderID;
    }

}
