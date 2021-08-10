package cs.superleague.delivery.requests;

import cs.superleague.delivery.dataclass.DeliveryStatus;

import java.util.UUID;

public class UpdateDeliveryStatusRequest {
    private DeliveryStatus status;
    private UUID deliveryID;
    private String detail;

    public UpdateDeliveryStatusRequest(DeliveryStatus status, UUID deliveryID, String detail) {
        this.status = status;
        this.deliveryID = deliveryID;
        this.detail = detail;
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }

    public UUID getDeliveryID() {
        return deliveryID;
    }

    public void setDeliveryID(UUID deliveryID) {
        this.deliveryID = deliveryID;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
