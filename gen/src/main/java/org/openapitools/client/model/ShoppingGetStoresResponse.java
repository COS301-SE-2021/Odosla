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
import org.openapitools.client.model.StoreObject;

/**
 * This object is expected as input
 */
@ApiModel(description = "This object is expected as input")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2021-08-16T17:50:09.822513100+02:00[Africa/Harare]")
public class ShoppingGetStoresResponse {
  public static final String SERIALIZED_NAME_RESPONSE = "response";
  @SerializedName(SERIALIZED_NAME_RESPONSE)
  private Boolean response;

  public static final String SERIALIZED_NAME_MESSAGE = "message";
  @SerializedName(SERIALIZED_NAME_MESSAGE)
  private String message;

  public static final String SERIALIZED_NAME_STORES = "stores";
  @SerializedName(SERIALIZED_NAME_STORES)
  private List<StoreObject> stores = null;


  public ShoppingGetStoresResponse response(Boolean response) {
    
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


  public ShoppingGetStoresResponse message(String message) {
    
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


  public ShoppingGetStoresResponse stores(List<StoreObject> stores) {
    
    this.stores = stores;
    return this;
  }

  public ShoppingGetStoresResponse addStoresItem(StoreObject storesItem) {
    if (this.stores == null) {
      this.stores = new ArrayList<StoreObject>();
    }
    this.stores.add(storesItem);
    return this;
  }

   /**
   * Get stores
   * @return stores
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public List<StoreObject> getStores() {
    return stores;
  }


  public void setStores(List<StoreObject> stores) {
    this.stores = stores;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ShoppingGetStoresResponse shoppingGetStoresResponse = (ShoppingGetStoresResponse) o;
    return Objects.equals(this.response, shoppingGetStoresResponse.response) &&
        Objects.equals(this.message, shoppingGetStoresResponse.message) &&
        Objects.equals(this.stores, shoppingGetStoresResponse.stores);
  }

  @Override
  public int hashCode() {
    return Objects.hash(response, message, stores);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ShoppingGetStoresResponse {\n");
    sb.append("    response: ").append(toIndentedString(response)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    stores: ").append(toIndentedString(stores)).append("\n");
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

