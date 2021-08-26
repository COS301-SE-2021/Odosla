package cs.superleague.analytics.requests;

import cs.superleague.analytics.dataclass.ReportType;

import java.util.Date;

public class CreateFinancialReportRequest {

    private final Date startDate;
    private final Date endDate;
    private final ReportType reportType;

    public CreateFinancialReportRequest(Date startDate, Date endDate, ReportType reportType) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.reportType = reportType;
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
