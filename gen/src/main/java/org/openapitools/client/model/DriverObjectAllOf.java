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
 * DriverObjectAllOf
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2021-08-13T13:18:08.276081+02:00[Africa/Harare]")
public class DriverObjectAllOf {
  public static final String SERIALIZED_NAME_DRIVER_I_D = "driverID";
  @SerializedName(SERIALIZED_NAME_DRIVER_I_D)
  private String driverID;

  public static final String SERIALIZED_NAME_RATING = "rating";
  @SerializedName(SERIALIZED_NAME_RATING)
  private BigDecimal rating;

  public static final String SERIALIZED_NAME_ON_SHIFT = "onShift";
  @SerializedName(SERIALIZED_NAME_ON_SHIFT)
  private Boolean onShift;


  public DriverObjectAllOf driverID(String driverID) {
    
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


  public DriverObjectAllOf rating(BigDecimal rating) {
    
    this.rating = rating;
    return this;
  }

   /**
   * Get rating
   * @return rating
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public BigDecimal getRating() {
    return rating;
  }


  public void setRating(BigDecimal rating) {
    this.rating = rating;
  }


  public DriverObjectAllOf onShift(Boolean onShift) {
    
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
    DriverObjectAllOf driverObjectAllOf = (DriverObjectAllOf) o;
    return Objects.equals(this.driverID, driverObjectAllOf.driverID) &&
        Objects.equals(this.rating, driverObjectAllOf.rating) &&
        Objects.equals(this.onShift, driverObjectAllOf.onShift);
  }

  @Override
  public int hashCode() {
    return Objects.hash(driverID, rating, onShift);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DriverObjectAllOf {\n");
    sb.append("    driverID: ").append(toIndentedString(driverID)).append("\n");
    sb.append("    rating: ").append(toIndentedString(rating)).append("\n");
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

