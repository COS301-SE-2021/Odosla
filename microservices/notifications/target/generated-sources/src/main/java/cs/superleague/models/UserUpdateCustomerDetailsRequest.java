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
 * This object is returned as output
 */
@ApiModel(description = "This object is returned as output")
@Validated
public class UserUpdateCustomerDetailsRequest   {
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

  @JsonProperty("currentPassword")
  private String currentPassword = null;

  @JsonProperty("address")
  private String address = null;

  @JsonProperty("latitude")
  private BigDecimal latitude = null;

  @JsonProperty("longitude")
  private BigDecimal longitude = null;

  public UserUpdateCustomerDetailsRequest name(String name) {
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

  public UserUpdateCustomerDetailsRequest surname(String surname) {
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

  public UserUpdateCustomerDetailsRequest email(String email) {
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

  public UserUpdateCustomerDetailsRequest phoneNumber(String phoneNumber) {
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

  public UserUpdateCustomerDetailsRequest password(String password) {
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

  public UserUpdateCustomerDetailsRequest currentPassword(String currentPassword) {
    this.currentPassword = currentPassword;
    return this;
  }

  /**
   * Get currentPassword
   * @return currentPassword
  **/
  @ApiModelProperty(value = "")
  
    public String getCurrentPassword() {
    return currentPassword;
  }

  public void setCurrentPassword(String currentPassword) {
    this.currentPassword = currentPassword;
  }

  public UserUpdateCustomerDetailsRequest address(String address) {
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

  public UserUpdateCustomerDetailsRequest latitude(BigDecimal latitude) {
    this.latitude = latitude;
    return this;
  }

  /**
   * Get latitude
   * @return latitude
  **/
  @ApiModelProperty(value = "")
  
    @Valid
    public BigDecimal getLatitude() {
    return latitude;
  }

  public void setLatitude(BigDecimal latitude) {
    this.latitude = latitude;
  }

  public UserUpdateCustomerDetailsRequest longitude(BigDecimal longitude) {
    this.longitude = longitude;
    return this;
  }

  /**
   * Get longitude
   * @return longitude
  **/
  @ApiModelProperty(value = "")
  
    @Valid
    public BigDecimal getLongitude() {
    return longitude;
  }

  public void setLongitude(BigDecimal longitude) {
    this.longitude = longitude;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserUpdateCustomerDetailsRequest userUpdateCustomerDetailsRequest = (UserUpdateCustomerDetailsRequest) o;
    return Objects.equals(this.name, userUpdateCustomerDetailsRequest.name) &&
        Objects.equals(this.surname, userUpdateCustomerDetailsRequest.surname) &&
        Objects.equals(this.email, userUpdateCustomerDetailsRequest.email) &&
        Objects.equals(this.phoneNumber, userUpdateCustomerDetailsRequest.phoneNumber) &&
        Objects.equals(this.password, userUpdateCustomerDetailsRequest.password) &&
        Objects.equals(this.currentPassword, userUpdateCustomerDetailsRequest.currentPassword) &&
        Objects.equals(this.address, userUpdateCustomerDetailsRequest.address) &&
        Objects.equals(this.latitude, userUpdateCustomerDetailsRequest.latitude) &&
        Objects.equals(this.longitude, userUpdateCustomerDetailsRequest.longitude);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, surname, email, phoneNumber, password, currentPassword, address, latitude, longitude);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserUpdateCustomerDetailsRequest {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    surname: ").append(toIndentedString(surname)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    phoneNumber: ").append(toIndentedString(phoneNumber)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
    sb.append("    currentPassword: ").append(toIndentedString(currentPassword)).append("\n");
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
    sb.append("    latitude: ").append(toIndentedString(latitude)).append("\n");
    sb.append("    longitude: ").append(toIndentedString(longitude)).append("\n");
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
