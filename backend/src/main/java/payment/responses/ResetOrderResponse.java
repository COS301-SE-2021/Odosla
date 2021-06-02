package payment.responses;

public class ResetOrderResponse {
    private boolean success;

    public ResetOrderResponse(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
