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
public class ShoppingGetShoppersRequest {
  public static final String SERIALIZED_NAME_JWT_TOKEN = "jwtToken";
  @SerializedName(SERIALIZED_NAME_JWT_TOKEN)
  private String jwtToken;

  public static final String SERIALIZED_NAME_STORE_I_D = "storeID";
  @SerializedName(SERIALIZED_NAME_STORE_I_D)
  private String storeID;


  public ShoppingGetShoppersRequest jwtToken(String jwtToken) {
    
    this.jwtToken = jwtToken;
    return this;
  }

   /**
   * generated token used to identify the caller of the endpoint
   * @return jwtToken
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "generated token used to identify the caller of the endpoint")

  public String getJwtToken() {
    return jwtToken;
  }


  public void setJwtToken(String jwtToken) {
    this.jwtToken = jwtToken;
  }


  public ShoppingGetShoppersRequest storeID(String storeID) {
    
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
    ShoppingGetShoppersRequest shoppingGetShoppersRequest = (ShoppingGetShoppersRequest) o;
    return Objects.equals(this.jwtToken, shoppingGetShoppersRequest.jwtToken) &&
        Objects.equals(this.storeID, shoppingGetShoppersRequest.storeID);
  }

  @Override
  public int hashCode() {
    return Objects.hash(jwtToken, storeID);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ShoppingGetShoppersRequest {\n");
    sb.append("    jwtToken: ").append(toIndentedString(jwtToken)).append("\n");
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

