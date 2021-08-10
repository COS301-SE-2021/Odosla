package cs.superleague.delivery.requests;

import cs.superleague.delivery.dataclass.DeliveryStatus;

import java.util.Calendar;
import java.util.UUID;

public class AddDeliveryDetailRequest {
    private DeliveryStatus status;
    private String detail;
    private UUID deliveryID;
    private Calendar timestamp;

    public AddDeliveryDetailRequest(DeliveryStatus status, String detail, UUID deliveryID, Calendar timestamp) {
        this.status = status;
        this.detail = detail;
        this.deliveryID = deliveryID;
        this.timestamp = timestamp;
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public UUID getDeliveryID() {
        return deliveryID;
    }

    public void setDeliveryID(UUID deliveryID) {
        this.deliveryID = deliveryID;
    }

    public Calendar getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Calendar timestamp) {
        this.timestamp = timestamp;
    }


}
