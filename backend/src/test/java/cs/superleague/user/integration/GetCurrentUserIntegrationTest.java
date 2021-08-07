package cs.superleague.user.integration;

import cs.superleague.integration.security.JwtUtil;
import cs.superleague.user.UserServiceImpl;
import cs.superleague.user.dataclass.*;
import cs.superleague.user.exceptions.InvalidRequestException;
import cs.superleague.user.repos.AdminRepo;
import cs.superleague.user.repos.CustomerRepo;
import cs.superleague.user.repos.DriverRepo;
import cs.superleague.user.repos.ShopperRepo;
import cs.superleague.user.requests.GetCurrentUserRequest;
import cs.superleague.user.responses.GetCurrentUserResponse;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class GetCurrentUserIntegrationTest {
    @Autowired
    ShopperRepo shopperRepo;

    @Autowired
    CustomerRepo customerRepo;

    @Autowired
    DriverRepo driverRepo;

    @Autowired
    AdminRepo adminRepo;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    private UserServiceImpl userService;


    GetCurrentUserRequest request;
    GetCurrentUserRequest validRequest;

    Admin admin=new Admin();
    Driver driver=new Driver();
    Customer customer=new Customer();
    Shopper shopper=new Shopper();

    UUID adminId= UUID.randomUUID();
    UUID driverId= UUID.randomUUID();
    UUID customerId= UUID.randomUUID();
    UUID shopperID= UUID.randomUUID();

    String adminEmail="adminEmail";
    String driverEmail="driverEmail";
    String customerEmail="customerEmail";
    String shopperEmail="shopperEmail";

    String jwtTokenAdmin;
    String jwtTokenCustomer;
    String jwtTokenDriver;
    String jwtTokenShopper;

    @BeforeEach
    void setUp(){
        request=new GetCurrentUserRequest(null);

        admin.setAdminID(adminId);
        admin.setEmail(adminEmail);
        admin.setAccountType(UserType.ADMIN);

        driver.setDriverID(driverId);
        driver.setEmail(driverEmail);
        driver.setAccountType(UserType.DRIVER);

        customer.setCustomerID(customerId);
        customer.setEmail(customerEmail);
        customer.setAccountType(UserType.CUSTOMER);

        shopper.setShopperID(shopperID);
        shopper.setEmail(shopperEmail);
        shopper.setAccountType(UserType.SHOPPER);

        jwtTokenAdmin=jwtTokenUtil.generateJWTTokenAdmin(admin);
        jwtTokenCustomer=jwtTokenUtil.generateJWTTokenCustomer(customer);
        jwtTokenDriver=jwtTokenUtil.generateJWTTokenDriver(driver);
        jwtTokenShopper=jwtTokenUtil.generateJWTTokenShopper(shopper);

    }

    @AfterEach
    void tearDown() {
        adminRepo.deleteAll();
        driverRepo.deleteAll();
        customerRepo.deleteAll();
        shopperRepo.deleteAll();
    }

    @Test
    @DisplayName("When request object is not specified")
    void IntegrationTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.getCurrentUser(null));
        assertEquals("GetCurrentUserRequest object is null", thrown.getMessage());
    }

    @Test
    @DisplayName("When JwtToken parameter is not specified")
    void IntegrationTest_NullRequestJwtTokenParameter(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.getCurrentUser(request));
        assertEquals("JWTToken in GetCurrentUserRequest is null", thrown.getMessage());
    }

    @Test
    @DisplayName("Checking request object is correctly created")
    void IntegrationTest_CorrectlyCreatedRequest(){
        GetCurrentUserRequest newRequest=new GetCurrentUserRequest("jwtToken");
        assertNotNull(newRequest);
        assertEquals("jwtToken",newRequest.getJWTToken());
    }

    @Test
    @DisplayName("Testing when could not retrieve User correctly-Customer")
    void IntegrationTest_CustomerNotRetrieved() throws InvalidRequestException {
        validRequest=new GetCurrentUserRequest(jwtTokenCustomer);
        GetCurrentUserResponse response= userService.getCurrentUser(validRequest);

        assertNotNull(response);
        assertEquals(null,response.getUser());
        assertEquals(false,response.isSuccess());
        assertEquals("User could not be returned",response.getMessage());
        assertNotNull(response.getTimestamp());
    }

    @Test
    @DisplayName("Testing when could not retrieve User correctly-Shopper")
    void IntegrationTest_ShopperNotRetrieved() throws InvalidRequestException {
        validRequest=new GetCurrentUserRequest(jwtTokenShopper);
        GetCurrentUserResponse response= userService.getCurrentUser(validRequest);

        assertNotNull(response);
        assertEquals(null,response.getUser());
        assertEquals(false,response.isSuccess());
        assertEquals("User could not be returned",response.getMessage());
        assertNotNull(response.getTimestamp());
    }

    @Test
    @DisplayName("Testing when could not retrieve User correctly-Driver")
    void IntegrationTest_DriverNotRetrieved() throws InvalidRequestException {
        validRequest=new GetCurrentUserRequest(jwtTokenDriver);
        GetCurrentUserResponse response= userService.getCurrentUser(validRequest);

        assertNotNull(response);
        assertEquals(null,response.getUser());
        assertEquals(false,response.isSuccess());
        assertEquals("User could not be returned",response.getMessage());
        assertNotNull(response.getTimestamp());
    }

    @Test
    @DisplayName("Testing when could not retrieve User correctly-Admin")
    void IntegrationTest_AdminNotRetrieved() throws InvalidRequestException {
        validRequest=new GetCurrentUserRequest(jwtTokenAdmin);
        GetCurrentUserResponse response= userService.getCurrentUser(validRequest);

        assertNotNull(response);
        assertEquals(null,response.getUser());
        assertEquals(false,response.isSuccess());
        assertEquals("User could not be returned",response.getMessage());
        assertNotNull(response.getTimestamp());
    }

    @Test
    @DisplayName("Testing when User is retrieved correctly-Shopper")
    void IntegrationTest_ShopperRetrieved() throws InvalidRequestException {
        validRequest=new GetCurrentUserRequest(jwtTokenShopper);
        shopperRepo.save(shopper);
        GetCurrentUserResponse response= userService.getCurrentUser(validRequest);

        assertNotNull(response);
        assertEquals(shopper.getEmail(),response.getUser().getEmail());
        assertEquals(shopper.getAccountType(),response.getUser().getAccountType());
        assertEquals(true,response.isSuccess());
        assertEquals("User successfully returned",response.getMessage());
        assertNotNull(response.getTimestamp());
    }

    @Test
    @DisplayName("Testing when User is retrieved correctly-Driver")
    void IntegrationTest_DriverRetrieved() throws InvalidRequestException {
        validRequest=new GetCurrentUserRequest(jwtTokenDriver);
        driverRepo.save(driver);
        GetCurrentUserResponse response= userService.getCurrentUser(validRequest);

        assertNotNull(response);
        assertEquals(driver.getEmail(),response.getUser().getEmail());
        assertEquals(driver.getAccountType(),response.getUser().getAccountType());
        assertEquals(true,response.isSuccess());
        assertEquals("User successfully returned",response.getMessage());
        assertNotNull(response.getTimestamp());
    }

    @Test
    @DisplayName("Testing when User is retrieved correctly-Admin")
    void IntegrationTest_AdminRetrieved() throws InvalidRequestException {
        validRequest=new GetCurrentUserRequest(jwtTokenAdmin);
        adminRepo.save(admin);
        GetCurrentUserResponse response= userService.getCurrentUser(validRequest);

        assertNotNull(response);
        assertEquals(admin.getEmail(),response.getUser().getEmail());
        assertEquals(admin.getAccountType(),response.getUser().getAccountType());
        assertEquals(true,response.isSuccess());
        assertEquals("User successfully returned",response.getMessage());
        assertNotNull(response.getTimestamp());
    }

    @Test
    @DisplayName("Testing when User is retrieved correctly-Customer")
    void IntegrationTest_CustomerRetrieved() throws InvalidRequestException {
        validRequest=new GetCurrentUserRequest(jwtTokenCustomer);
        customerRepo.save(customer);
        GetCurrentUserResponse response= userService.getCurrentUser(validRequest);

        assertNotNull(response);
        assertEquals(customer.getEmail(),response.getUser().getEmail());
        assertEquals(customer.getAccountType(),response.getUser().getAccountType());
        assertEquals(true,response.isSuccess());
        assertEquals("User successfully returned",response.getMessage());
        assertNotNull(response.getTimestamp());
    }
}
