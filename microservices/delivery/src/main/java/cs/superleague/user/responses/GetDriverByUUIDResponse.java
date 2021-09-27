package cs.superleague.user.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import cs.superleague.user.dataclass.Driver;

import java.io.Serializable;
import java.util.Date;

public class GetDriverByUUIDResponse implements Serializable {
    private final Driver driver;
    @JsonFormat(pattern="E MMM dd HH:mm:ss z yyyy")
    private final Date timestamp;
    private final String message;

    public GetDriverByUUIDResponse() {
        this.driver = null;
        this.timestamp = null;
        this.message = null;
    }

    public GetDriverByUUIDResponse(Driver driver, Date timestamp, String message) {
        this.driver = driver;
        this.timestamp = timestamp;
        this.message = message;
    }

    public Driver getDriver() {
        return driver;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }
}
