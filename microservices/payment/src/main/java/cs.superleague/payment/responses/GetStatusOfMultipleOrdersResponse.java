package cs.superleague.payment.responses;

import java.util.Date;

public class GetStatusOfMultipleOrdersResponse {
    private final String status;
    private final boolean success;
    private final Date timestamp;
    private final String message;

    public GetStatusOfMultipleOrdersResponse(String status, boolean success, Date timestamp, String message) {
        this.status = status;
        this.success = success;
        this.timestamp = timestamp;
        this.message = message;
    }

    public String getStatus() {
        return status;
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
