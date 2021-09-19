package cs.superleague.payment.dataclass;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
public class CartItem implements Serializable {

    @Id
    private UUID cartItemNo;

    @JsonProperty("productId")
    private String productID;

//    @ManyToOne
//    @JoinColumn(name="order_id", insertable = false)
//    private Order order;

    private String name;
    private String barcode;

    @JsonProperty("storeId")
    private UUID storeID;

    @JsonProperty("orderId")
    private UUID orderID;

    private double price;
    private int quantity;
    private double totalCost;
    private String description;
    private String imageUrl;
    private String brand;
    private String size;
    private String itemType;


    public CartItem(){ }

    public CartItem(String name, String productID, String barcode, UUID orderID, double price, int quantity, String description, String imageUrl) {
        this.name = name;
        this.productID = productID;
        this.barcode = barcode;
        this.orderID = orderID;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public CartItem(String name, String productID, String barcode, UUID orderID, double price, int quantity, String description, String imageUrl, String brand, String size, String itemType, Double totalCost, UUID storeID) {
        this.name = name;
        this.productID = productID;
        this.barcode = barcode;
        this.orderID = orderID;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.imageUrl = imageUrl;
        this.brand=brand;
        this.itemType=itemType;
        this.size=size;
        this.totalCost= totalCost;
        this.storeID = storeID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public UUID getOrderID() {
        return orderID;
    }

    public void setOrderID(UUID orderID) {
        this.orderID = orderID;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public UUID getStoreID() {
        return storeID;
    }

    public void setStoreID(UUID storeID) {
        this.storeID = storeID;
    }

    public UUID getCartItemNo() {
        return cartItemNo;
    }

    public void setCartItemNo(UUID cartItemNo) {
        this.cartItemNo = cartItemNo;
    }


}
