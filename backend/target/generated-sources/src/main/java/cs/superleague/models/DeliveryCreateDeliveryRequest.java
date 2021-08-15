package cs.superleague.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import cs.superleague.models.GeoPointObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * This object is expected as input
 */
@ApiModel(description = "This object is expected as input")
@Validated
public class DeliveryCreateDeliveryRequest   {
  @JsonProperty("orderID")
  private String orderID = null;

  @JsonProperty("customerID")
  private String customerID = null;

  @JsonProperty("storeID")
  private String storeID = null;

  @JsonProperty("timeOfDelivery")
  private String timeOfDelivery = null;

  @JsonProperty("placeOfDelivery")
  private GeoPointObject placeOfDelivery = null;

  public DeliveryCreateDeliveryRequest orderID(String orderID) {
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

  public DeliveryCreateDeliveryRequest customerID(String customerID) {
    this.customerID = customerID;
    return this;
  }

  /**
   * Get customerID
   * @return customerID
  **/
  @ApiModelProperty(value = "")
  
    public String getCustomerID() {
    return customerID;
  }

  public void setCustomerID(String customerID) {
    this.customerID = customerID;
  }

  public DeliveryCreateDeliveryRequest storeID(String storeID) {
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

  public DeliveryCreateDeliveryRequest timeOfDelivery(String timeOfDelivery) {
    this.timeOfDelivery = timeOfDelivery;
    return this;
  }

  /**
   * Get timeOfDelivery
   * @return timeOfDelivery
  **/
  @ApiModelProperty(value = "")
  
    public String getTimeOfDelivery() {
    return timeOfDelivery;
  }

  public void setTimeOfDelivery(String timeOfDelivery) {
    this.timeOfDelivery = timeOfDelivery;
  }

  public DeliveryCreateDeliveryRequest placeOfDelivery(GeoPointObject placeOfDelivery) {
    this.placeOfDelivery = placeOfDelivery;
    return this;
  }

  /**
   * Get placeOfDelivery
   * @return placeOfDelivery
  **/
  @ApiModelProperty(value = "")
  
    @Valid
    public GeoPointObject getPlaceOfDelivery() {
    return placeOfDelivery;
  }

  public void setPlaceOfDelivery(GeoPointObject placeOfDelivery) {
    this.placeOfDelivery = placeOfDelivery;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DeliveryCreateDeliveryRequest deliveryCreateDeliveryRequest = (DeliveryCreateDeliveryRequest) o;
    return Objects.equals(this.orderID, deliveryCreateDeliveryRequest.orderID) &&
        Objects.equals(this.customerID, deliveryCreateDeliveryRequest.customerID) &&
        Objects.equals(this.storeID, deliveryCreateDeliveryRequest.storeID) &&
        Objects.equals(this.timeOfDelivery, deliveryCreateDeliveryRequest.timeOfDelivery) &&
        Objects.equals(this.placeOfDelivery, deliveryCreateDeliveryRequest.placeOfDelivery);
  }

  @Override
  public int hashCode() {
    return Objects.hash(orderID, customerID, storeID, timeOfDelivery, placeOfDelivery);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DeliveryCreateDeliveryRequest {\n");
    
    sb.append("    orderID: ").append(toIndentedString(orderID)).append("\n");
    sb.append("    customerID: ").append(toIndentedString(customerID)).append("\n");
    sb.append("    storeID: ").append(toIndentedString(storeID)).append("\n");
    sb.append("    timeOfDelivery: ").append(toIndentedString(timeOfDelivery)).append("\n");
    sb.append("    placeOfDelivery: ").append(toIndentedString(placeOfDelivery)).append("\n");
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
