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
import cs.superleague.analytics.exceptions.NotAuthorizedException;
import cs.superleague.analytics.requests.CreateFinancialReportRequest;
import cs.superleague.analytics.requests.CreateMonthlyReportRequest;
import cs.superleague.analytics.requests.CreateUserReportRequest;
import cs.superleague.analytics.responses.CreateFinancialReportResponse;
import cs.superleague.analytics.responses.CreateMonthlyReportResponse;
import cs.superleague.analytics.responses.CreateUserReportResponse;
import cs.superleague.payment.PaymentService;
import cs.superleague.shopping.repos.StoreRepo;
import cs.superleague.user.UserService;
import cs.superleague.user.dataclass.Admin;
import cs.superleague.user.dataclass.UserType;
import cs.superleague.user.repos.AdminRepo;
import cs.superleague.user.requests.GetCurrentUserRequest;
import cs.superleague.user.responses.GetCurrentUserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service("analyticsServiceImpl")
public class AnalyticsServiceImpl implements AnalyticsService {

    private final AdminRepo adminRepo;
    private final StoreRepo storeRepo;
    private final UserService userService;
    private final PaymentService paymentService;

    @Autowired
    public AnalyticsServiceImpl(AdminRepo adminRepo, StoreRepo storeRepo, UserService userService, PaymentService paymentService) {
        this.adminRepo = adminRepo;
        this.userService = userService;
        this.paymentService = paymentService;
        this.storeRepo = storeRepo;
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

        if (request == null) {
            throw new InvalidRequestException("CreateUserReportRequest is null- Cannot create report");
        }

        validAnalyticsRequest(request.getReportType(), request.getStartDate(), request.getEndDate(), request.getAdminID());

        isAdmin(request.getAdminID());

        try {
            createUserAnalyticsData = new CreateUserAnalyticsData(request.getStartDate(),
                    request.getEndDate(), request.getAdminID(), userService);
        }catch (Exception e){
            throw new AnalyticsException("Problem with creating user statistics report");
        }

        UserAnalyticsHelper userAnalyticsHelper = new UserAnalyticsHelper(createUserAnalyticsData.getUserStatisticsData());

        String message;
        if(request.getReportType() == ReportType.PDF){
            message = "UserReport.pdf downloaded";
            userAnalyticsHelper.createPDF();
            response =  new CreateUserReportResponse(true, message, new Date());
        }else if(request.getReportType() == ReportType.CSV){
            message = "UserReport.csv downloaded";
            userAnalyticsHelper.createCSVReport();
            response = new CreateUserReportResponse(true, message, new Date());
        }else{
            throw new InvalidRequestException("Invalid Report Type Given - Unable to generate report");
        }

        return response;
    }

    @Override
    public CreateFinancialReportResponse createFinancialReport(CreateFinancialReportRequest request) throws Exception {

        Admin admin;
        String message = "Report successfully created";
        CreateFinancialReportResponse response;
        CreateFinancialAnalyticsData createFinancialAnalyticsData;
        GetCurrentUserResponse getCurrentUserResponse;

        if (request == null) {
            throw new InvalidRequestException("CreateFinancialReportRequest is null- Cannot create report");
        }

        validAnalyticsRequestJWT(request.getReportType(), request.getStartDate(), request.getEndDate(), request.getJWTToken());

        getCurrentUserResponse = userService.getCurrentUser(new GetCurrentUserRequest(request.getJWTToken()));

        if(getCurrentUserResponse.getUser() == null){
            message = "User Not Found - Could not create report";
            return new CreateFinancialReportResponse(false, message, new Date());
        }

        if(getCurrentUserResponse.getUser().getAccountType() != UserType.ADMIN){
            message = "User is not an admin - Could not create report";
            return new CreateFinancialReportResponse(false, message, new Date());
        }

        try{
            createFinancialAnalyticsData = new CreateFinancialAnalyticsData(request.getStartDate(), request.getEndDate(), request.getJWTToken(),
                    paymentService);
        }catch (Exception e){
            throw new AnalyticsException("Problem with creating financial statistics report");
        }

        FinancialAnalyticsHelper financialAnalyticsHelper = new FinancialAnalyticsHelper(createFinancialAnalyticsData.getFinancialStatisticsData(), storeRepo);

        if(request.getReportType() == ReportType.PDF){
            message = "FinancialReport.pdf downloaded";
            financialAnalyticsHelper.createPDF();
            response =  new CreateFinancialReportResponse(true, message, new Date());
        }else if(request.getReportType() == ReportType.CSV){
            message = "FinancialReport.csv downloaded";
            financialAnalyticsHelper.createCSVReport();
            response = new CreateFinancialReportResponse(true, message, new Date());
        }else{
            throw new InvalidRequestException("Invalid Report Type Given - Unable to generate report");
        }
        return response;
    }

