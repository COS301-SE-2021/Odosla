package cs.superleague.user.responses;

import java.util.Date;

public class UpdateShopperDetailsResponse {

    private final String message;
    private final String jwtToken;
    private final boolean success;
    private final Date timestamp;

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

    public UpdateShopperDetailsResponse(String message, boolean success, Date timestamp, String jwtToken) {
        this.message = message;
        this.success = success;
        this.timestamp = timestamp;
        this.jwtToken = jwtToken;
    }
}