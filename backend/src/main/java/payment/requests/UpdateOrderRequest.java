package payment.requests;

import payment.dataclass.GeoPoint;
import payment.dataclass.OrderStatus;
import payment.dataclass.OrderType;
import shopping.dataclass.Item;

import javax.persistence.CascadeType;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;
import java.util.List;
import java.util.UUID;

public class UpdateOrderRequest {
    /** attrbiutes */
    private UUID orderID;
    private UUID userID;
    private List<Item> listOfItems;
    private Double discount;
    private UUID storeID;
    private OrderType orderType;
    private GeoPoint deliveryAddress;
    private GeoPoint storeAddress;
    private OrderType type;

    public UpdateOrderRequest(){}
    /** constructor
     * @param orderID - unique order identifier
     * @param userID - unique order identifier of user creating order
     * @param listOfItems - list of items in the order of object type Item
     * @param discount - the amount of the discount
     * @param storeID - the store id of where the order will be placed
     * @param orderType - the type of order it is, whether it is a delivery or collection
     * @param deliveryAddress - the GeoPoint address of where the order is to be shipped if order type is delivery
     * @param storeAddress - the GeoPoint address of the store where order is being placed
     */
    public UpdateOrderRequest(UUID orderID, UUID userID, List<Item> listOfItems, Double discount, UUID storeID, OrderType orderType, GeoPoint deliveryAddress, GeoPoint storeAddress) {
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

    public Double getDiscount() {
        return discount;
    }

    public List<Item> getListOfItems() {
        return listOfItems;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public UUID getStoreID() {
        return storeID;
    }

    public GeoPoint getDeliveryAddress() {
        return deliveryAddress;
    }

    public GeoPoint getStoreAddress() {
        return storeAddress;
    }

    public void setUserID(UUID userID) {
        this.userID = userID;
    }

    public void setDeliveryAddress(GeoPoint deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public void setListOfItems(List<Item> listOfItems) {
        this.listOfItems = listOfItems;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public void setStoreID(UUID storeID) {
        this.storeID = storeID;
    }

    public void setStoreAddress(GeoPoint storeAddress) {
        this.storeAddress = storeAddress;
    }
}
