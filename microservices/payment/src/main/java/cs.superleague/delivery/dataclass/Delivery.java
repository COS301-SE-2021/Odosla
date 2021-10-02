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
    private GeoPoint pickUpLocationOne;

    @OneToOne(cascade={CascadeType.ALL})
    private GeoPoint pickUpLocationTwo;

    @OneToOne(cascade={CascadeType.ALL})
    private GeoPoint pickUpLocationThree;

    @OneToOne(cascade={CascadeType.ALL})
    private GeoPoint dropOffLocation;

    private UUID orderIDOne;
    private UUID orderIDTwo;
    private UUID orderIDThree;
    private UUID customerID;
    private UUID storeOneID;
    private UUID storeTwoID;
    private UUID storeThreeID;
    private UUID driverID;
    private boolean orderOnePacked = false;
    private boolean orderTwoPacked = false;
    private boolean orderThreePacked = false;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    private double cost;

    private boolean completed = false;

    @OneToMany(mappedBy = "delivery")
    private List<DeliveryDetail> deliveryDetail;

    public Delivery(UUID deliveryID, GeoPoint pickUpLocationOne, GeoPoint pickUpLocationTwo, GeoPoint pickUpLocationThree, GeoPoint dropOffLocation, UUID orderIDOne, UUID orderIDTwo, UUID orderIDThree, UUID customerID, UUID storeOneID, UUID driverID, DeliveryStatus status, double cost, boolean completed, List<DeliveryDetail> deliveryDetail) {
        this.deliveryID = deliveryID;
        this.pickUpLocationOne = pickUpLocationOne;
        this.pickUpLocationTwo = pickUpLocationTwo;
        this.pickUpLocationThree = pickUpLocationThree;
        this.dropOffLocation = dropOffLocation;
        this.orderIDOne = orderIDOne;
        this.orderIDTwo = orderIDTwo;
        this.orderIDThree = orderIDThree;
        this.customerID = customerID;
        this.storeOneID = storeOneID;
        this.driverID = driverID;
        this.status = status;
        this.cost = cost;
        this.completed = completed;
        this.deliveryDetail = deliveryDetail;
    }

    public Delivery(UUID deliveryID, UUID orderIDOne, GeoPoint pickUpLocationOne, GeoPoint dropOffLocation, UUID customerID, UUID storeOneID, DeliveryStatus status, double cost) {
        this.deliveryID = deliveryID;
        this.orderIDOne = orderIDOne;
        this.pickUpLocationOne = pickUpLocationOne;
        this.dropOffLocation = dropOffLocation;
        this.customerID = customerID;
        this.storeOneID = storeOneID;
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

    public UUID getDriverID() {
        return driverID;
    }

    public void setDriverID(UUID driverID) {
        this.driverID = driverID;
    }

    public UUID getDeliveryID() {
        return deliveryID;
    }

    public void setDeliveryID(UUID deliveryID) {
        this.deliveryID = deliveryID;
    }

    public GeoPoint getPickUpLocationOne() {
        return pickUpLocationOne;
    }

    public void setPickUpLocationOne(GeoPoint pickUpLocationOne) {
        this.pickUpLocationOne = pickUpLocationOne;
    }

    public GeoPoint getPickUpLocationTwo() {
        return pickUpLocationTwo;
    }

    public void setPickUpLocationTwo(GeoPoint pickUpLocationTwo) {
        this.pickUpLocationTwo = pickUpLocationTwo;
    }

    public GeoPoint getPickUpLocationThree() {
        return pickUpLocationThree;
    }

    public void setPickUpLocationThree(GeoPoint pickUpLocationThree) {
        this.pickUpLocationThree = pickUpLocationThree;
    }

    public UUID getOrderIDOne() {
        return orderIDOne;
    }

    public void setOrderIDOne(UUID orderIDOne) {
        this.orderIDOne = orderIDOne;
    }

    public UUID getOrderIDTwo() {
        return orderIDTwo;
    }

    public void setOrderIDTwo(UUID orderIDTwo) {
        this.orderIDTwo = orderIDTwo;
    }

    public UUID getOrderIDThree() {
        return orderIDThree;
    }

    public void setOrderIDThree(UUID orderIDThree) {
        this.orderIDThree = orderIDThree;
    }

    public GeoPoint getDropOffLocation() {
        return dropOffLocation;
    }

    public void setDropOffLocation(GeoPoint dropOffLocation) {
        this.dropOffLocation = dropOffLocation;
    }

    public UUID getCustomerID() {
        return customerID;
    }

    public void setCustomerID(UUID customerID) {
        this.customerID = customerID;
    }

    public UUID getStoreOneID() {
        return storeOneID;
    }

    public void setStoreOneID(UUID storeOneID) {
        this.storeOneID = storeOneID;
    }

    public UUID getStoreTwoID() {
        return storeTwoID;
    }

    public void setStoreTwoID(UUID storeTwoID) {
        this.storeTwoID = storeTwoID;
    }

    public UUID getStoreThreeID() {
        return storeThreeID;
    }

    public void setStoreThreeID(UUID storeThreeID) {
        this.storeThreeID = storeThreeID;
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

    public boolean isOrderOnePacked() {
        return orderOnePacked;
    }

    public void setOrderOnePacked(boolean orderOnePacked) {
        this.orderOnePacked = orderOnePacked;
    }

    public boolean isOrderTwoPacked() {
        return orderTwoPacked;
    }

    public void setOrderTwoPacked(boolean orderTwoPacked) {
        this.orderTwoPacked = orderTwoPacked;
    }

    public boolean isOrderThreePacked() {
        return orderThreePacked;
    }

    public void setOrderThreePacked(boolean orderThreePacked) {
        this.orderThreePacked = orderThreePacked;
    }
}
