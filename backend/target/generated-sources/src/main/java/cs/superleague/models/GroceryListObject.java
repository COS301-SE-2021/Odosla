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
 * Generic schema for a GroceryList
 */
@ApiModel(description = "Generic schema for a GroceryList")
@Validated
public class GroceryListObject   {
  @JsonProperty("groceryListID")
  private String groceryListID = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("items")
  @Valid
  private List<ItemObject> items = null;

  public GroceryListObject groceryListID(String groceryListID) {
    this.groceryListID = groceryListID;
    return this;
  }

  /**
   * Get groceryListID
   * @return groceryListID
  **/
  @ApiModelProperty(value = "")
  
    public String getGroceryListID() {
    return groceryListID;
  }

  public void setGroceryListID(String groceryListID) {
    this.groceryListID = groceryListID;
  }

  public GroceryListObject name(String name) {
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

  public GroceryListObject items(List<ItemObject> items) {
    this.items = items;
    return this;
  }

  public GroceryListObject addItemsItem(ItemObject itemsItem) {
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
    GroceryListObject groceryListObject = (GroceryListObject) o;
    return Objects.equals(this.groceryListID, groceryListObject.groceryListID) &&
        Objects.equals(this.name, groceryListObject.name) &&
        Objects.equals(this.items, groceryListObject.items);
  }

  @Override
  public int hashCode() {
    return Objects.hash(groceryListID, name, items);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GroceryListObject {\n");
    
    sb.append("    groceryListID: ").append(toIndentedString(groceryListID)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
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
