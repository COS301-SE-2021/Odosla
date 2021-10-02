package cs.superleague.user.requests;

import java.util.UUID;

public class ScanItemRequest {
    private final UUID orderID;
    private final String barcode;

    public ScanItemRequest(String barcode, UUID orderID) {
        this.orderID = orderID;
        this.barcode = barcode;
    }

    public UUID getOrderID() {
        return orderID;
    }

    public String getBarcode() {
        return barcode;
    }
}
