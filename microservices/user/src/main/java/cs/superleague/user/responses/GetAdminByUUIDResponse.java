package cs.superleague.user.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import cs.superleague.user.dataclass.Admin;

import java.util.Date;

public class GetAdminByUUIDResponse {
    private final Admin admin;
    @JsonFormat(pattern = "E MMM dd HH:mm:ss z yyyy")
    private final Date timestamp;
    private final String message;

    public GetAdminByUUIDResponse(Admin admin, Date timestamp, String message) {
        this.admin = admin;
        this.timestamp = timestamp;
        this.message = message;
    }

    public Admin getAdmin() {
        return admin;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }
}
