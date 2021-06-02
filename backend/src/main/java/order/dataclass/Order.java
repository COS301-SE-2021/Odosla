package order.dataclass;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import javax.persistence.*;

@Entity
@Table(name = "orderTable")
public class Order {

    @Id
    private UUID orderID;
    private UUID userID;
    private UUID storeID;
    private UUID shopperID;
    private Calendar createDate;
    private double totalCost;
    private List<Item> items;
    private double discount;
    private boolean requiresPharmacy;

    @Enumerated(EnumType.STRING)
    private OrderType type;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToOne (cascade={CascadeType.ALL})
    private GeoPoint deliveryAddress;

    @OneToOne (cascade={CascadeType.ALL})
    private GeoPoint storeAddress;

    public Order(){

    }

    public Order(UUID orderID, UUID userID, UUID storeID, UUID shopperID, Calendar createDate, double totalCost, OrderType type, OrderStatus status, List<Item> items, double discount, GeoPoint deliveryAddress, GeoPoint storeAddress, boolean requiresPharmacy) {
        this.orderID = orderID;
        this.userID = userID;
        this.storeID = storeID;
        this.shopperID = shopperID;
        this.createDate = createDate;
        this.totalCost = totalCost;
        this.type = type;
        this.status = status;
        this.items = items;
        this.discount = discount;
        this.deliveryAddress = deliveryAddress;
        this.storeAddress = storeAddress;
        this.requiresPharmacy = requiresPharmacy;
    }

    public UUID getOrderID() {
        return orderID;
    }

    public void setOrderID(UUID orderID) {
        this.orderID = orderID;
    }

    public UUID getUserID() {
        return userID;
    }

    public void setUserID(UUID userID) {
        this.userID = userID;
    }

    public UUID getStoreID() {
        return storeID;
    }

    public void setStoreID(UUID storeID) {
        this.storeID = storeID;
    }

    public UUID getShopperID() {
        return shopperID;
    }

    public void setShopperID(UUID shopperID) {
        this.shopperID = shopperID;
    }

    public Calendar getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Calendar createDate) {
        this.createDate = createDate;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public OrderType getType() {
        return type;
    }

    public void setType(OrderType type) {
        this.type = type;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public GeoPoint getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(GeoPoint deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public GeoPoint getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(GeoPoint storeAddress) {
        this.storeAddress = storeAddress;
    }

    public boolean isRequiresPharmacy() {
        return requiresPharmacy;
    }

    public void setRequiresPharmacy(boolean requiresPharmacy) {
        this.requiresPharmacy = requiresPharmacy;
    }
}