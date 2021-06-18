package cs.superleague.shopping.responses;

import java.util.Date;

public class AddToQueueResponse {
    private final boolean success;
    private final String message;
    private final Date timestamp;

    public AddToQueueResponse(boolean success, String message, Date timestamp) {
        this.success = success;
        this.message = message;
        this.timestamp = timestamp;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
