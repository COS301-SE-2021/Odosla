package cs.superleague.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import cs.superleague.models.ItemObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Generic schema for an Order
 */
@ApiModel(description = "Generic schema for an Order")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-08-14T15:01:46.157134600+02:00[Africa/Harare]")
public class OrderObject   {
  @JsonProperty("orderId")
  private String orderId = null;

  @JsonProperty("userId")
  private String userId = null;

  @JsonProperty("storeId")
  private String storeId = null;

  @JsonProperty("shopperId")
  private String shopperId = null;

  @JsonProperty("createDate")
  private String createDate = null;

  @JsonProperty("processDate")
  private String processDate = null;

  @JsonProperty("totalPrice")
  private BigDecimal totalPrice = null;

  @JsonProperty("status")
  private String status = null;

  @JsonProperty("items")
  @Valid
  private List<ItemObject> items = null;

  @JsonProperty("discount")
  private BigDecimal discount = null;

  @JsonProperty("deliveryAddress")
  private String deliveryAddress = null;

  @JsonProperty("storeAddress")
  private String storeAddress = null;

  @JsonProperty("requiresPharmacy")
  private Boolean requiresPharmacy = null;

  public OrderObject orderId(String orderId) {
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

  public OrderObject userId(String userId) {
    this.userId = userId;
    return this;
  }

  /**
   * Get userId
   * @return userId
  **/
  @ApiModelProperty(value = "")
  
    public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public OrderObject storeId(String storeId) {
    this.storeId = storeId;
    return this;
  }

  /**
   * Get storeId
   * @return storeId
  **/
  @ApiModelProperty(value = "")
  
    public String getStoreId() {
    return storeId;
  }

  public void setStoreId(String storeId) {
    this.storeId = storeId;
  }

  public OrderObject shopperId(String shopperId) {
    this.shopperId = shopperId;
    return this;
  }

  /**
   * Get shopperId
   * @return shopperId
  **/
  @ApiModelProperty(value = "")
  
    public String getShopperId() {
    return shopperId;
  }

  public void setShopperId(String shopperId) {
    this.shopperId = shopperId;
  }

  public OrderObject createDate(String createDate) {
    this.createDate = createDate;
    return this;
  }

  /**
   * Get createDate
   * @return createDate
  **/
  @ApiModelProperty(value = "")
  
    public String getCreateDate() {
    return createDate;
  }

  public void setCreateDate(String createDate) {
    this.createDate = createDate;
  }

  public OrderObject processDate(String processDate) {
    this.processDate = processDate;
    return this;
  }

  /**
   * Get processDate
   * @return processDate
  **/
  @ApiModelProperty(value = "")
  
    public String getProcessDate() {
    return processDate;
  }

  public void setProcessDate(String processDate) {
    this.processDate = processDate;
  }

  public OrderObject totalPrice(BigDecimal totalPrice) {
    this.totalPrice = totalPrice;
    return this;
  }

  /**
   * Get totalPrice
   * @return totalPrice
  **/
  @ApiModelProperty(value = "")
  
    @Valid
    public BigDecimal getTotalPrice() {
    return totalPrice;
  }

  public void setTotalPrice(BigDecimal totalPrice) {
    this.totalPrice = totalPrice;
  }

  public OrderObject status(String status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
  **/
  @ApiModelProperty(value = "")
  
    public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public OrderObject items(List<ItemObject> items) {
    this.items = items;
    return this;
  }

  public OrderObject addItemsItem(ItemObject itemsItem) {
    if (this.items == null) {
      this.items = new ArrayList<ItemObject>();
    }
    this.items.add(itemsItem);
    return this;
  }

  /**
   * Get items
   * @return items
  **/
  @ApiModelProperty(value = "")
      @Valid
    public List<ItemObject> getItems() {
    return items;
  }

  public void setItems(List<ItemObject> items) {
    this.items = items;
  }

  public OrderObject discount(BigDecimal discount) {
    this.discount = discount;
    return this;
  }

  /**
   * Get discount
   * @return discount
  **/
  @ApiModelProperty(value = "")
  
    @Valid
    public BigDecimal getDiscount() {
    return discount;
  }

  public void setDiscount(BigDecimal discount) {
    this.discount = discount;
  }

  public OrderObject deliveryAddress(String deliveryAddress) {
    this.deliveryAddress = deliveryAddress;
    return this;
  }

  /**
   * Get deliveryAddress
   * @return deliveryAddress
  **/
  @ApiModelProperty(value = "")
  
    public String getDeliveryAddress() {
    return deliveryAddress;
  }

  public void setDeliveryAddress(String deliveryAddress) {
    this.deliveryAddress = deliveryAddress;
  }

  public OrderObject storeAddress(String storeAddress) {
    this.storeAddress = storeAddress;
    return this;
  }

  /**
   * Get storeAddress
   * @return storeAddress
  **/
  @ApiModelProperty(value = "")
  
    public String getStoreAddress() {
    return storeAddress;
  }

  public void setStoreAddress(String storeAddress) {
    this.storeAddress = storeAddress;
  }

  public OrderObject requiresPharmacy(Boolean requiresPharmacy) {
    this.requiresPharmacy = requiresPharmacy;
    return this;
  }

  /**
   * Get requiresPharmacy
   * @return requiresPharmacy
  **/
  @ApiModelProperty(value = "")
  
    public Boolean isRequiresPharmacy() {
    return requiresPharmacy;
  }

  public void setRequiresPharmacy(Boolean requiresPharmacy) {
    this.requiresPharmacy = requiresPharmacy;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OrderObject orderObject = (OrderObject) o;
    return Objects.equals(this.orderId, orderObject.orderId) &&
        Objects.equals(this.userId, orderObject.userId) &&
        Objects.equals(this.storeId, orderObject.storeId) &&
        Objects.equals(this.shopperId, orderObject.shopperId) &&
        Objects.equals(this.createDate, orderObject.createDate) &&
        Objects.equals(this.processDate, orderObject.processDate) &&
        Objects.equals(this.totalPrice, orderObject.totalPrice) &&
        Objects.equals(this.status, orderObject.status) &&
        Objects.equals(this.items, orderObject.items) &&
        Objects.equals(this.discount, orderObject.discount) &&
        Objects.equals(this.deliveryAddress, orderObject.deliveryAddress) &&
        Objects.equals(this.storeAddress, orderObject.storeAddress) &&
        Objects.equals(this.requiresPharmacy, orderObject.requiresPharmacy);
  }

  @Override
  public int hashCode() {
    return Objects.hash(orderId, userId, storeId, shopperId, createDate, processDate, totalPrice, status, items, discount, deliveryAddress, storeAddress, requiresPharmacy);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OrderObject {\n");
    
    sb.append("    orderId: ").append(toIndentedString(orderId)).append("\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    storeId: ").append(toIndentedString(storeId)).append("\n");
    sb.append("    shopperId: ").append(toIndentedString(shopperId)).append("\n");
    sb.append("    createDate: ").append(toIndentedString(createDate)).append("\n");
    sb.append("    processDate: ").append(toIndentedString(processDate)).append("\n");
    sb.append("    totalPrice: ").append(toIndentedString(totalPrice)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
    sb.append("    discount: ").append(toIndentedString(discount)).append("\n");
    sb.append("    deliveryAddress: ").append(toIndentedString(deliveryAddress)).append("\n");
    sb.append("    storeAddress: ").append(toIndentedString(storeAddress)).append("\n");
    sb.append("    requiresPharmacy: ").append(toIndentedString(requiresPharmacy)).append("\n");
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
