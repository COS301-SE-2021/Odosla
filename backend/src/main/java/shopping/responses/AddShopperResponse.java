package shopping.responses;

import java.util.Date;

public class AddShopperResponse {
    /** attributes */
    private final boolean success;
    private final Date timestamp;
    private final String message;

    /** constructor
     *
     * @param success whether shopper was added or not
     * @param timestamp timestamp the response was created
     * @param message message associated response object
     */
    public AddShopperResponse(boolean success, Date timestamp, String message) {
        this.success = success;
        this.timestamp = timestamp;
        this.message = message;
    }

    /** getter */
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
