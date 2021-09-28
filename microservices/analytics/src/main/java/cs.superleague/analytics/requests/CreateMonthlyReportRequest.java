package cs.superleague.analytics.requests;

import cs.superleague.analytics.dataclass.ReportType;

public class CreateMonthlyReportRequest {

    private final ReportType reportType;

    public CreateMonthlyReportRequest(ReportType reportType) {
        this.reportType = reportType;
    }

    public ReportType getReportType() {
        return reportType;
    }
}
