package cs.superleague.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import cs.superleague.models.GeoPointObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * This object is expected as output
 */
@ApiModel(description = "This object is expected as output")
@Validated
public class DeliveryTrackDeliveryResponse   {
  @JsonProperty("currentLocation")
  private GeoPointObject currentLocation = null;

  @JsonProperty("message")
  private String message = null;

  public DeliveryTrackDeliveryResponse currentLocation(GeoPointObject currentLocation) {
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

  public DeliveryTrackDeliveryResponse message(String message) {
    this.message = message;
    return this;
  }

  /**
   * Get message
   * @return message
  **/
  @ApiModelProperty(value = "")
  
    public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DeliveryTrackDeliveryResponse deliveryTrackDeliveryResponse = (DeliveryTrackDeliveryResponse) o;
    return Objects.equals(this.currentLocation, deliveryTrackDeliveryResponse.currentLocation) &&
        Objects.equals(this.message, deliveryTrackDeliveryResponse.message);
  }

  @Override
  public int hashCode() {
    return Objects.hash(currentLocation, message);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DeliveryTrackDeliveryResponse {\n");
    
    sb.append("    currentLocation: ").append(toIndentedString(currentLocation)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
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
