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
 * This object is expected as output
 */
@ApiModel(description = "This object is expected as output")
@Validated
public class DeliveryAssignDriverToDeliveryResponse   {
  @JsonProperty("isAssigned")
  private Boolean isAssigned = null;

  @JsonProperty("message")
  private String message = null;

  @JsonProperty("pickUpLocation")
  private GeoPointObject pickUpLocation = null;

  @JsonProperty("dropOffLocation")
  private GeoPointObject dropOffLocation = null;

  @JsonProperty("driverID")
  private String driverID = null;

  public DeliveryAssignDriverToDeliveryResponse isAssigned(Boolean isAssigned) {
    this.isAssigned = isAssigned;
    return this;
  }

  /**
   * Get isAssigned
   * @return isAssigned
  **/
  @ApiModelProperty(value = "")
  
    public Boolean isIsAssigned() {
    return isAssigned;
  }

  public void setIsAssigned(Boolean isAssigned) {
    this.isAssigned = isAssigned;
  }

  public DeliveryAssignDriverToDeliveryResponse message(String message) {
    this.message = message;
    return this;
  }

  /**
   * Get message
   * @return message
  **/
  @ApiModelProperty(value = "")
  
    public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public DeliveryAssignDriverToDeliveryResponse pickUpLocation(GeoPointObject pickUpLocation) {
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

  public DeliveryAssignDriverToDeliveryResponse dropOffLocation(GeoPointObject dropOffLocation) {
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

  public DeliveryAssignDriverToDeliveryResponse driverID(String driverID) {
    this.driverID = driverID;
    return this;
  }

  /**
   * Get driverID
   * @return driverID
  **/
  @ApiModelProperty(value = "")
  
    public String getDriverID() {
    return driverID;
  }

  public void setDriverID(String driverID) {
    this.driverID = driverID;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DeliveryAssignDriverToDeliveryResponse deliveryAssignDriverToDeliveryResponse = (DeliveryAssignDriverToDeliveryResponse) o;
    return Objects.equals(this.isAssigned, deliveryAssignDriverToDeliveryResponse.isAssigned) &&
        Objects.equals(this.message, deliveryAssignDriverToDeliveryResponse.message) &&
        Objects.equals(this.pickUpLocation, deliveryAssignDriverToDeliveryResponse.pickUpLocation) &&
        Objects.equals(this.dropOffLocation, deliveryAssignDriverToDeliveryResponse.dropOffLocation) &&
        Objects.equals(this.driverID, deliveryAssignDriverToDeliveryResponse.driverID);
  }

  @Override
  public int hashCode() {
    return Objects.hash(isAssigned, message, pickUpLocation, dropOffLocation, driverID);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DeliveryAssignDriverToDeliveryResponse {\n");
    
    sb.append("    isAssigned: ").append(toIndentedString(isAssigned)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    pickUpLocation: ").append(toIndentedString(pickUpLocation)).append("\n");
    sb.append("    dropOffLocation: ").append(toIndentedString(dropOffLocation)).append("\n");
    sb.append("    driverID: ").append(toIndentedString(driverID)).append("\n");
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
