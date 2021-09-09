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
public class UserCompletePackagingOrderRequest   {
  @JsonProperty("orderID")
  private String orderID = null;

  @JsonProperty("getNext")
  private Boolean getNext = null;

  public UserCompletePackagingOrderRequest orderID(String orderID) {
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

  public UserCompletePackagingOrderRequest getNext(Boolean getNext) {
    this.getNext = getNext;
    return this;
  }

  /**
   * Get getNext
   * @return getNext
  **/
  @ApiModelProperty(value = "")
  
    public Boolean isGetNext() {
    return getNext;
  }

  public void setGetNext(Boolean getNext) {
    this.getNext = getNext;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserCompletePackagingOrderRequest userCompletePackagingOrderRequest = (UserCompletePackagingOrderRequest) o;
    return Objects.equals(this.orderID, userCompletePackagingOrderRequest.orderID) &&
        Objects.equals(this.getNext, userCompletePackagingOrderRequest.getNext);
  }

  @Override
  public int hashCode() {
    return Objects.hash(orderID, getNext);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserCompletePackagingOrderRequest {\n");
    
    sb.append("    orderID: ").append(toIndentedString(orderID)).append("\n");
    sb.append("    getNext: ").append(toIndentedString(getNext)).append("\n");
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