package cs.superleague.analytics.requests;

import cs.superleague.analytics.dataclass.ReportType;

import java.util.Calendar;
import java.util.UUID;

public class CreateUserReportRequest {

    private final String JWTToken;
    private final Calendar startDate;
    private final Calendar endDate;
    private final ReportType reportType;


    public CreateUserReportRequest(String JWTToken, Calendar startDate, Calendar endDate, ReportType reportType) {
        this.JWTToken = JWTToken;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reportType = reportType;
    }

    public String getJWTToken() {
        return JWTToken;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public ReportType getReportType() {
        return reportType;
    }
}
