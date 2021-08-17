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
public class AnalyticsCreateUserReportRequest   {
  @JsonProperty("adminID")
  private String adminID = null;

  @JsonProperty("startDate")
  private String startDate = null;

  @JsonProperty("endDate")
  private String endDate = null;

  @JsonProperty("reportType")
  private String reportType = null;

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

  public AnalyticsCreateUserReportRequest startDate(String startDate) {
    this.startDate = startDate;
    return this;
  }

  /**
   * Get startDate
   * @return startDate
  **/
  @ApiModelProperty(value = "")
  
    public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  public AnalyticsCreateUserReportRequest endDate(String endDate) {
    this.endDate = endDate;
    return this;
  }

  /**
   * Get endDate
   * @return endDate
  **/
  @ApiModelProperty(value = "")
  
    public String getEndDate() {
    return endDate;
  }

  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }

  public AnalyticsCreateUserReportRequest reportType(String reportType) {
    this.reportType = reportType;
    return this;
  }

  /**
   * Get reportType
   * @return reportType
  **/
  @ApiModelProperty(value = "")
  
    public String getReportType() {
    return reportType;
  }

  public void setReportType(String reportType) {
    this.reportType = reportType;
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
    return Objects.equals(this.adminID, analyticsCreateUserReportRequest.adminID) &&
        Objects.equals(this.startDate, analyticsCreateUserReportRequest.startDate) &&
        Objects.equals(this.endDate, analyticsCreateUserReportRequest.endDate) &&
        Objects.equals(this.reportType, analyticsCreateUserReportRequest.reportType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(adminID, startDate, endDate, reportType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AnalyticsCreateUserReportRequest {\n");
    
    sb.append("    adminID: ").append(toIndentedString(adminID)).append("\n");
    sb.append("    startDate: ").append(toIndentedString(startDate)).append("\n");
    sb.append("    endDate: ").append(toIndentedString(endDate)).append("\n");
    sb.append("    reportType: ").append(toIndentedString(reportType)).append("\n");
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
