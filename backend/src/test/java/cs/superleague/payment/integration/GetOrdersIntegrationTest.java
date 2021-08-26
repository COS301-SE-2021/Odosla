package cs.superleague.payment.integration;

import cs.superleague.integration.security.JwtUtil;
import cs.superleague.payment.PaymentServiceImpl;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.exceptions.InvalidRequestException;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.payment.requests.GetOrdersRequest;
import cs.superleague.payment.responses.GetOrdersResponse;
import cs.superleague.user.UserService;
import cs.superleague.user.dataclass.*;
import cs.superleague.user.repos.AdminRepo;
import cs.superleague.user.repos.CustomerRepo;
import cs.superleague.user.repos.DriverRepo;
import cs.superleague.user.repos.ShopperRepo;
import cs.superleague.user.responses.GetCurrentUserResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class GetOrdersIntegrationTest {

    @Autowired
    private ShopperRepo shopperRepo;

    @Autowired
    private AdminRepo adminRepo;

    @Autowired
    private DriverRepo driverRepo;

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private UserService userService;

    @Autowired
    JwtUtil jwtTokenUtil;

    @Autowired
    private PaymentServiceImpl paymentService;


    Shopper shopper;
    Driver driver;
    Customer customer;
    Admin admin;
    Order order;

    UUID adminUUID = UUID.randomUUID();
    UUID customerUUID = UUID.randomUUID();
    UUID driverUUID = UUID.randomUUID();
    UUID shopperUUID=UUID.randomUUID();

    String shopperJWT;
    String adminJWT;
    String driverJWT;

    List<Order> orders;
    @BeforeEach
    void setUp()
    {
        shopper = new Shopper();
        shopper.setShopperID(shopperUUID);
        shopper.setName("JJ");
        shopper.setEmail("shhhopper@sh.com");
        shopper.setAccountType(UserType.SHOPPER);
        shopperRepo.save(shopper);

        admin = new Admin();
        admin.setAdminID(adminUUID);
        admin.setName("Levy");
        admin.setEmail("aadmin@aaa.com");
        admin.setAccountType(UserType.ADMIN);
        adminRepo.save(admin);

        driver=new Driver();
        driver.setDriverID(driverUUID);
        driver.setOnShift(true);
        driver.setAccountType(UserType.DRIVER);
        driver.setEmail("driver@gmaill.com");

        driverJWT = jwtTokenUtil.generateJWTTokenDriver(driver);
        adminJWT = jwtTokenUtil.generateJWTTokenAdmin(admin);
        shopperJWT = jwtTokenUtil.generateJWTTokenShopper(shopper);

        order = new Order();
        order.setOrderID(UUID.randomUUID());
        orderRepo.save(order);
        orders = new ArrayList<>();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Description("Tests for when GetUsers is submitted with a null request object- exception should be thrown")
    @DisplayName("When request object is not specified")
    void UnitTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> paymentService.getOrders(null));
        assertEquals("GetOrders request is null - could not return orders", thrown.getMessage());
    }

    @Test
    @DisplayName("When request object parameter - JWTToken - valid user type")
    void UnitTest_testing_JWTToken_Parameter_RequestObject_ValidUserType(){
        GetOrdersRequest request = new GetOrdersRequest();

        try {
            GetOrdersResponse response = paymentService.getOrders(request);
            assertEquals("Users successfully returned", response.getMessage());
            assertNotNull(response.getOrders());
            assertTrue(response.isSuccess());
        }catch (Exception e){
            e.printStackTrace();
            fail();
        }
    }
}
