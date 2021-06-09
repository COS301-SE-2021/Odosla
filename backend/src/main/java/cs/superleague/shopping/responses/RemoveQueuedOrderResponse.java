package cs.superleague.shopping.responses;

public class RemoveQueuedOrderResponse {
    private boolean isRemoved;
    private String message;

    public RemoveQueuedOrderResponse(boolean isRemoved, String message) {
        this.isRemoved = isRemoved;
        this.message = message;
    }

    public boolean isRemoved() {
        return isRemoved;
    }

    public void setRemoved(boolean removed) {
        isRemoved = removed;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
