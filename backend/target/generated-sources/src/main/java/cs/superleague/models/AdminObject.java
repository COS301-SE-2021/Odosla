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
 * Generic schema for a Admin
 */
@ApiModel(description = "Generic schema for a Admin")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-08-13T16:31:49.521809+02:00[Africa/Harare]")
public class AdminObject  implements OneOfuserGetCurrentUserResponseUser {
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

  @JsonProperty("adminID")
  private String adminID = null;

  @JsonProperty("rating")
  private BigDecimal rating = null;

  @JsonProperty("isActive")
  private Boolean isActive = null;

  public AdminObject name(String name) {
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

  public AdminObject surname(String surname) {
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

  public AdminObject email(String email) {
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

  public AdminObject phoneNumber(String phoneNumber) {
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

  public AdminObject password(String password) {
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

  public AdminObject activationDate(String activationDate) {
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

  public AdminObject activationCode(String activationCode) {
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

  public AdminObject resetCode(String resetCode) {
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

  public AdminObject resetExpiration(String resetExpiration) {
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

  public AdminObject accountType(String accountType) {
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

  public AdminObject adminID(String adminID) {
    this.adminID = adminID;
    return this;
  }

  /**
   * Get adminID
   * @return adminID
  **/
  @ApiModelProperty(value = "")
  
    public String getAdminID() {
    return adminID;
  }

  public void setAdminID(String adminID) {
    this.adminID = adminID;
  }

  public AdminObject rating(BigDecimal rating) {
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

  public AdminObject isActive(Boolean isActive) {
    this.isActive = isActive;
    return this;
  }

  /**
   * Get isActive
   * @return isActive
  **/
  @ApiModelProperty(value = "")
  
    public Boolean isIsActive() {
    return isActive;
  }

  public void setIsActive(Boolean isActive) {
    this.isActive = isActive;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AdminObject adminObject = (AdminObject) o;
    return Objects.equals(this.name, adminObject.name) &&
        Objects.equals(this.surname, adminObject.surname) &&
        Objects.equals(this.email, adminObject.email) &&
        Objects.equals(this.phoneNumber, adminObject.phoneNumber) &&
        Objects.equals(this.password, adminObject.password) &&
        Objects.equals(this.activationDate, adminObject.activationDate) &&
        Objects.equals(this.activationCode, adminObject.activationCode) &&
        Objects.equals(this.resetCode, adminObject.resetCode) &&
        Objects.equals(this.resetExpiration, adminObject.resetExpiration) &&
        Objects.equals(this.accountType, adminObject.accountType) &&
        Objects.equals(this.adminID, adminObject.adminID) &&
        Objects.equals(this.rating, adminObject.rating) &&
        Objects.equals(this.isActive, adminObject.isActive);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, surname, email, phoneNumber, password, activationDate, activationCode, resetCode, resetExpiration, accountType, adminID, rating, isActive);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AdminObject {\n");
    
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
    sb.append("    adminID: ").append(toIndentedString(adminID)).append("\n");
    sb.append("    rating: ").append(toIndentedString(rating)).append("\n");
    sb.append("    isActive: ").append(toIndentedString(isActive)).append("\n");
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
