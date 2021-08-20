package cs.superleague.analytics.responses;

import java.util.Date;

public class CreateUserReportResponse {

    private final boolean success;
    private final String message;
    private final Date timestamp;

    public CreateUserReportResponse(boolean success, String message, Date timestamp) {
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