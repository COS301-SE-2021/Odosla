package cs.superleague.user.responses;

import java.util.Date;

public class AccountVerifyResponse {

    /* Attributes */
    private final String token;
    private final boolean success;
    private final Date timestamp;
    private final String message;

    public AccountVerifyResponse(String token, boolean success, Date timestamp, String message) {
        this.token = token;
        this.success = success;
        this.timestamp = timestamp;
        this.message = message;
    }

    /* Getters and Setters */
    public String getToken() {
        return token;
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