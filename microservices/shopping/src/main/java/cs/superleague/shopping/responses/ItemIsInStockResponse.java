package cs.superleague.shopping.responses;

public class ItemIsInStockResponse {

    private String message;
    private boolean success;

    public ItemIsInStockResponse() {
    }

    public ItemIsInStockResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
}
