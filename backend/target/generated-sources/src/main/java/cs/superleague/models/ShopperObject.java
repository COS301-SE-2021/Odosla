package cs.superleague.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Generic schema for a shopper
 */
@ApiModel(description = "Generic schema for a shopper")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-08-14T15:01:46.157134600+02:00[Africa/Harare]")
public class ShopperObject  implements OneOfuserGetCurrentUserResponseUser {
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

  @JsonProperty("shopperID")
  private String shopperID = null;

  @JsonProperty("storeID")
  private String storeID = null;

  @JsonProperty("ordersCompleted")
  private Integer ordersCompleted = null;

  @JsonProperty("onShift")
  private Boolean onShift = null;

  @JsonProperty("isActive")
  private Boolean isActive = null;

  public ShopperObject name(String name) {
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

  public ShopperObject surname(String surname) {
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

  public ShopperObject email(String email) {
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

  public ShopperObject phoneNumber(String phoneNumber) {
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

  public ShopperObject password(String password) {
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

  public ShopperObject activationDate(String activationDate) {
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

  public ShopperObject activationCode(String activationCode) {
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

  public ShopperObject resetCode(String resetCode) {
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

  public ShopperObject resetExpiration(String resetExpiration) {
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

  public ShopperObject accountType(String accountType) {
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

  public ShopperObject shopperID(String shopperID) {
    this.shopperID = shopperID;
    return this;
  }

  /**
   * Get shopperID
   * @return shopperID
  **/
  @ApiModelProperty(value = "")
  
    public String getShopperID() {
    return shopperID;
  }

  public void setShopperID(String shopperID) {
    this.shopperID = shopperID;
  }

  public ShopperObject storeID(String storeID) {
    this.storeID = storeID;
    return this;
  }

  /**
   * Get storeID
   * @return storeID
  **/
  @ApiModelProperty(value = "")
  
    public String getStoreID() {
    return storeID;
  }

  public void setStoreID(String storeID) {
    this.storeID = storeID;
  }

  public ShopperObject ordersCompleted(Integer ordersCompleted) {
    this.ordersCompleted = ordersCompleted;
    return this;
  }

  /**
   * Get ordersCompleted
   * @return ordersCompleted
  **/
  @ApiModelProperty(value = "")
  
    public Integer getOrdersCompleted() {
    return ordersCompleted;
  }

  public void setOrdersCompleted(Integer ordersCompleted) {
    this.ordersCompleted = ordersCompleted;
  }

  public ShopperObject onShift(Boolean onShift) {
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

  public ShopperObject isActive(Boolean isActive) {
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
    ShopperObject shopperObject = (ShopperObject) o;
    return Objects.equals(this.name, shopperObject.name) &&
        Objects.equals(this.surname, shopperObject.surname) &&
        Objects.equals(this.email, shopperObject.email) &&
        Objects.equals(this.phoneNumber, shopperObject.phoneNumber) &&
        Objects.equals(this.password, shopperObject.password) &&
        Objects.equals(this.activationDate, shopperObject.activationDate) &&
        Objects.equals(this.activationCode, shopperObject.activationCode) &&
        Objects.equals(this.resetCode, shopperObject.resetCode) &&
        Objects.equals(this.resetExpiration, shopperObject.resetExpiration) &&
        Objects.equals(this.accountType, shopperObject.accountType) &&
        Objects.equals(this.shopperID, shopperObject.shopperID) &&
        Objects.equals(this.storeID, shopperObject.storeID) &&
        Objects.equals(this.ordersCompleted, shopperObject.ordersCompleted) &&
        Objects.equals(this.onShift, shopperObject.onShift) &&
        Objects.equals(this.isActive, shopperObject.isActive);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, surname, email, phoneNumber, password, activationDate, activationCode, resetCode, resetExpiration, accountType, shopperID, storeID, ordersCompleted, onShift, isActive);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ShopperObject {\n");
    
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
    sb.append("    shopperID: ").append(toIndentedString(shopperID)).append("\n");
    sb.append("    storeID: ").append(toIndentedString(storeID)).append("\n");
    sb.append("    ordersCompleted: ").append(toIndentedString(ordersCompleted)).append("\n");
    sb.append("    onShift: ").append(toIndentedString(onShift)).append("\n");
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
