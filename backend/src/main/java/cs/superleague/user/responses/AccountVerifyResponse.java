package cs.superleague.user.responses;

import cs.superleague.user.dataclass.UserType;

import java.util.Date;

public class AccountVerifyResponse {

    /* Attributes */
    private final boolean success;
    private final Date timestamp;
    private final String message;
    private final UserType userType;

    public AccountVerifyResponse(boolean success, Date timestamp, String message, UserType userType) {
        this.success = success;
        this.timestamp = timestamp;
        this.message = message;
        this.userType = userType;
    }

    /* Getters and Setters */
    public boolean isSuccess() {
        return success;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public UserType getUserType() {
        return userType;
    }
}