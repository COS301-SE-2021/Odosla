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
 * Generic schema for a store
 */
@ApiModel(description = "Generic schema for a store")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2021-08-16T17:50:09.822513100+02:00[Africa/Harare]")
public class StoreObject {
  public static final String SERIALIZED_NAME_STORE_I_D = "storeID";
  @SerializedName(SERIALIZED_NAME_STORE_I_D)
  private String storeID;

  public static final String SERIALIZED_NAME_STORE_BRAND = "storeBrand";
  @SerializedName(SERIALIZED_NAME_STORE_BRAND)
  private String storeBrand;

  public static final String SERIALIZED_NAME_IS_OPEN = "isOpen";
  @SerializedName(SERIALIZED_NAME_IS_OPEN)
  private Boolean isOpen;

  public static final String SERIALIZED_NAME_MAX_SHOPPERS = "maxShoppers";
  @SerializedName(SERIALIZED_NAME_MAX_SHOPPERS)
  private Integer maxShoppers;

  public static final String SERIALIZED_NAME_MAX_ORDERS = "maxOrders";
  @SerializedName(SERIALIZED_NAME_MAX_ORDERS)
  private Integer maxOrders;

  public static final String SERIALIZED_NAME_OPENING_TIME = "openingTime";
  @SerializedName(SERIALIZED_NAME_OPENING_TIME)
  private Integer openingTime;

  public static final String SERIALIZED_NAME_CLOSING_TIME = "closingTime";
  @SerializedName(SERIALIZED_NAME_CLOSING_TIME)
  private Integer closingTime;

  public static final String SERIALIZED_NAME_IMAGE_URL = "imageUrl";
  @SerializedName(SERIALIZED_NAME_IMAGE_URL)
  private String imageUrl;


  public StoreObject storeID(String storeID) {
    
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


  public StoreObject storeBrand(String storeBrand) {
    
    this.storeBrand = storeBrand;
    return this;
  }

   /**
   * Get storeBrand
   * @return storeBrand
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getStoreBrand() {
    return storeBrand;
  }


  public void setStoreBrand(String storeBrand) {
    this.storeBrand = storeBrand;
  }


  public StoreObject isOpen(Boolean isOpen) {
    
    this.isOpen = isOpen;
    return this;
  }

   /**
   * Get isOpen
   * @return isOpen
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public Boolean getIsOpen() {
    return isOpen;
  }


  public void setIsOpen(Boolean isOpen) {
    this.isOpen = isOpen;
  }


  public StoreObject maxShoppers(Integer maxShoppers) {
    
    this.maxShoppers = maxShoppers;
    return this;
  }

   /**
   * Get maxShoppers
   * @return maxShoppers
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public Integer getMaxShoppers() {
    return maxShoppers;
  }


  public void setMaxShoppers(Integer maxShoppers) {
    this.maxShoppers = maxShoppers;
  }


  public StoreObject maxOrders(Integer maxOrders) {
    
    this.maxOrders = maxOrders;
    return this;
  }

   /**
   * Get maxOrders
   * @return maxOrders
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public Integer getMaxOrders() {
    return maxOrders;
  }


  public void setMaxOrders(Integer maxOrders) {
    this.maxOrders = maxOrders;
  }


  public StoreObject openingTime(Integer openingTime) {
    
    this.openingTime = openingTime;
    return this;
  }

   /**
   * Get openingTime
   * @return openingTime
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public Integer getOpeningTime() {
    return openingTime;
  }


  public void setOpeningTime(Integer openingTime) {
    this.openingTime = openingTime;
  }


  public StoreObject closingTime(Integer closingTime) {
    
    this.closingTime = closingTime;
    return this;
  }

   /**
   * Get closingTime
   * @return closingTime
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public Integer getClosingTime() {
    return closingTime;
  }


  public void setClosingTime(Integer closingTime) {
    this.closingTime = closingTime;
  }


  public StoreObject imageUrl(String imageUrl) {
    
    this.imageUrl = imageUrl;
    return this;
  }

   /**
   * Get imageUrl
   * @return imageUrl
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getImageUrl() {
    return imageUrl;
  }


  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StoreObject storeObject = (StoreObject) o;
    return Objects.equals(this.storeID, storeObject.storeID) &&
        Objects.equals(this.storeBrand, storeObject.storeBrand) &&
        Objects.equals(this.isOpen, storeObject.isOpen) &&
        Objects.equals(this.maxShoppers, storeObject.maxShoppers) &&
        Objects.equals(this.maxOrders, storeObject.maxOrders) &&
        Objects.equals(this.openingTime, storeObject.openingTime) &&
        Objects.equals(this.closingTime, storeObject.closingTime) &&
        Objects.equals(this.imageUrl, storeObject.imageUrl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(storeID, storeBrand, isOpen, maxShoppers, maxOrders, openingTime, closingTime, imageUrl);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StoreObject {\n");
    sb.append("    storeID: ").append(toIndentedString(storeID)).append("\n");
    sb.append("    storeBrand: ").append(toIndentedString(storeBrand)).append("\n");
    sb.append("    isOpen: ").append(toIndentedString(isOpen)).append("\n");
    sb.append("    maxShoppers: ").append(toIndentedString(maxShoppers)).append("\n");
    sb.append("    maxOrders: ").append(toIndentedString(maxOrders)).append("\n");
    sb.append("    openingTime: ").append(toIndentedString(openingTime)).append("\n");
    sb.append("    closingTime: ").append(toIndentedString(closingTime)).append("\n");
    sb.append("    imageUrl: ").append(toIndentedString(imageUrl)).append("\n");
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

