package cs.superleague.shopping.requests;

import java.util.UUID;

public class ItemIsInStockRequest {

    private String barcode;
    private UUID storeID;
    private boolean outOfStock;

    public ItemIsInStockRequest() {
    }

    public ItemIsInStockRequest(String barcode, UUID storeID, boolean outOfStock) {
        this.barcode = barcode;
        this.storeID = storeID;
        this.outOfStock = outOfStock;
    }

    public String getBarcode() {
        return barcode;
    }

    public UUID getStoreID() {
        return storeID;
    }

    public boolean isOutOfStock() {
        return outOfStock;
    }
}
