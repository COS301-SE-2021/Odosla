package cs.superleague.user.requests;

import java.util.UUID;

public class UpdateShopperShiftRequest {
    private String jwtToken;
    private Boolean onShift;
    private UUID storeID;

    public UpdateShopperShiftRequest(String jwtToken, boolean onShift, UUID storeID) {
        this.jwtToken = jwtToken;
        this.onShift = onShift;
        this.storeID = storeID;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
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
