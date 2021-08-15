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
 * This object is expected as output
 */
@ApiModel(description = "This object is expected as output")
@Validated
public class DeliveryAssignDriverToDeliveryResponse   {
  @JsonProperty("isAssigned")
  private Boolean isAssigned = null;

  @JsonProperty("message")
  private String message = null;

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
        Objects.equals(this.message, deliveryAssignDriverToDeliveryResponse.message);
  }

  @Override
  public int hashCode() {
    return Objects.hash(isAssigned, message);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DeliveryAssignDriverToDeliveryResponse {\n");
    
    sb.append("    isAssigned: ").append(toIndentedString(isAssigned)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
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
