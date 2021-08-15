package cs.superleague.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
public class DeliveryAssignDriverToDeliveryRequest   {
  @JsonProperty("driverID")
  private String driverID = null;

  @JsonProperty("deliveryID")
  private String deliveryID = null;

  public DeliveryAssignDriverToDeliveryRequest driverID(String driverID) {
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

  public DeliveryAssignDriverToDeliveryRequest deliveryID(String deliveryID) {
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


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DeliveryAssignDriverToDeliveryRequest deliveryAssignDriverToDeliveryRequest = (DeliveryAssignDriverToDeliveryRequest) o;
    return Objects.equals(this.driverID, deliveryAssignDriverToDeliveryRequest.driverID) &&
        Objects.equals(this.deliveryID, deliveryAssignDriverToDeliveryRequest.deliveryID);
  }

  @Override
  public int hashCode() {
    return Objects.hash(driverID, deliveryID);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DeliveryAssignDriverToDeliveryRequest {\n");
    
    sb.append("    driverID: ").append(toIndentedString(driverID)).append("\n");
    sb.append("    deliveryID: ").append(toIndentedString(deliveryID)).append("\n");
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
