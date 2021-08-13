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
 * This object is returned as output
 */
@ApiModel(description = "This object is returned as output")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-08-13T16:31:49.521809+02:00[Africa/Harare]")
public class ShoppingGetNextQueuedResponse   {
  @JsonProperty("message")
  private String message = null;

  @JsonProperty("success")
  private Boolean success = null;

  @JsonProperty("date")
  private String date = null;

  @JsonProperty("queueOfOrders")
  @Valid
  private List<OrderObject> queueOfOrders = null;

  @JsonProperty("newCurrentOrder")
  private Object newCurrentOrder = null;

  public ShoppingGetNextQueuedResponse message(String message) {
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

  public ShoppingGetNextQueuedResponse success(Boolean success) {
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

  public ShoppingGetNextQueuedResponse date(String date) {
    this.date = date;
    return this;
  }

  /**
   * Get date
   * @return date
  **/
  @ApiModelProperty(value = "")
  
    public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public ShoppingGetNextQueuedResponse queueOfOrders(List<OrderObject> queueOfOrders) {
    this.queueOfOrders = queueOfOrders;
    return this;
  }

  public ShoppingGetNextQueuedResponse addQueueOfOrdersItem(OrderObject queueOfOrdersItem) {
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

  public ShoppingGetNextQueuedResponse newCurrentOrder(Object newCurrentOrder) {
    this.newCurrentOrder = newCurrentOrder;
    return this;
  }

  /**
   * Get newCurrentOrder
   * @return newCurrentOrder
  **/
  @ApiModelProperty(value = "")
  
    public Object getNewCurrentOrder() {
    return newCurrentOrder;
  }

  public void setNewCurrentOrder(Object newCurrentOrder) {
    this.newCurrentOrder = newCurrentOrder;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ShoppingGetNextQueuedResponse shoppingGetNextQueuedResponse = (ShoppingGetNextQueuedResponse) o;
    return Objects.equals(this.message, shoppingGetNextQueuedResponse.message) &&
        Objects.equals(this.success, shoppingGetNextQueuedResponse.success) &&
        Objects.equals(this.date, shoppingGetNextQueuedResponse.date) &&
        Objects.equals(this.queueOfOrders, shoppingGetNextQueuedResponse.queueOfOrders) &&
        Objects.equals(this.newCurrentOrder, shoppingGetNextQueuedResponse.newCurrentOrder);
  }

  @Override
  public int hashCode() {
    return Objects.hash(message, success, date, queueOfOrders, newCurrentOrder);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ShoppingGetNextQueuedResponse {\n");
    
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    date: ").append(toIndentedString(date)).append("\n");
    sb.append("    queueOfOrders: ").append(toIndentedString(queueOfOrders)).append("\n");
    sb.append("    newCurrentOrder: ").append(toIndentedString(newCurrentOrder)).append("\n");
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
