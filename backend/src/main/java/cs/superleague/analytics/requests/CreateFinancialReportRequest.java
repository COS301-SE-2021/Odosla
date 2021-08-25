package cs.superleague.analytics.requests;

import cs.superleague.analytics.dataclass.ReportType;

import java.util.Date;

public class CreateFinancialReportRequest {

    private final String JWTToken;
    private final Date startDate;
    private final Date endDate;
    private final ReportType reportType;

    public CreateFinancialReportRequest(String JWTToken, Date startDate, Date endDate, ReportType reportType) {
        this.JWTToken = JWTToken;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reportType = reportType;
    }

    public String getJWTToken() {
        return JWTToken;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public ReportType getReportType() {
        return reportType;
    }
}
