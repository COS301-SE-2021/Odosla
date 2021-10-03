package cs.superleague.shopping.requests;

import cs.superleague.payment.dataclass.CartItem;

import java.util.List;
import java.util.UUID;

public class PriceCheckWithDeliveryRequest {
    private List<CartItem> cartItems;
    private UUID storeIDOne;
    private UUID storeIDTwo;
    private UUID storeIDThree;
    private String address;
    private double longitude;
    private double latitude;

    public PriceCheckWithDeliveryRequest(List<CartItem> cartItems, UUID storeIDOne, UUID storeIDTwo, UUID storeIDThree, String address, double longitude, double latitude) {
        this.cartItems = cartItems;
        this.storeIDOne = storeIDOne;
        this.storeIDTwo = storeIDTwo;
        this.storeIDThree = storeIDThree;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public UUID getStoreIDOne() {
        return storeIDOne;
    }

    public void setStoreIDOne(UUID storeIDOne) {
        this.storeIDOne = storeIDOne;
    }

    public UUID getStoreIDTwo() {
        return storeIDTwo;
    }

    public void setStoreIDTwo(UUID storeIDTwo) {
        this.storeIDTwo = storeIDTwo;
    }

    public UUID getStoreIDThree() {
        return storeIDThree;
    }

    public void setStoreIDThree(UUID storeIDThree) {
        this.storeIDThree = storeIDThree;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
