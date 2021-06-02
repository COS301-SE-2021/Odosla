package payment.requests;

import payment.dataclass.GeoPoint;
import payment.dataclass.Item;
import payment.dataclass.OrderType;

import java.util.List;
import java.util.UUID;

public class SubmitOrderRequest {
    /* Attrbiutes */
    private UUID userID;
    private List<Item> listOfItems;
    private Double discount;
    private UUID storeID;
    private OrderType orderType;
    private GeoPoint deliveryAddress;
    private GeoPoint storeAddress;

    public SubmitOrderRequest(UUID userID, List<Item> listOfItems, Double discount, UUID storeID, OrderType orderType, GeoPoint deliveryAddress, GeoPoint storeAddress) {
        this.userID = userID;
        this.listOfItems = listOfItems;
        this.discount = discount;
        this.storeID = storeID;
        this.orderType = orderType;
        this.deliveryAddress=deliveryAddress;
        this.storeAddress=storeAddress;
    }

    public UUID getUserID() {
        return userID;
    }

    public List<Item> getListOfItems() {
        return listOfItems;
    }

    public Double getDiscount() {
        return discount;
    }

    public UUID getStoreID() {
        return storeID;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public GeoPoint getDeliveryAddress() {
        return deliveryAddress;
    }

    public GeoPoint getStoreAddress() {
        return storeAddress;
    }
}
