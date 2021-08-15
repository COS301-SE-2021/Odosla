package cs.superleague.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import cs.superleague.models.ShopperObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * This object is expected as input
 */
@ApiModel(description = "This object is expected as input")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-08-15T11:11:07.319721+02:00[Africa/Harare]")
public class ShoppingGetShoppersResponse   {
  @JsonProperty("errorMessage")
  private String errorMessage = null;

  @JsonProperty("shoppers")
  @Valid
  private List<ShopperObject> shoppers = null;

  public ShoppingGetShoppersResponse errorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
    return this;
  }

  /**
   * Will contain a descriptive error message if response code is not 200, else will be empty
   * @return errorMessage
  **/
  @ApiModelProperty(value = "Will contain a descriptive error message if response code is not 200, else will be empty")
  
    public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public ShoppingGetShoppersResponse shoppers(List<ShopperObject> shoppers) {
    this.shoppers = shoppers;
    return this;
  }

  public ShoppingGetShoppersResponse addShoppersItem(ShopperObject shoppersItem) {
    if (this.shoppers == null) {
      this.shoppers = new ArrayList<ShopperObject>();
    }
    this.shoppers.add(shoppersItem);
    return this;
  }

  /**
   * Get shoppers
   * @return shoppers
  **/
  @ApiModelProperty(value = "")
      @Valid
    public List<ShopperObject> getShoppers() {
    return shoppers;
  }

  public void setShoppers(List<ShopperObject> shoppers) {
    this.shoppers = shoppers;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ShoppingGetShoppersResponse shoppingGetShoppersResponse = (ShoppingGetShoppersResponse) o;
    return Objects.equals(this.errorMessage, shoppingGetShoppersResponse.errorMessage) &&
        Objects.equals(this.shoppers, shoppingGetShoppersResponse.shoppers);
  }

  @Override
  public int hashCode() {
    return Objects.hash(errorMessage, shoppers);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ShoppingGetShoppersResponse {\n");
    
    sb.append("    errorMessage: ").append(toIndentedString(errorMessage)).append("\n");
    sb.append("    shoppers: ").append(toIndentedString(shoppers)).append("\n");
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
