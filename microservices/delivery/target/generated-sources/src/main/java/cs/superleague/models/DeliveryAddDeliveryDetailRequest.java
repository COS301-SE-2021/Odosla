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
public class DeliveryAddDeliveryDetailRequest   {
  @JsonProperty("deliveryStatus")
  private String deliveryStatus = null;

  @JsonProperty("detail")
  private String detail = null;

  @JsonProperty("deliveryID")
  private String deliveryID = null;

  @JsonProperty("timestamp")
  private String timestamp = null;

  public DeliveryAddDeliveryDetailRequest deliveryStatus(String deliveryStatus) {
    this.deliveryStatus = deliveryStatus;
    return this;
  }

  /**
   * Get deliveryStatus
   * @return deliveryStatus
  **/
  @ApiModelProperty(value = "")
  
    public String getDeliveryStatus() {
    return deliveryStatus;
  }

  public void setDeliveryStatus(String deliveryStatus) {
    this.deliveryStatus = deliveryStatus;
  }

  public DeliveryAddDeliveryDetailRequest detail(String detail) {
    this.detail = detail;
    return this;
  }

  /**
   * Get detail
   * @return detail
  **/
  @ApiModelProperty(value = "")
  
    public String getDetail() {
    return detail;
  }

  public void setDetail(String detail) {
    this.detail = detail;
  }

  public DeliveryAddDeliveryDetailRequest deliveryID(String deliveryID) {
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

  public DeliveryAddDeliveryDetailRequest timestamp(String timestamp) {
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
    DeliveryAddDeliveryDetailRequest deliveryAddDeliveryDetailRequest = (DeliveryAddDeliveryDetailRequest) o;
    return Objects.equals(this.deliveryStatus, deliveryAddDeliveryDetailRequest.deliveryStatus) &&
        Objects.equals(this.detail, deliveryAddDeliveryDetailRequest.detail) &&
        Objects.equals(this.deliveryID, deliveryAddDeliveryDetailRequest.deliveryID) &&
        Objects.equals(this.timestamp, deliveryAddDeliveryDetailRequest.timestamp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(deliveryStatus, detail, deliveryID, timestamp);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DeliveryAddDeliveryDetailRequest {\n");
    
    sb.append("    deliveryStatus: ").append(toIndentedString(deliveryStatus)).append("\n");
    sb.append("    detail: ").append(toIndentedString(detail)).append("\n");
    sb.append("    deliveryID: ").append(toIndentedString(deliveryID)).append("\n");
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
