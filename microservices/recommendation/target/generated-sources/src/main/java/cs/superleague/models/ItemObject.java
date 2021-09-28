package cs.superleague.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Generic schema for an item
 */
@ApiModel(description = "Generic schema for an item")
@Validated
public class ItemObject   {
  @JsonProperty("productID")
  private String productID = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("barcode")
  private String barcode = null;

  @JsonProperty("storeID")
  private String storeID = null;

  @JsonProperty("price")
  private BigDecimal price = null;

  @JsonProperty("quantity")
  private Integer quantity = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("imageUrl")
  private String imageUrl = null;

  @JsonProperty("brand")
  private String brand = null;

  @JsonProperty("size")
  private String size = null;

  @JsonProperty("itemType")
  private String itemType = null;

  public ItemObject productID(String productID) {
    this.productID = productID;
    return this;
  }

  /**
   * Get productID
   * @return productID
  **/
  @ApiModelProperty(value = "")
  
    public String getProductID() {
    return productID;
  }

  public void setProductID(String productID) {
    this.productID = productID;
  }

  public ItemObject name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
  **/
  @ApiModelProperty(value = "")
  
    public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ItemObject barcode(String barcode) {
    this.barcode = barcode;
    return this;
  }

  /**
   * Get barcode
   * @return barcode
  **/
  @ApiModelProperty(value = "")
  
    public String getBarcode() {
    return barcode;
  }

  public void setBarcode(String barcode) {
    this.barcode = barcode;
  }

  public ItemObject storeID(String storeID) {
    this.storeID = storeID;
    return this;
  }

  /**
   * Get storeID
   * @return storeID
  **/
  @ApiModelProperty(value = "")
  
    public String getStoreID() {
    return storeID;
  }

  public void setStoreID(String storeID) {
    this.storeID = storeID;
  }

  public ItemObject price(BigDecimal price) {
    this.price = price;
    return this;
  }

  /**
   * Get price
   * @return price
  **/
  @ApiModelProperty(value = "")
  
    @Valid
    public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public ItemObject quantity(Integer quantity) {
    this.quantity = quantity;
    return this;
  }

  /**
   * Get quantity
   * @return quantity
  **/
  @ApiModelProperty(value = "")
  
    public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public ItemObject description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
  **/
  @ApiModelProperty(value = "")
  
    public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public ItemObject imageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
    return this;
  }

  /**
   * Get imageUrl
   * @return imageUrl
  **/
  @ApiModelProperty(value = "")
  
    public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public ItemObject brand(String brand) {
    this.brand = brand;
    return this;
  }

  /**
   * Get brand
   * @return brand
  **/
  @ApiModelProperty(value = "")
  
    public String getBrand() {
    return brand;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }

  public ItemObject size(String size) {
    this.size = size;
    return this;
  }

  /**
   * Get size
   * @return size
  **/
  @ApiModelProperty(value = "")
  
    public String getSize() {
    return size;
  }

  public void setSize(String size) {
    this.size = size;
  }

  public ItemObject itemType(String itemType) {
    this.itemType = itemType;
    return this;
  }

  /**
   * Get itemType
   * @return itemType
  **/
  @ApiModelProperty(value = "")
  
    public String getItemType() {
    return itemType;
  }

  public void setItemType(String itemType) {
    this.itemType = itemType;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ItemObject itemObject = (ItemObject) o;
    return Objects.equals(this.productID, itemObject.productID) &&
        Objects.equals(this.name, itemObject.name) &&
        Objects.equals(this.barcode, itemObject.barcode) &&
        Objects.equals(this.storeID, itemObject.storeID) &&
        Objects.equals(this.price, itemObject.price) &&
        Objects.equals(this.quantity, itemObject.quantity) &&
        Objects.equals(this.description, itemObject.description) &&
        Objects.equals(this.imageUrl, itemObject.imageUrl) &&
        Objects.equals(this.brand, itemObject.brand) &&
        Objects.equals(this.size, itemObject.size) &&
        Objects.equals(this.itemType, itemObject.itemType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(productID, name, barcode, storeID, price, quantity, description, imageUrl, brand, size, itemType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ItemObject {\n");
    
    sb.append("    productID: ").append(toIndentedString(productID)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    barcode: ").append(toIndentedString(barcode)).append("\n");
    sb.append("    storeID: ").append(toIndentedString(storeID)).append("\n");
    sb.append("    price: ").append(toIndentedString(price)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    imageUrl: ").append(toIndentedString(imageUrl)).append("\n");
    sb.append("    brand: ").append(toIndentedString(brand)).append("\n");
    sb.append("    size: ").append(toIndentedString(size)).append("\n");
    sb.append("    itemType: ").append(toIndentedString(itemType)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
