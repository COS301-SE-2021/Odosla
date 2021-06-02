package order.responses;

public class SubmitOrderResponse {
    private boolean success;

    public SubmitOrderResponse(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}