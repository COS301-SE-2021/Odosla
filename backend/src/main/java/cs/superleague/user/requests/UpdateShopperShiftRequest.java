package cs.superleague.user.requests;

import java.util.UUID;

public class UpdateShopperShiftRequest {
    private UUID shopperID;
    private Boolean onShift;

    public UpdateShopperShiftRequest(UUID shopperID, boolean onShift) {
        this.shopperID = shopperID;
        this.onShift = onShift;
    }

    public UUID getShopperID() {
        return shopperID;
    }

    public void setShopperID(UUID shopperID) {
        this.shopperID = shopperID;
    }

    public Boolean getOnShift() {
        return onShift;
    }

    public void setOnShift(Boolean onShift) {
        this.onShift = onShift;
    }
}
