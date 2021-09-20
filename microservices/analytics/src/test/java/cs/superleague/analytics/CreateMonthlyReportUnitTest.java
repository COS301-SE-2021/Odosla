package cs.superleague.analytics;

import cs.superleague.analytics.AnalyticsHelpers.MonthlyAnalyticsHelper;
import cs.superleague.analytics.dataclass.ReportType;
import cs.superleague.analytics.exceptions.InvalidRequestException;
//import cs.superleague.analytics.requests.CreateFinancialReportRequest;
import cs.superleague.analytics.requests.CreateMonthlyReportRequest;
//import cs.superleague.analytics.responses.CreateFinancialReportResponse;
import cs.superleague.analytics.responses.CreateMonthlyReportResponse;
import cs.superleague.integration.security.JwtUtil;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.responses.GetOrdersResponse;
import cs.superleague.shopping.dataclass.Store;
import cs.superleague.shopping.responses.GetStoresResponse;
import cs.superleague.user.dataclass.*;
import cs.superleague.user.responses.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.*;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateMonthlyReportUnitTest {

    @InjectMocks
    private AnalyticsServiceImpl analyticsService;

    @InjectMocks
    MonthlyAnalyticsHelper monthlyAnalyticsHelper;

    @Mock
    RestTemplate restTemplate;

    @Value("${shoppingHost}")
    private String shoppingHost;
    @Value("${shoppingPort}")
    private String shoppingPort;
    @Value("${paymentHost}")
    private String paymentHost;
    @Value("${paymentPort}")
    private String paymentPort;
    @Value("${userHost}")
    private String userHost;
    @Value("${userPort}")
    private String userPort;

    @InjectMocks
    JwtUtil jwtTokenUtil;

    CreateMonthlyReportResponse response;
    CreateMonthlyReportRequest request;
    Admin admin;
    Customer customer;
    Shopper shopper;
    Driver driver;
    Driver driver2;

    Calendar startDate = Calendar.getInstance();
    Calendar endDate;

    Order order1, order2, order3;
    Store store1, store2;

    UUID adminID = UUID.randomUUID();

    String jwtTokenCustomer;
    String jwtTokenAdmin;

    List<Order> orders = new ArrayList<>();
    List<User> users = new ArrayList<>();

    @BeforeEach
    void setup(){

        admin = new Admin();
        admin.setAdminID(adminID);
        admin.setName("Levy");
        admin.setAccountType(UserType.ADMIN);
        admin.setEmail("addddddddddd@gggg.com");
        admin.setActivationDate(new Date());

        customer = new Customer();
        customer.setCustomerID(adminID);
        customer.setAccountType(UserType.CUSTOMER);
        customer.setEmail("myECustomer@gmi.com");
        customer.setActivationDate(new Date());

        store1 = new Store();
        store1.setStoreID(UUID.randomUUID());
        store1.setStoreBrand("Woolworth's");

        store2 = new Store();
        store2.setStoreBrand("PnP");
        store2.setStoreID(UUID.randomUUID());

        order1 = new Order();
        order1.setOrderID(UUID.randomUUID());
        order1.setCreateDate(new Date());
        order1.setStoreID(store1.getStoreID());
        order1.setTotalCost(12.3);

        order2 = new Order();
        order2.setOrderID(UUID.randomUUID());
        order2.setCreateDate(new Date());
        order2.setStoreID(store2.getStoreID());
        order2.setTotalCost(14.3);

        order3 = new Order();
        order3.setOrderID(UUID.randomUUID());
        order3.setCreateDate(new Date());
        order3.setStoreID(store1.getStoreID());
        order3.setTotalCost(22.3);

        shopper = new Shopper();
        shopper.setShopperID(adminID);
        shopper.setAccountType(UserType.SHOPPER);
        shopper.setOrdersCompleted(5);
        shopper.setActivationDate(new Date());

        driver = new Driver();
        driver.setDriverID(adminID);
        driver.setAccountType(UserType.DRIVER);
        driver.setEmail("levy@smallFC.com");
        driver.setRating(4.7);
        driver.setOnShift(true);
        driver.setActivationDate(new Date());

        driver2 = new Driver();
        driver2.setDriverID(adminID);
        driver2.setAccountType(UserType.DRIVER);
        driver2.setEmail("kane@smallFC.com");
        driver2.setRating(4.6);
        driver2.setOnShift(false);
        driver2.setActivationDate(new Date());

        users.add(admin);
        users.add(customer);
        users.add(shopper);
        users.add(driver);
        users.add(driver2);

        orders.add(order1);
        orders.add(order2);
        orders.add(order3);



        jwtTokenCustomer=jwtTokenUtil.generateJWTTokenCustomer(customer);
        jwtTokenAdmin=jwtTokenUtil.generateJWTTokenAdmin(admin);


        this.endDate = Calendar.getInstance();

    }

    @AfterEach
    void teardown(){}

    @Test
    @DisplayName("When request object is not specified")
    void UnitTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> analyticsService.createMonthlyReport(null));
        assertEquals("CreateMonthlyReportRequest is null- Cannot create report", thrown.getMessage());
    }

    @Test
    @DisplayName("When ReportType parameter is not specified")
    void UnitTest_testingNullRequestReportTypeParameter(){
        request = new CreateMonthlyReportRequest(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> analyticsService.createMonthlyReport(request));
        assertEquals("Exception: Report Type in request object is null", thrown.getMessage());
    }

    @Test
    @DisplayName("When validJWT parameter")
    void UnitTest_testingValidAdminJWTParameterCSV(){
        request = new CreateMonthlyReportRequest(ReportType.CSV);

        try {
            response =  analyticsService.createMonthlyReport(request);
            assertEquals("FinancialReport CSV successfully generated", response.getMessage());
            assertTrue(response.isSuccess());

        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }


}