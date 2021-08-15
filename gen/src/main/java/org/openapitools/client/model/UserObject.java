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
 * Generic schema for a User
 */
@ApiModel(description = "Generic schema for a User")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2021-08-14T22:27:51.898546700+02:00[Africa/Harare]")
public class UserObject {
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

  public static final String SERIALIZED_NAME_ACTIVATION_DATE = "activationDate";
  @SerializedName(SERIALIZED_NAME_ACTIVATION_DATE)
  private String activationDate;

  public static final String SERIALIZED_NAME_ACTIVATION_CODE = "activationCode";
  @SerializedName(SERIALIZED_NAME_ACTIVATION_CODE)
  private String activationCode;

  public static final String SERIALIZED_NAME_RESET_CODE = "resetCode";
  @SerializedName(SERIALIZED_NAME_RESET_CODE)
  private String resetCode;

  public static final String SERIALIZED_NAME_RESET_EXPIRATION = "resetExpiration";
  @SerializedName(SERIALIZED_NAME_RESET_EXPIRATION)
  private String resetExpiration;

  public static final String SERIALIZED_NAME_ACCOUNT_TYPE = "accountType";
  @SerializedName(SERIALIZED_NAME_ACCOUNT_TYPE)
  private String accountType;


  public UserObject name(String name) {
    
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


  public UserObject surname(String surname) {
    
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


  public UserObject email(String email) {
    
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


  public UserObject phoneNumber(String phoneNumber) {
    
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


  public UserObject password(String password) {
    
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


  public UserObject activationDate(String activationDate) {
    
    this.activationDate = activationDate;
    return this;
  }

   /**
   * Get activationDate
   * @return activationDate
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getActivationDate() {
    return activationDate;
  }


  public void setActivationDate(String activationDate) {
    this.activationDate = activationDate;
  }


  public UserObject activationCode(String activationCode) {
    
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


  public UserObject resetCode(String resetCode) {
    
    this.resetCode = resetCode;
    return this;
  }

   /**
   * Get resetCode
   * @return resetCode
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getResetCode() {
    return resetCode;
  }


  public void setResetCode(String resetCode) {
    this.resetCode = resetCode;
  }


  public UserObject resetExpiration(String resetExpiration) {
    
    this.resetExpiration = resetExpiration;
    return this;
  }

   /**
   * Get resetExpiration
   * @return resetExpiration
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getResetExpiration() {
    return resetExpiration;
  }


  public void setResetExpiration(String resetExpiration) {
    this.resetExpiration = resetExpiration;
  }


  public UserObject accountType(String accountType) {
    
    this.accountType = accountType;
    return this;
  }

   /**
   * Get accountType
   * @return accountType
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getAccountType() {
    return accountType;
  }


  public void setAccountType(String accountType) {
    this.accountType = accountType;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserObject userObject = (UserObject) o;
    return Objects.equals(this.name, userObject.name) &&
        Objects.equals(this.surname, userObject.surname) &&
        Objects.equals(this.email, userObject.email) &&
        Objects.equals(this.phoneNumber, userObject.phoneNumber) &&
        Objects.equals(this.password, userObject.password) &&
        Objects.equals(this.activationDate, userObject.activationDate) &&
        Objects.equals(this.activationCode, userObject.activationCode) &&
        Objects.equals(this.resetCode, userObject.resetCode) &&
        Objects.equals(this.resetExpiration, userObject.resetExpiration) &&
        Objects.equals(this.accountType, userObject.accountType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, surname, email, phoneNumber, password, activationDate, activationCode, resetCode, resetExpiration, accountType);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserObject {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    surname: ").append(toIndentedString(surname)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    phoneNumber: ").append(toIndentedString(phoneNumber)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
    sb.append("    activationDate: ").append(toIndentedString(activationDate)).append("\n");
    sb.append("    activationCode: ").append(toIndentedString(activationCode)).append("\n");
    sb.append("    resetCode: ").append(toIndentedString(resetCode)).append("\n");
    sb.append("    resetExpiration: ").append(toIndentedString(resetExpiration)).append("\n");
    sb.append("    accountType: ").append(toIndentedString(accountType)).append("\n");
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

