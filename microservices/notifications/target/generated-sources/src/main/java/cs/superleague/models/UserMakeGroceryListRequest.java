package cs.superleague.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
public class UserMakeGroceryListRequest   {
  @JsonProperty("productIds")
  @Valid
  private List<String> productIds = null;

  @JsonProperty("name")
  private String name = null;

  public UserMakeGroceryListRequest productIds(List<String> productIds) {
    this.productIds = productIds;
    return this;
  }

  public UserMakeGroceryListRequest addProductIdsItem(String productIdsItem) {
    if (this.productIds == null) {
      this.productIds = new ArrayList<String>();
    }
    this.productIds.add(productIdsItem);
    return this;
  }

  /**
   * Get productIds
   * @return productIds
  **/
  @ApiModelProperty(value = "")
  
    public List<String> getProductIds() {
    return productIds;
  }

  public void setProductIds(List<String> productIds) {
    this.productIds = productIds;
  }

  public UserMakeGroceryListRequest name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
  **/
  @ApiModelProperty(value = "")
  
    public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserMakeGroceryListRequest userMakeGroceryListRequest = (UserMakeGroceryListRequest) o;
    return Objects.equals(this.productIds, userMakeGroceryListRequest.productIds) &&
        Objects.equals(this.name, userMakeGroceryListRequest.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(productIds, name);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserMakeGroceryListRequest {\n");
    
    sb.append("    productIds: ").append(toIndentedString(productIds)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
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
