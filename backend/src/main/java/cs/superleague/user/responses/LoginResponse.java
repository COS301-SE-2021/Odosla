package cs.superleague.user.responses;

import java.util.Date;

public class LoginResponse {

    private final String Token;
    private final boolean success;
    private final Date timestamp;
    private final String message;

    public LoginResponse(String token, boolean success, Date timestamp, String message) {
        Token = token;
        this.success = success;
        this.timestamp = timestamp;
        this.message = message;
    }

    public String getToken() {
        return Token;
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