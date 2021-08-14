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
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-08-14T21:50:06.372722+02:00[Africa/Johannesburg]")
public class AnalyticsCreateUserReportRequest   {
  @JsonProperty("adminID")
  private String adminID = null;

  public AnalyticsCreateUserReportRequest adminID(String adminID) {
    this.adminID = adminID;
    return this;
  }

  /**
   * Get adminID
   * @return adminID
  **/
  @ApiModelProperty(value = "")
  
    public String getAdminID() {
    return adminID;
  }

  public void setAdminID(String adminID) {
    this.adminID = adminID;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AnalyticsCreateUserReportRequest analyticsCreateUserReportRequest = (AnalyticsCreateUserReportRequest) o;
    return Objects.equals(this.adminID, analyticsCreateUserReportRequest.adminID);
  }

  @Override
  public int hashCode() {
    return Objects.hash(adminID);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AnalyticsCreateUserReportRequest {\n");
    
    sb.append("    adminID: ").append(toIndentedString(adminID)).append("\n");
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
