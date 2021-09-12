package cs.superleague.delivery.stub.responses;
import cs.superleague.delivery.stub.dataclass.Store;

import java.util.Date;

public class GetStoreResponse {
    /** attributes */
    private final Store store;
    private final boolean success;
    private final Date timestamp;
    private final String message;

    /** CONSTRUCTOR
     * @param store - the store object requested
     * @param success - success status of the operation
     * @param timestamp - time that the system sends the response object
     * @param message - message returned after response
     */
    public GetStoreResponse(Store store, boolean success, Date timestamp, String message) {
        this.store = GetStoreResponse.this.store;
        this.success = success;
        this.timestamp = timestamp;
        this.message = message;
    }

    public Store getStore() {
        return store;
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
