package cs.superleague.payment;

import cs.superleague.integration.security.JwtUtil;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.exceptions.InvalidRequestException;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.payment.requests.GetOrdersRequest;
import cs.superleague.payment.responses.GetOrdersResponse;
import cs.superleague.user.UserService;
import cs.superleague.user.UserServiceImpl;
import cs.superleague.user.dataclass.*;
import cs.superleague.user.repos.AdminRepo;
import cs.superleague.user.repos.CustomerRepo;
import cs.superleague.user.repos.DriverRepo;
import cs.superleague.user.repos.ShopperRepo;
import cs.superleague.user.responses.GetCurrentUserResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetOrdersUnitTest {

    @Mock
    private ShopperRepo shopperRepo;

    @Mock
    private AdminRepo adminRepo;

    @Mock
    private DriverRepo driverRepo;

    @Mock
    private CustomerRepo customerRepo;

    @Mock
    private OrderRepo orderRepo;

    @Mock
    private UserService userService;

    @InjectMocks
    JwtUtil jwtTokenUtil;

    @InjectMocks
    private PaymentServiceImpl paymentService;


    Shopper shopper;
    Driver driver;
    Admin admin;
    Order order;

    UUID adminUUID = UUID.randomUUID();
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

        admin = new Admin();
        admin.setAdminID(adminUUID);
        admin.setName("Levy");
        admin.setEmail("aadmin@aaa.com");
        admin.setAccountType(UserType.ADMIN);

        driver=new Driver();
        driver.setDriverID(driverUUID);
        driver.setOnShift(true);
        driver.setAccountType(UserType.DRIVER);
        driver.setEmail("driver@gmaill.com");

        driverJWT = jwtTokenUtil.generateJWTTokenDriver(driver);
        adminJWT = jwtTokenUtil.generateJWTTokenDriver(driver);
        shopperJWT = jwtTokenUtil.generateJWTTokenShopper(shopper);

        order = new Order();
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
    @DisplayName("When request object parameter - JWTToken - valid user type, emptyOrders")
    void UnitTest_testing_JWTToken_Parameter_RequestObject_ValidUserTypeEmptyOrders(){
        GetOrdersRequest request = new GetOrdersRequest();

        try {
            GetCurrentUserResponse getCurrentUserResponse = new GetCurrentUserResponse(admin,true, Calendar.getInstance().getTime(),"User successfully returned");
            when(orderRepo.findAll()).thenReturn(orders);
            GetOrdersResponse response = paymentService.getOrders(request);
            assertTrue(response.isSuccess());
            assertNotNull(response.getOrders());
            assertEquals("There no orders", response.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("When request object parameter - JWTToken - valid user type")
    void UnitTest_testing_JWTToken_Parameter_RequestObject_ValidUserType(){
        GetOrdersRequest request = new GetOrdersRequest();

        orders.add(order);

        try {
            GetCurrentUserResponse getCurrentUserResponse = new GetCurrentUserResponse(admin,true, Calendar.getInstance().getTime(),"User successfully returned");
            when(orderRepo.findAll()).thenReturn(orders);
            GetOrdersResponse response = paymentService.getOrders(request);
            assertTrue(response.isSuccess());
            assertNotNull(response.getOrders());
            assertEquals("Users successfully returned", response.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            fail();
        }
    }
}
