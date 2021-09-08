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
public class UserUpdateAdminDetailsResponse   {
  @JsonProperty("jwtToken")
  private String jwtToken = null;

  @JsonProperty("message")
  private String message = null;

  @JsonProperty("success")
  private Boolean success = null;

  @JsonProperty("timestamp")
  private String timestamp = null;

  public UserUpdateAdminDetailsResponse jwtToken(String jwtToken) {
    this.jwtToken = jwtToken;
    return this;
  }

  /**
   * Get jwtToken
   * @return jwtToken
  **/
  @ApiModelProperty(value = "")
  
    public String getJwtToken() {
    return jwtToken;
  }

  public void setJwtToken(String jwtToken) {
    this.jwtToken = jwtToken;
  }

  public UserUpdateAdminDetailsResponse message(String message) {
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

  public UserUpdateAdminDetailsResponse success(Boolean success) {
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

  public UserUpdateAdminDetailsResponse timestamp(String timestamp) {
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
    UserUpdateAdminDetailsResponse userUpdateAdminDetailsResponse = (UserUpdateAdminDetailsResponse) o;
    return Objects.equals(this.jwtToken, userUpdateAdminDetailsResponse.jwtToken) &&
        Objects.equals(this.message, userUpdateAdminDetailsResponse.message) &&
        Objects.equals(this.success, userUpdateAdminDetailsResponse.success) &&
        Objects.equals(this.timestamp, userUpdateAdminDetailsResponse.timestamp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(jwtToken, message, success, timestamp);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserUpdateAdminDetailsResponse {\n");
    
    sb.append("    jwtToken: ").append(toIndentedString(jwtToken)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
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
