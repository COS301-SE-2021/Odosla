package cs.superleague.user.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import cs.superleague.user.dataclass.User;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class GetUsersResponse implements Serializable {

    @JsonProperty
    private final List<User> users;
    private final boolean success;
    private final String message;

    @JsonFormat(pattern="E MMM dd HH:mm:ss z yyyy")
    private final Date timestamp;

    public GetUsersResponse(){
        this.users = null;
        this.success = false;
        this.message = null;
        this.timestamp = null;
    }

    public GetUsersResponse(List<User> users, boolean success, String message, Date timestamp) {
        this.users = users;
        this.success = success;
        this.message = message;
        this.timestamp = timestamp;
    }

    public List<User> getUsers() {
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
