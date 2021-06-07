package shopping.responses;

import java.util.Date;

public class ToggleStoreOpenResponse {

    String message;
    Date timestamp;

    public ToggleStoreOpenResponse(Date timestamp, String message)
    {
        this.timestamp=timestamp;
        this.message=message;
    }

    public void setMessage(String message)
    {
        this.message=message;
    }

    public String getMessage() {
        return message;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
