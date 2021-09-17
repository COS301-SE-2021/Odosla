package cs.superleague.analytics;

import cs.superleague.analytics.AnalyticsHelpers.FinancialAnalyticsHelper;
import cs.superleague.analytics.AnalyticsHelpers.MonthlyAnalyticsHelper;
import cs.superleague.analytics.AnalyticsHelpers.UserAnalyticsHelper;
import cs.superleague.analytics.CreateAnalyticsDataHelpers.CreateFinancialAnalyticsData;
import cs.superleague.analytics.CreateAnalyticsDataHelpers.CreateMonthlyAnalyticsData;
import cs.superleague.analytics.CreateAnalyticsDataHelpers.CreateUserAnalyticsData;
import cs.superleague.analytics.dataclass.ReportType;
import cs.superleague.analytics.exceptions.AnalyticsException;
import cs.superleague.analytics.exceptions.InvalidRequestException;
import cs.superleague.analytics.requests.CreateFinancialReportRequest;
import cs.superleague.analytics.requests.CreateMonthlyReportRequest;
import cs.superleague.analytics.requests.CreateUserReportRequest;
import cs.superleague.analytics.responses.CreateFinancialReportResponse;
import cs.superleague.analytics.responses.CreateMonthlyReportResponse;
import cs.superleague.analytics.responses.CreateUserReportResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@Service("analyticsServiceImpl")
public class AnalyticsServiceImpl implements AnalyticsService {

    @Value("${paymentHost}")
    private String paymentHost;
    @Value("${paymentPort}")
    private String paymentPort;

    @Value("${shoppingHost}")
    private String shoppingHost;
    @Value("${shoppingPort}")
    private String shoppingPort;

    RestTemplate restTemplate;

