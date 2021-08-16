package cs.superleague.analytics;

import cs.superleague.analytics.dataclass.ReportType;
import cs.superleague.analytics.exceptions.InvalidRequestException;
import cs.superleague.analytics.exceptions.NotAuthorizedException;
import cs.superleague.analytics.requests.CreateUserReportRequest;
import cs.superleague.analytics.responses.CreateUserReportResponse;
import cs.superleague.user.UserService;
import cs.superleague.user.dataclass.*;
import cs.superleague.user.repos.AdminRepo;
import cs.superleague.user.requests.GetUsersRequest;
import cs.superleague.user.responses.GetUsersResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateUserReportUnitTest {

    @Mock
    AdminRepo adminRepo;

    @Mock
    UserService userService;

    @InjectMocks
    private AnalyticsServiceImpl analyticsService;

    CreateUserReportResponse response;
    CreateUserReportRequest request;
    Admin admin;
    Customer customer;
    Shopper shopper;
    Driver driver;
    Driver driver2;

    UUID adminID = UUID.randomUUID();

    List<User> users = new ArrayList<>();

    @BeforeEach
    void setup(){

        admin = new Admin();
        admin.setAdminID(adminID);
        admin.setName("Levy");
        admin.setAccountType(UserType.ADMIN);

        customer = new Customer();
        customer.setCustomerID(adminID);
        customer.setAccountType(UserType.CUSTOMER);

        shopper = new Shopper();
        shopper.setShopperID(adminID);
        shopper.setAccountType(UserType.SHOPPER);
        shopper.setOrdersCompleted(5);

        driver = new Driver();
        driver.setDriverID(adminID);
        driver.setAccountType(UserType.DRIVER);
        driver.setEmail("levy@smallFC.com");
        driver.setRating(4.7);
        driver.setOnShift(true);

        driver2 = new Driver();
        driver2.setDriverID(adminID);
        driver2.setAccountType(UserType.DRIVER);
        driver2.setEmail("kane@smallFC.com");
        driver2.setRating(4.6);
        driver2.setOnShift(false);

        users.add(admin);
        users.add(customer);
        users.add(shopper);
        users.add(driver);
        users.add(driver2);

    }

    @AfterEach
    void teardown(){}

    @Test
    @DisplayName("When request object is not specified")
    void UnitTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> analyticsService.createUserReport(null));
        assertEquals("CreateUserReportRequest is null- Cannot create report", thrown.getMessage());
    }

    @Test
    @DisplayName("When adminID parameter is not specified")
    void UnitTest_testingNullRequestOrderIDParameter(){
        request = new CreateUserReportRequest(null, Calendar.getInstance(), Calendar.getInstance(), ReportType.CSV);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> analyticsService.createUserReport(request));
        assertEquals("Exception: User ID in request object is null", thrown.getMessage());
    }

    @Test
    @DisplayName("When ReportType parameter is not specified")
    void UnitTest_testingNullRequestReportTypeParameter(){
        request = new CreateUserReportRequest(adminID, Calendar.getInstance(), Calendar.getInstance(), null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> analyticsService.createUserReport(request));
        assertEquals("Exception: Report Type in request object is null", thrown.getMessage());
    }

    @Test
    @DisplayName("When StartDate parameter is not specified")
    void UnitTest_testingNullRequestStartDateParameter(){
        request = new CreateUserReportRequest(adminID, null, Calendar.getInstance(), ReportType.CSV);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> analyticsService.createUserReport(request));
        assertEquals("Exception: Start Date in request object is null", thrown.getMessage());
    }

    @Test
    @DisplayName("When endDate parameter is not specified")
    void UnitTest_testingNullRequestEndDateParameter(){
        request = new CreateUserReportRequest(adminID, Calendar.getInstance(), null, ReportType.CSV);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> analyticsService.createUserReport(request));
        assertEquals("Exception: End Date in request object is null", thrown.getMessage());
    }

    @Test
    @DisplayName("When Invalid adminID parameter")
    void UnitTest_testingInvalidAdminParameter(){
        request = new CreateUserReportRequest(UUID.randomUUID(), Calendar.getInstance(), Calendar.getInstance(), ReportType.CSV);
        when(adminRepo.findById(Mockito.any())).thenReturn(null);
        Throwable thrown = Assertions.assertThrows(NotAuthorizedException.class, ()-> analyticsService.createUserReport(request));
        assertEquals("ID given does not belong to admin - Could not generate report", thrown.getMessage());
    }

    @Test
    @DisplayName("When valid adminID parameter")
    void UnitTest_testingValidAdminParameterPDF(){
        request = new CreateUserReportRequest(adminID, Calendar.getInstance(), Calendar.getInstance(), ReportType.PDF);
        when(adminRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(admin));

        GetUsersResponse getUsersResponse = new GetUsersResponse(users, true, "Users successfully returned", new Date());

        try{
            when(userService.getUsers(new GetUsersRequest(Mockito.any()))).thenReturn(getUsersResponse);
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
    void UnitTest_testingValidAdminParameterCSV(){
        request = new CreateUserReportRequest(adminID, Calendar.getInstance(), Calendar.getInstance(), ReportType.CSV);
        when(adminRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(admin));

        GetUsersResponse getUsersResponse = new GetUsersResponse(users, true, "Users successfully returned", new Date());

        try{
            when(userService.getUsers(new GetUsersRequest(Mockito.any()))).thenReturn(getUsersResponse);
            response = analyticsService.createUserReport(request);
            assertTrue(response.isSuccess());
            assertEquals("UserReport.csv downloaded", response.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            Assertions.fail();
        }
    }
}
