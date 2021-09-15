package cs.superleague.shopping.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import cs.superleague.shopping.dataclass.Store;

import java.util.Date;

public class GetStoreByUUIDResponse {

    private final Store storeEntity;
    @JsonFormat(pattern="E MMM dd HH:mm:ss z yyyy")
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
