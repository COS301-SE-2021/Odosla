package order.responses;

public class CancelOrderResponse {
    private boolean success;

    public CancelOrderResponse(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
