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
 * this object is output
 */
@ApiModel(description = "this object is output")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-08-14T17:15:32.811465800+02:00[Africa/Harare]")
public class ShoppingRemoveQueuedOrderResponse   {
  @JsonProperty("isRemoved")
  private Boolean isRemoved = null;

  @JsonProperty("message")
  private String message = null;

  @JsonProperty("orderID")
  private String orderID = null;

  public ShoppingRemoveQueuedOrderResponse isRemoved(Boolean isRemoved) {
    this.isRemoved = isRemoved;
    return this;
  }

  /**
   * Get isRemoved
   * @return isRemoved
  **/
  @ApiModelProperty(value = "")
  
    public Boolean isIsRemoved() {
    return isRemoved;
  }

  public void setIsRemoved(Boolean isRemoved) {
    this.isRemoved = isRemoved;
  }

  public ShoppingRemoveQueuedOrderResponse message(String message) {
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

  public ShoppingRemoveQueuedOrderResponse orderID(String orderID) {
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


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ShoppingRemoveQueuedOrderResponse shoppingRemoveQueuedOrderResponse = (ShoppingRemoveQueuedOrderResponse) o;
    return Objects.equals(this.isRemoved, shoppingRemoveQueuedOrderResponse.isRemoved) &&
        Objects.equals(this.message, shoppingRemoveQueuedOrderResponse.message) &&
        Objects.equals(this.orderID, shoppingRemoveQueuedOrderResponse.orderID);
  }

  @Override
  public int hashCode() {
    return Objects.hash(isRemoved, message, orderID);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ShoppingRemoveQueuedOrderResponse {\n");
    
    sb.append("    isRemoved: ").append(toIndentedString(isRemoved)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    orderID: ").append(toIndentedString(orderID)).append("\n");
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
