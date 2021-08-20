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
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2021-08-17T14:13:21.516052200+02:00[Africa/Harare]")
public class PaymentSubmitOrderResponse {
  public static final String SERIALIZED_NAME_ORDER_ID = "orderId";
  @SerializedName(SERIALIZED_NAME_ORDER_ID)
  private String orderId;

  public static final String SERIALIZED_NAME_MESSAGE = "message";
  @SerializedName(SERIALIZED_NAME_MESSAGE)
  private String message;

  public static final String SERIALIZED_NAME_SUCCESS = "success";
  @SerializedName(SERIALIZED_NAME_SUCCESS)
  private Boolean success;

  public static final String SERIALIZED_NAME_TIMESTAMP = "timestamp";
  @SerializedName(SERIALIZED_NAME_TIMESTAMP)
  private String timestamp;


  public PaymentSubmitOrderResponse orderId(String orderId) {
    
    this.orderId = orderId;
    return this;
  }

   /**
   * Get orderId
   * @return orderId
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getOrderId() {
    return orderId;
  }


  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }


  public PaymentSubmitOrderResponse message(String message) {
    
    this.message = message;
    return this;
  }

   /**
   * Will contain a descriptive error message if response code is not 200, else will be empty
   * @return message
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "Will contain a descriptive error message if response code is not 200, else will be empty")

  public String getMessage() {
    return message;
  }


  public void setMessage(String message) {
    this.message = message;
  }


  public PaymentSubmitOrderResponse success(Boolean success) {
    
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


  public PaymentSubmitOrderResponse timestamp(String timestamp) {
    
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
    PaymentSubmitOrderResponse paymentSubmitOrderResponse = (PaymentSubmitOrderResponse) o;
    return Objects.equals(this.orderId, paymentSubmitOrderResponse.orderId) &&
        Objects.equals(this.message, paymentSubmitOrderResponse.message) &&
        Objects.equals(this.success, paymentSubmitOrderResponse.success) &&
        Objects.equals(this.timestamp, paymentSubmitOrderResponse.timestamp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(orderId, message, success, timestamp);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PaymentSubmitOrderResponse {\n");
    sb.append("    orderId: ").append(toIndentedString(orderId)).append("\n");
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
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}

