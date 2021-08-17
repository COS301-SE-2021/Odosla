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
public class PaymentGetCustomersActiveOrdersResponse   {
  @JsonProperty("orderID")
  private String orderID = null;

  @JsonProperty("hasActiveOrder")
  private Boolean hasActiveOrder = null;

  @JsonProperty("message")
  private String message = null;

  public PaymentGetCustomersActiveOrdersResponse orderID(String orderID) {
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

  public PaymentGetCustomersActiveOrdersResponse hasActiveOrder(Boolean hasActiveOrder) {
    this.hasActiveOrder = hasActiveOrder;
    return this;
  }

  /**
   * Get hasActiveOrder
   * @return hasActiveOrder
  **/
  @ApiModelProperty(value = "")
  
    public Boolean isHasActiveOrder() {
    return hasActiveOrder;
  }

  public void setHasActiveOrder(Boolean hasActiveOrder) {
    this.hasActiveOrder = hasActiveOrder;
  }

  public PaymentGetCustomersActiveOrdersResponse message(String message) {
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
    PaymentGetCustomersActiveOrdersResponse paymentGetCustomersActiveOrdersResponse = (PaymentGetCustomersActiveOrdersResponse) o;
    return Objects.equals(this.orderID, paymentGetCustomersActiveOrdersResponse.orderID) &&
        Objects.equals(this.hasActiveOrder, paymentGetCustomersActiveOrdersResponse.hasActiveOrder) &&
        Objects.equals(this.message, paymentGetCustomersActiveOrdersResponse.message);
  }

  @Override
  public int hashCode() {
    return Objects.hash(orderID, hasActiveOrder, message);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PaymentGetCustomersActiveOrdersResponse {\n");
    
    sb.append("    orderID: ").append(toIndentedString(orderID)).append("\n");
    sb.append("    hasActiveOrder: ").append(toIndentedString(hasActiveOrder)).append("\n");
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
