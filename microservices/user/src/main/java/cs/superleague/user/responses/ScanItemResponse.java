package cs.superleague.user.responses;

import java.util.Date;

public class ScanItemResponse {

    private final boolean success;
    private final Date timestamp;
    private final String message;

    public ScanItemResponse(boolean success, Date timestamp, String message) {

        this.success = success;
        this.timestamp = timestamp;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }
}
