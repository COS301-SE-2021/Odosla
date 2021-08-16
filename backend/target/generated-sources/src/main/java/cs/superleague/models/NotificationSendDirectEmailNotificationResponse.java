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
 * This object is expected as output
 */
@ApiModel(description = "This object is expected as output")
@Validated
public class NotificationSendDirectEmailNotificationResponse   {
  @JsonProperty("responseMessage")
  private String responseMessage = null;

  @JsonProperty("success")
  private Boolean success = null;

  public NotificationSendDirectEmailNotificationResponse responseMessage(String responseMessage) {
    this.responseMessage = responseMessage;
    return this;
  }

  /**
   * Will contain a descriptive error message if response code is not 200, else will be empty
   * @return responseMessage
  **/
  @ApiModelProperty(value = "Will contain a descriptive error message if response code is not 200, else will be empty")
  
    public String getResponseMessage() {
    return responseMessage;
  }

  public void setResponseMessage(String responseMessage) {
    this.responseMessage = responseMessage;
  }

  public NotificationSendDirectEmailNotificationResponse success(Boolean success) {
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


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NotificationSendDirectEmailNotificationResponse notificationSendDirectEmailNotificationResponse = (NotificationSendDirectEmailNotificationResponse) o;
    return Objects.equals(this.responseMessage, notificationSendDirectEmailNotificationResponse.responseMessage) &&
        Objects.equals(this.success, notificationSendDirectEmailNotificationResponse.success);
  }

  @Override
  public int hashCode() {
    return Objects.hash(responseMessage, success);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NotificationSendDirectEmailNotificationResponse {\n");
    
    sb.append("    responseMessage: ").append(toIndentedString(responseMessage)).append("\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
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
