package cs.superleague.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import cs.superleague.models.GeoPointObject;
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
public class DeliveryGetNextOrderForDriverRequest   {
  @JsonProperty("driverID")
  private String driverID = null;

  @JsonProperty("currentLocation")
  private GeoPointObject currentLocation = null;

  @JsonProperty("rangeOfDelivery")
  private BigDecimal rangeOfDelivery = null;

  public DeliveryGetNextOrderForDriverRequest driverID(String driverID) {
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

  public DeliveryGetNextOrderForDriverRequest currentLocation(GeoPointObject currentLocation) {
    this.currentLocation = currentLocation;
    return this;
  }

  /**
   * Get currentLocation
   * @return currentLocation
  **/
  @ApiModelProperty(value = "")
  
    @Valid
    public GeoPointObject getCurrentLocation() {
    return currentLocation;
  }

  public void setCurrentLocation(GeoPointObject currentLocation) {
    this.currentLocation = currentLocation;
  }

  public DeliveryGetNextOrderForDriverRequest rangeOfDelivery(BigDecimal rangeOfDelivery) {
    this.rangeOfDelivery = rangeOfDelivery;
    return this;
  }

  /**
   * Get rangeOfDelivery
   * @return rangeOfDelivery
  **/
  @ApiModelProperty(value = "")
  
    @Valid
    public BigDecimal getRangeOfDelivery() {
    return rangeOfDelivery;
  }

  public void setRangeOfDelivery(BigDecimal rangeOfDelivery) {
    this.rangeOfDelivery = rangeOfDelivery;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DeliveryGetNextOrderForDriverRequest deliveryGetNextOrderForDriverRequest = (DeliveryGetNextOrderForDriverRequest) o;
    return Objects.equals(this.driverID, deliveryGetNextOrderForDriverRequest.driverID) &&
        Objects.equals(this.currentLocation, deliveryGetNextOrderForDriverRequest.currentLocation) &&
        Objects.equals(this.rangeOfDelivery, deliveryGetNextOrderForDriverRequest.rangeOfDelivery);
  }

  @Override
  public int hashCode() {
    return Objects.hash(driverID, currentLocation, rangeOfDelivery);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DeliveryGetNextOrderForDriverRequest {\n");
    
    sb.append("    driverID: ").append(toIndentedString(driverID)).append("\n");
    sb.append("    currentLocation: ").append(toIndentedString(currentLocation)).append("\n");
    sb.append("    rangeOfDelivery: ").append(toIndentedString(rangeOfDelivery)).append("\n");
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
