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
public class ShoppingAddShopperRequest {
  public static final String SERIALIZED_NAME_SHOPPER_I_D = "shopperID";
  @SerializedName(SERIALIZED_NAME_SHOPPER_I_D)
  private String shopperID;

  public static final String SERIALIZED_NAME_STORE_I_D = "storeID";
  @SerializedName(SERIALIZED_NAME_STORE_I_D)
  private String storeID;


  public ShoppingAddShopperRequest shopperID(String shopperID) {
    
    this.shopperID = shopperID;
    return this;
  }

   /**
   * Get shopperID
   * @return shopperID
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getShopperID() {
    return shopperID;
  }


  public void setShopperID(String shopperID) {
    this.shopperID = shopperID;
  }


  public ShoppingAddShopperRequest storeID(String storeID) {
    
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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ShoppingAddShopperRequest shoppingAddShopperRequest = (ShoppingAddShopperRequest) o;
    return Objects.equals(this.shopperID, shoppingAddShopperRequest.shopperID) &&
        Objects.equals(this.storeID, shoppingAddShopperRequest.storeID);
  }

  @Override
  public int hashCode() {
    return Objects.hash(shopperID, storeID);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ShoppingAddShopperRequest {\n");
    sb.append("    shopperID: ").append(toIndentedString(shopperID)).append("\n");
    sb.append("    storeID: ").append(toIndentedString(storeID)).append("\n");
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

