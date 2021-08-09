package cs.superleague.analytics;

import cs.superleague.analytics.exceptions.AnalyticsException;
import cs.superleague.analytics.requests.CreateCustomerReportRequest;
import cs.superleague.analytics.responses.CreateCustomerReportResponse;

public interface AnalyticsService {

    CreateCustomerReportResponse createCustomerReport(CreateCustomerReportRequest request) throws AnalyticsException;

}
