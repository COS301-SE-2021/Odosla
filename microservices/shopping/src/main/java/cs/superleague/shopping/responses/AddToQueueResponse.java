package cs.superleague.shopping.responses;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class AddToQueueResponse {

    private final boolean success;
    private final String message;
    @JsonFormat(pattern = "E MMM dd HH:mm:ss z yyyy")
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