    @Override
    public CreateMonthlyReportResponse createMonthlyReport(CreateMonthlyReportRequest request) throws Exception {

        Admin admin;
        String message = "Report successfully created";
        CreateMonthlyReportResponse response;
        CreateMonthlyAnalyticsData createMonthlyAnalyticsData;
        GetCurrentUserResponse getCurrentUserResponse;

        if (request == null) {
            throw new InvalidRequestException("CreateMonthlyReportRequest is null- Cannot create report");
        }

        validAnalyticsRequestJWT(request.getReportType(), Calendar.getInstance(), Calendar.getInstance(), request.getJWTToken());

        getCurrentUserResponse = userService.getCurrentUser(new GetCurrentUserRequest(request.getJWTToken()));

        if(getCurrentUserResponse.getUser() == null){
            message = "User Not Found - Could not create report";
            return new CreateMonthlyReportResponse(false, message, new Date());
        }

        if(getCurrentUserResponse.getUser().getAccountType() != UserType.ADMIN){
            message = "User is not an admin - Could not create report";
            return new CreateMonthlyReportResponse(false, message, new Date());
        }

        admin = (Admin)getCurrentUserResponse.getUser();
        UUID adminID = admin.getAdminID();

        try{
            createMonthlyAnalyticsData = new CreateMonthlyAnalyticsData(request.getJWTToken(),
                    paymentService, userService, adminID);
        }catch (Exception e){
            throw new AnalyticsException("Problem with creating monthly statistics report");
        }

        MonthlyAnalyticsHelper monthlyAnalyticsHelper = new MonthlyAnalyticsHelper(createMonthlyAnalyticsData.getMonthlyStatisticsData(), storeRepo);

        if(request.getReportType() == ReportType.PDF){
            message = "MonthlyReport.pdf downloaded";
            monthlyAnalyticsHelper.createPDF();
            response =  new CreateMonthlyReportResponse(true, message, new Date());
        }else if(request.getReportType() == ReportType.CSV){
            message = "MonthlyReport.csv downloaded";
            monthlyAnalyticsHelper.createCSVReport();
            response = new CreateMonthlyReportResponse(true, message, new Date());
        }else{
            throw new InvalidRequestException("Invalid Report Type Given - Unable to generate report");
        }

        return response;
    }

    private void validAnalyticsRequest(ReportType reportType, Calendar startDate, Calendar endDate, UUID userID) throws InvalidRequestException {
        if (reportType == null) {
            throw new InvalidRequestException("Exception: Report Type in request object is null");
        }
        if (startDate == null) {
            throw new InvalidRequestException("Exception: Start Date in request object is null");
        }
        if (endDate == null) {
            throw new InvalidRequestException("Exception: End Date in request object is null");
        }
        if (userID == null) {
            throw new InvalidRequestException("Exception: User ID in request object is null");
        }
    }

    private void validAnalyticsRequestJWT(ReportType reportType, Calendar startDate, Calendar endDate, String userID) throws InvalidRequestException {
        if (reportType == null) {
            throw new InvalidRequestException("Exception: Report Type in request object is null");
        }
        if (startDate == null) {
            throw new InvalidRequestException("Exception: Start Date in request object is null");
        }
        if (endDate == null) {
            throw new InvalidRequestException("Exception: End Date in request object is null");
        }
        if (userID == null) {
            throw new InvalidRequestException("Exception: JWTToken in request object is null");
        }
    }

    private void isAdmin(UUID userID) throws NotAuthorizedException {

        Optional<Admin> adminOptional;

        adminOptional = adminRepo.findById(userID);

        if(adminOptional == null || !adminOptional.isPresent()){
            throw new NotAuthorizedException("ID given does not belong to admin - Could not generate report");
        }

    }

}
