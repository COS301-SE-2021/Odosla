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
import java.util.ArrayList;
import java.util.List;
import org.openapitools.client.model.OrderObject;

/**
 * This object is expected as output
 */
@ApiModel(description = "This object is expected as output")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2021-08-26T18:20:06.034903200+02:00[Africa/Harare]")
public class ShoppingGetQueueResponse {
  public static final String SERIALIZED_NAME_RESPONSE = "response";
  @SerializedName(SERIALIZED_NAME_RESPONSE)
  private Boolean response;

  public static final String SERIALIZED_NAME_MESSAGE = "message";
  @SerializedName(SERIALIZED_NAME_MESSAGE)
  private String message;

  public static final String SERIALIZED_NAME_QUEUE_OF_ORDERS = "queueOfOrders";
  @SerializedName(SERIALIZED_NAME_QUEUE_OF_ORDERS)
  private List<OrderObject> queueOfOrders = null;


  public ShoppingGetQueueResponse response(Boolean response) {
    
    this.response = response;
    return this;
  }

   /**
   * Get response
   * @return response
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public Boolean getResponse() {
    return response;
  }


  public void setResponse(Boolean response) {
    this.response = response;
  }


  public ShoppingGetQueueResponse message(String message) {
    
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


  public ShoppingGetQueueResponse queueOfOrders(List<OrderObject> queueOfOrders) {
    
    this.queueOfOrders = queueOfOrders;
    return this;
  }

  public ShoppingGetQueueResponse addQueueOfOrdersItem(OrderObject queueOfOrdersItem) {
    if (this.queueOfOrders == null) {
      this.queueOfOrders = new ArrayList<OrderObject>();
    }
    this.queueOfOrders.add(queueOfOrdersItem);
    return this;
  }

   /**
   * Get queueOfOrders
   * @return queueOfOrders
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public List<OrderObject> getQueueOfOrders() {
    return queueOfOrders;
  }


  public void setQueueOfOrders(List<OrderObject> queueOfOrders) {
    this.queueOfOrders = queueOfOrders;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ShoppingGetQueueResponse shoppingGetQueueResponse = (ShoppingGetQueueResponse) o;
    return Objects.equals(this.response, shoppingGetQueueResponse.response) &&
        Objects.equals(this.message, shoppingGetQueueResponse.message) &&
        Objects.equals(this.queueOfOrders, shoppingGetQueueResponse.queueOfOrders);
  }

  @Override
  public int hashCode() {
    return Objects.hash(response, message, queueOfOrders);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ShoppingGetQueueResponse {\n");
    sb.append("    response: ").append(toIndentedString(response)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    queueOfOrders: ").append(toIndentedString(queueOfOrders)).append("\n");
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

