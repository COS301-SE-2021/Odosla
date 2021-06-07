package shopping.responses;

import java.util.Date;

public class ToggleStoreOpenResponse {

    Boolean isOpen = false;
    String message;
    Date timestamp;

    public ToggleStoreOpenResponse(Boolean isOpen, Date timestamp, String message)
    {
        this.isOpen= isOpen;
        this.timestamp=timestamp;
        this.message=message;
    }

    public void setOpen(Boolean open) {
        isOpen = open;
    }

    public Boolean getOpen() {
        return isOpen;
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
