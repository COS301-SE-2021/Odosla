package cs.superleague.analytics.requests;

import cs.superleague.analytics.dataclass.ReportType;

import java.util.Calendar;

public class CreateMonthlyReportRequest {

    private final String JWTToken;
    private final ReportType reportType;

    public CreateMonthlyReportRequest(String JWTToken,ReportType reportType) {
        this.JWTToken = JWTToken;
        this.reportType = reportType;
    }

    public String getJWTToken() {
        return JWTToken;
    }

    public ReportType getReportType() {
        return reportType;
    }
}
