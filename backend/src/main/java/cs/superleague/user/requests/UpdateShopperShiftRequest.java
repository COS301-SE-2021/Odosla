package cs.superleague.user.requests;

import java.util.UUID;

public class UpdateShopperShiftRequest {
    private String shopperID;
    private Boolean onShift;

    public UpdateShopperShiftRequest(String shopperID, boolean onShift) {
        this.shopperID = shopperID;
        this.onShift = onShift;
    }

    public String getShopperID() {
        return shopperID;
    }

    public void setShopperID(String shopperID) {
        this.shopperID = shopperID;
    }

    public Boolean getOnShift() {
        return onShift;
    }

    public void setOnShift(Boolean onShift) {
        this.onShift = onShift;
    }
}
