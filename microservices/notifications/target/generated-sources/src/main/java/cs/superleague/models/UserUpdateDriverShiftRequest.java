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
 * This object is expected as input
 */
@ApiModel(description = "This object is expected as input")
@Validated
public class UserUpdateDriverShiftRequest   {
  @JsonProperty("onShift")
  private Boolean onShift = null;

  public UserUpdateDriverShiftRequest onShift(Boolean onShift) {
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


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserUpdateDriverShiftRequest userUpdateDriverShiftRequest = (UserUpdateDriverShiftRequest) o;
    return Objects.equals(this.onShift, userUpdateDriverShiftRequest.onShift);
  }

  @Override
  public int hashCode() {
    return Objects.hash(onShift);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserUpdateDriverShiftRequest {\n");
    
    sb.append("    onShift: ").append(toIndentedString(onShift)).append("\n");
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
