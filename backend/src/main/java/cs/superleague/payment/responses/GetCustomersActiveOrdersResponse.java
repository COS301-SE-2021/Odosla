package cs.superleague.payment.responses;

import java.util.UUID;

public class GetCustomersActiveOrdersResponse {

    private UUID orderID;
    private boolean hasActiveOrder;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UUID getOrderID() {
        return orderID;
    }

    public void setOrderID(UUID orderID) {
        this.orderID = orderID;
    }

    public boolean isHasActiveOrder() {
        return hasActiveOrder;
    }

    public void setHasActiveOrder(boolean hasActiveOrder) {
        this.hasActiveOrder = hasActiveOrder;
    }

    public GetCustomersActiveOrdersResponse(UUID orderID, boolean hasActiveOrder, String message) {
        this.orderID = orderID;
        this.hasActiveOrder = hasActiveOrder;
        this.message = message;
    }
}
