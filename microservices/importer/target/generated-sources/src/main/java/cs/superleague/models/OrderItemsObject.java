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
 * Generic schema for an order item
 */
@ApiModel(description = "Generic schema for an order item")
@Validated
public class OrderItemsObject   {
  @JsonProperty("productId")
  private String productId = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("barcode")
  private String barcode = null;

  @JsonProperty("orderId")
  private String orderId = null;

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

  @JsonProperty("totalCost")
  private BigDecimal totalCost = null;

  public OrderItemsObject productId(String productId) {
    this.productId = productId;
    return this;
  }

  /**
   * Get productId
   * @return productId
  **/
  @ApiModelProperty(value = "")
  
    public String getProductId() {
    return productId;
  }

  public void setProductId(String productId) {
    this.productId = productId;
  }

  public OrderItemsObject name(String name) {
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

  public OrderItemsObject barcode(String barcode) {
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

  public OrderItemsObject orderId(String orderId) {
    this.orderId = orderId;
    return this;
  }

  /**
   * Get orderId
   * @return orderId
  **/
  @ApiModelProperty(value = "")
  
    public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  public OrderItemsObject price(BigDecimal price) {
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

  public OrderItemsObject quantity(Integer quantity) {
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

  public OrderItemsObject description(String description) {
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

  public OrderItemsObject imageUrl(String imageUrl) {
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

  public OrderItemsObject brand(String brand) {
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

  public OrderItemsObject size(String size) {
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

  public OrderItemsObject itemType(String itemType) {
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

  public OrderItemsObject totalCost(BigDecimal totalCost) {
    this.totalCost = totalCost;
    return this;
  }

  /**
   * Get totalCost
   * @return totalCost
  **/
  @ApiModelProperty(value = "")
  
    @Valid
    public BigDecimal getTotalCost() {
    return totalCost;
  }

  public void setTotalCost(BigDecimal totalCost) {
    this.totalCost = totalCost;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OrderItemsObject orderItemsObject = (OrderItemsObject) o;
    return Objects.equals(this.productId, orderItemsObject.productId) &&
        Objects.equals(this.name, orderItemsObject.name) &&
        Objects.equals(this.barcode, orderItemsObject.barcode) &&
        Objects.equals(this.orderId, orderItemsObject.orderId) &&
        Objects.equals(this.price, orderItemsObject.price) &&
        Objects.equals(this.quantity, orderItemsObject.quantity) &&
        Objects.equals(this.description, orderItemsObject.description) &&
        Objects.equals(this.imageUrl, orderItemsObject.imageUrl) &&
        Objects.equals(this.brand, orderItemsObject.brand) &&
        Objects.equals(this.size, orderItemsObject.size) &&
        Objects.equals(this.itemType, orderItemsObject.itemType) &&
        Objects.equals(this.totalCost, orderItemsObject.totalCost);
  }

  @Override
  public int hashCode() {
    return Objects.hash(productId, name, barcode, orderId, price, quantity, description, imageUrl, brand, size, itemType, totalCost);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OrderItemsObject {\n");
    
    sb.append("    productId: ").append(toIndentedString(productId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    barcode: ").append(toIndentedString(barcode)).append("\n");
    sb.append("    orderId: ").append(toIndentedString(orderId)).append("\n");
    sb.append("    price: ").append(toIndentedString(price)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    imageUrl: ").append(toIndentedString(imageUrl)).append("\n");
    sb.append("    brand: ").append(toIndentedString(brand)).append("\n");
    sb.append("    size: ").append(toIndentedString(size)).append("\n");
    sb.append("    itemType: ").append(toIndentedString(itemType)).append("\n");
    sb.append("    totalCost: ").append(toIndentedString(totalCost)).append("\n");
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
