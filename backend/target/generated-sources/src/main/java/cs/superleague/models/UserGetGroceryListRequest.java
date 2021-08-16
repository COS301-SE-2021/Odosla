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
public class UserGetGroceryListRequest   {
  @JsonProperty("JWTToken")
  private String jwTToken = null;

  public UserGetGroceryListRequest jwTToken(String jwTToken) {
    this.jwTToken = jwTToken;
    return this;
  }

  /**
   * Get jwTToken
   * @return jwTToken
  **/
  @ApiModelProperty(value = "")
  
    public String getJwTToken() {
    return jwTToken;
  }

  public void setJwTToken(String jwTToken) {
    this.jwTToken = jwTToken;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserGetGroceryListRequest userGetGroceryListRequest = (UserGetGroceryListRequest) o;
    return Objects.equals(this.jwTToken, userGetGroceryListRequest.jwTToken);
  }

  @Override
  public int hashCode() {
    return Objects.hash(jwTToken);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserGetGroceryListRequest {\n");
    
    sb.append("    jwTToken: ").append(toIndentedString(jwTToken)).append("\n");
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
