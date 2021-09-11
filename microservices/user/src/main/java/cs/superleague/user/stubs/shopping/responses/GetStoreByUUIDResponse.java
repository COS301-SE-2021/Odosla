package cs.superleague.user.stubs.shopping.responses;

import cs.superleague.user.stubs.shopping.dataclass.Store;

import java.util.Date;

public class GetStoreByUUIDResponse {

    private final Store storeEntity;
    private final Date timestamp;
    private final String message;


    public GetStoreByUUIDResponse(Store storeEntity, Date timestamp, String message) {
        this.storeEntity = storeEntity;
        this.timestamp = timestamp;
        this.message = message;
    }

    public Store getStore() {
        return storeEntity;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }
}
