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
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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

    private final String SECRET = "uQmMa86HgOi6uweJ1JSftIN7TBHFDa3KVJh6kCyoJ9bwnLBqA0YoCAhMMk";

    List<Order> orders;
    @BeforeEach
    void setUp()
    {
//        shopper = new Shopper();
//        shopper.setShopperID(shopperUUID);
//        shopper.setName("JJ");
//        shopper.setEmail("shhhopper@sh.com");
//        shopper.setAccountType(UserType.SHOPPER);
//        shopperRepo.save(shopper);

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

//        adminJWT = jwtTokenUtil.generateJWTTokenAdmin(admin);

        JwtUtil jwtUtil = new JwtUtil();
        String jwt = jwtUtil.generateJWTTokenAdmin(admin);
        jwt = jwt.replace("Bearer ","");
        Claims claims= Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(jwt).getBody();
        List<String> authorities = (List) claims.get("authorities");
        String userType= (String) claims.get("userType");
        String email = (String) claims.get("email");
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
                authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        HashMap<String, Object> info=new HashMap<String, Object>();
        info.put("userType",userType);
        info.put("email",email);
        auth.setDetails(info);
        SecurityContextHolder.getContext().setAuthentication(auth);


        order = new Order();
        order.setOrderID(UUID.randomUUID());
        orderRepo.save(order);
        orders = new ArrayList<>();
    }

    @AfterEach
    void tearDown() {
        orderRepo.deleteAll();
        SecurityContextHolder.clearContext();
    }

    @Test
    @Description("Tests for when GetUsers is submitted with a null request object- exception should be thrown")
    @DisplayName("When request object is not specified")
    void UnitTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> paymentService.getOrders(null));
        assertEquals("GetOrders request is null - could not return orders", thrown.getMessage());
    }

//    @Test
//    @DisplayName("When request object parameter - JWTToken - is not specified")
//    void UnitTest_testingNull_JWTToken_Parameter_RequestObject(){
//        GetOrdersRequest request=new GetOrdersRequest();
//        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> paymentService.getOrders(request));
//        assertEquals("JWTToken is null in GetUsersRequest request - could not return orders", thrown.getMessage());
//    }
//
//    @Test
//    @DisplayName("When request object parameter - JWTToken - is invalid")
//    void UnitTest_testing_InvalidJWTToken_Parameter_RequestObject(){
//        GetOrdersRequest request = new GetOrdersRequest();
//
//        try {
//            Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> paymentService.getOrders(request));
//            assertEquals("Invalid JWTToken - could not get Orders", thrown.getMessage());
//        }catch (Exception e){
//            e.printStackTrace();
//            fail();
//        }
//    }
//
//    @Test
//    @Description("Tests for when JWT does not return admin userType")
//    @DisplayName("When request object parameter - JWT does not return admin userType")
//    void UnitTest_testing_JWTToken_Parameter_RequestObject_InvalidUserType(){
//        GetOrdersRequest request = new GetOrdersRequest();
//
//        try {
//            GetCurrentUserResponse getCurrentUserResponse = new GetCurrentUserResponse(shopper,true, Calendar.getInstance().getTime(),"User successfully returned");
//            GetOrdersResponse response = paymentService.getOrders(request);
//            assertFalse(response.isSuccess());
//            assertNull(response.getOrders());
//            assertEquals("JWTToken returns an invalid user type - could not get Orders", response.getMessage());
//        }catch (Exception e){
//            e.printStackTrace();
//            fail();
//        }
//    }

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
