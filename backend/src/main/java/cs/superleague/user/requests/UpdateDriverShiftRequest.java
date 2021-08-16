package cs.superleague.user.requests;

import java.util.UUID;

public class UpdateDriverShiftRequest {

    private String JWTToken;
    private Boolean onShift;

    public UpdateDriverShiftRequest(String JWTToken, Boolean onShift) {
        this.JWTToken = JWTToken;
        this.onShift = onShift;
    }

    public String getJWTToken() {
        return JWTToken;
    }

    public Boolean getOnShift() {
        return onShift;
    }

    public void setJWTToken(String driverID) {
        this.JWTToken = driverID;
    }

    public void setOnShift(Boolean onShift) {
        this.onShift = onShift;
    }
}
