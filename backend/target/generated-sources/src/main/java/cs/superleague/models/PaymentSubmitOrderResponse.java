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
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-08-14T17:26:44.499818600+02:00[Africa/Harare]")
public class PaymentSubmitOrderResponse   {
  @JsonProperty("orderStatus")
  private String orderStatus = null;

  @JsonProperty("message")
  private String message = null;

  @JsonProperty("success")
  private Boolean success = null;

  @JsonProperty("timestamp")
  private String timestamp = null;

  public PaymentSubmitOrderResponse orderStatus(String orderStatus) {
    this.orderStatus = orderStatus;
    return this;
  }

  /**
   * Get orderStatus
   * @return orderStatus
  **/
  @ApiModelProperty(value = "")
  
    public String getOrderStatus() {
    return orderStatus;
  }

  public void setOrderStatus(String orderStatus) {
    this.orderStatus = orderStatus;
  }

  public PaymentSubmitOrderResponse message(String message) {
    this.message = message;
    return this;
  }

  /**
   * Will contain a descriptive error message if response code is not 200, else will be empty
   * @return message
  **/
  @ApiModelProperty(value = "Will contain a descriptive error message if response code is not 200, else will be empty")
  
    public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public PaymentSubmitOrderResponse success(Boolean success) {
    this.success = success;
    return this;
  }

  /**
   * Get success
   * @return success
  **/
  @ApiModelProperty(value = "")
  
    public Boolean isSuccess() {
    return success;
  }

  public void setSuccess(Boolean success) {
    this.success = success;
  }

  public PaymentSubmitOrderResponse timestamp(String timestamp) {
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
    PaymentSubmitOrderResponse paymentSubmitOrderResponse = (PaymentSubmitOrderResponse) o;
    return Objects.equals(this.orderStatus, paymentSubmitOrderResponse.orderStatus) &&
        Objects.equals(this.message, paymentSubmitOrderResponse.message) &&
        Objects.equals(this.success, paymentSubmitOrderResponse.success) &&
        Objects.equals(this.timestamp, paymentSubmitOrderResponse.timestamp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(orderStatus, message, success, timestamp);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PaymentSubmitOrderResponse {\n");
    
    sb.append("    orderStatus: ").append(toIndentedString(orderStatus)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
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
