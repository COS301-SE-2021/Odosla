package shopping.responses;

import java.util.Calendar;
import java.util.Date;

public class GetNextQueuedResponse {
    private Date timeStamp;
    private boolean response;
    private String message;

    public GetNextQueuedResponse(Date timeStamp, boolean response, String message) {
        this.timeStamp = timeStamp;
        this.response = response;
        this.message = message;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean isResponse() {
        return response;
    }

    public void setResponse(boolean response) {
        this.response = response;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
