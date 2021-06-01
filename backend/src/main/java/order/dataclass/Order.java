public static class Order {
    /* Attributes */
    private UUID orderID;
    private String storeID;
    private float totalCost;
    private boolean delivery;
    private List<Item> listOfItems;
    private float discount;
    private Calendar orderDate;
    private GeoPoint deliveryAddress;
    private GeoPoint storeAddress;
    private boolean reuiresPharmacy;
    private String couponCode;

    /* Constructor */
    public Order(Calendar orderDate, boolean delivery, List<Item> listOfItems, String couponCode) {
        this.orderDate = orderDate;
        this.delivery = delivery;
        this.listOfItems=listOfItems;
    }