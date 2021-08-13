package cs.superleague.payment.requests;

import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.payment.dataclass.OrderType;
import cs.superleague.shopping.dataclass.Item;

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
    private OrderType orderType;
    private GeoPoint deliveryAddress;

    public UpdateOrderRequest(){}
    /** constructor
     * @param orderID - unique order identifier
     * @param userID - unique order identifier of user creating order
     * @param listOfItems - list of items in the order of object type Item
     * @param discount - the amount of the discount
     * @param orderType - the type of order it is, whether it is a delivery or collection
     * @param deliveryAddress - the GeoPoint address of where the order is to be shipped if order type is delivery
     */
    public UpdateOrderRequest(UUID orderID, UUID userID, List<Item> listOfItems, Double discount, OrderType orderType, GeoPoint deliveryAddress) {
        this.orderID = orderID;
        this.userID = userID;
        this.listOfItems = listOfItems;
        this.discount = discount;
        this.orderType = orderType;
        this.deliveryAddress=deliveryAddress;
    }

    public UpdateOrderRequest(UUID userID, List<Item> listOfItems, Double discount, OrderType orderType, GeoPoint deliveryAddress) {
        this.userID = userID;
        this.listOfItems = listOfItems;
        this.discount = discount;
        this.orderType = orderType;
        this.deliveryAddress=deliveryAddress;
    }

    public UUID getOrderID() {
        return orderID;
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

    public GeoPoint getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setOrderID(UUID orderID) {
        this.orderID = orderID;
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
}
