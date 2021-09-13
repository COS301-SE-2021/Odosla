package cs.superleague.analytics.controller;

import cs.superleague.analytics.AnalyticsServiceImpl;
import cs.superleague.analytics.dataclass.ReportType;
import cs.superleague.analytics.requests.CreateFinancialReportRequest;
import cs.superleague.analytics.requests.CreateMonthlyReportRequest;
import cs.superleague.analytics.requests.CreateUserReportRequest;
import cs.superleague.analytics.responses.CreateFinancialReportResponse;
import cs.superleague.analytics.responses.CreateMonthlyReportResponse;
import cs.superleague.analytics.responses.CreateUserReportResponse;
import cs.superleague.api.AnalyticsApi;
import cs.superleague.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

@CrossOrigin
@RestController
public class AnalyticsController implements AnalyticsApi {

    private final AnalyticsServiceImpl analyticsService;

    @Autowired
    public AnalyticsController(AnalyticsServiceImpl analyticsService){
        this.analyticsService = analyticsService;
    }

    @Override
    public ResponseEntity<AnalyticsCreateUserReportResponse> createUserReport(AnalyticsCreateUserReportRequest body) {
        AnalyticsCreateUserReportResponse response = new AnalyticsCreateUserReportResponse();
        HttpStatus status = HttpStatus.OK;

        try{

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date startDate = sdf.parse(body.getStartDate());
            Date endDate = sdf.parse(body.getEndDate());

            CreateUserReportRequest req = new CreateUserReportRequest(startDate, endDate, ReportType.valueOf(body.getReportType()));
            CreateUserReportResponse createUserReportResponse = analyticsService.createUserReport(req);

            try {
                response.setMessage(createUserReportResponse.getMessage());
                response.setSuccess(createUserReportResponse.isSuccess());
                response.setTimestamp(createUserReportResponse.getTimestamp().toString());
                if(createUserReportResponse.getStringBuilder() != null)
                response.setCsv(createUserReportResponse.getStringBuilder());
                response.setPdf(createUserReportResponse.getDocument());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch(Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<>(response, status);

    }

    @Override
    public ResponseEntity<AnalyticsCreateFinancialReportResponse> createFinancialReport(AnalyticsCreateFinancialReportRequest body) {

        //creating response object  and default return status
        AnalyticsCreateFinancialReportResponse response = new AnalyticsCreateFinancialReportResponse();
        HttpStatus status = HttpStatus.OK;

        try{

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date startDate = sdf.parse(body.getStartDate());
            Date endDate = sdf.parse(body.getEndDate());

            CreateFinancialReportRequest req = new CreateFinancialReportRequest(startDate, endDate, ReportType.valueOf(body.getReportType()));
            CreateFinancialReportResponse createFinancialReportResponse = analyticsService.createFinancialReport(req);

            try {
                response.setMessage(createFinancialReportResponse.getMessage());
                response.setSuccess(createFinancialReportResponse.isSuccess());
                response.setTimestamp(createFinancialReportResponse.getTimestamp().toString());
                if(createFinancialReportResponse.getStringBuilder() != null)
                    response.setCsv(createFinancialReportResponse.getStringBuilder());
                response.setPdf(createFinancialReportResponse.getDocument());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch(Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<>(response, status);

    }

    @Override
    public ResponseEntity<AnalyticsCreateMonthlyReportResponse> createMonthlyReport(AnalyticsCreateMonthlyReportRequest body) {


        //creating response object  and default return status
        AnalyticsCreateMonthlyReportResponse response = new AnalyticsCreateMonthlyReportResponse();
        HttpStatus status = HttpStatus.OK;

        try{

            CreateMonthlyReportRequest req = new CreateMonthlyReportRequest(ReportType.valueOf(body.getReportType()));
            CreateMonthlyReportResponse createMonthlyReportResponse = analyticsService.createMonthlyReport(req);

            try {
                response.setMessage(createMonthlyReportResponse.getMessage());
                response.setSuccess(createMonthlyReportResponse.isSuccess());
                response.setTimestamp(createMonthlyReportResponse.getTimestamp().toString());
                if(createMonthlyReportResponse.getStringBuilder()!= null)
                response.setCsv(createMonthlyReportResponse.getStringBuilder());
                response.setPdf(createMonthlyReportResponse.getDocument());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch(Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<>(response, status);

    }
}
