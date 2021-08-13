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
 * This object is expected as input
 */
@ApiModel(description = "This object is expected as input")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-08-13T15:49:46.017974400+02:00[Africa/Harare]")
public class PaymentUpdateOrderRequest   {
  @JsonProperty("orderId")
  private String orderId = null;

  @JsonProperty("userId")
  private String userId = null;

  @JsonProperty("items")
  @Valid
  private List<ItemObject> items = null;

  @JsonProperty("discount")
  private BigDecimal discount = null;

  @JsonProperty("orderType")
  private String orderType = null;

  @JsonProperty("deliveryAddress")
  private String deliveryAddress = null;

  public PaymentUpdateOrderRequest orderId(String orderId) {
    this.orderId = orderId;
    return this;
  }

  /**
   * the id of the order to be generated
   * @return orderId
  **/
  @ApiModelProperty(value = "the id of the order to be generated")
  
    public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  public PaymentUpdateOrderRequest userId(String userId) {
    this.userId = userId;
    return this;
  }

  /**
   * generated token used to identify the caller of the endpoint
   * @return userId
  **/
  @ApiModelProperty(value = "generated token used to identify the caller of the endpoint")
  
    public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public PaymentUpdateOrderRequest items(List<ItemObject> items) {
    this.items = items;
    return this;
  }

  public PaymentUpdateOrderRequest addItemsItem(ItemObject itemsItem) {
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

  public PaymentUpdateOrderRequest discount(BigDecimal discount) {
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

  public PaymentUpdateOrderRequest orderType(String orderType) {
    this.orderType = orderType;
    return this;
  }

  /**
   * Get orderType
   * @return orderType
  **/
  @ApiModelProperty(value = "")
  
    public String getOrderType() {
    return orderType;
  }

  public void setOrderType(String orderType) {
    this.orderType = orderType;
  }

  public PaymentUpdateOrderRequest deliveryAddress(String deliveryAddress) {
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


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PaymentUpdateOrderRequest paymentUpdateOrderRequest = (PaymentUpdateOrderRequest) o;
    return Objects.equals(this.orderId, paymentUpdateOrderRequest.orderId) &&
        Objects.equals(this.userId, paymentUpdateOrderRequest.userId) &&
        Objects.equals(this.items, paymentUpdateOrderRequest.items) &&
        Objects.equals(this.discount, paymentUpdateOrderRequest.discount) &&
        Objects.equals(this.orderType, paymentUpdateOrderRequest.orderType) &&
        Objects.equals(this.deliveryAddress, paymentUpdateOrderRequest.deliveryAddress);
  }

  @Override
  public int hashCode() {
    return Objects.hash(orderId, userId, items, discount, orderType, deliveryAddress);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PaymentUpdateOrderRequest {\n");
    
    sb.append("    orderId: ").append(toIndentedString(orderId)).append("\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
    sb.append("    discount: ").append(toIndentedString(discount)).append("\n");
    sb.append("    orderType: ").append(toIndentedString(orderType)).append("\n");
    sb.append("    deliveryAddress: ").append(toIndentedString(deliveryAddress)).append("\n");
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
