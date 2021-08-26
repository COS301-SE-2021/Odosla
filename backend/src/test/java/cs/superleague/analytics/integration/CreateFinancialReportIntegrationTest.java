package cs.superleague.analytics.integration;

import cs.superleague.analytics.AnalyticsServiceImpl;
import cs.superleague.analytics.dataclass.ReportType;
import cs.superleague.analytics.exceptions.InvalidRequestException;
import cs.superleague.analytics.requests.CreateFinancialReportRequest;
import cs.superleague.analytics.responses.CreateFinancialReportResponse;
import cs.superleague.integration.security.JwtUtil;
import cs.superleague.payment.PaymentService;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.shopping.dataclass.Store;
import cs.superleague.shopping.repos.StoreRepo;
import cs.superleague.user.UserService;
import cs.superleague.user.dataclass.*;
import cs.superleague.user.repos.AdminRepo;
import cs.superleague.user.repos.CustomerRepo;
import cs.superleague.user.responses.GetCurrentUserResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.*;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class CreateFinancialReportIntegrationTest {

    @Autowired
    AdminRepo adminRepo;

    @Autowired
    CustomerRepo customerRepo;

    @Autowired
    UserService userService;

    @Autowired
    PaymentService paymentService;

    @Autowired
    StoreRepo storeRepo;

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    private AnalyticsServiceImpl analyticsService;

    @Autowired
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
        adminRepo.save(admin);

        customer = new Customer();
        customer.setCustomerID(adminID);
        customer.setAccountType(UserType.CUSTOMER);
        customer.setEmail("myECustomer@gmi.com");
        customerRepo.save(customer);

        store1 = new Store();
        store1.setStoreID(UUID.randomUUID());
        store1.setStoreBrand("Woolworth's");
        storeRepo.save(store1);

        store2 = new Store();
        store2.setStoreBrand("PnP");
        store2.setStoreID(UUID.randomUUID());
        storeRepo.save(store2);

        order1 = new Order();
        order1.setOrderID(UUID.randomUUID());
        order1.setCreateDate(Calendar.getInstance());
        order1.setStoreID(store1.getStoreID());
        order1.setTotalCost(12.3);
        orderRepo.save(order1);

        order2 = new Order();
        order2.setOrderID(UUID.randomUUID());
        order2.setCreateDate(Calendar.getInstance());
        order2.setStoreID(store2.getStoreID());
        order2.setTotalCost(14.3);
        orderRepo.save(order2);

        order3 = new Order();
        order3.setOrderID(UUID.randomUUID());
        order3.setCreateDate(Calendar.getInstance());
        order3.setStoreID(store1.getStoreID());
        order3.setTotalCost(22.3);
        orderRepo.save(order3);

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
    void Integration_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> analyticsService.createFinancialReport(null));
        assertEquals("CreateFinancialReportRequest is null- Cannot create report", thrown.getMessage());
    }

    @Test
    @DisplayName("When adminID parameter is not specified")
    void Integration_testingNullRequestOrderIDParameter(){
        request = new CreateFinancialReportRequest(null, new Date(), new Date(), ReportType.CSV);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> analyticsService.createFinancialReport(request));
        assertEquals("Exception: JWTToken in request object is null", thrown.getMessage());
    }

    @Test
    @DisplayName("When ReportType parameter is not specified")
    void Integration_testingNullRequestReportTypeParameter(){
        request = new CreateFinancialReportRequest("adminID", new Date(), new Date(), null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> analyticsService.createFinancialReport(request));
        assertEquals("Exception: Report Type in request object is null", thrown.getMessage());
    }

    @Test
    @DisplayName("When StartDate parameter is not specified")
    void Integration_testingNullRequestStartDateParameter(){
        request = new CreateFinancialReportRequest("adminID", null, new Date(), ReportType.CSV);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> analyticsService.createFinancialReport(request));
        assertEquals("Exception: Start Date in request object is null", thrown.getMessage());
    }

    @Test
    @DisplayName("When endDate parameter is not specified")
    void Integration_testingNullRequestEndDateParameter(){
        request = new CreateFinancialReportRequest("adminID", new Date(), null, ReportType.CSV);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> analyticsService.createFinancialReport(request));
        assertEquals("Exception: End Date in request object is null", thrown.getMessage());
    }

    @Test
    @DisplayName("When Invalid adminID parameter")
    void Integration_testingInvalidAdminParameter(){
        request = new CreateFinancialReportRequest(jwtTokenCustomer, new Date(), new Date(), ReportType.CSV);

        GetCurrentUserResponse getCurrentUserResponse = new GetCurrentUserResponse(customer, true, new Date(), "");

        try {
            response =  analyticsService.createFinancialReport(request);
            assertEquals("User is not an admin - Could not create report", response.getMessage());
            assertFalse(response.isSuccess());

        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("When validJWT parameter")
    void Integration_testingValidAdminJWTParameterCSV(){
        request = new CreateFinancialReportRequest(jwtTokenAdmin, startDate, endDate, ReportType.CSV);

        GetCurrentUserResponse getCurrentUserResponse = new GetCurrentUserResponse(admin, true, new Date(), "");

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
    void Integration_testingValidAdminJWTParameterPDF(){
        request = new CreateFinancialReportRequest(jwtTokenAdmin, startDate, this.endDate, ReportType.PDF);

        GetCurrentUserResponse getCurrentUserResponse = new GetCurrentUserResponse(admin, true, new Date(), "");

        try {
            response =  analyticsService.createFinancialReport(request);
            assertEquals("FinancialReport.pdf downloaded", response.getMessage());
            assertTrue(response.isSuccess());

        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }
}
