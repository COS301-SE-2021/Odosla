package shopping.responses;

import java.util.Date;

public class GetStoreOpenResponse {

    Boolean isOpen = false;
    String message;
    Date timestamp;
    int openingTime;
    int closingTime;

    public GetStoreOpenResponse(Boolean isOpen, Date timestamp, String message)
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

    public void setOpeningTime(int openingTime) {
        this.openingTime = openingTime;
    }

    public int getOpeningTime() {
        return openingTime;
    }

    public void setClosingTime(int closingTime) {
        this.closingTime = closingTime;
    }

    public int getClosingTime() {
        return closingTime;
    }
}