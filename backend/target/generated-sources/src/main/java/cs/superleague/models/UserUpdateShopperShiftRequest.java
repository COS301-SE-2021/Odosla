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
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-08-14T17:15:32.811465800+02:00[Africa/Harare]")
public class UserUpdateShopperShiftRequest   {
  @JsonProperty("shopperID")
  private String shopperID = null;

  @JsonProperty("onShift")
  private Boolean onShift = null;

  public UserUpdateShopperShiftRequest shopperID(String shopperID) {
    this.shopperID = shopperID;
    return this;
  }

  /**
   * Get shopperID
   * @return shopperID
  **/
  @ApiModelProperty(value = "")
  
    public String getShopperID() {
    return shopperID;
  }

  public void setShopperID(String shopperID) {
    this.shopperID = shopperID;
  }

  public UserUpdateShopperShiftRequest onShift(Boolean onShift) {
    this.onShift = onShift;
    return this;
  }

  /**
   * Get onShift
   * @return onShift
  **/
  @ApiModelProperty(value = "")
  
    public Boolean isOnShift() {
    return onShift;
  }

  public void setOnShift(Boolean onShift) {
    this.onShift = onShift;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserUpdateShopperShiftRequest userUpdateShopperShiftRequest = (UserUpdateShopperShiftRequest) o;
    return Objects.equals(this.shopperID, userUpdateShopperShiftRequest.shopperID) &&
        Objects.equals(this.onShift, userUpdateShopperShiftRequest.onShift);
  }

  @Override
  public int hashCode() {
    return Objects.hash(shopperID, onShift);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserUpdateShopperShiftRequest {\n");
    
    sb.append("    shopperID: ").append(toIndentedString(shopperID)).append("\n");
    sb.append("    onShift: ").append(toIndentedString(onShift)).append("\n");
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
