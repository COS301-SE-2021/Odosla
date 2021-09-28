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
 * The details of the delivery
 */
@ApiModel(description = "The details of the delivery")
@Validated
public class DeliveryDetailObject   {
  @JsonProperty("id")
  private Integer id = null;

  @JsonProperty("deliveryID")
  private String deliveryID = null;

  @JsonProperty("time")
  private String time = null;

  @JsonProperty("status")
  private String status = null;

  @JsonProperty("detail")
  private String detail = null;

  public DeliveryDetailObject id(Integer id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
  **/
  @ApiModelProperty(value = "")
  
    public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public DeliveryDetailObject deliveryID(String deliveryID) {
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

  public DeliveryDetailObject time(String time) {
    this.time = time;
    return this;
  }

  /**
   * Get time
   * @return time
  **/
  @ApiModelProperty(value = "")
  
    public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public DeliveryDetailObject status(String status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
  **/
  @ApiModelProperty(value = "")
  
    public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public DeliveryDetailObject detail(String detail) {
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


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DeliveryDetailObject deliveryDetailObject = (DeliveryDetailObject) o;
    return Objects.equals(this.id, deliveryDetailObject.id) &&
        Objects.equals(this.deliveryID, deliveryDetailObject.deliveryID) &&
        Objects.equals(this.time, deliveryDetailObject.time) &&
        Objects.equals(this.status, deliveryDetailObject.status) &&
        Objects.equals(this.detail, deliveryDetailObject.detail);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, deliveryID, time, status, detail);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DeliveryDetailObject {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    deliveryID: ").append(toIndentedString(deliveryID)).append("\n");
    sb.append("    time: ").append(toIndentedString(time)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    detail: ").append(toIndentedString(detail)).append("\n");
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
