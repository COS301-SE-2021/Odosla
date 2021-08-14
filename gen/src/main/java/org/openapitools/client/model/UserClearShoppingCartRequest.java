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
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2021-08-14T16:46:33.559003800+02:00[Africa/Harare]")
public class UserClearShoppingCartRequest {
  public static final String SERIALIZED_NAME_CUSTOMER_I_D = "customerID";
  @SerializedName(SERIALIZED_NAME_CUSTOMER_I_D)
  private String customerID;


  public UserClearShoppingCartRequest customerID(String customerID) {
    
    this.customerID = customerID;
    return this;
  }

   /**
   * generated token used to identify the caller of the endpoint
   * @return customerID
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "generated token used to identify the caller of the endpoint")

  public String getCustomerID() {
    return customerID;
  }


  public void setCustomerID(String customerID) {
    this.customerID = customerID;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserClearShoppingCartRequest userClearShoppingCartRequest = (UserClearShoppingCartRequest) o;
    return Objects.equals(this.customerID, userClearShoppingCartRequest.customerID);
  }

  @Override
  public int hashCode() {
    return Objects.hash(customerID);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserClearShoppingCartRequest {\n");
    sb.append("    customerID: ").append(toIndentedString(customerID)).append("\n");
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

