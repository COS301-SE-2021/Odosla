package shopping.dataclass;

import payment.dataclass.Order;
import user.dataclass.Shopper;
import java.util.List;
import java.util.UUID;

public class Store {

    private UUID storeID;
    private String storeBrand;
    private Catalogue stock;
    private int maxShoppers = 2;
    private List<Shopper> shoppers;
    private List<Order> currentOrders;
    private List<Order> orderQueue;
    private int maxOrders;

    public Store() {
    }

    public Store(UUID storeID, String storeBrand, Catalogue stock, int maxShoppers, List<Order> currentOrders, List<Order> orderQueue, int maxOrders) {
        this.storeID = storeID;
        this.storeBrand = storeBrand;
        this.stock = stock;
        this.maxShoppers = maxShoppers;
        this.currentOrders = currentOrders;
        this.orderQueue = orderQueue;
        this.maxOrders = maxOrders;
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
}

