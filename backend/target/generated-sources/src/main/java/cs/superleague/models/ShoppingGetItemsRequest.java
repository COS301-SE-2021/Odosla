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
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-08-13T15:49:46.017974400+02:00[Africa/Harare]")
public class ShoppingGetItemsRequest   {
  @JsonProperty("jwtToken")
  private String jwtToken = null;

  @JsonProperty("storeID")
  private String storeID = null;

  public ShoppingGetItemsRequest jwtToken(String jwtToken) {
    this.jwtToken = jwtToken;
    return this;
  }

  /**
   * generated token used to identify the caller of the endpoint
   * @return jwtToken
  **/
  @ApiModelProperty(value = "generated token used to identify the caller of the endpoint")
  
    public String getJwtToken() {
    return jwtToken;
  }

  public void setJwtToken(String jwtToken) {
    this.jwtToken = jwtToken;
  }

  public ShoppingGetItemsRequest storeID(String storeID) {
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
    ShoppingGetItemsRequest shoppingGetItemsRequest = (ShoppingGetItemsRequest) o;
    return Objects.equals(this.jwtToken, shoppingGetItemsRequest.jwtToken) &&
        Objects.equals(this.storeID, shoppingGetItemsRequest.storeID);
  }

  @Override
  public int hashCode() {
    return Objects.hash(jwtToken, storeID);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ShoppingGetItemsRequest {\n");
    
    sb.append("    jwtToken: ").append(toIndentedString(jwtToken)).append("\n");
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
