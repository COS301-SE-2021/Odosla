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
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-08-14T13:48:59.955289300+02:00[Africa/Harare]")
public class UserGetCurrentUserRequest   {
  @JsonProperty("JWTToken")
  private String jwTToken = null;

  public UserGetCurrentUserRequest jwTToken(String jwTToken) {
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
    UserGetCurrentUserRequest userGetCurrentUserRequest = (UserGetCurrentUserRequest) o;
    return Objects.equals(this.jwTToken, userGetCurrentUserRequest.jwTToken);
  }

  @Override
  public int hashCode() {
    return Objects.hash(jwTToken);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserGetCurrentUserRequest {\n");
    
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
