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
 * This object is expected as input
 */
@ApiModel(description = "This object is expected as input")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-08-14T16:05:50.792457600+02:00[Africa/Harare]")
public class UserSetCurrentLocationRequest   {
  @JsonProperty("driverID")
  private String driverID = null;

  @JsonProperty("latitude")
  private BigDecimal latitude = null;

  @JsonProperty("longitude")
  private BigDecimal longitude = null;

  @JsonProperty("address")
  private String address = null;

  public UserSetCurrentLocationRequest driverID(String driverID) {
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

  public UserSetCurrentLocationRequest latitude(BigDecimal latitude) {
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

  public UserSetCurrentLocationRequest longitude(BigDecimal longitude) {
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

  public UserSetCurrentLocationRequest address(String address) {
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


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserSetCurrentLocationRequest userSetCurrentLocationRequest = (UserSetCurrentLocationRequest) o;
    return Objects.equals(this.driverID, userSetCurrentLocationRequest.driverID) &&
        Objects.equals(this.latitude, userSetCurrentLocationRequest.latitude) &&
        Objects.equals(this.longitude, userSetCurrentLocationRequest.longitude) &&
        Objects.equals(this.address, userSetCurrentLocationRequest.address);
  }

  @Override
  public int hashCode() {
    return Objects.hash(driverID, latitude, longitude, address);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserSetCurrentLocationRequest {\n");
    
    sb.append("    driverID: ").append(toIndentedString(driverID)).append("\n");
    sb.append("    latitude: ").append(toIndentedString(latitude)).append("\n");
    sb.append("    longitude: ").append(toIndentedString(longitude)).append("\n");
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
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
