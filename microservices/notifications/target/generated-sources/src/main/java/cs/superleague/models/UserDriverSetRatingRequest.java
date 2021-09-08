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
public class UserDriverSetRatingRequest   {
  @JsonProperty("driverID")
  private String driverID = null;

  @JsonProperty("rating")
  private BigDecimal rating = null;

  public UserDriverSetRatingRequest driverID(String driverID) {
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

  public UserDriverSetRatingRequest rating(BigDecimal rating) {
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


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserDriverSetRatingRequest userDriverSetRatingRequest = (UserDriverSetRatingRequest) o;
    return Objects.equals(this.driverID, userDriverSetRatingRequest.driverID) &&
        Objects.equals(this.rating, userDriverSetRatingRequest.rating);
  }

  @Override
  public int hashCode() {
    return Objects.hash(driverID, rating);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserDriverSetRatingRequest {\n");
    
    sb.append("    driverID: ").append(toIndentedString(driverID)).append("\n");
    sb.append("    rating: ").append(toIndentedString(rating)).append("\n");
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
