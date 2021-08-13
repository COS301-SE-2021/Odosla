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
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-08-13T16:31:49.521809+02:00[Africa/Harare]")
public class UserAccountVerifyRequest   {
  @JsonProperty("email")
  private String email = null;

  @JsonProperty("activationCode")
  private String activationCode = null;

  @JsonProperty("userType")
  private String userType = null;

  public UserAccountVerifyRequest email(String email) {
    this.email = email;
    return this;
  }

  /**
   * Get email
   * @return email
  **/
  @ApiModelProperty(value = "")
  
    public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public UserAccountVerifyRequest activationCode(String activationCode) {
    this.activationCode = activationCode;
    return this;
  }

  /**
   * Get activationCode
   * @return activationCode
  **/
  @ApiModelProperty(value = "")
  
    public String getActivationCode() {
    return activationCode;
  }

  public void setActivationCode(String activationCode) {
    this.activationCode = activationCode;
  }

  public UserAccountVerifyRequest userType(String userType) {
    this.userType = userType;
    return this;
  }

  /**
   * Get userType
   * @return userType
  **/
  @ApiModelProperty(value = "")
  
    public String getUserType() {
    return userType;
  }

  public void setUserType(String userType) {
    this.userType = userType;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserAccountVerifyRequest userAccountVerifyRequest = (UserAccountVerifyRequest) o;
    return Objects.equals(this.email, userAccountVerifyRequest.email) &&
        Objects.equals(this.activationCode, userAccountVerifyRequest.activationCode) &&
        Objects.equals(this.userType, userAccountVerifyRequest.userType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(email, activationCode, userType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserAccountVerifyRequest {\n");
    
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    activationCode: ").append(toIndentedString(activationCode)).append("\n");
    sb.append("    userType: ").append(toIndentedString(userType)).append("\n");
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
