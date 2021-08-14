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
 * This object is returned as output
 */
@ApiModel(description = "This object is returned as output")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-08-14T15:42:59.236683100+02:00[Africa/Harare]")
public class UserResetPasswordResponse   {
  @JsonProperty("message")
  private String message = null;

  @JsonProperty("success")
  private Boolean success = null;

  @JsonProperty("resetCode")
  private String resetCode = null;

  public UserResetPasswordResponse message(String message) {
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

  public UserResetPasswordResponse success(Boolean success) {
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

  public UserResetPasswordResponse resetCode(String resetCode) {
    this.resetCode = resetCode;
    return this;
  }

  /**
   * Get resetCode
   * @return resetCode
  **/
  @ApiModelProperty(value = "")
  
    public String getResetCode() {
    return resetCode;
  }

  public void setResetCode(String resetCode) {
    this.resetCode = resetCode;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserResetPasswordResponse userResetPasswordResponse = (UserResetPasswordResponse) o;
    return Objects.equals(this.message, userResetPasswordResponse.message) &&
        Objects.equals(this.success, userResetPasswordResponse.success) &&
        Objects.equals(this.resetCode, userResetPasswordResponse.resetCode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(message, success, resetCode);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserResetPasswordResponse {\n");
    
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    resetCode: ").append(toIndentedString(resetCode)).append("\n");
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
