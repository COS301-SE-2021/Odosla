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
 * this object is expected as input
 */
@ApiModel(description = "this object is expected as input")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-08-13T18:57:36.804039800+02:00[Africa/Harare]")
public class ShoppingRemoveQueuedOrderRequest   {
  @JsonProperty("orderID")
  private String orderID = null;

  @JsonProperty("storeID")
  private String storeID = null;

  public ShoppingRemoveQueuedOrderRequest orderID(String orderID) {
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

  public ShoppingRemoveQueuedOrderRequest storeID(String storeID) {
    this.storeID = storeID;
    return this;
  }

  /**
   * Get storeID
   * @return storeID
  **/
  @ApiModelProperty(value = "")
  
    public String getStoreID() {
    return storeID;
  }

  public void setStoreID(String storeID) {
    this.storeID = storeID;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ShoppingRemoveQueuedOrderRequest shoppingRemoveQueuedOrderRequest = (ShoppingRemoveQueuedOrderRequest) o;
    return Objects.equals(this.orderID, shoppingRemoveQueuedOrderRequest.orderID) &&
        Objects.equals(this.storeID, shoppingRemoveQueuedOrderRequest.storeID);
  }

  @Override
  public int hashCode() {
    return Objects.hash(orderID, storeID);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ShoppingRemoveQueuedOrderRequest {\n");
    
    sb.append("    orderID: ").append(toIndentedString(orderID)).append("\n");
    sb.append("    storeID: ").append(toIndentedString(storeID)).append("\n");
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
