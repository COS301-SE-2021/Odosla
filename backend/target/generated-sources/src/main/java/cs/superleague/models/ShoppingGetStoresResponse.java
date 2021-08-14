package cs.superleague.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import cs.superleague.models.StoreObject;
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
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-08-14T17:54:17.453730100+02:00[Africa/Harare]")
public class ShoppingGetStoresResponse   {
  @JsonProperty("response")
  private Boolean response = null;

  @JsonProperty("message")
  private String message = null;

  @JsonProperty("stores")
  @Valid
  private List<StoreObject> stores = null;

  public ShoppingGetStoresResponse response(Boolean response) {
    this.response = response;
    return this;
  }

  /**
   * Get response
   * @return response
  **/
  @ApiModelProperty(value = "")
  
    public Boolean isResponse() {
    return response;
  }

  public void setResponse(Boolean response) {
    this.response = response;
  }

  public ShoppingGetStoresResponse message(String message) {
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

  public ShoppingGetStoresResponse stores(List<StoreObject> stores) {
    this.stores = stores;
    return this;
  }

  public ShoppingGetStoresResponse addStoresItem(StoreObject storesItem) {
    if (this.stores == null) {
      this.stores = new ArrayList<StoreObject>();
    }
    this.stores.add(storesItem);
    return this;
  }

  /**
   * Get stores
   * @return stores
  **/
  @ApiModelProperty(value = "")
      @Valid
    public List<StoreObject> getStores() {
    return stores;
  }

  public void setStores(List<StoreObject> stores) {
    this.stores = stores;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ShoppingGetStoresResponse shoppingGetStoresResponse = (ShoppingGetStoresResponse) o;
    return Objects.equals(this.response, shoppingGetStoresResponse.response) &&
        Objects.equals(this.message, shoppingGetStoresResponse.message) &&
        Objects.equals(this.stores, shoppingGetStoresResponse.stores);
  }

  @Override
  public int hashCode() {
    return Objects.hash(response, message, stores);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ShoppingGetStoresResponse {\n");
    
    sb.append("    response: ").append(toIndentedString(response)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    stores: ").append(toIndentedString(stores)).append("\n");
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
