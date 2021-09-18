package cs.superleague.user.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import cs.superleague.user.dataclass.Driver;

import java.util.Date;
import java.util.List;

public class GetDriversResponse {

    private final List<Driver> users;
    private final boolean success;
    private final String message;
    @JsonFormat(pattern="E MMM dd HH:mm:ss z yyyy")
    private final Date timestamp;

    public GetDriversResponse() {
        this.users = null;
        this.success = false;
        this.message = null;
        this.timestamp = null;
    }

    public GetDriversResponse(List<Driver> users, boolean success, String message, Date timestamp) {
        this.users = users;
        this.success = success;
        this.message = message;
        this.timestamp = timestamp;
    }

    public List<Driver> getUsers() {
        return users;
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
