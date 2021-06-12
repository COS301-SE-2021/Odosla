package user.requests;

import java.util.Date;
import java.util.UUID;

public class GetShopperByUUIDRequest {

    private final UUID ShopperID;
    private final boolean success;
    private final Date timestamp;
    private final String message;

    public GetShopperByUUIDRequest(UUID shopperID, boolean success, Date timestamp, String message) {
        ShopperID = shopperID;
        this.success = success;
        this.timestamp = timestamp;
        this.message = message;
    }

    public UUID getShopperID() {
        return ShopperID;
    }

    public boolean isSuccess() {
        return success;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }
}
