package cs.superleague.user.responses;

import cs.superleague.user.dataclass.User;

import java.util.Date;

public class GetCurrentUserResponse {

    private final User user;
    private final boolean success;
    private final Date timestamp;
    private final String message;

    public GetCurrentUserResponse(User user, boolean success, Date timestamp, String message) {
        this.user = user;
        this.success = success;
        this.timestamp = timestamp;
        this.message = message;
    }

    public User getUser() {
        return user;
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
