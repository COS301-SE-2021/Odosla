package cs.superleague.user.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import cs.superleague.user.dataclass.Shopper;

import java.util.Date;

public class GetShopperByUUIDResponse {
    private final Shopper shopperEntity;
    @JsonFormat(pattern = "E MMM dd HH:mm:ss z yyyy")
    private final Date timestamp;
    private final String message;

    public GetShopperByUUIDResponse(Shopper shopperEntity, Date timestamp, String message) {
        this.shopperEntity = shopperEntity;
        this.timestamp = timestamp;
        this.message = message;
    }


    public Shopper getShopper() {
        return shopperEntity;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }
}
