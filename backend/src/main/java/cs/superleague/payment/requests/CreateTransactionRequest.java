package cs.superleague.payment.requests;

import java.util.UUID;

public class CreateTransactionRequest {
    private UUID orderID;
    private String transactionAddress;
    //private PayOption card;


    public CreateTransactionRequest(UUID orderID, String transactionAddress) {
        this.orderID = orderID;
        this.transactionAddress = transactionAddress;
    }

    public UUID getOrderID() {
        return orderID;
    }

    public void setOrderID(UUID orderID) {
        this.orderID = orderID;
    }

    public String getTransactionAddress() {
        return transactionAddress;
    }

    public void setTransactionAddress(String transactionAddress) {
        this.transactionAddress = transactionAddress;
    }
}
