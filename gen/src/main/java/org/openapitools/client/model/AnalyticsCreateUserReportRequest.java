/*
 * Library Service
 * The library service
 *
 * The version of the OpenAPI document: 0.0.1
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package org.openapitools.client.model;

import java.util.Objects;
import java.util.Arrays;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;

/**
 * This object is expected as input
 */
@ApiModel(description = "This object is expected as input")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2021-08-26T18:20:06.034903200+02:00[Africa/Harare]")
public class AnalyticsCreateUserReportRequest {
  public static final String SERIALIZED_NAME_JW_T_TOKEN = "JWTToken";
  @SerializedName(SERIALIZED_NAME_JW_T_TOKEN)
  private String jwTToken;

  public static final String SERIALIZED_NAME_START_DATE = "startDate";
  @SerializedName(SERIALIZED_NAME_START_DATE)
  private String startDate;

  public static final String SERIALIZED_NAME_END_DATE = "endDate";
  @SerializedName(SERIALIZED_NAME_END_DATE)
  private String endDate;

  public static final String SERIALIZED_NAME_REPORT_TYPE = "reportType";
  @SerializedName(SERIALIZED_NAME_REPORT_TYPE)
  private String reportType;


  public AnalyticsCreateUserReportRequest jwTToken(String jwTToken) {
    
    this.jwTToken = jwTToken;
    return this;
  }

   /**
   * Get jwTToken
   * @return jwTToken
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getJwTToken() {
    return jwTToken;
  }


  public void setJwTToken(String jwTToken) {
    this.jwTToken = jwTToken;
  }


  public AnalyticsCreateUserReportRequest startDate(String startDate) {
    
    this.startDate = startDate;
    return this;
  }

   /**
   * Get startDate
   * @return startDate
  **/
  @javax.annotation.Nullable
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
  @javax.annotation.Nullable
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
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getReportType() {
    return reportType;
  }


  public void setReportType(String reportType) {
    this.reportType = reportType;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AnalyticsCreateUserReportRequest analyticsCreateUserReportRequest = (AnalyticsCreateUserReportRequest) o;
    return Objects.equals(this.jwTToken, analyticsCreateUserReportRequest.jwTToken) &&
        Objects.equals(this.startDate, analyticsCreateUserReportRequest.startDate) &&
        Objects.equals(this.endDate, analyticsCreateUserReportRequest.endDate) &&
        Objects.equals(this.reportType, analyticsCreateUserReportRequest.reportType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(jwTToken, startDate, endDate, reportType);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AnalyticsCreateUserReportRequest {\n");
    sb.append("    jwTToken: ").append(toIndentedString(jwTToken)).append("\n");
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
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}

