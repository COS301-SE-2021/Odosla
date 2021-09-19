package cs.superleague.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import cs.superleague.models.DeliveryDetailObject;
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
public class DeliveryGetDeliveryDetailResponse   {
  @JsonProperty("message")
  private String message = null;

  @JsonProperty("detail")
  @Valid
  private List<DeliveryDetailObject> detail = null;

  public DeliveryGetDeliveryDetailResponse message(String message) {
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

  public DeliveryGetDeliveryDetailResponse detail(List<DeliveryDetailObject> detail) {
    this.detail = detail;
    return this;
  }

  public DeliveryGetDeliveryDetailResponse addDetailItem(DeliveryDetailObject detailItem) {
    if (this.detail == null) {
      this.detail = new ArrayList<DeliveryDetailObject>();
    }
    this.detail.add(detailItem);
    return this;
  }

  /**
   * Get detail
   * @return detail
  **/
  @ApiModelProperty(value = "")
      @Valid
    public List<DeliveryDetailObject> getDetail() {
    return detail;
  }

  public void setDetail(List<DeliveryDetailObject> detail) {
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
    DeliveryGetDeliveryDetailResponse deliveryGetDeliveryDetailResponse = (DeliveryGetDeliveryDetailResponse) o;
    return Objects.equals(this.message, deliveryGetDeliveryDetailResponse.message) &&
        Objects.equals(this.detail, deliveryGetDeliveryDetailResponse.detail);
  }

  @Override
  public int hashCode() {
    return Objects.hash(message, detail);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DeliveryGetDeliveryDetailResponse {\n");
    
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
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
