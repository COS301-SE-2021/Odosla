package cs.superleague.shopping.responses;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.UUID;

public class GetStoreOpenResponse {

    Boolean isOpen = false;
    String message;
    @JsonFormat(pattern = "E MMM dd HH:mm:ss z yyyy")
    Date timestamp;
    int openingTime;
    int closingTime;
    UUID storeID;

    public GetStoreOpenResponse(UUID storeID, Boolean isOpen, Date timestamp, String message) {
        this.isOpen = isOpen;
        this.timestamp = timestamp;
        this.message = message;
        this.storeID = storeID;
    }

    public UUID getStoreID() {
        return storeID;
    }

    public void setStoreID(UUID storeID) {
        this.storeID = storeID;
    }

    public void setOpen(Boolean open) {
        isOpen = open;
    }

    public Boolean getOpen() {
        return isOpen;
    }

    public void setMessage(String message) {
        this.message = message;
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