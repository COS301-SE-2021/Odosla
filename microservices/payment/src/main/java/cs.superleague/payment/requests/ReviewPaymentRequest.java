package cs.superleague.payment.requests;

import cs.superleague.payment.dataclass.CartItem;
import cs.superleague.payment.dataclass.OrderType;

import java.util.List;
import java.util.UUID;

public class ReviewPaymentRequest {
    private final List<CartItem> listOfItems;
    private final Double discount;
    private final UUID storeIDOne;
    private final UUID storeIDTwo;
    private final UUID storeIDThree;
    private final OrderType orderType;
    private final Double longitude;
    private final Double latitude;
    private final String address;

    public ReviewPaymentRequest(List<CartItem> listOfItems, Double discount, UUID storeIDOne, UUID storeIDTwo, UUID storeIDThree, OrderType orderType, Double longitude, Double latitude, String address) {
        this.listOfItems = listOfItems;
        this.discount = discount;
        this.storeIDOne = storeIDOne;
        this.storeIDTwo = storeIDTwo;
        this.storeIDThree = storeIDThree;
        this.orderType = orderType;
        this.longitude = longitude;
        this.latitude = latitude;
        this.address = address;
    }

    public List<CartItem> getListOfItems() {
        return listOfItems;
    }

    public Double getDiscount() {
        return discount;
    }

    public UUID getStoreIDOne() {
        return storeIDOne;
    }

    public UUID getStoreIDTwo() {
        return storeIDTwo;
    }

    public UUID getStoreIDThree() {
        return storeIDThree;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public String getAddress() {
        return address;
    }
}
