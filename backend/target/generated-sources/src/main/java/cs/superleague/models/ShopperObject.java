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
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-08-13T15:49:46.017974400+02:00[Africa/Harare]")
public class ShopperObject  implements OneOfuserGetCurrentUserResponseUser {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("username")
  private String username = null;

  @JsonProperty("password")
  private String password = null;

  @JsonProperty("storeID")
  private String storeID = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("surname")
  private String surname = null;

  @JsonProperty("ordersCompleted")
  private Integer ordersCompleted = null;

  public ShopperObject id(String id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
  **/
  @ApiModelProperty(value = "")
  
    public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public ShopperObject username(String username) {
    this.username = username;
    return this;
  }

  /**
   * Get username
   * @return username
  **/
  @ApiModelProperty(value = "")
  
    public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
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


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ShopperObject shopperObject = (ShopperObject) o;
    return Objects.equals(this.id, shopperObject.id) &&
        Objects.equals(this.username, shopperObject.username) &&
        Objects.equals(this.password, shopperObject.password) &&
        Objects.equals(this.storeID, shopperObject.storeID) &&
        Objects.equals(this.name, shopperObject.name) &&
        Objects.equals(this.surname, shopperObject.surname) &&
        Objects.equals(this.ordersCompleted, shopperObject.ordersCompleted);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, username, password, storeID, name, surname, ordersCompleted);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ShopperObject {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    username: ").append(toIndentedString(username)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
    sb.append("    storeID: ").append(toIndentedString(storeID)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    surname: ").append(toIndentedString(surname)).append("\n");
    sb.append("    ordersCompleted: ").append(toIndentedString(ordersCompleted)).append("\n");
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
