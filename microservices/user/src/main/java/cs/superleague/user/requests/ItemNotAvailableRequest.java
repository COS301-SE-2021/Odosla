package cs.superleague.user.requests;

import java.util.UUID;

public class ItemNotAvailableRequest {
    private UUID orderID;
    private String currentProductBarcode;
    private String alternativeProductBarcode;

    public ItemNotAvailableRequest(UUID orderID, String currentProductBarcode, String alternativeProductBarcode) {
        this.orderID = orderID;
        this.currentProductBarcode = currentProductBarcode;
        this.alternativeProductBarcode = alternativeProductBarcode;
    }

    public UUID getOrderID() {
        return orderID;
    }

    public void setOrderID(UUID orderID) {
        this.orderID = orderID;
    }

    public String getCurrentProductBarcode() {
        return currentProductBarcode;
    }

    public void setCurrentProductBarcode(String currentProductBarcode) {
        this.currentProductBarcode = currentProductBarcode;
    }

    public String getAlternativeProductBarcode() {
        return alternativeProductBarcode;
    }

    public void setAlternativeProductBarcode(String alternativeProductBarcode) {
        this.alternativeProductBarcode = alternativeProductBarcode;
    }
}
