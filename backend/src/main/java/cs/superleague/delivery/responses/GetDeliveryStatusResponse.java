package cs.superleague.delivery.responses;

import cs.superleague.delivery.dataclass.DeliveryStatus;

public class GetDeliveryStatusResponse {
    private DeliveryStatus status;
    private String message;

    public GetDeliveryStatusResponse(DeliveryStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
