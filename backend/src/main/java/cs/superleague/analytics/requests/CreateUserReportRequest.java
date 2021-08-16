package cs.superleague.analytics.requests;

import cs.superleague.analytics.dataclass.ReportType;

import java.util.Calendar;
import java.util.UUID;

public class CreateUserReportRequest {

    private final UUID adminID;
    private final Calendar startDate;
    private final Calendar endDate;
    private final ReportType reportType;

    public CreateUserReportRequest(UUID adminID, Calendar startDate, Calendar endDate, ReportType reportType) {
        this.adminID = adminID;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reportType = reportType;
    }

    public UUID getAdminID() {
        return adminID;
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
