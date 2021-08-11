package cs.superleague.user;

import cs.superleague.integration.security.JwtUtil;
import cs.superleague.user.dataclass.*;
import cs.superleague.user.exceptions.InvalidRequestException;
import cs.superleague.user.repos.AdminRepo;
import cs.superleague.user.repos.CustomerRepo;
import cs.superleague.user.repos.DriverRepo;
import cs.superleague.user.repos.ShopperRepo;
import cs.superleague.user.requests.GetCurrentUserRequest;
import cs.superleague.user.responses.GetCurrentUserResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class GetCurrentUserUnitTest {

    @Mock
    CustomerRepo customerRepo;

    @Mock
    ShopperRepo shopperRepo;

    @Mock
    AdminRepo adminRepo;

    @Mock
    DriverRepo driverRepo;

    @InjectMocks
    JwtUtil jwtTokenUtil;

    @InjectMocks
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
    void tearDown() {}

    @Test
    @DisplayName("When request object is not specified")
    void UnitTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.getCurrentUser(null));
        assertEquals("GetCurrentUserRequest object is null", thrown.getMessage());
    }

    @Test
    @DisplayName("When JwtToken parameter is not specified")
    void UnitTest_NullRequestJwtTokenParameter(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.getCurrentUser(request));
        assertEquals("JWTToken in GetCurrentUserRequest is null", thrown.getMessage());
    }

    @Test
    @DisplayName("Checking request object is correctly created")
    void UnitTest_CorrectlyCreatedRequest(){
        GetCurrentUserRequest newRequest=new GetCurrentUserRequest("jwtToken");
        assertNotNull(newRequest);
        assertEquals("jwtToken",newRequest.getJWTToken());
    }

    @Test
    @DisplayName("Testing when could not retrieve User correctly-Customer")
    void UnitTest_CustomerNotRetrieved() throws InvalidRequestException {
        validRequest=new GetCurrentUserRequest(jwtTokenCustomer);
        Mockito.when(customerRepo.findCustomerByEmail(Mockito.any())).thenReturn(null);
        GetCurrentUserResponse response= userService.getCurrentUser(validRequest);

        assertNotNull(response);
        assertEquals(null,response.getUser());
        assertEquals(false,response.isSuccess());
        assertEquals("User could not be returned",response.getMessage());
        assertNotNull(response.getTimestamp());
    }

    @Test
    @DisplayName("Testing when could not retrieve User correctly-Shopper")
    void UnitTest_ShopperNotRetrieved() throws InvalidRequestException {
        validRequest=new GetCurrentUserRequest(jwtTokenShopper);
        Mockito.when(shopperRepo.findByEmail(Mockito.any())).thenReturn(null);
        GetCurrentUserResponse response= userService.getCurrentUser(validRequest);

        assertNotNull(response);
        assertEquals(null,response.getUser());
        assertEquals(false,response.isSuccess());
        assertEquals("User could not be returned",response.getMessage());
        assertNotNull(response.getTimestamp());
    }

    @Test
    @DisplayName("Testing when could not retrieve User correctly-Driver")
    void UnitTest_DriverNotRetrieved() throws InvalidRequestException {
        validRequest=new GetCurrentUserRequest(jwtTokenDriver);
        Mockito.when(driverRepo.findDriverByEmail(Mockito.any())).thenReturn(null);
        GetCurrentUserResponse response= userService.getCurrentUser(validRequest);

        assertNotNull(response);
        assertEquals(null,response.getUser());
        assertEquals(false,response.isSuccess());
        assertEquals("User could not be returned",response.getMessage());
        assertNotNull(response.getTimestamp());
    }

    @Test
    @DisplayName("Testing when could not retrieve User correctly-Admin")
    void UnitTest_AdminNotRetrieved() throws InvalidRequestException {
        validRequest=new GetCurrentUserRequest(jwtTokenAdmin);
        Mockito.when(adminRepo.findAdminByEmail(Mockito.any())).thenReturn(null);
        GetCurrentUserResponse response= userService.getCurrentUser(validRequest);

        assertNotNull(response);
        assertEquals(null,response.getUser());
        assertEquals(false,response.isSuccess());
        assertEquals("User could not be returned",response.getMessage());
        assertNotNull(response.getTimestamp());
    }

    @Test
    @DisplayName("Testing when User is retrieved correctly-Shopper")
    void UnitTest_ShopperRetrieved() throws InvalidRequestException {
        validRequest=new GetCurrentUserRequest(jwtTokenShopper);
        Mockito.when(shopperRepo.findByEmail(Mockito.any())).thenReturn(java.util.Optional.ofNullable(shopper));
        //Mockito.when(shopperRepo.findByEmail(Mockito.any())).thenReturn(java.util.Optional.of(shopper));
        GetCurrentUserResponse response= userService.getCurrentUser(validRequest);

        assertNotNull(response);
        assertEquals(shopper,response.getUser());
        assertEquals(true,response.isSuccess());
        assertEquals("User successfully returned",response.getMessage());
        assertNotNull(response.getTimestamp());
    }

    @Test
    @DisplayName("Testing when User is retrieved correctly-Driver")
    void UnitTest_DriverRetrieved() throws InvalidRequestException {
        validRequest=new GetCurrentUserRequest(jwtTokenDriver);
        Mockito.when(driverRepo.findDriverByEmail(Mockito.any())).thenReturn(driver);
        GetCurrentUserResponse response= userService.getCurrentUser(validRequest);

        assertNotNull(response);
        assertEquals(driver,response.getUser());
        assertEquals(true,response.isSuccess());
        assertEquals("User successfully returned",response.getMessage());
        assertNotNull(response.getTimestamp());
    }

    @Test
    @DisplayName("Testing when User is retrieved correctly-Admin")
    void UnitTest_AdminRetrieved() throws InvalidRequestException {
        validRequest=new GetCurrentUserRequest(jwtTokenAdmin);
        Mockito.when(adminRepo.findAdminByEmail(Mockito.any())).thenReturn(admin);
        GetCurrentUserResponse response= userService.getCurrentUser(validRequest);

        assertNotNull(response);
        assertEquals(admin,response.getUser());
        assertEquals(true,response.isSuccess());
        assertEquals("User successfully returned",response.getMessage());
        assertNotNull(response.getTimestamp());
    }

    @Test
    @DisplayName("Testing when User is retrieved correctly-Customer")
    void UnitTest_CustomerRetrieved() throws InvalidRequestException {
        validRequest=new GetCurrentUserRequest(jwtTokenCustomer);
        Mockito.when(customerRepo.findCustomerByEmail(Mockito.any())).thenReturn(customer);
        GetCurrentUserResponse response= userService.getCurrentUser(validRequest);

        assertNotNull(response);
        assertEquals(customer,response.getUser());
        assertEquals(true,response.isSuccess());
        assertEquals("User successfully returned",response.getMessage());
        assertNotNull(response.getTimestamp());
    }
}
