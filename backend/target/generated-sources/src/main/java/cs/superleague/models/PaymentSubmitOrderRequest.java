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
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-08-14T16:05:50.792457600+02:00[Africa/Harare]")
public class PaymentSubmitOrderRequest   {
  @JsonProperty("userId")
  private String userId = null;

  @JsonProperty("listOfItems")
  @Valid
  private List<ItemObject> listOfItems = null;

  @JsonProperty("discount")
  private BigDecimal discount = null;

  @JsonProperty("storeId")
  private String storeId = null;

  @JsonProperty("orderType")
  private String orderType = null;

  @JsonProperty("latitude")
  private BigDecimal latitude = null;

  @JsonProperty("longitude")
  private BigDecimal longitude = null;

  @JsonProperty("deliveryAddress")
  private String deliveryAddress = null;

  public PaymentSubmitOrderRequest userId(String userId) {
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

  public PaymentSubmitOrderRequest listOfItems(List<ItemObject> listOfItems) {
    this.listOfItems = listOfItems;
    return this;
  }

  public PaymentSubmitOrderRequest addListOfItemsItem(ItemObject listOfItemsItem) {
    if (this.listOfItems == null) {
      this.listOfItems = new ArrayList<ItemObject>();
    }
    this.listOfItems.add(listOfItemsItem);
    return this;
  }

  /**
   * Get listOfItems
   * @return listOfItems
  **/
  @ApiModelProperty(value = "")
      @Valid
    public List<ItemObject> getListOfItems() {
    return listOfItems;
  }

  public void setListOfItems(List<ItemObject> listOfItems) {
    this.listOfItems = listOfItems;
  }

  public PaymentSubmitOrderRequest discount(BigDecimal discount) {
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

  public PaymentSubmitOrderRequest storeId(String storeId) {
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

  public PaymentSubmitOrderRequest orderType(String orderType) {
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

  public PaymentSubmitOrderRequest latitude(BigDecimal latitude) {
    this.latitude = latitude;
    return this;
  }

  /**
   * Get latitude
   * @return latitude
  **/
  @ApiModelProperty(value = "")
  
    @Valid
    public BigDecimal getLatitude() {
    return latitude;
  }

  public void setLatitude(BigDecimal latitude) {
    this.latitude = latitude;
  }

  public PaymentSubmitOrderRequest longitude(BigDecimal longitude) {
    this.longitude = longitude;
    return this;
  }

  /**
   * Get longitude
   * @return longitude
  **/
  @ApiModelProperty(value = "")
  
    @Valid
    public BigDecimal getLongitude() {
    return longitude;
  }

  public void setLongitude(BigDecimal longitude) {
    this.longitude = longitude;
  }

  public PaymentSubmitOrderRequest deliveryAddress(String deliveryAddress) {
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
    PaymentSubmitOrderRequest paymentSubmitOrderRequest = (PaymentSubmitOrderRequest) o;
    return Objects.equals(this.userId, paymentSubmitOrderRequest.userId) &&
        Objects.equals(this.listOfItems, paymentSubmitOrderRequest.listOfItems) &&
        Objects.equals(this.discount, paymentSubmitOrderRequest.discount) &&
        Objects.equals(this.storeId, paymentSubmitOrderRequest.storeId) &&
        Objects.equals(this.orderType, paymentSubmitOrderRequest.orderType) &&
        Objects.equals(this.latitude, paymentSubmitOrderRequest.latitude) &&
        Objects.equals(this.longitude, paymentSubmitOrderRequest.longitude) &&
        Objects.equals(this.deliveryAddress, paymentSubmitOrderRequest.deliveryAddress);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, listOfItems, discount, storeId, orderType, latitude, longitude, deliveryAddress);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PaymentSubmitOrderRequest {\n");
    
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    listOfItems: ").append(toIndentedString(listOfItems)).append("\n");
    sb.append("    discount: ").append(toIndentedString(discount)).append("\n");
    sb.append("    storeId: ").append(toIndentedString(storeId)).append("\n");
    sb.append("    orderType: ").append(toIndentedString(orderType)).append("\n");
    sb.append("    latitude: ").append(toIndentedString(latitude)).append("\n");
    sb.append("    longitude: ").append(toIndentedString(longitude)).append("\n");
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
