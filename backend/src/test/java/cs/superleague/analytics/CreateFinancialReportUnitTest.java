package cs.superleague.analytics;

import cs.superleague.analytics.dataclass.ReportType;
import cs.superleague.analytics.exceptions.InvalidRequestException;
import cs.superleague.analytics.exceptions.NotAuthorizedException;
import cs.superleague.analytics.requests.CreateFinancialReportRequest;
import cs.superleague.analytics.requests.CreateUserReportRequest;
import cs.superleague.analytics.responses.CreateFinancialReportResponse;
import cs.superleague.analytics.responses.CreateUserReportResponse;
import cs.superleague.integration.security.JwtUtil;
import cs.superleague.payment.PaymentService;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.responses.GetOrdersResponse;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.shopping.dataclass.Store;
import cs.superleague.shopping.repos.StoreRepo;
import cs.superleague.user.UserService;
import cs.superleague.user.dataclass.*;
import cs.superleague.user.repos.AdminRepo;
import cs.superleague.user.requests.GetUsersRequest;
import cs.superleague.user.responses.GetCurrentUserResponse;
import cs.superleague.user.responses.GetUsersResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateFinancialReportUnitTest {

    @Mock
    AdminRepo adminRepo;

    @Mock
    UserService userService;

    @Mock
    PaymentService paymentService;

    @Mock
    StoreRepo storeRepo;

    @InjectMocks
    private AnalyticsServiceImpl analyticsService;

    @InjectMocks
    JwtUtil jwtTokenUtil;

    CreateFinancialReportResponse response;
    CreateFinancialReportRequest request;
    Admin admin;
    Customer customer;
    Shopper shopper;
    Driver driver;
    Driver driver2;

    Date startDate = new Date();
    Date endDate;

    Order order1, order2, order3;
    Store store1, store2;

    UUID adminID = UUID.randomUUID();

    String jwtTokenCustomer;
    String jwtTokenAdmin;

    List<Order> orders = new ArrayList<>();

    @BeforeEach
    void setup(){

        admin = new Admin();
        admin.setAdminID(adminID);
        admin.setName("Levy");
        admin.setAccountType(UserType.ADMIN);
        admin.setEmail("addddddddddd@gggg.com");

        customer = new Customer();
        customer.setCustomerID(adminID);
        customer.setAccountType(UserType.CUSTOMER);
        customer.setEmail("myECustomer@gmi.com");

        store1 = new Store();
        store1.setStoreID(UUID.randomUUID());
        store1.setStoreBrand("Woolworth's");

        store2 = new Store();
        store2.setStoreBrand("PnP");
        store2.setStoreID(UUID.randomUUID());

        order1 = new Order();
        order1.setOrderID(UUID.randomUUID());
        order1.setCreateDate(Calendar.getInstance());
        order1.setStoreID(store1.getStoreID());
        order1.setTotalCost(12.3);

        order2 = new Order();
        order2.setOrderID(UUID.randomUUID());
        order2.setCreateDate(Calendar.getInstance());
        order2.setStoreID(store2.getStoreID());
        order2.setTotalCost(14.3);

        order3 = new Order();
        order3.setOrderID(UUID.randomUUID());
        order3.setCreateDate(Calendar.getInstance());
        order3.setStoreID(store1.getStoreID());
        order3.setTotalCost(22.3);

        orders.add(order1);
        orders.add(order2);
        orders.add(order3);



        jwtTokenCustomer=jwtTokenUtil.generateJWTTokenCustomer(customer);
        jwtTokenAdmin=jwtTokenUtil.generateJWTTokenAdmin(admin);


        this.endDate = new Date();

    }

    @AfterEach
    void teardown(){}

    @Test
    @DisplayName("When request object is not specified")
    void UnitTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> analyticsService.createFinancialReport(null));
        assertEquals("CreateFinancialReportRequest is null- Cannot create report", thrown.getMessage());
    }

    @Test
    @DisplayName("When ReportType parameter is not specified")
    void UnitTest_testingNullRequestReportTypeParameter(){
        request = new CreateFinancialReportRequest(new Date(), new Date(), null);
        System.out.println(new Date());
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> analyticsService.createFinancialReport(request));
        assertEquals("Exception: Report Type in request object is null", thrown.getMessage());
    }

    @Test
    @DisplayName("When StartDate parameter is not specified")
    void UnitTest_testingNullRequestStartDateParameter(){
        request = new CreateFinancialReportRequest(null, new Date(), ReportType.CSV);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> analyticsService.createFinancialReport(request));
        assertEquals("Exception: Start Date in request object is null", thrown.getMessage());
    }

    @Test
    @DisplayName("When endDate parameter is not specified")
    void UnitTest_testingNullRequestEndDateParameter(){
        request = new CreateFinancialReportRequest(new Date(), null, ReportType.CSV);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> analyticsService.createFinancialReport(request));
        assertEquals("Exception: End Date in request object is null", thrown.getMessage());
    }

    @Test
    @DisplayName("When validJWT parameter")
    void UnitTest_testingValidAdminJWTParameterCSV(){
        request = new CreateFinancialReportRequest(startDate, endDate, ReportType.CSV);

        try {

            response =  analyticsService.createFinancialReport(request);
            assertEquals("FinancialReport.csv downloaded", response.getMessage());
            assertTrue(response.isSuccess());

        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("When validJWT parameter")
    void UnitTest_testingValidAdminJWTParameterPDF(){
        request = new CreateFinancialReportRequest(startDate, this.endDate, ReportType.PDF);

        try {
            when(paymentService.getOrders(Mockito.any())).thenReturn(new GetOrdersResponse(orders, true, "", new Date()));
            when(storeRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(store1));
            response =  analyticsService.createFinancialReport(request);
            assertEquals("FinancialReport.pdf downloaded", response.getMessage());
            assertTrue(response.isSuccess());

        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }
}
