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
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2021-08-14T14:53:01.663657800+02:00[Africa/Harare]")
public class UserScanItemRequest {
  public static final String SERIALIZED_NAME_ORDER_I_D = "orderID";
  @SerializedName(SERIALIZED_NAME_ORDER_I_D)
  private String orderID;

  public static final String SERIALIZED_NAME_BARCODE = "barcode";
  @SerializedName(SERIALIZED_NAME_BARCODE)
  private String barcode;


  public UserScanItemRequest orderID(String orderID) {
    
    this.orderID = orderID;
    return this;
  }

   /**
   * Get orderID
   * @return orderID
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getOrderID() {
    return orderID;
  }


  public void setOrderID(String orderID) {
    this.orderID = orderID;
  }


  public UserScanItemRequest barcode(String barcode) {
    
    this.barcode = barcode;
    return this;
  }

   /**
   * Get barcode
   * @return barcode
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getBarcode() {
    return barcode;
  }


  public void setBarcode(String barcode) {
    this.barcode = barcode;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserScanItemRequest userScanItemRequest = (UserScanItemRequest) o;
    return Objects.equals(this.orderID, userScanItemRequest.orderID) &&
        Objects.equals(this.barcode, userScanItemRequest.barcode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(orderID, barcode);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserScanItemRequest {\n");
    sb.append("    orderID: ").append(toIndentedString(orderID)).append("\n");
    sb.append("    barcode: ").append(toIndentedString(barcode)).append("\n");
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
