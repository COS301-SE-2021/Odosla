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
 * This object is expected as input
 */
@ApiModel(description = "This object is expected as input")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2021-08-16T17:50:09.822513100+02:00[Africa/Harare]")
public class DeliveryTrackDeliveryRequest {
  public static final String SERIALIZED_NAME_DELIVERY_I_D = "deliveryID";
  @SerializedName(SERIALIZED_NAME_DELIVERY_I_D)
  private String deliveryID;


  public DeliveryTrackDeliveryRequest deliveryID(String deliveryID) {
    
    this.deliveryID = deliveryID;
    return this;
  }

   /**
   * Get deliveryID
   * @return deliveryID
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getDeliveryID() {
    return deliveryID;
  }


  public void setDeliveryID(String deliveryID) {
    this.deliveryID = deliveryID;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DeliveryTrackDeliveryRequest deliveryTrackDeliveryRequest = (DeliveryTrackDeliveryRequest) o;
    return Objects.equals(this.deliveryID, deliveryTrackDeliveryRequest.deliveryID);
  }

  @Override
  public int hashCode() {
    return Objects.hash(deliveryID);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DeliveryTrackDeliveryRequest {\n");
    sb.append("    deliveryID: ").append(toIndentedString(deliveryID)).append("\n");
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

