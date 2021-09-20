package cs.superleague.user.responses;

import java.util.Date;

public class FinalisePasswordResetResponse {

    private final String message;
    private final boolean success;
    private final Date timestamp;

    public FinalisePasswordResetResponse(String message, boolean success, Date timestamp) {
        this.message = message;
        this.success = success;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}