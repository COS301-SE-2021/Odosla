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
import java.util.ArrayList;
import java.util.List;
import org.openapitools.client.model.GroceryListObject;
import org.openapitools.client.model.ItemObject;

/**
 * CustomerObjectAllOf
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2021-08-13T13:18:08.276081+02:00[Africa/Harare]")
public class CustomerObjectAllOf {
  public static final String SERIALIZED_NAME_CUSTOMER_I_D = "customerID";
  @SerializedName(SERIALIZED_NAME_CUSTOMER_I_D)
  private String customerID;

  public static final String SERIALIZED_NAME_ADDRESS = "address";
  @SerializedName(SERIALIZED_NAME_ADDRESS)
  private String address;

  public static final String SERIALIZED_NAME_GROCERY_LISTS = "groceryLists";
  @SerializedName(SERIALIZED_NAME_GROCERY_LISTS)
  private List<GroceryListObject> groceryLists = null;

  public static final String SERIALIZED_NAME_SHOPPING_CART = "shoppingCart";
  @SerializedName(SERIALIZED_NAME_SHOPPING_CART)
  private List<ItemObject> shoppingCart = null;

  public static final String SERIALIZED_NAME_PREFERENCE = "preference";
  @SerializedName(SERIALIZED_NAME_PREFERENCE)
  private String preference;

  public static final String SERIALIZED_NAME_WALLET = "wallet";
  @SerializedName(SERIALIZED_NAME_WALLET)
  private String wallet;


  public CustomerObjectAllOf customerID(String customerID) {
    
    this.customerID = customerID;
    return this;
  }

   /**
   * Get customerID
   * @return customerID
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getCustomerID() {
    return customerID;
  }


  public void setCustomerID(String customerID) {
    this.customerID = customerID;
  }


  public CustomerObjectAllOf address(String address) {
    
    this.address = address;
    return this;
  }

   /**
   * Get address
   * @return address
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getAddress() {
    return address;
  }


  public void setAddress(String address) {
    this.address = address;
  }


  public CustomerObjectAllOf groceryLists(List<GroceryListObject> groceryLists) {
    
    this.groceryLists = groceryLists;
    return this;
  }

  public CustomerObjectAllOf addGroceryListsItem(GroceryListObject groceryListsItem) {
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
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public List<GroceryListObject> getGroceryLists() {
    return groceryLists;
  }


  public void setGroceryLists(List<GroceryListObject> groceryLists) {
    this.groceryLists = groceryLists;
  }


  public CustomerObjectAllOf shoppingCart(List<ItemObject> shoppingCart) {
    
    this.shoppingCart = shoppingCart;
    return this;
  }

  public CustomerObjectAllOf addShoppingCartItem(ItemObject shoppingCartItem) {
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
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public List<ItemObject> getShoppingCart() {
    return shoppingCart;
  }


  public void setShoppingCart(List<ItemObject> shoppingCart) {
    this.shoppingCart = shoppingCart;
  }


  public CustomerObjectAllOf preference(String preference) {
    
    this.preference = preference;
    return this;
  }

   /**
   * Get preference
   * @return preference
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getPreference() {
    return preference;
  }


  public void setPreference(String preference) {
    this.preference = preference;
  }


  public CustomerObjectAllOf wallet(String wallet) {
    
    this.wallet = wallet;
    return this;
  }

   /**
   * Get wallet
   * @return wallet
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getWallet() {
    return wallet;
  }


  public void setWallet(String wallet) {
    this.wallet = wallet;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CustomerObjectAllOf customerObjectAllOf = (CustomerObjectAllOf) o;
    return Objects.equals(this.customerID, customerObjectAllOf.customerID) &&
        Objects.equals(this.address, customerObjectAllOf.address) &&
        Objects.equals(this.groceryLists, customerObjectAllOf.groceryLists) &&
        Objects.equals(this.shoppingCart, customerObjectAllOf.shoppingCart) &&
        Objects.equals(this.preference, customerObjectAllOf.preference) &&
        Objects.equals(this.wallet, customerObjectAllOf.wallet);
  }

  @Override
  public int hashCode() {
    return Objects.hash(customerID, address, groceryLists, shoppingCart, preference, wallet);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CustomerObjectAllOf {\n");
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
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}

