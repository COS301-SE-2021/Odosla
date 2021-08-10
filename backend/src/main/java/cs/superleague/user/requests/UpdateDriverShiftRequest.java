package cs.superleague.user.requests;

import java.util.UUID;

public class UpdateDriverShiftRequest {

    private UUID driverID;
    private Boolean onShift;

    public UpdateDriverShiftRequest(UUID driverID, Boolean onShift) {
        this.driverID = driverID;
        this.onShift = onShift;
    }

    public UUID getDriverID() {
        return driverID;
    }

    public Boolean getOnShift() {
        return onShift;
    }

    public void setDriverID(UUID driverID) {
        this.driverID = driverID;
    }

    public void setOnShift(Boolean onShift) {
        this.onShift = onShift;
    }
}
