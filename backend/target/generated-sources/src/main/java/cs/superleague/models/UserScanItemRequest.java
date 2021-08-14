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
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-08-14T16:05:50.792457600+02:00[Africa/Harare]")
public class UserScanItemRequest   {
  @JsonProperty("orderID")
  private String orderID = null;

  @JsonProperty("barcode")
  private String barcode = null;

  public UserScanItemRequest orderID(String orderID) {
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

  public UserScanItemRequest barcode(String barcode) {
    this.barcode = barcode;
    return this;
  }

  /**
   * Get barcode
   * @return barcode
  **/
  @ApiModelProperty(value = "")
  
    public String getBarcode() {
    return barcode;
  }

  public void setBarcode(String barcode) {
    this.barcode = barcode;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserScanItemRequest userScanItemRequest = (UserScanItemRequest) o;
    return Objects.equals(this.orderID, userScanItemRequest.orderID) &&
        Objects.equals(this.barcode, userScanItemRequest.barcode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(orderID, barcode);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserScanItemRequest {\n");
    
    sb.append("    orderID: ").append(toIndentedString(orderID)).append("\n");
    sb.append("    barcode: ").append(toIndentedString(barcode)).append("\n");
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
