package cs.superleague.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.core.io.Resource;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * This object is expected as output
 */
@ApiModel(description = "This object is expected as output")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-08-14T21:50:06.372722+02:00[Africa/Johannesburg]")
public class AnalyticsCreateUserReportResponse   {
  @JsonProperty("PDFReport")
  private Resource pdFReport = null;

  @JsonProperty("CSVReport")
  private String csVReport = null;

  public AnalyticsCreateUserReportResponse pdFReport(Resource pdFReport) {
    this.pdFReport = pdFReport;
    return this;
  }

  /**
   * Get pdFReport
   * @return pdFReport
  **/
  @ApiModelProperty(value = "")
  
    @Valid
    public Resource getPdFReport() {
    return pdFReport;
  }

  public void setPdFReport(Resource pdFReport) {
    this.pdFReport = pdFReport;
  }

  public AnalyticsCreateUserReportResponse csVReport(String csVReport) {
    this.csVReport = csVReport;
    return this;
  }

  /**
   * Get csVReport
   * @return csVReport
  **/
  @ApiModelProperty(value = "")
  
    public String getCsVReport() {
    return csVReport;
  }

  public void setCsVReport(String csVReport) {
    this.csVReport = csVReport;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AnalyticsCreateUserReportResponse analyticsCreateUserReportResponse = (AnalyticsCreateUserReportResponse) o;
    return Objects.equals(this.pdFReport, analyticsCreateUserReportResponse.pdFReport) &&
        Objects.equals(this.csVReport, analyticsCreateUserReportResponse.csVReport);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pdFReport, csVReport);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AnalyticsCreateUserReportResponse {\n");
    
    sb.append("    pdFReport: ").append(toIndentedString(pdFReport)).append("\n");
    sb.append("    csVReport: ").append(toIndentedString(csVReport)).append("\n");
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
