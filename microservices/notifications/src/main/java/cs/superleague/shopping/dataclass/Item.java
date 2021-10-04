package cs.superleague.shopping.dataclass;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table
public class Item implements Serializable {

    @Id
    private String productID;
    private String name;
    private String barcode;
    private UUID storeID;
    private double price;
    private int quantity;
    private String description;
    private String imageUrl;
    private String brand;
    private String size;
    private String itemType;
    private boolean soldOut;

    public Item() {
    }

    public Item(String name, String productID, String barcode, UUID storeID, double price, int quantity, String description, String imageUrl) {
        this.name = name;
        this.productID = productID;
        this.barcode = barcode;
        this.storeID = storeID;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public Item(String name, String productID, String barcode, UUID storeID, double price, int quantity, String description, String imageUrl, String brand, String size, String itemType) {
        this.name = name;
        this.productID = productID;
        this.barcode = barcode;
        this.storeID = storeID;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.imageUrl = imageUrl;
        this.brand = brand;
        this.itemType = itemType;
        this.size = size;
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

    public UUID getStoreID() {
        return storeID;
    }

    public void setStoreID(UUID storeID) {
        this.storeID = storeID;
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

    public boolean isSoldOut() {
        return soldOut;
    }

    public void setSoldOut(boolean soldOut) {
        this.soldOut = soldOut;
    }
}