    @Autowired
    public AnalyticsServiceImpl(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    /**
     * WHAT TO DO:
     *
     * @param request is used to bring in:
     *                -               private startDate   //the start Date and Time the report is being generated from
     *                -               private endDate     //the end Date and Time the report is being generated to
     *                -               private reportType  //the Type of report being generated, CSV or PDF
     *                -               private UUID userId //The user ID that is requesting the report
     *                <p>
     *                <p>
     *                GenerateUserStatisticsReport should:
     *                1.Check valid request with all required fields such as startDate, endDate, ReportType and userID is specified else throw InvalidRequestException
     *                2.Check if user requesting the report is logged in and an admin else throw NotAuthorizedException
     *                3.Generate Report with all required user statistics information, if problem creating report, throw ReportException
     *                <p>
     *                Request Object: (GenerateUserStatisticsReportRequest)
     *                {
     *                "startDate":"2021-01-05T11:50:55"
     *                "endDate":"2021-02-05T15:50:55"
     *                "reportType":"PDF"
     *                "userID":"8b337604-b0f6-11eb-8529-0242ac130003"
     *                }
     *                <p>
     *                <p>
     *                Response object: (GenerateUserStatisticsResponse)
     *                {
     *                "PDFReport":"com.itextpdf.text.Document@68999068"
     *                }
     * @return
     * @throws AnalyticsException
     */

    @Override
    public CreateUserReportResponse createUserReport(CreateUserReportRequest request) throws Exception {

        CreateUserReportResponse response;
        CreateUserAnalyticsData createUserAnalyticsData;
        String message;

        if (request == null) {
            throw new InvalidRequestException("CreateUserReportRequest is null- Cannot create report");
        }

        validAnalyticsRequest(request.getReportType(), request.getStartDate(), request.getEndDate());

        try {
            createUserAnalyticsData = new CreateUserAnalyticsData(request.getStartDate(),
                    request.getEndDate(), restTemplate);
        }catch (Exception e){
            throw new AnalyticsException("Problem with creating user statistics report");
        }

        UserAnalyticsHelper userAnalyticsHelper = new UserAnalyticsHelper(createUserAnalyticsData.getUserStatisticsData());

        if(request.getReportType() == ReportType.PDF){
            message = "UserReport.pdf downloaded";
            byte[] document = userAnalyticsHelper.createPDF();
            response =  new CreateUserReportResponse(true, message, new Date(), document, null);
        }else if(request.getReportType() == ReportType.CSV){
            message = "UserReport.csv downloaded";
            StringBuilder sb = userAnalyticsHelper.createCSVReport();
            response = new CreateUserReportResponse(true, message, new Date(), null, sb.toString());
        }else{
            throw new InvalidRequestException("Invalid Report Type Given - Unable to generate report");
        }

        return response;
    }

    @Override
    public CreateFinancialReportResponse createFinancialReport(CreateFinancialReportRequest request) throws Exception {

        String message;
        CreateFinancialReportResponse response;
        CreateFinancialAnalyticsData createFinancialAnalyticsData;

        if (request == null) {
            throw new InvalidRequestException("CreateFinancialReportRequest is null- Cannot create report");
        }

        validAnalyticsRequest(request.getReportType(), request.getStartDate(), request.getEndDate());

        try{
            createFinancialAnalyticsData = new CreateFinancialAnalyticsData(request.getStartDate(),
                    request.getEndDate(), restTemplate, paymentPort, paymentHost);
        }catch (Exception e){
            throw new AnalyticsException("Problem with creating financial statistics report");
        }

        FinancialAnalyticsHelper financialAnalyticsHelper = new
                FinancialAnalyticsHelper(createFinancialAnalyticsData.getFinancialStatisticsData(),
                restTemplate, shoppingHost, shoppingPort);

        if(request.getReportType() == ReportType.PDF){
            message = "FinancialReport PDF successfully generated";
            byte[] document = financialAnalyticsHelper.createPDF();
            response =  new CreateFinancialReportResponse(true, message, new Date(), document, null);
        }else if(request.getReportType() == ReportType.CSV){
            message = "FinancialReport CSV successfully generated";
            StringBuilder sb = financialAnalyticsHelper.createCSVReport();
            response = new CreateFinancialReportResponse(true, message, new Date(), null, sb.toString());
        }else{
            throw new InvalidRequestException("Invalid Report Type Given - Unable to generate report");
        }
        return response;
    }
//
    @Override
    public CreateMonthlyReportResponse createMonthlyReport(CreateMonthlyReportRequest request) throws Exception {

        String message;
        CreateMonthlyReportResponse response;
        CreateMonthlyAnalyticsData createMonthlyAnalyticsData;

        if (request == null) {
            throw new InvalidRequestException("CreateMonthlyReportRequest is null- Cannot create report");
        }

        validAnalyticsRequest(request.getReportType(), new Date(), new Date());

        try{
            createMonthlyAnalyticsData = new CreateMonthlyAnalyticsData(restTemplate);
        }catch (Exception e){
            throw new AnalyticsException("Problem with creating monthly statistics report");
        }

        MonthlyAnalyticsHelper monthlyAnalyticsHelper = new MonthlyAnalyticsHelper(createMonthlyAnalyticsData.getMonthlyStatisticsData());

        if(request.getReportType() == ReportType.PDF){
            message = "MonthlyReport.pdf downloaded";
            byte[] document = monthlyAnalyticsHelper.createPDF();
            response =  new CreateMonthlyReportResponse(true, message, new Date(), document, null);
        }else if(request.getReportType() == ReportType.CSV){
            message = "MonthlyReport.csv downloaded";
            String sb = monthlyAnalyticsHelper.createCSVReport().toString();
            response = new CreateMonthlyReportResponse(true, message, new Date(), null, sb);
        }else{
            throw new InvalidRequestException("Invalid Report Type Given - Unable to generate report");
        }

        return response;
    }

    private void validAnalyticsRequest(ReportType reportType, Date startDate, Date endDate) throws InvalidRequestException {
        if (reportType == null) {
            throw new InvalidRequestException("Exception: Report Type in request object is null");
        }
        if (startDate == null) {
            throw new InvalidRequestException("Exception: Start Date in request object is null");
        }
        if (endDate == null) {
            throw new InvalidRequestException("Exception: End Date in request object is null");
        }
    }
}
