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
public class ShoppingGetShoppersRequest   {
  @JsonProperty("jwtToken")
  private String jwtToken = null;

  @JsonProperty("storeID")
  private String storeID = null;

  public ShoppingGetShoppersRequest jwtToken(String jwtToken) {
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

  public ShoppingGetShoppersRequest storeID(String storeID) {
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
    ShoppingGetShoppersRequest shoppingGetShoppersRequest = (ShoppingGetShoppersRequest) o;
    return Objects.equals(this.jwtToken, shoppingGetShoppersRequest.jwtToken) &&
        Objects.equals(this.storeID, shoppingGetShoppersRequest.storeID);
  }

  @Override
  public int hashCode() {
    return Objects.hash(jwtToken, storeID);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ShoppingGetShoppersRequest {\n");
    
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
