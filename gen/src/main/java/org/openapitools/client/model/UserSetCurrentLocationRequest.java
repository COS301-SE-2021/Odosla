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

/**
 * This object is expected as input
 */
@ApiModel(description = "This object is expected as input")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2021-08-13T14:22:29.077005400+02:00[Africa/Harare]")
public class UserSetCurrentLocationRequest {
  public static final String SERIALIZED_NAME_DRIVER_I_D = "driverID";
  @SerializedName(SERIALIZED_NAME_DRIVER_I_D)
  private String driverID;

  public static final String SERIALIZED_NAME_LATITUDE = "latitude";
  @SerializedName(SERIALIZED_NAME_LATITUDE)
  private BigDecimal latitude;

  public static final String SERIALIZED_NAME_LONGITUDE = "longitude";
  @SerializedName(SERIALIZED_NAME_LONGITUDE)
  private BigDecimal longitude;

  public static final String SERIALIZED_NAME_ADDRESS = "address";
  @SerializedName(SERIALIZED_NAME_ADDRESS)
  private String address;


  public UserSetCurrentLocationRequest driverID(String driverID) {
    
    this.driverID = driverID;
    return this;
  }

   /**
   * Get driverID
   * @return driverID
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getDriverID() {
    return driverID;
  }


  public void setDriverID(String driverID) {
    this.driverID = driverID;
  }


  public UserSetCurrentLocationRequest latitude(BigDecimal latitude) {
    
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


  public UserSetCurrentLocationRequest longitude(BigDecimal longitude) {
    
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


  public UserSetCurrentLocationRequest address(String address) {
    
    this.address = address;
    return this;
  }

   /**
   * Get address
   * @return address
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getAddress() {
    return address;
  }


  public void setAddress(String address) {
    this.address = address;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserSetCurrentLocationRequest userSetCurrentLocationRequest = (UserSetCurrentLocationRequest) o;
    return Objects.equals(this.driverID, userSetCurrentLocationRequest.driverID) &&
        Objects.equals(this.latitude, userSetCurrentLocationRequest.latitude) &&
        Objects.equals(this.longitude, userSetCurrentLocationRequest.longitude) &&
        Objects.equals(this.address, userSetCurrentLocationRequest.address);
  }

  @Override
  public int hashCode() {
    return Objects.hash(driverID, latitude, longitude, address);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserSetCurrentLocationRequest {\n");
    sb.append("    driverID: ").append(toIndentedString(driverID)).append("\n");
    sb.append("    latitude: ").append(toIndentedString(latitude)).append("\n");
    sb.append("    longitude: ").append(toIndentedString(longitude)).append("\n");
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
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

