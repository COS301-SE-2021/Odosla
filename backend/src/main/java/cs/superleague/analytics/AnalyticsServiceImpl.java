package cs.superleague.analytics;

import cs.superleague.analytics.dataclass.ReportType;
import cs.superleague.analytics.exceptions.AnalyticsException;
import cs.superleague.analytics.exceptions.InvalidRequestException;
import cs.superleague.analytics.exceptions.NotAuthorizedException;
import cs.superleague.analytics.requests.CreateCustomerReportRequest;
import cs.superleague.analytics.responses.CreateCustomerReportResponse;
import cs.superleague.integration.ServiceSelector;
import cs.superleague.user.requests.GetUserByUUIDRequest;
import cs.superleague.user.responses.GetUserByUUIDResponse;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.UUID;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

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
     * @throws // NotAuthorizedException
     * @throws // ReportException
     * @throws // InvalidRequestException
     */

    @Override
    public CreateCustomerReportResponse createCustomerReport(CreateCustomerReportRequest request) throws AnalyticsException {

        if (request == null) {
            throw new InvalidRequestException("CreateCustomerReportRequest is null- Cannot create report");
        }

        validAnalyticsRequest(request.getReportType(), request.getStartDate(), request.getEndDate(), request.getCustomerID());


        return null;
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

//    public void isAuthorized(UUID userID) throws NotAuthorizedException {
//        GetUserByUUIDRequest request=new GetUserByUUIDRequest(userID);
//        boolean isAdmin=false;
//        try {
//            GetUserByUUIDResponse response= .getUserByUUID(request);
//            isAdmin=response.getAdmin();
//        } catch (UserDoesNotExist userDoesNotExist) {
//            throw new NotAuthorizedException("Error: User is not authorized to perform this action");
//        }
//    }
}
