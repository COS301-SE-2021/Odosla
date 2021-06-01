package order.dataclass;

import user.GeoPoint;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class Order {

    private UUID orderID;
    private UUID userID;
    private UUID storeID;

    public Order(){

    }

    public Order(UUID orderID, UUID userID, UUID storeID, UUID shopperID, Calendar createDate, Calendar orderDate, float totalCost, OrderType type, List<Item> items, float discount, GeoPoint deliveryAddress, GeoPoint storeAddress, boolean requiresPharmacy) {
        this.orderID = orderID;
        this.userID = userID;
        this.storeID = storeID;
        this.shopperID = shopperID;
        this.createDate = createDate;
        this.orderDate = orderDate;
        this.totalCost = totalCost;
        this.type = type;
        this.items = items;
        this.discount = discount;
        this.deliveryAddress = deliveryAddress;
        this.storeAddress = storeAddress;
        this.requiresPharmacy = requiresPharmacy;
    }

    private UUID shopperID;
    private Calendar createDate;
    private Calendar orderDate;
    private float totalCost;
    private OrderType type;
    private List<Item> items;
    private float discount;
    private GeoPoint deliveryAddress;
    private GeoPoint storeAddress;
    private boolean requiresPharmacy;

}