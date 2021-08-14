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
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-08-14T16:05:50.792457600+02:00[Africa/Harare]")
public class PaymentUpdateOrderResponse   {
  @JsonProperty("message")
  private String message = null;

  @JsonProperty("success")
  private Boolean success = null;

  @JsonProperty("timestamp")
  private String timestamp = null;

  @JsonProperty("order")
  private Object order = null;

  public PaymentUpdateOrderResponse message(String message) {
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

  public PaymentUpdateOrderResponse success(Boolean success) {
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

  public PaymentUpdateOrderResponse timestamp(String timestamp) {
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

  public PaymentUpdateOrderResponse order(Object order) {
    this.order = order;
    return this;
  }

  /**
   * Get order
   * @return order
  **/
  @ApiModelProperty(value = "")
  
    public Object getOrder() {
    return order;
  }

  public void setOrder(Object order) {
    this.order = order;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PaymentUpdateOrderResponse paymentUpdateOrderResponse = (PaymentUpdateOrderResponse) o;
    return Objects.equals(this.message, paymentUpdateOrderResponse.message) &&
        Objects.equals(this.success, paymentUpdateOrderResponse.success) &&
        Objects.equals(this.timestamp, paymentUpdateOrderResponse.timestamp) &&
        Objects.equals(this.order, paymentUpdateOrderResponse.order);
  }

  @Override
  public int hashCode() {
    return Objects.hash(message, success, timestamp, order);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PaymentUpdateOrderResponse {\n");
    
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    order: ").append(toIndentedString(order)).append("\n");
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
