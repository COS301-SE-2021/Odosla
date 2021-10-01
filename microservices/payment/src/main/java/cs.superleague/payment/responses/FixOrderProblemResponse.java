package cs.superleague.payment.responses;

import java.util.Date;

public class FixOrderProblemResponse {

    private boolean success;
    private String message;
    private Date timestamp;

    public FixOrderProblemResponse() {
    }

    public FixOrderProblemResponse(boolean success, String message, Date timestamp) {
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
