package cs.superleague.analytics;

import cs.superleague.analytics.dataclass.ReportType;
import cs.superleague.analytics.exceptions.NotAuthorizedException;
import cs.superleague.analytics.requests.CreateUserReportRequest;
import cs.superleague.analytics.responses.CreateUserReportResponse;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.exceptions.OrderDoesNotExist;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.user.UserServiceImpl;
import cs.superleague.analytics.exceptions.InvalidRequestException;
import cs.superleague.user.dataclass.Admin;
import cs.superleague.user.repos.AdminRepo;
import cs.superleague.user.repos.DriverRepo;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateUserReportUnitTest {

    @Mock
    OrderRepo orderRepo;

    @Mock
    DriverRepo driverRepo;

    @Mock
    AdminRepo adminRepo;

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private AnalyticsServiceImpl analyticsService;

    CreateUserReportResponse response;
    CreateUserReportRequest request;
    Admin admin;

    UUID adminID = UUID.randomUUID();

    @BeforeEach
    void setup(){

        admin = new Admin();
        admin.setAdminID(adminID);
        admin.setName("Levy");

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
    void UnitTest_testingValidAdminParameter(){
        request = new CreateUserReportRequest(adminID, Calendar.getInstance(), Calendar.getInstance(), ReportType.CSV);
        when(adminRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(admin));

        try{
            response = analyticsService.createUserReport(request);


        }catch (Exception e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("When order with OrderID does not exist - OrderDoesNotExistException")
    void UnitTest_testingOrderDoesNotExistException(){
//        Mockito.when(orderRepo.findById(Mockito.any())).thenReturn(null);
//        Throwable thrown = Assertions.assertThrows(OrderDoesNotExist.class, ()-> userService.collectOrder(request));
//        assertEquals("Order does not exist in database", thrown.getMessage());
    }

    @Test
    @DisplayName("When order with OrderID exists - but couldn't update Order")
    void UnitTest_testingCouldntUpdateOrder() throws InvalidRequestException, OrderDoesNotExist {
//        Order newOrder = new Order();
//        newOrder.setStatus(OrderStatus.AWAITING_COLLECTION);
//        order.setStatus(OrderStatus.AWAITING_COLLECTION);
//        Mockito.when(orderRepo.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(order)).thenReturn(java.util.Optional.of(newOrder));
//        response=userService.collectOrder(request);
//        assertFalse(response.isSuccess());
//        assertNotNull(response);
//        assertNotNull(response.getTimestamp());
//        assertEquals("Couldn't update that order has been collected in database",response.getMessage());
    }

    @Test
    @DisplayName("Order correctly collected")
    void UnitTest_testingCorrectlyCollected() throws InvalidRequestException, OrderDoesNotExist {
//        order.setStatus(OrderStatus.DELIVERY_COLLECTED);
//        Mockito.when(orderRepo.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(order));
//        response=userService.collectOrder(request);
//        assertNotNull(response);
//        assertTrue(response.isSuccess());
//        assertNotNull(response.getTimestamp());
//        assertEquals("Order successfully been collected and status has been changed",response.getMessage());
    }
}
