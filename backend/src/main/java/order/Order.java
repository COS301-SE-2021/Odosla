package order;

public class Order {
    /* Attributes */
    private UUID orderNumber;
    private DateTime creationDateTime;
    private double totalCost;
    private boolean delivery;
    private String couponCode;
    private List<Item> listOfItems;
    /* Constructor */
    public Order(DateTime creationDateTime, boolean delivery, Item[] listOfItems, String couponCode=null) {
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
    public DateTime getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(DateTime creationDateTime) {
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

    public Item[] getListOfItems() {
        return listOfItems;
    }

    public void setListOfItems(Item[] listOfItems) {
        this.listOfItems = listOfItems;
    }
}