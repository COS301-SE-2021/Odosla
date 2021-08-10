package cs.superleague.delivery.dataclass;

import cs.superleague.payment.dataclass.GeoPoint;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="deliveryTable")
public class Delivery {

    @Id
    private UUID deliveryID;

    @OneToOne(cascade={CascadeType.ALL})
    private GeoPoint pickUpLocation;

    @OneToOne(cascade={CascadeType.ALL})
    private GeoPoint dropOffLocation;

    private UUID customerId;
    private UUID storeId;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    private double cost;

    @OneToMany(mappedBy = "delivery")
    private List<DeliveryDetail> deliveryDetail;

    public Delivery(UUID deliveryID, GeoPoint pickUpLocation, GeoPoint dropOffLocation, UUID customerId, UUID storeId, DeliveryStatus status, double cost) {
        this.deliveryID = deliveryID;
        this.pickUpLocation = pickUpLocation;
        this.dropOffLocation = dropOffLocation;
        this.customerId = customerId;
        this.storeId = storeId;
        this.status = status;
        this.cost = cost;
    }

    public Delivery() {

    }
}
