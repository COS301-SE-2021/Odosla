package cs.superleague.shopping.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import cs.superleague.shopping.stubs.user.dataclass.Shopper;

import java.util.Date;
import java.util.List;

public class GetShoppersResponse {

    private final List<Shopper> listOfShoppers;
    private final boolean success;
    @JsonFormat(pattern="E MMM dd HH:mm:ss z yyyy")
    private final Date timeStamp;
    private final String message;

    public GetShoppersResponse(List<Shopper> listOfShoppers, boolean success, Date timeStamp,String message) {
        this.listOfShoppers = listOfShoppers;
        this.timeStamp=timeStamp;
        this.success = success;
        this.message = message;
    }

    public List<Shopper> getListOfShoppers() {
        return listOfShoppers;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }
}
