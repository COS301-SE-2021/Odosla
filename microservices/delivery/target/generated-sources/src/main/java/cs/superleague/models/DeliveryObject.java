package cs.superleague.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import cs.superleague.models.DeliveryDetailObject;
import cs.superleague.models.GeoPointObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * This is a delivery object
 */
@ApiModel(description = "This is a delivery object")
@Validated
public class DeliveryObject   {
  @JsonProperty("deliveryID")
  private String deliveryID = null;

  @JsonProperty("pickUpLocation")
  private GeoPointObject pickUpLocation = null;

  @JsonProperty("dropOffLocation")
  private GeoPointObject dropOffLocation = null;

  @JsonProperty("orderID")
  private String orderID = null;

  @JsonProperty("customerId")
  private String customerId = null;

  @JsonProperty("storeId")
  private String storeId = null;

  @JsonProperty("driverId")
  private String driverId = null;

  @JsonProperty("status")
  private String status = null;

  @JsonProperty("cost")
  private BigDecimal cost = null;

  @JsonProperty("completed")
  private Boolean completed = null;

  @JsonProperty("deliveryDetail")
  @Valid
  private List<DeliveryDetailObject> deliveryDetail = null;

  public DeliveryObject deliveryID(String deliveryID) {
    this.deliveryID = deliveryID;
    return this;
  }

  /**
   * Get deliveryID
   * @return deliveryID
  **/
  @ApiModelProperty(value = "")
  
    public String getDeliveryID() {
    return deliveryID;
  }

  public void setDeliveryID(String deliveryID) {
    this.deliveryID = deliveryID;
  }

  public DeliveryObject pickUpLocation(GeoPointObject pickUpLocation) {
    this.pickUpLocation = pickUpLocation;
    return this;
  }

  /**
   * Get pickUpLocation
   * @return pickUpLocation
  **/
  @ApiModelProperty(value = "")
  
    @Valid
    public GeoPointObject getPickUpLocation() {
    return pickUpLocation;
  }

  public void setPickUpLocation(GeoPointObject pickUpLocation) {
    this.pickUpLocation = pickUpLocation;
  }

  public DeliveryObject dropOffLocation(GeoPointObject dropOffLocation) {
    this.dropOffLocation = dropOffLocation;
    return this;
  }

  /**
   * Get dropOffLocation
   * @return dropOffLocation
  **/
  @ApiModelProperty(value = "")
  
    @Valid
    public GeoPointObject getDropOffLocation() {
    return dropOffLocation;
  }

  public void setDropOffLocation(GeoPointObject dropOffLocation) {
    this.dropOffLocation = dropOffLocation;
  }

  public DeliveryObject orderID(String orderID) {
    this.orderID = orderID;
    return this;
  }

  /**
   * Get orderID
   * @return orderID
  **/
  @ApiModelProperty(value = "")
  
    public String getOrderID() {
    return orderID;
  }

  public void setOrderID(String orderID) {
    this.orderID = orderID;
  }

  public DeliveryObject customerId(String customerId) {
    this.customerId = customerId;
    return this;
  }

  /**
   * Get customerId
   * @return customerId
  **/
  @ApiModelProperty(value = "")
  
    public String getCustomerId() {
    return customerId;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  public DeliveryObject storeId(String storeId) {
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

  public DeliveryObject driverId(String driverId) {
    this.driverId = driverId;
    return this;
  }

  /**
   * Get driverId
   * @return driverId
  **/
  @ApiModelProperty(value = "")
  
    public String getDriverId() {
    return driverId;
  }

  public void setDriverId(String driverId) {
    this.driverId = driverId;
  }

  public DeliveryObject status(String status) {
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

  public DeliveryObject cost(BigDecimal cost) {
    this.cost = cost;
    return this;
  }

  /**
   * Get cost
   * @return cost
  **/
  @ApiModelProperty(value = "")
  
    @Valid
    public BigDecimal getCost() {
    return cost;
  }

  public void setCost(BigDecimal cost) {
    this.cost = cost;
  }

  public DeliveryObject completed(Boolean completed) {
    this.completed = completed;
    return this;
  }

  /**
   * Get completed
   * @return completed
  **/
  @ApiModelProperty(value = "")
  
    public Boolean isCompleted() {
    return completed;
  }

  public void setCompleted(Boolean completed) {
    this.completed = completed;
  }

  public DeliveryObject deliveryDetail(List<DeliveryDetailObject> deliveryDetail) {
    this.deliveryDetail = deliveryDetail;
    return this;
  }

  public DeliveryObject addDeliveryDetailItem(DeliveryDetailObject deliveryDetailItem) {
    if (this.deliveryDetail == null) {
      this.deliveryDetail = new ArrayList<DeliveryDetailObject>();
    }
    this.deliveryDetail.add(deliveryDetailItem);
    return this;
  }

  /**
   * Get deliveryDetail
   * @return deliveryDetail
  **/
  @ApiModelProperty(value = "")
      @Valid
    public List<DeliveryDetailObject> getDeliveryDetail() {
    return deliveryDetail;
  }

  public void setDeliveryDetail(List<DeliveryDetailObject> deliveryDetail) {
    this.deliveryDetail = deliveryDetail;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DeliveryObject deliveryObject = (DeliveryObject) o;
    return Objects.equals(this.deliveryID, deliveryObject.deliveryID) &&
        Objects.equals(this.pickUpLocation, deliveryObject.pickUpLocation) &&
        Objects.equals(this.dropOffLocation, deliveryObject.dropOffLocation) &&
        Objects.equals(this.orderID, deliveryObject.orderID) &&
        Objects.equals(this.customerId, deliveryObject.customerId) &&
        Objects.equals(this.storeId, deliveryObject.storeId) &&
        Objects.equals(this.driverId, deliveryObject.driverId) &&
        Objects.equals(this.status, deliveryObject.status) &&
        Objects.equals(this.cost, deliveryObject.cost) &&
        Objects.equals(this.completed, deliveryObject.completed) &&
        Objects.equals(this.deliveryDetail, deliveryObject.deliveryDetail);
  }

  @Override
  public int hashCode() {
    return Objects.hash(deliveryID, pickUpLocation, dropOffLocation, orderID, customerId, storeId, driverId, status, cost, completed, deliveryDetail);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DeliveryObject {\n");
    
    sb.append("    deliveryID: ").append(toIndentedString(deliveryID)).append("\n");
    sb.append("    pickUpLocation: ").append(toIndentedString(pickUpLocation)).append("\n");
    sb.append("    dropOffLocation: ").append(toIndentedString(dropOffLocation)).append("\n");
    sb.append("    orderID: ").append(toIndentedString(orderID)).append("\n");
    sb.append("    customerId: ").append(toIndentedString(customerId)).append("\n");
    sb.append("    storeId: ").append(toIndentedString(storeId)).append("\n");
    sb.append("    driverId: ").append(toIndentedString(driverId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    cost: ").append(toIndentedString(cost)).append("\n");
    sb.append("    completed: ").append(toIndentedString(completed)).append("\n");
    sb.append("    deliveryDetail: ").append(toIndentedString(deliveryDetail)).append("\n");
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
