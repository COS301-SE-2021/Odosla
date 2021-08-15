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
import org.openapitools.client.model.GeoPointObject;

/**
 * This object is expected as input
 */
@ApiModel(description = "This object is expected as input")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2021-08-14T22:27:51.898546700+02:00[Africa/Harare]")
public class DeliveryCreateDeliveryRequest {
  public static final String SERIALIZED_NAME_ORDER_I_D = "orderID";
  @SerializedName(SERIALIZED_NAME_ORDER_I_D)
  private String orderID;

  public static final String SERIALIZED_NAME_CUSTOMER_I_D = "customerID";
  @SerializedName(SERIALIZED_NAME_CUSTOMER_I_D)
  private String customerID;

  public static final String SERIALIZED_NAME_STORE_I_D = "storeID";
  @SerializedName(SERIALIZED_NAME_STORE_I_D)
  private String storeID;

  public static final String SERIALIZED_NAME_TIME_OF_DELIVERY = "timeOfDelivery";
  @SerializedName(SERIALIZED_NAME_TIME_OF_DELIVERY)
  private String timeOfDelivery;

  public static final String SERIALIZED_NAME_PLACE_OF_DELIVERY = "placeOfDelivery";
  @SerializedName(SERIALIZED_NAME_PLACE_OF_DELIVERY)
  private GeoPointObject placeOfDelivery;


  public DeliveryCreateDeliveryRequest orderID(String orderID) {
    
    this.orderID = orderID;
    return this;
  }

   /**
   * Get orderID
   * @return orderID
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getOrderID() {
    return orderID;
  }


  public void setOrderID(String orderID) {
    this.orderID = orderID;
  }


  public DeliveryCreateDeliveryRequest customerID(String customerID) {
    
    this.customerID = customerID;
    return this;
  }

   /**
   * Get customerID
   * @return customerID
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getCustomerID() {
    return customerID;
  }


  public void setCustomerID(String customerID) {
    this.customerID = customerID;
  }


  public DeliveryCreateDeliveryRequest storeID(String storeID) {
    
    this.storeID = storeID;
    return this;
  }

   /**
   * Get storeID
   * @return storeID
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getStoreID() {
    return storeID;
  }


  public void setStoreID(String storeID) {
    this.storeID = storeID;
  }


  public DeliveryCreateDeliveryRequest timeOfDelivery(String timeOfDelivery) {
    
    this.timeOfDelivery = timeOfDelivery;
    return this;
  }

   /**
   * Get timeOfDelivery
   * @return timeOfDelivery
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getTimeOfDelivery() {
    return timeOfDelivery;
  }


  public void setTimeOfDelivery(String timeOfDelivery) {
    this.timeOfDelivery = timeOfDelivery;
  }


  public DeliveryCreateDeliveryRequest placeOfDelivery(GeoPointObject placeOfDelivery) {
    
    this.placeOfDelivery = placeOfDelivery;
    return this;
  }

   /**
   * Get placeOfDelivery
   * @return placeOfDelivery
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public GeoPointObject getPlaceOfDelivery() {
    return placeOfDelivery;
  }


  public void setPlaceOfDelivery(GeoPointObject placeOfDelivery) {
    this.placeOfDelivery = placeOfDelivery;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DeliveryCreateDeliveryRequest deliveryCreateDeliveryRequest = (DeliveryCreateDeliveryRequest) o;
    return Objects.equals(this.orderID, deliveryCreateDeliveryRequest.orderID) &&
        Objects.equals(this.customerID, deliveryCreateDeliveryRequest.customerID) &&
        Objects.equals(this.storeID, deliveryCreateDeliveryRequest.storeID) &&
        Objects.equals(this.timeOfDelivery, deliveryCreateDeliveryRequest.timeOfDelivery) &&
        Objects.equals(this.placeOfDelivery, deliveryCreateDeliveryRequest.placeOfDelivery);
  }

  @Override
  public int hashCode() {
    return Objects.hash(orderID, customerID, storeID, timeOfDelivery, placeOfDelivery);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DeliveryCreateDeliveryRequest {\n");
    sb.append("    orderID: ").append(toIndentedString(orderID)).append("\n");
    sb.append("    customerID: ").append(toIndentedString(customerID)).append("\n");
    sb.append("    storeID: ").append(toIndentedString(storeID)).append("\n");
    sb.append("    timeOfDelivery: ").append(toIndentedString(timeOfDelivery)).append("\n");
    sb.append("    placeOfDelivery: ").append(toIndentedString(placeOfDelivery)).append("\n");
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

