package cs.superleague.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import cs.superleague.models.GroceryListObject;
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
public class UserGetGroceryListResponse   {
  @JsonProperty("groceryLists")
  @Valid
  private List<GroceryListObject> groceryLists = null;

  @JsonProperty("success")
  private Boolean success = null;

  @JsonProperty("message")
  private String message = null;

  @JsonProperty("timestamp")
  private String timestamp = null;

  public UserGetGroceryListResponse groceryLists(List<GroceryListObject> groceryLists) {
    this.groceryLists = groceryLists;
    return this;
  }

  public UserGetGroceryListResponse addGroceryListsItem(GroceryListObject groceryListsItem) {
    if (this.groceryLists == null) {
      this.groceryLists = new ArrayList<GroceryListObject>();
    }
    this.groceryLists.add(groceryListsItem);
    return this;
  }

  /**
   * Get groceryLists
   * @return groceryLists
  **/
  @ApiModelProperty(value = "")
      @Valid
    public List<GroceryListObject> getGroceryLists() {
    return groceryLists;
  }

  public void setGroceryLists(List<GroceryListObject> groceryLists) {
    this.groceryLists = groceryLists;
  }

  public UserGetGroceryListResponse success(Boolean success) {
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

  public UserGetGroceryListResponse message(String message) {
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

  public UserGetGroceryListResponse timestamp(String timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Get timestamp
   * @return timestamp
  **/
  @ApiModelProperty(value = "")
  
    public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserGetGroceryListResponse userGetGroceryListResponse = (UserGetGroceryListResponse) o;
    return Objects.equals(this.groceryLists, userGetGroceryListResponse.groceryLists) &&
        Objects.equals(this.success, userGetGroceryListResponse.success) &&
        Objects.equals(this.message, userGetGroceryListResponse.message) &&
        Objects.equals(this.timestamp, userGetGroceryListResponse.timestamp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(groceryLists, success, message, timestamp);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserGetGroceryListResponse {\n");
    
    sb.append("    groceryLists: ").append(toIndentedString(groceryLists)).append("\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
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
