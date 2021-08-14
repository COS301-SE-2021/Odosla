package cs.superleague.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import cs.superleague.models.ItemObject;
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
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-08-14T13:48:59.955289300+02:00[Africa/Harare]")
public class ShoppingGetItemsResponse   {
  @JsonProperty("errorMessage")
  private String errorMessage = null;

  @JsonProperty("items")
  @Valid
  private List<ItemObject> items = null;

  public ShoppingGetItemsResponse errorMessage(String errorMessage) {
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

  public ShoppingGetItemsResponse items(List<ItemObject> items) {
    this.items = items;
    return this;
  }

  public ShoppingGetItemsResponse addItemsItem(ItemObject itemsItem) {
    if (this.items == null) {
      this.items = new ArrayList<ItemObject>();
    }
    this.items.add(itemsItem);
    return this;
  }

  /**
   * Get items
   * @return items
  **/
  @ApiModelProperty(value = "")
      @Valid
    public List<ItemObject> getItems() {
    return items;
  }

  public void setItems(List<ItemObject> items) {
    this.items = items;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ShoppingGetItemsResponse shoppingGetItemsResponse = (ShoppingGetItemsResponse) o;
    return Objects.equals(this.errorMessage, shoppingGetItemsResponse.errorMessage) &&
        Objects.equals(this.items, shoppingGetItemsResponse.items);
  }

  @Override
  public int hashCode() {
    return Objects.hash(errorMessage, items);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ShoppingGetItemsResponse {\n");
    
    sb.append("    errorMessage: ").append(toIndentedString(errorMessage)).append("\n");
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
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
