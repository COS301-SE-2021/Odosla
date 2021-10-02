package cs.superleague.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import cs.superleague.models.DeliveryObject;
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
public class DeliveryGetDeliveryByUUIDResponse   {
  @JsonProperty("deliveryEntity")
  private DeliveryObject deliveryEntity = null;

  @JsonProperty("message")
  private String message = null;

  @JsonProperty("timestamp")
  private String timestamp = null;

  public DeliveryGetDeliveryByUUIDResponse deliveryEntity(DeliveryObject deliveryEntity) {
    this.deliveryEntity = deliveryEntity;
    return this;
  }

  /**
   * Get deliveryEntity
   * @return deliveryEntity
  **/
  @ApiModelProperty(value = "")
  
    @Valid
    public DeliveryObject getDeliveryEntity() {
    return deliveryEntity;
  }

  public void setDeliveryEntity(DeliveryObject deliveryEntity) {
    this.deliveryEntity = deliveryEntity;
  }

  public DeliveryGetDeliveryByUUIDResponse message(String message) {
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

  public DeliveryGetDeliveryByUUIDResponse timestamp(String timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Get timestamp
   * @return timestamp
  **/
  @ApiModelProperty(value = "")
  
    public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DeliveryGetDeliveryByUUIDResponse deliveryGetDeliveryByUUIDResponse = (DeliveryGetDeliveryByUUIDResponse) o;
    return Objects.equals(this.deliveryEntity, deliveryGetDeliveryByUUIDResponse.deliveryEntity) &&
        Objects.equals(this.message, deliveryGetDeliveryByUUIDResponse.message) &&
        Objects.equals(this.timestamp, deliveryGetDeliveryByUUIDResponse.timestamp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(deliveryEntity, message, timestamp);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DeliveryGetDeliveryByUUIDResponse {\n");
    
    sb.append("    deliveryEntity: ").append(toIndentedString(deliveryEntity)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
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