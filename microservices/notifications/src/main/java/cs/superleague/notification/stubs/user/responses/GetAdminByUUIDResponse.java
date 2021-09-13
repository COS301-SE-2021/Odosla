package cs.superleague.notification.stubs.user.responses;

import cs.superleague.notification.stubs.user.dataclass.Admin;

import java.util.Calendar;

public class GetAdminByUUIDResponse {
    private final Admin admin;
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
