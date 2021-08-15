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
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2021-08-14T22:27:51.898546700+02:00[Africa/Harare]")
public class DeliveryAssignDriverToDeliveryResponse {
  public static final String SERIALIZED_NAME_IS_ASSIGNED = "isAssigned";
  @SerializedName(SERIALIZED_NAME_IS_ASSIGNED)
  private Boolean isAssigned;

  public static final String SERIALIZED_NAME_MESSAGE = "message";
  @SerializedName(SERIALIZED_NAME_MESSAGE)
  private String message;


  public DeliveryAssignDriverToDeliveryResponse isAssigned(Boolean isAssigned) {
    
    this.isAssigned = isAssigned;
    return this;
  }

   /**
   * Get isAssigned
   * @return isAssigned
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public Boolean getIsAssigned() {
    return isAssigned;
  }


  public void setIsAssigned(Boolean isAssigned) {
    this.isAssigned = isAssigned;
  }


  public DeliveryAssignDriverToDeliveryResponse message(String message) {
    
    this.message = message;
    return this;
  }

   /**
   * Get message
   * @return message
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getMessage() {
    return message;
  }


  public void setMessage(String message) {
    this.message = message;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DeliveryAssignDriverToDeliveryResponse deliveryAssignDriverToDeliveryResponse = (DeliveryAssignDriverToDeliveryResponse) o;
    return Objects.equals(this.isAssigned, deliveryAssignDriverToDeliveryResponse.isAssigned) &&
        Objects.equals(this.message, deliveryAssignDriverToDeliveryResponse.message);
  }

  @Override
  public int hashCode() {
    return Objects.hash(isAssigned, message);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DeliveryAssignDriverToDeliveryResponse {\n");
    sb.append("    isAssigned: ").append(toIndentedString(isAssigned)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
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
