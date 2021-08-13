package cs.superleague.payment.requests;

import java.util.UUID;

public class GetItemsRequest {
    /* Attributes */
    private String orderID;

    public GetItemsRequest(String orderID) {
        this.orderID = orderID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }
}
