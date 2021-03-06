package cs.superleague.shopping.responses;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class RemoveShopperResponse {

    /**
     * attributes
     */
    private final boolean success;
    @JsonFormat(pattern = "E MMM dd HH:mm:ss z yyyy")
    private final Date timestamp;
    private final String message;

    /**
     * constructor
     *
     * @param success   - whether shopper was succesfully removed from list or not
     * @param timestamp - time the response was created
     * @param message   - message associated with response
     */
    public RemoveShopperResponse(boolean success, Date timestamp, String message) {
        this.success = success;
        this.timestamp = timestamp;
        this.message = message;
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
