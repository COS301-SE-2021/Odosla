package cs.superleague.user.requests;

import java.util.UUID;

public class UpdateShopperShiftRequest {

    private Boolean onShift;
    private UUID storeID;

    public UpdateShopperShiftRequest(boolean onShift, UUID storeID) {
        this.onShift = onShift;
        this.storeID = storeID;
    }

    public Boolean getOnShift() {
        return onShift;
    }

    public void setOnShift(Boolean onShift) {
        this.onShift = onShift;
    }

    public UUID getStoreID() {
        return storeID;
    }

    public void setStoreID(UUID storeID) {
        this.storeID = storeID;
    }
}
