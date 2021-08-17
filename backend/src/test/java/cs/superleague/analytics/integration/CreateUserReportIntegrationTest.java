package cs.superleague.analytics.integration;

import cs.superleague.analytics.AnalyticsServiceImpl;
import cs.superleague.analytics.dataclass.ReportType;
import cs.superleague.analytics.exceptions.InvalidRequestException;
import cs.superleague.analytics.exceptions.NotAuthorizedException;
import cs.superleague.analytics.requests.CreateUserReportRequest;
import cs.superleague.analytics.responses.CreateUserReportResponse;
import cs.superleague.user.UserService;
import cs.superleague.user.dataclass.*;
import cs.superleague.user.repos.AdminRepo;
import cs.superleague.user.repos.CustomerRepo;
import cs.superleague.user.repos.DriverRepo;
import cs.superleague.user.repos.ShopperRepo;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class CreateUserReportIntegrationTest {

    @Autowired
    AdminRepo adminRepo;

    @Autowired
    DriverRepo driverRepo;

    @Autowired
    CustomerRepo customerRepo;

    @Autowired
    ShopperRepo shopperRepo;

    @Autowired
    UserService userService;

    @Autowired
    private AnalyticsServiceImpl analyticsService;

    CreateUserReportResponse response;
    CreateUserReportRequest request;
    Admin admin;
    Customer customer;
    Shopper shopper;
    Driver driver;
    Driver driver2;

    UUID adminID = UUID.randomUUID();

    @BeforeEach
    void setup(){

        admin = new Admin();
        admin.setAdminID(adminID);
        admin.setName("Levy");
        admin.setAccountType(UserType.ADMIN);
        adminRepo.save(admin);

        customer = new Customer();
        customer.setCustomerID(adminID);
        customer.setAccountType(UserType.CUSTOMER);
        customerRepo.save(customer);

        shopper = new Shopper();
        shopper.setShopperID(adminID);
        shopper.setAccountType(UserType.SHOPPER);
        shopper.setOrdersCompleted(5);
        shopperRepo.save(shopper);

        driver = new Driver();
        driver.setDriverID(adminID);
        driver.setAccountType(UserType.DRIVER);
        driver.setEmail("Dlevy@smallFC.com");
        driver.setRating(4.7);
        driver.setOnShift(true);
        driverRepo.save(driver);

        driver2 = new Driver();
        driver2.setDriverID(UUID.randomUUID());
        driver2.setAccountType(UserType.DRIVER);
        driver2.setEmail("Hkane@smallFC.com");
        driver2.setRating(4.6);
        driver2.setOnShift(false);
        driverRepo.save(driver2);
    }

    @AfterEach
    void teardown(){}

    @Test
    @DisplayName("When request object is not specified")
    void IntegrationTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> analyticsService.createUserReport(null));
        assertEquals("CreateUserReportRequest is null- Cannot create report", thrown.getMessage());
    }

    @Test
    @DisplayName("When adminID parameter is not specified")
    void IntegrationTest_testingNullRequestOrderIDParameter(){
        request = new CreateUserReportRequest(null, Calendar.getInstance(), Calendar.getInstance(), ReportType.CSV);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> analyticsService.createUserReport(request));
        assertEquals("Exception: User ID in request object is null", thrown.getMessage());
    }

    @Test
    @DisplayName("When ReportType parameter is not specified")
    void IntegrationTest_testingNullRequestReportTypeParameter(){
        request = new CreateUserReportRequest(adminID, Calendar.getInstance(), Calendar.getInstance(), null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> analyticsService.createUserReport(request));
        assertEquals("Exception: Report Type in request object is null", thrown.getMessage());
    }

    @Test
    @DisplayName("When StartDate parameter is not specified")
    void IntegrationTest_testingNullRequestStartDateParameter(){
        request = new CreateUserReportRequest(adminID, null, Calendar.getInstance(), ReportType.CSV);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> analyticsService.createUserReport(request));
        assertEquals("Exception: Start Date in request object is null", thrown.getMessage());
    }

    @Test
    @DisplayName("When endDate parameter is not specified")
    void IntegrationTest_testingNullRequestEndDateParameter(){
        request = new CreateUserReportRequest(adminID, Calendar.getInstance(), null, ReportType.CSV);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> analyticsService.createUserReport(request));
        assertEquals("Exception: End Date in request object is null", thrown.getMessage());
    }

    @Test
    @DisplayName("When Invalid adminID parameter")
    void IntegrationTest_testingInvalidAdminParameter(){
        request = new CreateUserReportRequest(UUID.randomUUID(), Calendar.getInstance(), Calendar.getInstance(), ReportType.CSV);
        Throwable thrown = Assertions.assertThrows(NotAuthorizedException.class, ()-> analyticsService.createUserReport(request));
        assertEquals("ID given does not belong to admin - Could not generate report", thrown.getMessage());
    }

    @Test
    @DisplayName("When valid adminID parameter")
    void IntegrationTest_testingValidAdminParameterPDF(){
        request = new CreateUserReportRequest(adminID, Calendar.getInstance(), Calendar.getInstance(), ReportType.PDF);

        try{
            response = analyticsService.createUserReport(request);
            assertTrue(response.isSuccess());
            assertEquals("UserReport.pdf downloaded", response.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("When valid adminID parameter")
    void IntegrationTest_testingValidAdminParameterCSV(){
        request = new CreateUserReportRequest(adminID, Calendar.getInstance(), Calendar.getInstance(), ReportType.CSV);

        try{
            response = analyticsService.createUserReport(request);
            assertTrue(response.isSuccess());
            assertEquals("UserReport.csv downloaded", response.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            Assertions.fail();
        }
    }
}
