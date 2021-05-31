package order;

import user.GeoPoint;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Order {
    /* Attributes */
    private UUID orderID;
    private String storeID;
    private float totalCost;
    private boolean delivery;
    private List<Item> listOfItems;
    private float discount;
    private LocalDateTime orderDate;
    private GeoPoint deliveryAddress;
    private GeoPoint storeAddress;
    private boolean reuiresPharmacy;

    private String couponCode;

    /* Constructor */
    public Order(LocalDateTime orderDate, boolean delivery, List<Item> listOfItems, String couponCode) {
        this.orderDate = orderDate;
        this.delivery = delivery;
        this.listOfItems=listOfItems;
        this.couponCode = couponCode;
        /* Work out cost here */
//        totalCost=0.0
//        for (int i=0; i<listOfItems; i++){
//            cost += listOfItems.get(i).getCost();
//        }
    }

    /* Getters and Setters */
    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public float getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(float cost) {
        this.totalCost= totalCost;
    }

    public boolean isDelivery() {
        return delivery;
    }

    public void setDelivery(boolean delivery) {
        this.delivery = delivery;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public List<Item> getListOfItems() {
        return listOfItems;
    }

    public void setListOfItems(List<Item> listOfItems) {
        this.listOfItems = listOfItems;
    }
}