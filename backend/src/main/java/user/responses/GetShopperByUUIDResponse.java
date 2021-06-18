package user.responses;


import user.dataclass.User;

import java.util.Date;

public class GetShopperByUUIDResponse {
    private final User userEntity;
    private final Date timestamp;
    private final String message;

    public GetShopperByUUIDResponse(User userEntity, Date timestamp, String message) {
        this.userEntity = userEntity;
        this.timestamp = timestamp;
        this.message = message;
    }


    public User getUser() {
        return userEntity;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

}
