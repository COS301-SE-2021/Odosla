package cs.superleague.user.stubs.payment.dataclass;

import cs.superleague.user.stubs.shopping.dataclass.Item;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

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
    private UUID driverID;
    private Calendar createDate;
    private Calendar processDate;
    private Double totalCost;
    private Double discount;
    private boolean requiresPharmacy;

    @Enumerated(EnumType.STRING)
    private OrderType type;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToOne (cascade={CascadeType.ALL})
    private GeoPoint deliveryAddress;

    @OneToOne (cascade={CascadeType.ALL})
    private GeoPoint storeAddress;

    @ManyToMany
    @JoinTable
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Item> items;

    public Order(){

    }

    public Order(UUID orderID, UUID userID, UUID storeID, UUID shopperID, Calendar createDate, Calendar processDate, Double totalCost, OrderType type, OrderStatus status, List<Item> items, double discount, GeoPoint deliveryAddress, GeoPoint storeAddress, boolean requiresPharmacy) {
        this.orderID = orderID;
        this.userID = userID;
        this.storeID = storeID;
        this.shopperID = shopperID;
        this.createDate = createDate;
        this.processDate = processDate;
        this.totalCost = totalCost;
        this.type = type;
        this.status = status;
        this.items = items;
        this.discount = discount;
        this.deliveryAddress = deliveryAddress;
        this.storeAddress = storeAddress;
        this.requiresPharmacy = requiresPharmacy;
    }

    public Order(UUID orderID, UUID userID, UUID storeID, UUID shopperID, Calendar createDate, Calendar processDate, Double totalCost, OrderType type, OrderStatus status, List<Item> items, double discount, GeoPoint storeAddress, boolean requiresPharmacy) {
        this.orderID = orderID;
        this.userID = userID;
        this.storeID = storeID;
        this.shopperID = shopperID;
        this.createDate = createDate;
        this.processDate = processDate;
        this.totalCost = totalCost;
        this.type = type;
        this.status = status;
        this.items = items;
        this.discount = discount;
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

    public Calendar getProcessDate() {return processDate; }

    public void setProcessDate(Calendar processDate){this.processDate = processDate; }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
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

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
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

    public UUID getDriverID() {
        return driverID;
    }

    public void setDriverID(UUID driverID) {
        this.driverID = driverID;
    }
}