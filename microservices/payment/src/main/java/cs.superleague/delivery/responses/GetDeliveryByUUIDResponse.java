package cs.superleague.delivery.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import cs.superleague.delivery.dataclass.Delivery;

import java.util.Date;

public class GetDeliveryByUUIDResponse {

    @JsonProperty("deliveryEntity")
    private final Delivery deliveryEntity;
    @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss")
    private final Date timestamp;
    private final String message;


    public GetDeliveryByUUIDResponse(Delivery deliveryEntity, Date timestamp, String message) {
        this.deliveryEntity = deliveryEntity;
        this.timestamp = timestamp;
        this.message = message;
    }

    public GetDeliveryByUUIDResponse() {
        this.deliveryEntity = null;
        this.timestamp = null;
        this.message = null;
    }

    public Delivery getDelivery() {
        return deliveryEntity;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }
}
