/*
 * Library Service
 * The library service
 *
 * The version of the OpenAPI document: 0.0.1
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package org.openapitools.client.model;

import java.util.Objects;
import java.util.Arrays;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This object is expected as input
 */
@ApiModel(description = "This object is expected as input")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2021-08-14T16:46:33.559003800+02:00[Africa/Harare]")
public class NotificationSendEmailNotificationRequest {
  public static final String SERIALIZED_NAME_MESSAGE = "message";
  @SerializedName(SERIALIZED_NAME_MESSAGE)
  private String message;

  public static final String SERIALIZED_NAME_PROPERTIES = "properties";
  @SerializedName(SERIALIZED_NAME_PROPERTIES)
  private Map<String, String> properties = null;


  public NotificationSendEmailNotificationRequest message(String message) {
    
    this.message = message;
    return this;
  }

   /**
   * This is the message that will be sent as a notification
   * @return message
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "This is the message that will be sent as a notification")

  public String getMessage() {
    return message;
  }


  public void setMessage(String message) {
    this.message = message;
  }


  public NotificationSendEmailNotificationRequest properties(Map<String, String> properties) {
    
    this.properties = properties;
    return this;
  }

  public NotificationSendEmailNotificationRequest putPropertiesItem(String key, String propertiesItem) {
    if (this.properties == null) {
      this.properties = new HashMap<String, String>();
    }
    this.properties.put(key, propertiesItem);
    return this;
  }

   /**
   * Hash map that needs to contain the Type (e.g delivery), Subject, UserType (e.g admin) and the UserID
   * @return properties
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "Hash map that needs to contain the Type (e.g delivery), Subject, UserType (e.g admin) and the UserID")

  public Map<String, String> getProperties() {
    return properties;
  }


  public void setProperties(Map<String, String> properties) {
    this.properties = properties;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NotificationSendEmailNotificationRequest notificationSendEmailNotificationRequest = (NotificationSendEmailNotificationRequest) o;
    return Objects.equals(this.message, notificationSendEmailNotificationRequest.message) &&
        Objects.equals(this.properties, notificationSendEmailNotificationRequest.properties);
  }

  @Override
  public int hashCode() {
    return Objects.hash(message, properties);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NotificationSendEmailNotificationRequest {\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    properties: ").append(toIndentedString(properties)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}

