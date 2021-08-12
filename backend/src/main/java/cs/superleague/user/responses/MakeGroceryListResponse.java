package cs.superleague.user.responses;

import java.util.Date;

public class MakeGroceryListResponse {

    private final boolean success;
    private final String message;
    private final Date timestamp;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public MakeGroceryListResponse(boolean success, String message, Date timestamp) {
        this.success = success;
        this.message = message;
        this.timestamp = timestamp;
    }
}
