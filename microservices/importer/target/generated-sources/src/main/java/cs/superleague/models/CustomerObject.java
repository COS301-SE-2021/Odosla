package cs.superleague.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import cs.superleague.models.GroceryListObject;
import cs.superleague.models.ItemObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Generic schema for a Customer
 */
@ApiModel(description = "Generic schema for a Customer")
@Validated
public class CustomerObject   {
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

  @JsonProperty("customerID")
  private String customerID = null;

  @JsonProperty("address")
  private String address = null;

  @JsonProperty("groceryLists")
  @Valid
  private List<GroceryListObject> groceryLists = null;

  @JsonProperty("shoppingCart")
  @Valid
  private List<ItemObject> shoppingCart = null;

  @JsonProperty("preference")
  private Object preference = null;

  @JsonProperty("wallet")
  private Object wallet = null;

  public CustomerObject name(String name) {
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

  public CustomerObject surname(String surname) {
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

  public CustomerObject email(String email) {
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

  public CustomerObject phoneNumber(String phoneNumber) {
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

  public CustomerObject password(String password) {
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

  public CustomerObject activationDate(String activationDate) {
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

  public CustomerObject activationCode(String activationCode) {
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

  public CustomerObject resetCode(String resetCode) {
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

  public CustomerObject resetExpiration(String resetExpiration) {
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

  public CustomerObject accountType(String accountType) {
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

  public CustomerObject customerID(String customerID) {
    this.customerID = customerID;
    return this;
  }

  /**
   * Get customerID
   * @return customerID
  **/
  @ApiModelProperty(value = "")
  
    public String getCustomerID() {
    return customerID;
  }

  public void setCustomerID(String customerID) {
    this.customerID = customerID;
  }

  public CustomerObject address(String address) {
    this.address = address;
    return this;
  }

  /**
   * Get address
   * @return address
  **/
  @ApiModelProperty(value = "")
  
    public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public CustomerObject groceryLists(List<GroceryListObject> groceryLists) {
    this.groceryLists = groceryLists;
    return this;
  }

  public CustomerObject addGroceryListsItem(GroceryListObject groceryListsItem) {
    if (this.groceryLists == null) {
      this.groceryLists = new ArrayList<GroceryListObject>();
    }
    this.groceryLists.add(groceryListsItem);
    return this;
  }

  /**
   * Get groceryLists
   * @return groceryLists
  **/
  @ApiModelProperty(value = "")
      @Valid
    public List<GroceryListObject> getGroceryLists() {
    return groceryLists;
  }

  public void setGroceryLists(List<GroceryListObject> groceryLists) {
    this.groceryLists = groceryLists;
  }

  public CustomerObject shoppingCart(List<ItemObject> shoppingCart) {
    this.shoppingCart = shoppingCart;
    return this;
  }

  public CustomerObject addShoppingCartItem(ItemObject shoppingCartItem) {
    if (this.shoppingCart == null) {
      this.shoppingCart = new ArrayList<ItemObject>();
    }
    this.shoppingCart.add(shoppingCartItem);
    return this;
  }

  /**
   * Get shoppingCart
   * @return shoppingCart
  **/
  @ApiModelProperty(value = "")
      @Valid
    public List<ItemObject> getShoppingCart() {
    return shoppingCart;
  }

  public void setShoppingCart(List<ItemObject> shoppingCart) {
    this.shoppingCart = shoppingCart;
  }

  public CustomerObject preference(Object preference) {
    this.preference = preference;
    return this;
  }

  /**
   * Get preference
   * @return preference
  **/
  @ApiModelProperty(value = "")
  
    public Object getPreference() {
    return preference;
  }

  public void setPreference(Object preference) {
    this.preference = preference;
  }

  public CustomerObject wallet(Object wallet) {
    this.wallet = wallet;
    return this;
  }

  /**
   * Get wallet
   * @return wallet
  **/
  @ApiModelProperty(value = "")
  
    public Object getWallet() {
    return wallet;
  }

  public void setWallet(Object wallet) {
    this.wallet = wallet;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CustomerObject customerObject = (CustomerObject) o;
    return Objects.equals(this.name, customerObject.name) &&
        Objects.equals(this.surname, customerObject.surname) &&
        Objects.equals(this.email, customerObject.email) &&
        Objects.equals(this.phoneNumber, customerObject.phoneNumber) &&
        Objects.equals(this.password, customerObject.password) &&
        Objects.equals(this.activationDate, customerObject.activationDate) &&
        Objects.equals(this.activationCode, customerObject.activationCode) &&
        Objects.equals(this.resetCode, customerObject.resetCode) &&
        Objects.equals(this.resetExpiration, customerObject.resetExpiration) &&
        Objects.equals(this.accountType, customerObject.accountType) &&
        Objects.equals(this.customerID, customerObject.customerID) &&
        Objects.equals(this.address, customerObject.address) &&
        Objects.equals(this.groceryLists, customerObject.groceryLists) &&
        Objects.equals(this.shoppingCart, customerObject.shoppingCart) &&
        Objects.equals(this.preference, customerObject.preference) &&
        Objects.equals(this.wallet, customerObject.wallet);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, surname, email, phoneNumber, password, activationDate, activationCode, resetCode, resetExpiration, accountType, customerID, address, groceryLists, shoppingCart, preference, wallet);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CustomerObject {\n");
    
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
    sb.append("    customerID: ").append(toIndentedString(customerID)).append("\n");
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
    sb.append("    groceryLists: ").append(toIndentedString(groceryLists)).append("\n");
    sb.append("    shoppingCart: ").append(toIndentedString(shoppingCart)).append("\n");
    sb.append("    preference: ").append(toIndentedString(preference)).append("\n");
    sb.append("    wallet: ").append(toIndentedString(wallet)).append("\n");
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
