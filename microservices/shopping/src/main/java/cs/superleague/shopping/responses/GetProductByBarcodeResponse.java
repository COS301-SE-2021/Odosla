package cs.superleague.shopping.responses;

import cs.superleague.shopping.dataclass.Item;

public class GetProductByBarcodeResponse {
    private boolean success;
    private String message;
    private Item product;

    public GetProductByBarcodeResponse(boolean success, String message, Item product) {
        this.success = success;
        this.message = message;
        this.product = product;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Item getProduct() {
        return product;
    }

    public void setProduct(Item product) {
        this.product = product;
    }
}
