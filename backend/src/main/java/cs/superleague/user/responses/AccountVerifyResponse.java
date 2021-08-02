package cs.superleague.user.responses;

import java.util.Date;

public class AccountVerifyResponse {

    /* Attributes */
    private final boolean success;
    private final Date timestamp;
    private final String message;

    public AccountVerifyResponse(boolean success, Date timestamp, String message) {
        this.success = success;
        this.timestamp = timestamp;
        this.message = message;
    }

    /* Getters and Setters */
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