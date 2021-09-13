package cs.superleague.user.responses;

import cs.superleague.user.dataclass.Driver;

import java.util.Calendar;

public class GetDriverByUUIDResponse {
    private final Driver driver;
    private final Calendar timestamp;
    private final String message;

    public GetDriverByUUIDResponse(Driver driver, Calendar timestamp, String message) {
        this.driver = driver;
        this.timestamp = timestamp;
        this.message = message;
    }

    public Driver getDriver() {
        return driver;
    }

    public Calendar getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }
}
