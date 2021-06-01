package order.responses;

public class RemoveItemResponse {
    private boolean success;

    public RemoveItemResponse(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
