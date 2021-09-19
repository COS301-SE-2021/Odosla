package cs.superleague.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import cs.superleague.models.DriverObject;
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
public class DeliveryGetDeliveryDriverByOrderIdResponse   {
  @JsonProperty("driver")
  private DriverObject driver = null;

  @JsonProperty("message")
  private String message = null;

  @JsonProperty("deliveryID")
  private String deliveryID = null;

  public DeliveryGetDeliveryDriverByOrderIdResponse driver(DriverObject driver) {
    this.driver = driver;
    return this;
  }

  /**
   * Get driver
   * @return driver
  **/
  @ApiModelProperty(value = "")
  
    @Valid
    public DriverObject getDriver() {
    return driver;
  }

  public void setDriver(DriverObject driver) {
    this.driver = driver;
  }

  public DeliveryGetDeliveryDriverByOrderIdResponse message(String message) {
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

  public DeliveryGetDeliveryDriverByOrderIdResponse deliveryID(String deliveryID) {
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
    DeliveryGetDeliveryDriverByOrderIdResponse deliveryGetDeliveryDriverByOrderIdResponse = (DeliveryGetDeliveryDriverByOrderIdResponse) o;
    return Objects.equals(this.driver, deliveryGetDeliveryDriverByOrderIdResponse.driver) &&
        Objects.equals(this.message, deliveryGetDeliveryDriverByOrderIdResponse.message) &&
        Objects.equals(this.deliveryID, deliveryGetDeliveryDriverByOrderIdResponse.deliveryID);
  }

  @Override
  public int hashCode() {
    return Objects.hash(driver, message, deliveryID);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DeliveryGetDeliveryDriverByOrderIdResponse {\n");
    
    sb.append("    driver: ").append(toIndentedString(driver)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
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
