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
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2021-08-16T17:50:09.822513100+02:00[Africa/Harare]")
public class UserUpdateDriverShiftRequest {
  public static final String SERIALIZED_NAME_JWT_TOKEN = "jwtToken";
  @SerializedName(SERIALIZED_NAME_JWT_TOKEN)
  private String jwtToken;

  public static final String SERIALIZED_NAME_ON_SHIFT = "onShift";
  @SerializedName(SERIALIZED_NAME_ON_SHIFT)
  private Boolean onShift;


  public UserUpdateDriverShiftRequest jwtToken(String jwtToken) {
    
    this.jwtToken = jwtToken;
    return this;
  }

   /**
   * Get jwtToken
   * @return jwtToken
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getJwtToken() {
    return jwtToken;
  }


  public void setJwtToken(String jwtToken) {
    this.jwtToken = jwtToken;
  }


  public UserUpdateDriverShiftRequest onShift(Boolean onShift) {
    
    this.onShift = onShift;
    return this;
  }

   /**
   * Get onShift
   * @return onShift
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public Boolean getOnShift() {
    return onShift;
  }


  public void setOnShift(Boolean onShift) {
    this.onShift = onShift;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserUpdateDriverShiftRequest userUpdateDriverShiftRequest = (UserUpdateDriverShiftRequest) o;
    return Objects.equals(this.jwtToken, userUpdateDriverShiftRequest.jwtToken) &&
        Objects.equals(this.onShift, userUpdateDriverShiftRequest.onShift);
  }

  @Override
  public int hashCode() {
    return Objects.hash(jwtToken, onShift);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserUpdateDriverShiftRequest {\n");
    sb.append("    jwtToken: ").append(toIndentedString(jwtToken)).append("\n");
    sb.append("    onShift: ").append(toIndentedString(onShift)).append("\n");
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

