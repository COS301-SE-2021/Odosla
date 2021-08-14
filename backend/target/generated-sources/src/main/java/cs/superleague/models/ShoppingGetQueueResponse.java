package cs.superleague.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import cs.superleague.models.OrderObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * This object is expected as output
 */
@ApiModel(description = "This object is expected as output")
@Validated
public class ShoppingGetQueueResponse   {
  @JsonProperty("response")
  private Boolean response = null;

  @JsonProperty("message")
  private String message = null;

  @JsonProperty("queueOfOrders")
  @Valid
  private List<OrderObject> queueOfOrders = null;

  public ShoppingGetQueueResponse response(Boolean response) {
    this.response = response;
    return this;
  }

  /**
   * Get response
   * @return response
  **/
  @ApiModelProperty(value = "")
  
    public Boolean isResponse() {
    return response;
  }

  public void setResponse(Boolean response) {
    this.response = response;
  }

  public ShoppingGetQueueResponse message(String message) {
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

  public ShoppingGetQueueResponse queueOfOrders(List<OrderObject> queueOfOrders) {
    this.queueOfOrders = queueOfOrders;
    return this;
  }

  public ShoppingGetQueueResponse addQueueOfOrdersItem(OrderObject queueOfOrdersItem) {
    if (this.queueOfOrders == null) {
      this.queueOfOrders = new ArrayList<OrderObject>();
    }
    this.queueOfOrders.add(queueOfOrdersItem);
    return this;
  }

  /**
   * Get queueOfOrders
   * @return queueOfOrders
  **/
  @ApiModelProperty(value = "")
      @Valid
    public List<OrderObject> getQueueOfOrders() {
    return queueOfOrders;
  }

  public void setQueueOfOrders(List<OrderObject> queueOfOrders) {
    this.queueOfOrders = queueOfOrders;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ShoppingGetQueueResponse shoppingGetQueueResponse = (ShoppingGetQueueResponse) o;
    return Objects.equals(this.response, shoppingGetQueueResponse.response) &&
        Objects.equals(this.message, shoppingGetQueueResponse.message) &&
        Objects.equals(this.queueOfOrders, shoppingGetQueueResponse.queueOfOrders);
  }

  @Override
  public int hashCode() {
    return Objects.hash(response, message, queueOfOrders);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ShoppingGetQueueResponse {\n");
    
    sb.append("    response: ").append(toIndentedString(response)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    queueOfOrders: ").append(toIndentedString(queueOfOrders)).append("\n");
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
