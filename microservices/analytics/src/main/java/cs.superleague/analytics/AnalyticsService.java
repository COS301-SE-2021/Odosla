package cs.superleague.analytics;

import cs.superleague.analytics.requests.CreateFinancialReportRequest;
import cs.superleague.analytics.requests.CreateMonthlyReportRequest;
import cs.superleague.analytics.requests.CreateUserReportRequest;
import cs.superleague.analytics.responses.CreateFinancialReportResponse;
import cs.superleague.analytics.responses.CreateMonthlyReportResponse;
import cs.superleague.analytics.responses.CreateUserReportResponse;

public interface AnalyticsService {

    CreateUserReportResponse createUserReport(CreateUserReportRequest request) throws Exception;

    CreateFinancialReportResponse createFinancialReport(CreateFinancialReportRequest request) throws Exception;

    CreateMonthlyReportResponse createMonthlyReport(CreateMonthlyReportRequest request) throws Exception;

}
