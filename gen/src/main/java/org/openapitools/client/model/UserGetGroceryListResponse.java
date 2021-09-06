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
import org.openapitools.client.model.GroceryListObject;

/**
 * This object is expected as output
 */
@ApiModel(description = "This object is expected as output")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2021-08-26T18:20:06.034903200+02:00[Africa/Harare]")
public class UserGetGroceryListResponse {
  public static final String SERIALIZED_NAME_GROCERY_LISTS = "groceryLists";
  @SerializedName(SERIALIZED_NAME_GROCERY_LISTS)
  private List<GroceryListObject> groceryLists = null;

  public static final String SERIALIZED_NAME_SUCCESS = "success";
  @SerializedName(SERIALIZED_NAME_SUCCESS)
  private Boolean success;

  public static final String SERIALIZED_NAME_MESSAGE = "message";
  @SerializedName(SERIALIZED_NAME_MESSAGE)
  private String message;

  public static final String SERIALIZED_NAME_TIMESTAMP = "timestamp";
  @SerializedName(SERIALIZED_NAME_TIMESTAMP)
  private String timestamp;


  public UserGetGroceryListResponse groceryLists(List<GroceryListObject> groceryLists) {
    
    this.groceryLists = groceryLists;
    return this;
  }

  public UserGetGroceryListResponse addGroceryListsItem(GroceryListObject groceryListsItem) {
    if (this.groceryLists == null) {
      this.groceryLists = new ArrayList<GroceryListObject>();
    }
    this.groceryLists.add(groceryListsItem);
    return this;
  }

   /**
   * Get groceryLists
   * @return groceryLists
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public List<GroceryListObject> getGroceryLists() {
    return groceryLists;
  }


  public void setGroceryLists(List<GroceryListObject> groceryLists) {
    this.groceryLists = groceryLists;
  }


  public UserGetGroceryListResponse success(Boolean success) {
    
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


  public UserGetGroceryListResponse message(String message) {
    
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


  public UserGetGroceryListResponse timestamp(String timestamp) {
    
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
    UserGetGroceryListResponse userGetGroceryListResponse = (UserGetGroceryListResponse) o;
    return Objects.equals(this.groceryLists, userGetGroceryListResponse.groceryLists) &&
        Objects.equals(this.success, userGetGroceryListResponse.success) &&
        Objects.equals(this.message, userGetGroceryListResponse.message) &&
        Objects.equals(this.timestamp, userGetGroceryListResponse.timestamp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(groceryLists, success, message, timestamp);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserGetGroceryListResponse {\n");
    sb.append("    groceryLists: ").append(toIndentedString(groceryLists)).append("\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
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

