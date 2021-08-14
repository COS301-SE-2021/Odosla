package cs.superleague.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Generic schema for a Driver
 */
@ApiModel(description = "Generic schema for a Driver")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-08-14T13:48:59.955289300+02:00[Africa/Harare]")
public class DriverObject  implements OneOfuserGetCurrentUserResponseUser {
  @JsonProperty("name")
  private String name = null;

  @JsonProperty("surname")
  private String surname = null;

  @JsonProperty("email")
  private String email = null;

  @JsonProperty("phoneNumber")
  private String phoneNumber = null;

  @JsonProperty("password")
  private String password = null;

  @JsonProperty("activationDate")
  private String activationDate = null;

  @JsonProperty("activationCode")
  private String activationCode = null;

  @JsonProperty("resetCode")
  private String resetCode = null;

  @JsonProperty("resetExpiration")
  private String resetExpiration = null;

  @JsonProperty("accountType")
  private String accountType = null;

  @JsonProperty("driverID")
  private String driverID = null;

  @JsonProperty("rating")
  private BigDecimal rating = null;

  @JsonProperty("onShift")
  private Boolean onShift = null;

  public DriverObject name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
  **/
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
  @ApiModelProperty(value = "")
  
    @Valid
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
  @ApiModelProperty(value = "")
  
    public Boolean isOnShift() {
    return onShift;
  }

  public void setOnShift(Boolean onShift) {
    this.onShift = onShift;
  }


  @Override
  public boolean equals(java.lang.Object o) {
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
        Objects.equals(this.onShift, driverObject.onShift);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, surname, email, phoneNumber, password, activationDate, activationCode, resetCode, resetExpiration, accountType, driverID, rating, onShift);
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
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
