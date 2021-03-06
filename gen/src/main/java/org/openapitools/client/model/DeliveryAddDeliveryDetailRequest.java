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
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2021-08-26T18:20:06.034903200+02:00[Africa/Harare]")
public class DeliveryAddDeliveryDetailRequest {
  public static final String SERIALIZED_NAME_DELIVERY_STATUS = "deliveryStatus";
  @SerializedName(SERIALIZED_NAME_DELIVERY_STATUS)
  private String deliveryStatus;

  public static final String SERIALIZED_NAME_DETAIL = "detail";
  @SerializedName(SERIALIZED_NAME_DETAIL)
  private String detail;

  public static final String SERIALIZED_NAME_DELIVERY_I_D = "deliveryID";
  @SerializedName(SERIALIZED_NAME_DELIVERY_I_D)
  private String deliveryID;

  public static final String SERIALIZED_NAME_TIMESTAMP = "timestamp";
  @SerializedName(SERIALIZED_NAME_TIMESTAMP)
  private String timestamp;


  public DeliveryAddDeliveryDetailRequest deliveryStatus(String deliveryStatus) {
    
    this.deliveryStatus = deliveryStatus;
    return this;
  }

   /**
   * Get deliveryStatus
   * @return deliveryStatus
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getDeliveryStatus() {
    return deliveryStatus;
  }


  public void setDeliveryStatus(String deliveryStatus) {
    this.deliveryStatus = deliveryStatus;
  }


  public DeliveryAddDeliveryDetailRequest detail(String detail) {
    
    this.detail = detail;
    return this;
  }

   /**
   * Get detail
   * @return detail
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getDetail() {
    return detail;
  }


  public void setDetail(String detail) {
    this.detail = detail;
  }


  public DeliveryAddDeliveryDetailRequest deliveryID(String deliveryID) {
    
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


  public DeliveryAddDeliveryDetailRequest timestamp(String timestamp) {
    
    this.timestamp = timestamp;
    return this;
  }

   /**
   * Get timestamp
   * @return timestamp
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getTimestamp() {
    return timestamp;
  }


  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DeliveryAddDeliveryDetailRequest deliveryAddDeliveryDetailRequest = (DeliveryAddDeliveryDetailRequest) o;
    return Objects.equals(this.deliveryStatus, deliveryAddDeliveryDetailRequest.deliveryStatus) &&
        Objects.equals(this.detail, deliveryAddDeliveryDetailRequest.detail) &&
        Objects.equals(this.deliveryID, deliveryAddDeliveryDetailRequest.deliveryID) &&
        Objects.equals(this.timestamp, deliveryAddDeliveryDetailRequest.timestamp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(deliveryStatus, detail, deliveryID, timestamp);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DeliveryAddDeliveryDetailRequest {\n");
    sb.append("    deliveryStatus: ").append(toIndentedString(deliveryStatus)).append("\n");
    sb.append("    detail: ").append(toIndentedString(detail)).append("\n");
    sb.append("    deliveryID: ").append(toIndentedString(deliveryID)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
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

