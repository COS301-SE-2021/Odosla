package cs.superleague.shopping.stubs.user.responses;

import cs.superleague.shopping.stubs.user.dataclass.Shopper;

import java.util.Date;

public class GetShopperByUUIDResponse {
    private final Shopper shopperEntity;
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
