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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.openapitools.client.model.ItemObject;

/**
 * This object is expected as input
 */
@ApiModel(description = "This object is expected as input")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2021-08-14T14:53:01.663657800+02:00[Africa/Harare]")
public class PaymentSubmitOrderRequest {
  public static final String SERIALIZED_NAME_USER_ID = "userId";
  @SerializedName(SERIALIZED_NAME_USER_ID)
  private String userId;

  public static final String SERIALIZED_NAME_LIST_OF_ITEMS = "listOfItems";
  @SerializedName(SERIALIZED_NAME_LIST_OF_ITEMS)
  private List<ItemObject> listOfItems = null;

  public static final String SERIALIZED_NAME_DISCOUNT = "discount";
  @SerializedName(SERIALIZED_NAME_DISCOUNT)
  private BigDecimal discount;

  public static final String SERIALIZED_NAME_STORE_ID = "storeId";
  @SerializedName(SERIALIZED_NAME_STORE_ID)
  private String storeId;

  public static final String SERIALIZED_NAME_ORDER_TYPE = "orderType";
  @SerializedName(SERIALIZED_NAME_ORDER_TYPE)
  private String orderType;

  public static final String SERIALIZED_NAME_LATITUDE = "latitude";
  @SerializedName(SERIALIZED_NAME_LATITUDE)
  private BigDecimal latitude;

  public static final String SERIALIZED_NAME_LONGITUDE = "longitude";
  @SerializedName(SERIALIZED_NAME_LONGITUDE)
  private BigDecimal longitude;

  public static final String SERIALIZED_NAME_DELIVERY_ADDRESS = "deliveryAddress";
  @SerializedName(SERIALIZED_NAME_DELIVERY_ADDRESS)
  private String deliveryAddress;


  public PaymentSubmitOrderRequest userId(String userId) {
    
    this.userId = userId;
    return this;
  }

   /**
   * generated token used to identify the caller of the endpoint
   * @return userId
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "generated token used to identify the caller of the endpoint")

  public String getUserId() {
    return userId;
  }


  public void setUserId(String userId) {
    this.userId = userId;
  }


  public PaymentSubmitOrderRequest listOfItems(List<ItemObject> listOfItems) {
    
    this.listOfItems = listOfItems;
    return this;
  }

  public PaymentSubmitOrderRequest addListOfItemsItem(ItemObject listOfItemsItem) {
    if (this.listOfItems == null) {
      this.listOfItems = new ArrayList<ItemObject>();
    }
    this.listOfItems.add(listOfItemsItem);
    return this;
  }

   /**
   * Get listOfItems
   * @return listOfItems
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public List<ItemObject> getListOfItems() {
    return listOfItems;
  }


  public void setListOfItems(List<ItemObject> listOfItems) {
    this.listOfItems = listOfItems;
  }


  public PaymentSubmitOrderRequest discount(BigDecimal discount) {
    
    this.discount = discount;
    return this;
  }

   /**
   * Get discount
   * @return discount
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public BigDecimal getDiscount() {
    return discount;
  }


  public void setDiscount(BigDecimal discount) {
    this.discount = discount;
  }


  public PaymentSubmitOrderRequest storeId(String storeId) {
    
    this.storeId = storeId;
    return this;
  }

   /**
   * Get storeId
   * @return storeId
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getStoreId() {
    return storeId;
  }


  public void setStoreId(String storeId) {
    this.storeId = storeId;
  }


  public PaymentSubmitOrderRequest orderType(String orderType) {
    
    this.orderType = orderType;
    return this;
  }

   /**
   * Get orderType
   * @return orderType
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getOrderType() {
    return orderType;
  }


  public void setOrderType(String orderType) {
    this.orderType = orderType;
  }


  public PaymentSubmitOrderRequest latitude(BigDecimal latitude) {
    
    this.latitude = latitude;
    return this;
  }

   /**
   * Get latitude
   * @return latitude
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public BigDecimal getLatitude() {
    return latitude;
  }


  public void setLatitude(BigDecimal latitude) {
    this.latitude = latitude;
  }


  public PaymentSubmitOrderRequest longitude(BigDecimal longitude) {
    
    this.longitude = longitude;
    return this;
  }

   /**
   * Get longitude
   * @return longitude
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public BigDecimal getLongitude() {
    return longitude;
  }


  public void setLongitude(BigDecimal longitude) {
    this.longitude = longitude;
  }


  public PaymentSubmitOrderRequest deliveryAddress(String deliveryAddress) {
    
    this.deliveryAddress = deliveryAddress;
    return this;
  }

   /**
   * Get deliveryAddress
   * @return deliveryAddress
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getDeliveryAddress() {
    return deliveryAddress;
  }


  public void setDeliveryAddress(String deliveryAddress) {
    this.deliveryAddress = deliveryAddress;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PaymentSubmitOrderRequest paymentSubmitOrderRequest = (PaymentSubmitOrderRequest) o;
    return Objects.equals(this.userId, paymentSubmitOrderRequest.userId) &&
        Objects.equals(this.listOfItems, paymentSubmitOrderRequest.listOfItems) &&
        Objects.equals(this.discount, paymentSubmitOrderRequest.discount) &&
        Objects.equals(this.storeId, paymentSubmitOrderRequest.storeId) &&
        Objects.equals(this.orderType, paymentSubmitOrderRequest.orderType) &&
        Objects.equals(this.latitude, paymentSubmitOrderRequest.latitude) &&
        Objects.equals(this.longitude, paymentSubmitOrderRequest.longitude) &&
        Objects.equals(this.deliveryAddress, paymentSubmitOrderRequest.deliveryAddress);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, listOfItems, discount, storeId, orderType, latitude, longitude, deliveryAddress);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PaymentSubmitOrderRequest {\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    listOfItems: ").append(toIndentedString(listOfItems)).append("\n");
    sb.append("    discount: ").append(toIndentedString(discount)).append("\n");
    sb.append("    storeId: ").append(toIndentedString(storeId)).append("\n");
    sb.append("    orderType: ").append(toIndentedString(orderType)).append("\n");
    sb.append("    latitude: ").append(toIndentedString(latitude)).append("\n");
    sb.append("    longitude: ").append(toIndentedString(longitude)).append("\n");
    sb.append("    deliveryAddress: ").append(toIndentedString(deliveryAddress)).append("\n");
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
