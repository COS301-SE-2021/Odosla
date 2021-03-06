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
import org.openapitools.client.model.GeoPointObject;

/**
 * Generic schema for a Driver
 */
@ApiModel(description = "Generic schema for a Driver")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2021-08-26T18:20:06.034903200+02:00[Africa/Harare]")
public class DriverObject {
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

  public static final String SERIALIZED_NAME_DRIVER_I_D = "driverID";
  @SerializedName(SERIALIZED_NAME_DRIVER_I_D)
  private String driverID;

  public static final String SERIALIZED_NAME_RATING = "rating";
  @SerializedName(SERIALIZED_NAME_RATING)
  private BigDecimal rating;

  public static final String SERIALIZED_NAME_ON_SHIFT = "onShift";
  @SerializedName(SERIALIZED_NAME_ON_SHIFT)
  private Boolean onShift;

  public static final String SERIALIZED_NAME_CURRENT_ADDRESS = "currentAddress";
  @SerializedName(SERIALIZED_NAME_CURRENT_ADDRESS)
  private GeoPointObject currentAddress;

  public static final String SERIALIZED_NAME_DELIVERIES_COMPLETED = "deliveriesCompleted";
  @SerializedName(SERIALIZED_NAME_DELIVERIES_COMPLETED)
  private BigDecimal deliveriesCompleted;


  public DriverObject name(String name) {
    
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


  public DriverObject surname(String surname) {
    
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


  public DriverObject email(String email) {
    
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


  public DriverObject phoneNumber(String phoneNumber) {
    
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


  public DriverObject password(String password) {
    
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


  public DriverObject activationDate(String activationDate) {
    
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


  public DriverObject activationCode(String activationCode) {
    
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


  public DriverObject resetCode(String resetCode) {
    
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


  public DriverObject resetExpiration(String resetExpiration) {
    
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


  public DriverObject accountType(String accountType) {
    
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


  public DriverObject driverID(String driverID) {
    
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


  public DriverObject rating(BigDecimal rating) {
    
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


  public DriverObject onShift(Boolean onShift) {
    
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


  public DriverObject currentAddress(GeoPointObject currentAddress) {
    
    this.currentAddress = currentAddress;
    return this;
  }

   /**
   * Get currentAddress
   * @return currentAddress
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public GeoPointObject getCurrentAddress() {
    return currentAddress;
  }


  public void setCurrentAddress(GeoPointObject currentAddress) {
    this.currentAddress = currentAddress;
  }


  public DriverObject deliveriesCompleted(BigDecimal deliveriesCompleted) {
    
    this.deliveriesCompleted = deliveriesCompleted;
    return this;
  }

   /**
   * Get deliveriesCompleted
   * @return deliveriesCompleted
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public BigDecimal getDeliveriesCompleted() {
    return deliveriesCompleted;
  }


  public void setDeliveriesCompleted(BigDecimal deliveriesCompleted) {
    this.deliveriesCompleted = deliveriesCompleted;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DriverObject driverObject = (DriverObject) o;
    return Objects.equals(this.name, driverObject.name) &&
        Objects.equals(this.surname, driverObject.surname) &&
        Objects.equals(this.email, driverObject.email) &&
        Objects.equals(this.phoneNumber, driverObject.phoneNumber) &&
        Objects.equals(this.password, driverObject.password) &&
        Objects.equals(this.activationDate, driverObject.activationDate) &&
        Objects.equals(this.activationCode, driverObject.activationCode) &&
        Objects.equals(this.resetCode, driverObject.resetCode) &&
        Objects.equals(this.resetExpiration, driverObject.resetExpiration) &&
        Objects.equals(this.accountType, driverObject.accountType) &&
        Objects.equals(this.driverID, driverObject.driverID) &&
        Objects.equals(this.rating, driverObject.rating) &&
        Objects.equals(this.onShift, driverObject.onShift) &&
        Objects.equals(this.currentAddress, driverObject.currentAddress) &&
        Objects.equals(this.deliveriesCompleted, driverObject.deliveriesCompleted);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, surname, email, phoneNumber, password, activationDate, activationCode, resetCode, resetExpiration, accountType, driverID, rating, onShift, currentAddress, deliveriesCompleted);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DriverObject {\n");
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
    sb.append("    driverID: ").append(toIndentedString(driverID)).append("\n");
    sb.append("    rating: ").append(toIndentedString(rating)).append("\n");
    sb.append("    onShift: ").append(toIndentedString(onShift)).append("\n");
    sb.append("    currentAddress: ").append(toIndentedString(currentAddress)).append("\n");
    sb.append("    deliveriesCompleted: ").append(toIndentedString(deliveriesCompleted)).append("\n");
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

