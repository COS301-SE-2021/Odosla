package order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Order {
    /* Attributes */
    private UUID orderNumber;
    private LocalDateTime creationDateTime;
    private double totalCost;
    private boolean delivery;
    private String couponCode;
    private List<Item> listOfItems;
    /* Constructor */
    public Order(LocalDateTime creationDateTime, boolean delivery, List<Item> listOfItems, String couponCode) {
        this.creationDateTime = creationDateTime;
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
    public LocalDateTime getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(LocalDateTime creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double cost) {
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