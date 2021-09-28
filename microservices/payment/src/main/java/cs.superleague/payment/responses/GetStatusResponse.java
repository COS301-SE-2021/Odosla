package cs.superleague.payment.responses;

import java.util.Date;

public class GetStatusResponse {
    /**
     * attributes
     */
    private final String status;
    private final boolean success;
    private final Date timestamp;
    private final String message;

    /**
     * CONSTRUCTOR
     *
     * @param status    - the order object requested
     * @param success   - success status of the operation
     * @param timestamp - time that the system sends the response object
     * @param message   - message returned after response
     */
    public GetStatusResponse(String status, boolean success, Date timestamp, String message) {
        this.success = success;
        this.timestamp = timestamp;
        this.message = message;
        this.status = status;
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
