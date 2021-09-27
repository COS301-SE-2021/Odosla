package cs.superleague.shopping.requests;

import java.util.UUID;

public class GetProductByBarcodeRequest {
    private String productBarcode;
    private UUID storeID;

    public GetProductByBarcodeRequest(String productBarcode, UUID storeID) {
        this.productBarcode = productBarcode;
        this.storeID = storeID;
    }

    public String getProductBarcode() {
        return productBarcode;
    }

    public void setProductBarcode(String productBarcode) {
        this.productBarcode = productBarcode;
    }

    public UUID getStoreID() {
        return storeID;
    }

    public void setStoreID(UUID storeID) {
        this.storeID = storeID;
    }
}
