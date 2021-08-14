package cs.superleague.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * This object is expected as input
 */
@ApiModel(description = "This object is expected as input")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-08-14T15:01:46.157134600+02:00[Africa/Harare]")
public class NotificationSendDirectEmailNotificationRequest   {
  @JsonProperty("message")
  private String message = null;

  @JsonProperty("properties")
  @Valid
  private Map<String, String> properties = null;

  public NotificationSendDirectEmailNotificationRequest message(String message) {
    this.message = message;
    return this;
  }

  /**
   * This is the message that will be sent as a notification
   * @return message
  **/
  @ApiModelProperty(value = "This is the message that will be sent as a notification")
  
    public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public NotificationSendDirectEmailNotificationRequest properties(Map<String, String> properties) {
    this.properties = properties;
    return this;
  }

  public NotificationSendDirectEmailNotificationRequest putPropertiesItem(String key, String propertiesItem) {
    if (this.properties == null) {
      this.properties = new HashMap<String, String>();
    }
    this.properties.put(key, propertiesItem);
    return this;
  }

  /**
   * Hash map that needs to contain the Subject and the email
   * @return properties
  **/
  @ApiModelProperty(value = "Hash map that needs to contain the Subject and the email")
  
    public Map<String, String> getProperties() {
    return properties;
  }

  public void setProperties(Map<String, String> properties) {
    this.properties = properties;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NotificationSendDirectEmailNotificationRequest notificationSendDirectEmailNotificationRequest = (NotificationSendDirectEmailNotificationRequest) o;
    return Objects.equals(this.message, notificationSendDirectEmailNotificationRequest.message) &&
        Objects.equals(this.properties, notificationSendDirectEmailNotificationRequest.properties);
  }

  @Override
  public int hashCode() {
    return Objects.hash(message, properties);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NotificationSendDirectEmailNotificationRequest {\n");
    
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    properties: ").append(toIndentedString(properties)).append("\n");
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
