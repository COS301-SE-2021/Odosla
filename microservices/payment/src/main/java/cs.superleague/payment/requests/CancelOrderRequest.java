package cs.superleague.payment.requests;

import java.util.UUID;

public class CancelOrderRequest {

    /** attributes */
    private UUID orderID;
    private UUID userID;

    /** constructor
     *
     * @param orderID - orderId that needs to be cancelled
     * @param userID - id of the user who placed the order
     */
    public CancelOrderRequest(UUID orderID, UUID userID){
        this.userID = userID;
        this.orderID = orderID;
    }

    /** getters */
    public UUID getOrderID() {
        return orderID;
    }

    public UUID getUserID() {
        return userID;
    }

    public void setOrderID(UUID orderID) {
        this.orderID = orderID;
    }

    public void setUserID(UUID userID) {
        this.userID = userID;
    }
}
