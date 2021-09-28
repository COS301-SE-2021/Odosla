package cs.superleague.user.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import cs.superleague.user.dataclass.Shopper;

import java.util.Date;
import java.util.List;

public class GetShoppersResponse {

    private final List<Shopper> users;
    private final boolean success;
    private final String message;
    @JsonFormat(pattern = "E MMM dd HH:mm:ss z yyyy")
    private final Date timestamp;

    public GetShoppersResponse() {
        this.users = null;
        this.success = false;
        this.message = null;
        this.timestamp = null;
    }

    public GetShoppersResponse(List<Shopper> users, boolean success, String message, Date timestamp) {
        this.users = users;
        this.success = success;
        this.message = message;
        this.timestamp = timestamp;
    }

    public List<Shopper> getUsers() {
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
