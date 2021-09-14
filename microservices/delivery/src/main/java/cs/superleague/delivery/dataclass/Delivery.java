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

    private UUID orderID;
    private UUID customerId;
    private UUID storeId;
    private UUID driverId;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    private double cost;

    private boolean completed = false;

    @OneToMany(mappedBy = "delivery")
    private List<DeliveryDetail> deliveryDetail;

    public Delivery(UUID deliveryID, UUID orderID, GeoPoint pickUpLocation, GeoPoint dropOffLocation, UUID customerId, UUID storeId, DeliveryStatus status, double cost) {
        this.deliveryID = deliveryID;
        this.orderID = orderID;
        this.pickUpLocation = pickUpLocation;
        this.dropOffLocation = dropOffLocation;
        this.customerId = customerId;
        this.storeId = storeId;
        this.status = status;
        this.cost = cost;
    }

    public Delivery() {

    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public UUID getDriverId() {
        return driverId;
    }

    public void setDriverId(UUID driverId) {
        this.driverId = driverId;
    }

    public UUID getDeliveryID() {
        return deliveryID;
    }

    public void setDeliveryID(UUID deliveryID) {
        this.deliveryID = deliveryID;
    }

    public GeoPoint getPickUpLocation() {
        return pickUpLocation;
    }

    public void setPickUpLocation(GeoPoint pickUpLocation) {
        this.pickUpLocation = pickUpLocation;
    }

    public GeoPoint getDropOffLocation() {
        return dropOffLocation;
    }

    public void setDropOffLocation(GeoPoint dropOffLocation) {
        this.dropOffLocation = dropOffLocation;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public UUID getStoreId() {
        return storeId;
    }

    public void setStoreId(UUID storeId) {
        this.storeId = storeId;
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public List<DeliveryDetail> getDeliveryDetail() {
        return deliveryDetail;
    }

    public void setDeliveryDetail(List<DeliveryDetail> deliveryDetail) {
        this.deliveryDetail = deliveryDetail;
    }

    public UUID getOrderID() {
        return orderID;
    }

    public void setOrderID(UUID orderID) {
        this.orderID = orderID;
    }
}
