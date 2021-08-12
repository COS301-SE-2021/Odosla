package cs.superleague.payment.requests;

import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.payment.dataclass.OrderType;

import java.util.List;
import java.util.UUID;

public class SubmitOrderRequest {

    /** attrbiutes */
    private UUID userID;
    private List<Item> listOfItems;
    private Double discount;
    private UUID storeID;
    private OrderType orderType;

    /** constructor
     * @param userID - unique order identifier of cs.superleague.user creating order
     * @param listOfItems - list of items in the order of object type Item
     * @param discount - the amount of the discount
     * @param storeID - the store id of where the order will be placed
     * @param orderType - the type of order it is, whether it is a cs.superleague.delivery or collection
     */
    public SubmitOrderRequest(UUID userID, List<Item> listOfItems, Double discount, UUID storeID, OrderType orderType) {
        this.userID = userID;
        this.listOfItems = listOfItems;
        this.discount = discount;
        this.storeID = storeID;
        this.orderType = orderType;
    }

    /* getters */
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

}
