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
import cs.superleague.integration.ServiceSelector;
import cs.superleague.models.*;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.shopping.ShoppingServiceImpl;
import cs.superleague.shopping.repos.CatalogueRepo;
import cs.superleague.shopping.repos.ItemRepo;
import cs.superleague.shopping.repos.StoreRepo;
import cs.superleague.user.UserService;
import cs.superleague.user.repos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.UUID;

@CrossOrigin
@RestController
public class AnalyticsController implements AnalyticsApi {

    @Autowired
    ShoppingServiceImpl shoppingService;

    @Autowired
    StoreRepo storeRepo;

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    CatalogueRepo catalogueRepo;

    @Autowired
    ItemRepo itemRepo;

    @Autowired
    ShopperRepo shopperRepo;

    @Autowired
    GroceryListRepo groceryListRepo;

    @Autowired
    CustomerRepo customerRepo;

    @Autowired
    AdminRepo adminRepo;

    @Autowired
    DriverRepo driverRepo;

    @Autowired
    UserService userService;

    @Autowired
    private AnalyticsServiceImpl analyticsService;


    UUID storeID = UUID.fromString("01234567-9ABC-DEF0-1234-56789ABCDEF0");


    UUID userID = UUID.fromString("55534567-9CBC-FEF0-1254-56789ABCDEF0");


    UUID orderID = UUID.fromString("99134567-9CBC-FEF0-1254-56789ABCDEF0");


    private final boolean mockMode = false;


    @Override
    public ResponseEntity<AnalyticsCreateUserReportResponse> createUserReport(AnalyticsCreateUserReportRequest body) {
//
//        Admin admin;
//        Customer customer;
//        Shopper shopper;
//        Driver driver;
//        Driver driver2;
//
//        UUID adminID = UUID.randomUUID();
//
//
//        admin = new Admin();
//        admin.setAdminID(adminID);
//        admin.setName("Levy");
//        admin.setAccountType(UserType.ADMIN);
//        adminRepo.save(admin);
//
//        customer = new Customer();
//        customer.setCustomerID(adminID);
//        customer.setAccountType(UserType.CUSTOMER);
//        customerRepo.save(customer);
//
//        shopper = new Shopper();
//        shopper.setShopperID(adminID);
//        shopper.setAccountType(UserType.SHOPPER);
//        shopper.setOrdersCompleted(5);
//        shopperRepo.save(shopper);
//
//        driver = new Driver();
//        driver.setDriverID(adminID);
//        driver.setAccountType(UserType.DRIVER);
//        driver.setEmail("levy@smallFC.com");
//        driver.setRating(4.7);
//        driver.setOnShift(true);
//        driverRepo.save(driver);
//
//        driver2 = new Driver();
//        driver2.setDriverID(UUID.randomUUID());
//        driver2.setAccountType(UserType.DRIVER);
//        driver2.setEmail("kane@smallFC.com");
//        driver2.setRating(4.6);
//        driver2.setOnShift(false);
//        driverRepo.save(driver2);

        //creating response object  and default return status
        AnalyticsCreateUserReportResponse response = new AnalyticsCreateUserReportResponse();
        HttpStatus status = HttpStatus.OK;

        try{

            CreateUserReportRequest req = new CreateUserReportRequest(UUID.fromString(body.getAdminID()),
                    Calendar.getInstance(), Calendar.getInstance(), ReportType.valueOf(body.getReportType()));
            CreateUserReportResponse createUserReportResponse = ServiceSelector.getAnalyticsService().createUserReport(req);

            try {
                response.setMessage(createUserReportResponse.getMessage());
                response.setSuccess(createUserReportResponse.isSuccess());
                response.setTimestamp(createUserReportResponse.getTimestamp().toString());
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

            CreateFinancialReportRequest req = new CreateFinancialReportRequest(body.getJwWTToken(),
                    Calendar.getInstance(), Calendar.getInstance(), ReportType.valueOf(body.getReportType()));
            CreateFinancialReportResponse createFinancialReportResponse = ServiceSelector.getAnalyticsService().createFinancialReport(req);

            try {
                response.setMessage(createFinancialReportResponse.getMessage());
                response.setSuccess(createFinancialReportResponse.isSuccess());
                response.setTimestamp(createFinancialReportResponse.getTimestamp().toString());
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

            CreateMonthlyReportRequest req = new CreateMonthlyReportRequest(body.getJwWTToken(),
                    ReportType.valueOf(body.getReportType()));
            CreateMonthlyReportResponse createMonthlyReportResponse = ServiceSelector.getAnalyticsService().createMonthlyReport(req);

            try {
                response.setMessage(createMonthlyReportResponse.getMessage());
                response.setSuccess(createMonthlyReportResponse.isSuccess());
                response.setTimestamp(createMonthlyReportResponse.getTimestamp().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch(Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<>(response, status);

    }
}
