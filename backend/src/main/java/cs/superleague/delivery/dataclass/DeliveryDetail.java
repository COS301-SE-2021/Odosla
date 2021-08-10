package cs.superleague.delivery.dataclass;

import javax.persistence.*;
import java.util.Calendar;
import java.util.UUID;

@Entity
public class DeliveryDetail {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private UUID deliveryID;
    private Calendar time;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;
    private String detail;

    @ManyToOne
    private Delivery delivery;

    public DeliveryDetail(UUID deliveryID, Calendar time, DeliveryStatus status, String detail) {
        this.deliveryID = deliveryID;
        this.time = time;
        this.status = status;
        this.detail = detail;
    }

    public DeliveryDetail() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UUID getDeliveryID() {
        return deliveryID;
    }

    public void setDeliveryID(UUID deliveryID) {
        this.deliveryID = deliveryID;
    }

    public Calendar getTime() {
        return time;
    }

    public void setTime(Calendar time) {
        this.time = time;
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

    public Delivery getDelivery() {
        return delivery;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }
}
