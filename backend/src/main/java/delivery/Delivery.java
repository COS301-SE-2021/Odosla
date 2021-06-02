package delivery;

import order.dataclass.GeoPoint;

import java.util.UUID;

public class Delivery {
    private GeoPoint currentLocation;
    private UUID customerId;
    private UUID storeId;
    private DeliveryStatus status;
    private double cost;

    public Delivery(GeoPoint currentLocation, UUID customerId, UUID storeId, DeliveryStatus status, double cost) {
        this.currentLocation = currentLocation;
        this.customerId = customerId;
        this.storeId = storeId;
        this.status = status;
        this.cost = cost;
    }

    public GeoPoint getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(GeoPoint currentLocation) {
        this.currentLocation = currentLocation;
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

}
