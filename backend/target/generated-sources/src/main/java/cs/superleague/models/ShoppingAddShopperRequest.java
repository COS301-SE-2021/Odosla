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
public class ShoppingAddShopperRequest   {
  @JsonProperty("shopperID")
  private String shopperID = null;

  @JsonProperty("storeID")
  private String storeID = null;

  public ShoppingAddShopperRequest shopperID(String shopperID) {
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

  public ShoppingAddShopperRequest storeID(String storeID) {
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
    ShoppingAddShopperRequest shoppingAddShopperRequest = (ShoppingAddShopperRequest) o;
    return Objects.equals(this.shopperID, shoppingAddShopperRequest.shopperID) &&
        Objects.equals(this.storeID, shoppingAddShopperRequest.storeID);
  }

  @Override
  public int hashCode() {
    return Objects.hash(shopperID, storeID);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ShoppingAddShopperRequest {\n");
    
    sb.append("    shopperID: ").append(toIndentedString(shopperID)).append("\n");
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
