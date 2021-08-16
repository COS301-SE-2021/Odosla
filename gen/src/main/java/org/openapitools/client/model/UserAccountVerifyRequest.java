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
public class UserAccountVerifyRequest {
  public static final String SERIALIZED_NAME_EMAIL = "email";
  @SerializedName(SERIALIZED_NAME_EMAIL)
  private String email;

  public static final String SERIALIZED_NAME_ACTIVATION_CODE = "activationCode";
  @SerializedName(SERIALIZED_NAME_ACTIVATION_CODE)
  private String activationCode;

  public static final String SERIALIZED_NAME_USER_TYPE = "userType";
  @SerializedName(SERIALIZED_NAME_USER_TYPE)
  private String userType;


  public UserAccountVerifyRequest email(String email) {
    
    this.email = email;
    return this;
  }

   /**
   * Get email
   * @return email
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getEmail() {
    return email;
  }


  public void setEmail(String email) {
    this.email = email;
  }


  public UserAccountVerifyRequest activationCode(String activationCode) {
    
    this.activationCode = activationCode;
    return this;
  }

   /**
   * Get activationCode
   * @return activationCode
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getActivationCode() {
    return activationCode;
  }


  public void setActivationCode(String activationCode) {
    this.activationCode = activationCode;
  }


  public UserAccountVerifyRequest userType(String userType) {
    
    this.userType = userType;
    return this;
  }

   /**
   * Get userType
   * @return userType
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getUserType() {
    return userType;
  }


  public void setUserType(String userType) {
    this.userType = userType;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserAccountVerifyRequest userAccountVerifyRequest = (UserAccountVerifyRequest) o;
    return Objects.equals(this.email, userAccountVerifyRequest.email) &&
        Objects.equals(this.activationCode, userAccountVerifyRequest.activationCode) &&
        Objects.equals(this.userType, userAccountVerifyRequest.userType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(email, activationCode, userType);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserAccountVerifyRequest {\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    activationCode: ").append(toIndentedString(activationCode)).append("\n");
    sb.append("    userType: ").append(toIndentedString(userType)).append("\n");
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

