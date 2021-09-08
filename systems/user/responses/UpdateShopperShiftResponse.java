package cs.superleague.user.responses;

import java.util.Calendar;

public class UpdateShopperShiftResponse {
    private final boolean success;
    private final Calendar timestamp;
    private final String message;

    public UpdateShopperShiftResponse(boolean success, Calendar timestamp, String message) {
        this.success = success;
        this.timestamp = timestamp;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public Calendar getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }
}
