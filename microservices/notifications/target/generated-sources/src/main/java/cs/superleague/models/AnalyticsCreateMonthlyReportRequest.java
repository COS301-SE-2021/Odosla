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
public class AnalyticsCreateMonthlyReportRequest   {
  @JsonProperty("reportType")
  private String reportType = null;

  public AnalyticsCreateMonthlyReportRequest reportType(String reportType) {
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
    AnalyticsCreateMonthlyReportRequest analyticsCreateMonthlyReportRequest = (AnalyticsCreateMonthlyReportRequest) o;
    return Objects.equals(this.reportType, analyticsCreateMonthlyReportRequest.reportType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reportType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AnalyticsCreateMonthlyReportRequest {\n");
    
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
