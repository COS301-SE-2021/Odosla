package cs.superleague.user.requests;

import java.util.UUID;

public class UpdateDriverShiftRequest {
    private Boolean onShift;

    public UpdateDriverShiftRequest(Boolean onShift) {
        this.onShift = onShift;
    }

    public Boolean getOnShift() {
        return onShift;
    }

    public void setOnShift(Boolean onShift) {
        this.onShift = onShift;
    }
}
