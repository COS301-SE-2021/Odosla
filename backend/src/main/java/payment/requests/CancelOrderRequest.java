package payment.requests;

import payment.dataclass.Order;
import payment.dataclass.OrderStatus;

import java.util.UUID;

public class CancelOrderRequest {
    private UUID orderID;

    public CancelOrderRequest(){

    }

    public CancelOrderRequest(UUID orderID){
        this.orderID = orderID;
    }

    public UUID getOrderID() {
        return orderID;
    }

    public void setOrderID(UUID orderID) {
        this.orderID = orderID;
    }
}
