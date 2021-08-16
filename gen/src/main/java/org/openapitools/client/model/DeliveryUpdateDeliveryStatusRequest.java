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
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2021-08-14T22:27:51.898546700+02:00[Africa/Harare]")
public class DeliveryUpdateDeliveryStatusRequest {
  public static final String SERIALIZED_NAME_STATUS = "status";
  @SerializedName(SERIALIZED_NAME_STATUS)
  private String status;

  public static final String SERIALIZED_NAME_DELIVERY_I_D = "deliveryID";
  @SerializedName(SERIALIZED_NAME_DELIVERY_I_D)
  private String deliveryID;

  public static final String SERIALIZED_NAME_DETAIL = "detail";
  @SerializedName(SERIALIZED_NAME_DETAIL)
  private String detail;


  public DeliveryUpdateDeliveryStatusRequest status(String status) {
    
    this.status = status;
    return this;
  }

   /**
   * Get status
   * @return status
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getStatus() {
    return status;
  }


  public void setStatus(String status) {
    this.status = status;
  }


  public DeliveryUpdateDeliveryStatusRequest deliveryID(String deliveryID) {
    
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


  public DeliveryUpdateDeliveryStatusRequest detail(String detail) {
    
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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DeliveryUpdateDeliveryStatusRequest deliveryUpdateDeliveryStatusRequest = (DeliveryUpdateDeliveryStatusRequest) o;
    return Objects.equals(this.status, deliveryUpdateDeliveryStatusRequest.status) &&
        Objects.equals(this.deliveryID, deliveryUpdateDeliveryStatusRequest.deliveryID) &&
        Objects.equals(this.detail, deliveryUpdateDeliveryStatusRequest.detail);
  }

  @Override
  public int hashCode() {
    return Objects.hash(status, deliveryID, detail);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DeliveryUpdateDeliveryStatusRequest {\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    deliveryID: ").append(toIndentedString(deliveryID)).append("\n");
    sb.append("    detail: ").append(toIndentedString(detail)).append("\n");
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

