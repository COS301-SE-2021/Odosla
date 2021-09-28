package cs.superleague.user.responses;

import java.util.Date;

public class UpdateDriverDetailsResponse {


    private final String message;
    private final boolean success;
    private final Date timestamp;
    private final String jwtToken;

    public UpdateDriverDetailsResponse(String message, boolean success, Date timestamp, String jwtToken) {
        this.message = message;
        this.success = success;
        this.timestamp = timestamp;
        this.jwtToken = jwtToken;
    }

    public String getJwtToken() {
        return jwtToken;
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
