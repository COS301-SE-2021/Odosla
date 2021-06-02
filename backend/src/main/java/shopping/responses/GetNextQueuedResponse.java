package shopping.responses;

import java.util.Calendar;

public class GetNextQueuedResponse {
    private Calendar timeStamp;
    private boolean response;
    private String message;

    public GetNextQueuedResponse(Calendar timeStamp, boolean response, String message) {
        this.timeStamp = timeStamp;
        this.response = response;
        this.message = message;
    }

    public Calendar getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Calendar timeStamp) {
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
