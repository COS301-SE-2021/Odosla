package order.dataclass;

import user.GeoPoint;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class Order {

    private UUID orderID;
    private UUID userID;
    private UUID storeID;
    private UUID shopperID;
    private Calendar createDate;
    private Calendar orderDate;
    private float totalCost;
    private OrderType type;
    private OrderStatus status;
    private List<Item> items;
    private float discount;
    private GeoPoint deliveryAddress;
    private GeoPoint storeAddress;
    private boolean requiresPharmacy;

    public Order(){

    }

    public Order(UUID orderID, UUID userID, UUID storeID, UUID shopperID, Calendar createDate, Calendar orderDate, float totalCost, OrderType type, OrderStatus status, List<Item> items, float discount, GeoPoint deliveryAddress, GeoPoint storeAddress, boolean requiresPharmacy) {
        this.orderID = orderID;
        this.userID = userID;
        this.storeID = storeID;
        this.shopperID = shopperID;
        this.createDate = createDate;
        this.orderDate = orderDate;
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

    public Calendar getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Calendar orderDate) {
        this.orderDate = orderDate;
    }

    public float getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(float totalCost) {
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

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
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