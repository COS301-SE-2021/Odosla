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

/**
 * This object is expected as output
 */
@ApiModel(description = "This object is expected as output")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2021-08-15T15:58:41.636762500+02:00[Africa/Harare]")
public class NotificationSendEmailNotificationResponse {
  public static final String SERIALIZED_NAME_RESPONSE_MESSAGE = "responseMessage";
  @SerializedName(SERIALIZED_NAME_RESPONSE_MESSAGE)
  private String responseMessage;

  public static final String SERIALIZED_NAME_SUCCESS = "success";
  @SerializedName(SERIALIZED_NAME_SUCCESS)
  private Boolean success;


  public NotificationSendEmailNotificationResponse responseMessage(String responseMessage) {
    
    this.responseMessage = responseMessage;
    return this;
  }

   /**
   * Will contain a descriptive error message if response code is not 200, else will be empty
   * @return responseMessage
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "Will contain a descriptive error message if response code is not 200, else will be empty")

  public String getResponseMessage() {
    return responseMessage;
  }


  public void setResponseMessage(String responseMessage) {
    this.responseMessage = responseMessage;
  }


  public NotificationSendEmailNotificationResponse success(Boolean success) {
    
    this.success = success;
    return this;
  }

   /**
   * Get success
   * @return success
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public Boolean getSuccess() {
    return success;
  }


  public void setSuccess(Boolean success) {
    this.success = success;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NotificationSendEmailNotificationResponse notificationSendEmailNotificationResponse = (NotificationSendEmailNotificationResponse) o;
    return Objects.equals(this.responseMessage, notificationSendEmailNotificationResponse.responseMessage) &&
        Objects.equals(this.success, notificationSendEmailNotificationResponse.success);
  }

  @Override
  public int hashCode() {
    return Objects.hash(responseMessage, success);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NotificationSendEmailNotificationResponse {\n");
    sb.append("    responseMessage: ").append(toIndentedString(responseMessage)).append("\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
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

