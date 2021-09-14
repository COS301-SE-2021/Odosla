package cs.superleague.delivery.stubs.shopping.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import cs.superleague.delivery.stubs.shopping.dataclass.Store;

import java.util.Date;

public class GetStoreByUUIDResponse {

    private final Store store;
    @JsonFormat(pattern="E MMM dd HH:mm:ss z yyyy")
    private final Date timestamp;
    private final String message;


    public GetStoreByUUIDResponse(Store store, Date timestamp, String message) {
        this.store = store;
        this.timestamp = timestamp;
        this.message = message;
    }

    public Store getStore() {
        return store;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }
}
