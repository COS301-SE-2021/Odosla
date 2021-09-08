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
 * This object is expected as output
 */
@ApiModel(description = "This object is expected as output")
@Validated
public class AnalyticsCreateFinancialReportResponse   {
  @JsonProperty("success")
  private Boolean success = null;

  @JsonProperty("message")
  private String message = null;

  @JsonProperty("timestamp")
  private String timestamp = null;

  @JsonProperty("pdf")
  private byte[] pdf = null;

  @JsonProperty("csv")
  private byte[] csv = null;

  public AnalyticsCreateFinancialReportResponse success(Boolean success) {
    this.success = success;
    return this;
  }

  /**
   * Get success
   * @return success
  **/
  @ApiModelProperty(value = "")
  
    public Boolean isSuccess() {
    return success;
  }

  public void setSuccess(Boolean success) {
    this.success = success;
  }

  public AnalyticsCreateFinancialReportResponse message(String message) {
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

  public AnalyticsCreateFinancialReportResponse timestamp(String timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Get timestamp
   * @return timestamp
  **/
  @ApiModelProperty(value = "")
  
    public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public AnalyticsCreateFinancialReportResponse pdf(byte[] pdf) {
    this.pdf = pdf;
    return this;
  }

  /**
   * Get pdf
   * @return pdf
  **/
  @ApiModelProperty(value = "")
  
    public byte[] getPdf() {
    return pdf;
  }

  public void setPdf(byte[] pdf) {
    this.pdf = pdf;
  }

  public AnalyticsCreateFinancialReportResponse csv(byte[] csv) {
    this.csv = csv;
    return this;
  }

  /**
   * Get csv
   * @return csv
  **/
  @ApiModelProperty(value = "")
  
    public byte[] getCsv() {
    return csv;
  }

  public void setCsv(byte[] csv) {
    this.csv = csv;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AnalyticsCreateFinancialReportResponse analyticsCreateFinancialReportResponse = (AnalyticsCreateFinancialReportResponse) o;
    return Objects.equals(this.success, analyticsCreateFinancialReportResponse.success) &&
        Objects.equals(this.message, analyticsCreateFinancialReportResponse.message) &&
        Objects.equals(this.timestamp, analyticsCreateFinancialReportResponse.timestamp) &&
        Objects.equals(this.pdf, analyticsCreateFinancialReportResponse.pdf) &&
        Objects.equals(this.csv, analyticsCreateFinancialReportResponse.csv);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, message, timestamp, pdf, csv);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AnalyticsCreateFinancialReportResponse {\n");
    
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    pdf: ").append(toIndentedString(pdf)).append("\n");
    sb.append("    csv: ").append(toIndentedString(csv)).append("\n");
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
