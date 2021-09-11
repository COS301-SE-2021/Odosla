package cs.superleague.payment.stubs;

import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.user.dataclass.Shopper;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table (name = "store")
@DynamicUpdate
public class Store {

    @Id
    private UUID storeID;
    private String storeBrand;
    private int maxShoppers = 2;
    private int maxOrders;
    private Boolean isOpen;
    private int openingTime;
    private int closingTime;
    private String imgUrl;

    @OneToOne(cascade={CascadeType.ALL})
    private GeoPoint storeLocation;

    @OneToOne(cascade={CascadeType.ALL})
    private Catalogue stock;

    @ManyToMany
    @JoinTable
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Shopper> shoppers=null;

    @ManyToMany
    @JoinTable
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Order> currentOrders;

    @ManyToMany
    @JoinTable
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Order> orderQueue;

    public Store() { }

    public Store(UUID storeID, String storeBrand, Catalogue stock, int maxShoppers, List<Order> currentOrders, List<Order> orderQueue, int maxOrders, Boolean isOpen) {
        this.storeID = storeID;
        this.storeBrand = storeBrand;
        this.stock = stock;
        this.maxShoppers = maxShoppers;
        this.currentOrders = currentOrders;
        this.orderQueue = orderQueue;
        this.maxOrders = maxOrders;
        this.isOpen = isOpen;
    }

    public Store(UUID storeID, String storeBrand, Catalogue stock, int maxShoppers, List<Order> currentOrders, List<Order> orderQueue, int maxOrders, Boolean isOpen, String imgUrl) {
        this.storeID = storeID;
        this.storeBrand = storeBrand;
        this.stock = stock;
        this.maxShoppers = maxShoppers;
        this.currentOrders = currentOrders;
        this.orderQueue = orderQueue;
        this.maxOrders = maxOrders;
        this.isOpen = isOpen;
        this.imgUrl=imgUrl;
    }

    public Store(UUID storeID, int openingTime, int closingTime, String storeBrand, int maxShoppers, int maxOrders, Boolean isOpen, String imgUrl) {
        this.storeID = storeID;
        this.openingTime= openingTime;
        this.closingTime= closingTime;
        this.storeBrand = storeBrand;
        this.maxShoppers = maxShoppers;
        this.maxOrders = maxOrders;
        this.isOpen = isOpen;
        this.imgUrl= imgUrl;
    }
    public UUID getStoreID() {
        return storeID;
    }

    public void setStoreID(UUID storeID) {
        this.storeID = storeID;
    }

    public String getStoreBrand() {
        return storeBrand;
    }

    public void setStoreBrand(String storeBrand) {
        this.storeBrand = storeBrand;
    }

    public Catalogue getStock() {
        return stock;
    }

    public void setStock(Catalogue stock) {
        this.stock = stock;
    }

    public int getMaxShoppers() {
        return maxShoppers;
    }

    public void setMaxShoppers(int maxShoppers) {
        this.maxShoppers = maxShoppers;
    }

    public List<Shopper> getShoppers() {
        return shoppers;
    }

    public void setShoppers(List<Shopper> shoppers) {
        this.shoppers = shoppers;
    }

    public List<Order> getCurrentOrders() {
        return currentOrders;
    }

    public void setCurrentOrders(List<Order> currentOrders) {
        this.currentOrders = currentOrders;
    }

    public List<Order> getOrderQueue() {
        return orderQueue;
    }

    public void setOrderQueue(List<Order> orderQueue) {
        this.orderQueue = orderQueue;
    }

    public int getMaxOrders() {
        return maxOrders;
    }

    public void setMaxOrders(int maxOrders) {
        this.maxOrders = maxOrders;
    }

    public Boolean getOpen() {
        return isOpen;
    }

    public void setOpen(Boolean open) {
        isOpen = open;
    }

    public void setOpeningTime(int openingTime) {
        this.openingTime = openingTime;
    }

    public int getOpeningTime() {
        return openingTime;
    }

    public void setClosingTime(int closingTime) {
        this.closingTime = closingTime;
    }

    public int getClosingTime() {
        return closingTime;
    }

    public GeoPoint getStoreLocation(){
        return storeLocation;
    }

    public void setStoreLocation(GeoPoint storeLocation) {
        this.storeLocation = storeLocation;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}

