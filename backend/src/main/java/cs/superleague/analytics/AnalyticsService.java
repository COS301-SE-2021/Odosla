package cs.superleague.analytics;

import cs.superleague.analytics.requests.CreateUserReportRequest;
import cs.superleague.analytics.responses.CreateUserReportResponse;

public interface AnalyticsService {

    CreateUserReportResponse createUserReport(CreateUserReportRequest request) throws Exception;

}
