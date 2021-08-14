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
 * Generic schema for an Order
 */
@ApiModel(description = "Generic schema for an Order")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2021-08-14T12:05:00.868146800+02:00[Africa/Harare]")
public class OrderObject {
  public static final String SERIALIZED_NAME_ORDER_ID = "orderId";
  @SerializedName(SERIALIZED_NAME_ORDER_ID)
  private String orderId;

  public static final String SERIALIZED_NAME_USER_ID = "userId";
  @SerializedName(SERIALIZED_NAME_USER_ID)
  private String userId;

  public static final String SERIALIZED_NAME_STORE_ID = "storeId";
  @SerializedName(SERIALIZED_NAME_STORE_ID)
  private String storeId;

  public static final String SERIALIZED_NAME_SHOPPER_ID = "shopperId";
  @SerializedName(SERIALIZED_NAME_SHOPPER_ID)
  private String shopperId;

  public static final String SERIALIZED_NAME_CREATE_DATE = "createDate";
  @SerializedName(SERIALIZED_NAME_CREATE_DATE)
  private String createDate;

  public static final String SERIALIZED_NAME_PROCESS_DATE = "processDate";
  @SerializedName(SERIALIZED_NAME_PROCESS_DATE)
  private String processDate;

  public static final String SERIALIZED_NAME_TOTAL_PRICE = "totalPrice";
  @SerializedName(SERIALIZED_NAME_TOTAL_PRICE)
  private BigDecimal totalPrice;

  public static final String SERIALIZED_NAME_STATUS = "status";
  @SerializedName(SERIALIZED_NAME_STATUS)
  private String status;

  public static final String SERIALIZED_NAME_ITEMS = "items";
  @SerializedName(SERIALIZED_NAME_ITEMS)
  private List<ItemObject> items = null;

  public static final String SERIALIZED_NAME_DISCOUNT = "discount";
  @SerializedName(SERIALIZED_NAME_DISCOUNT)
  private BigDecimal discount;

  public static final String SERIALIZED_NAME_DELIVERY_ADDRESS = "deliveryAddress";
  @SerializedName(SERIALIZED_NAME_DELIVERY_ADDRESS)
  private String deliveryAddress;

  public static final String SERIALIZED_NAME_STORE_ADDRESS = "storeAddress";
  @SerializedName(SERIALIZED_NAME_STORE_ADDRESS)
  private String storeAddress;

  public static final String SERIALIZED_NAME_REQUIRES_PHARMACY = "requiresPharmacy";
  @SerializedName(SERIALIZED_NAME_REQUIRES_PHARMACY)
  private Boolean requiresPharmacy;


  public OrderObject orderId(String orderId) {
    
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


  public OrderObject userId(String userId) {
    
    this.userId = userId;
    return this;
  }

   /**
   * Get userId
   * @return userId
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getUserId() {
    return userId;
  }


  public void setUserId(String userId) {
    this.userId = userId;
  }


  public OrderObject storeId(String storeId) {
    
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


  public OrderObject shopperId(String shopperId) {
    
    this.shopperId = shopperId;
    return this;
  }

   /**
   * Get shopperId
   * @return shopperId
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getShopperId() {
    return shopperId;
  }


  public void setShopperId(String shopperId) {
    this.shopperId = shopperId;
  }


  public OrderObject createDate(String createDate) {
    
    this.createDate = createDate;
    return this;
  }

   /**
   * Get createDate
   * @return createDate
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getCreateDate() {
    return createDate;
  }


  public void setCreateDate(String createDate) {
    this.createDate = createDate;
  }


  public OrderObject processDate(String processDate) {
    
    this.processDate = processDate;
    return this;
  }

   /**
   * Get processDate
   * @return processDate
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getProcessDate() {
    return processDate;
  }


  public void setProcessDate(String processDate) {
    this.processDate = processDate;
  }


  public OrderObject totalPrice(BigDecimal totalPrice) {
    
    this.totalPrice = totalPrice;
    return this;
  }

   /**
   * Get totalPrice
   * @return totalPrice
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public BigDecimal getTotalPrice() {
    return totalPrice;
  }


  public void setTotalPrice(BigDecimal totalPrice) {
    this.totalPrice = totalPrice;
  }


  public OrderObject status(String status) {
    
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


  public OrderObject items(List<ItemObject> items) {
    
    this.items = items;
    return this;
  }

  public OrderObject addItemsItem(ItemObject itemsItem) {
    if (this.items == null) {
      this.items = new ArrayList<ItemObject>();
    }
    this.items.add(itemsItem);
    return this;
  }

   /**
   * Get items
   * @return items
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public List<ItemObject> getItems() {
    return items;
  }


  public void setItems(List<ItemObject> items) {
    this.items = items;
  }


  public OrderObject discount(BigDecimal discount) {
    
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


  public OrderObject deliveryAddress(String deliveryAddress) {
    
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


  public OrderObject storeAddress(String storeAddress) {
    
    this.storeAddress = storeAddress;
    return this;
  }

   /**
   * Get storeAddress
   * @return storeAddress
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getStoreAddress() {
    return storeAddress;
  }


  public void setStoreAddress(String storeAddress) {
    this.storeAddress = storeAddress;
  }


  public OrderObject requiresPharmacy(Boolean requiresPharmacy) {
    
    this.requiresPharmacy = requiresPharmacy;
    return this;
  }

   /**
   * Get requiresPharmacy
   * @return requiresPharmacy
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public Boolean getRequiresPharmacy() {
    return requiresPharmacy;
  }


  public void setRequiresPharmacy(Boolean requiresPharmacy) {
    this.requiresPharmacy = requiresPharmacy;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OrderObject orderObject = (OrderObject) o;
    return Objects.equals(this.orderId, orderObject.orderId) &&
        Objects.equals(this.userId, orderObject.userId) &&
        Objects.equals(this.storeId, orderObject.storeId) &&
        Objects.equals(this.shopperId, orderObject.shopperId) &&
        Objects.equals(this.createDate, orderObject.createDate) &&
        Objects.equals(this.processDate, orderObject.processDate) &&
        Objects.equals(this.totalPrice, orderObject.totalPrice) &&
        Objects.equals(this.status, orderObject.status) &&
        Objects.equals(this.items, orderObject.items) &&
        Objects.equals(this.discount, orderObject.discount) &&
        Objects.equals(this.deliveryAddress, orderObject.deliveryAddress) &&
        Objects.equals(this.storeAddress, orderObject.storeAddress) &&
        Objects.equals(this.requiresPharmacy, orderObject.requiresPharmacy);
  }

  @Override
  public int hashCode() {
    return Objects.hash(orderId, userId, storeId, shopperId, createDate, processDate, totalPrice, status, items, discount, deliveryAddress, storeAddress, requiresPharmacy);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OrderObject {\n");
    sb.append("    orderId: ").append(toIndentedString(orderId)).append("\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    storeId: ").append(toIndentedString(storeId)).append("\n");
    sb.append("    shopperId: ").append(toIndentedString(shopperId)).append("\n");
    sb.append("    createDate: ").append(toIndentedString(createDate)).append("\n");
    sb.append("    processDate: ").append(toIndentedString(processDate)).append("\n");
    sb.append("    totalPrice: ").append(toIndentedString(totalPrice)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
    sb.append("    discount: ").append(toIndentedString(discount)).append("\n");
    sb.append("    deliveryAddress: ").append(toIndentedString(deliveryAddress)).append("\n");
    sb.append("    storeAddress: ").append(toIndentedString(storeAddress)).append("\n");
    sb.append("    requiresPharmacy: ").append(toIndentedString(requiresPharmacy)).append("\n");
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

