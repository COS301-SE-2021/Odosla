package cs.superleague.shopping.requests;

import java.util.UUID;

public class ItemIsInStockRequest {

    private String barcode;
    private UUID storeID;

    public ItemIsInStockRequest() {
    }

    public ItemIsInStockRequest(String barcode, UUID storeID) {
        this.barcode = barcode;
        this.storeID = storeID;
    }

    public String getBarcode() {
        return barcode;
    }

    public UUID getStoreID() {
        return storeID;
    }
}
