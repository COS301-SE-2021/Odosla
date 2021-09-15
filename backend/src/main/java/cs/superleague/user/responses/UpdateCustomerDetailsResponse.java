package cs.superleague.user.responses;

import java.util.Date;

public class UpdateCustomerDetailsResponse {

    private final String message;
    private final boolean success;
    private final Date timestamp;
    private final String jwtToken;

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public UpdateCustomerDetailsResponse(String message, boolean success, Date timestamp, String jwtToken) {
        this.message = message;
        this.success = success;
        this.timestamp = timestamp;
        this.jwtToken = jwtToken;
    }
}
