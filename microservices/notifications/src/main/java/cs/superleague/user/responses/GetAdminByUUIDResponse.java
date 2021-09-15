package cs.superleague.user.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import cs.superleague.user.dataclass.Admin;

import java.util.Calendar;

public class GetAdminByUUIDResponse {
    private final Admin admin;

    @JsonFormat(pattern="E MMM dd HH:mm:ss z yyyy")
    private final Calendar timestamp;
    private final String message;

    public GetAdminByUUIDResponse(Admin admin, Calendar timestamp, String message) {
        this.admin = admin;
        this.timestamp = timestamp;
        this.message = message;
    }

    public Admin getAdmin() {
        return admin;
    }

    public Calendar getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }
}
