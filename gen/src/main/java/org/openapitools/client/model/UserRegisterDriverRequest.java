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
public class UserRegisterDriverRequest {
  public static final String SERIALIZED_NAME_NAME = "name";
  @SerializedName(SERIALIZED_NAME_NAME)
  private String name;

  public static final String SERIALIZED_NAME_SURNAME = "surname";
  @SerializedName(SERIALIZED_NAME_SURNAME)
  private String surname;

  public static final String SERIALIZED_NAME_EMAIL = "email";
  @SerializedName(SERIALIZED_NAME_EMAIL)
  private String email;

  public static final String SERIALIZED_NAME_PHONE_NUMBER = "phoneNumber";
  @SerializedName(SERIALIZED_NAME_PHONE_NUMBER)
  private String phoneNumber;

  public static final String SERIALIZED_NAME_PASSWORD = "password";
  @SerializedName(SERIALIZED_NAME_PASSWORD)
  private String password;


  public UserRegisterDriverRequest name(String name) {
    
    this.name = name;
    return this;
  }

   /**
   * Get name
   * @return name
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getName() {
    return name;
  }


  public void setName(String name) {
    this.name = name;
  }


  public UserRegisterDriverRequest surname(String surname) {
    
    this.surname = surname;
    return this;
  }

   /**
   * Get surname
   * @return surname
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getSurname() {
    return surname;
  }


  public void setSurname(String surname) {
    this.surname = surname;
  }


  public UserRegisterDriverRequest email(String email) {
    
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


  public UserRegisterDriverRequest phoneNumber(String phoneNumber) {
    
    this.phoneNumber = phoneNumber;
    return this;
  }

   /**
   * Get phoneNumber
   * @return phoneNumber
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getPhoneNumber() {
    return phoneNumber;
  }


  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }


  public UserRegisterDriverRequest password(String password) {
    
    this.password = password;
    return this;
  }

   /**
   * Get password
   * @return password
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getPassword() {
    return password;
  }


  public void setPassword(String password) {
    this.password = password;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserRegisterDriverRequest userRegisterDriverRequest = (UserRegisterDriverRequest) o;
    return Objects.equals(this.name, userRegisterDriverRequest.name) &&
        Objects.equals(this.surname, userRegisterDriverRequest.surname) &&
        Objects.equals(this.email, userRegisterDriverRequest.email) &&
        Objects.equals(this.phoneNumber, userRegisterDriverRequest.phoneNumber) &&
        Objects.equals(this.password, userRegisterDriverRequest.password);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, surname, email, phoneNumber, password);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserRegisterDriverRequest {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    surname: ").append(toIndentedString(surname)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    phoneNumber: ").append(toIndentedString(phoneNumber)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
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

