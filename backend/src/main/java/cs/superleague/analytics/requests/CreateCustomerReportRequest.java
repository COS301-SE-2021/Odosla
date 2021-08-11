package cs.superleague.analytics.requests;

import cs.superleague.analytics.dataclass.ReportType;

import java.util.Calendar;
import java.util.UUID;

public class CreateCustomerReportRequest {

    private final UUID customerID;
    private final Calendar startDate;
    private final Calendar endDate;
    private final ReportType reportType;

    public CreateCustomerReportRequest(UUID customerID, Calendar startDate, Calendar endDate, ReportType reportType) {
        this.customerID = customerID;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reportType = reportType;
    }

    public UUID getCustomerID() {
        return customerID;
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
