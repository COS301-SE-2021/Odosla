package cs.superleague.shopping.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import cs.superleague.shopping.dataclass.Item;

import java.util.Date;
import java.util.List;

public class SaveItemToRepoResponse {

    @JsonFormat(pattern = "E MMM dd HH:mm:ss z yyyy")
    private Date timestamp;
    private String message;
    private Boolean isSuccess;

    public SaveItemToRepoResponse(Boolean isSuccess, Date timestamp, String message) {
        this.isSuccess = isSuccess;
        this.timestamp = timestamp;
        this.message = message;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public Boolean getSuccess() {
        return isSuccess;
    }

    public void setSuccess(Boolean success) {
        isSuccess = success;
    }
}
