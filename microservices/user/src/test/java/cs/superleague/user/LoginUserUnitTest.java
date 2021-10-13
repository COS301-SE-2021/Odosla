package cs.superleague.user;

import cs.superleague.integration.security.JwtUtil;
import cs.superleague.user.dataclass.*;
import cs.superleague.user.exceptions.*;
import cs.superleague.user.repos.AdminRepo;
import cs.superleague.user.repos.CustomerRepo;
import cs.superleague.user.repos.DriverRepo;
import cs.superleague.user.repos.ShopperRepo;
import cs.superleague.user.requests.LoginRequest;
import cs.superleague.user.responses.LoginResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Description;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoginUserUnitTest {
    @Mock
    ShopperRepo shopperRepo;
    @Mock
    DriverRepo driverRepo;
    @Mock
    CustomerRepo customerRepo;
    @Mock
    AdminRepo adminRepo;
    @Mock
    JwtUtil jwtUtil;

    @InjectMocks
    private UserServiceImpl userService;

    LoginRequest loginRequest1;
    UUID userID=UUID.randomUUID();
    Shopper shopperToLogin;
    Driver driverToLogin;
    Admin adminToLogin;
    Customer customerToLogin;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(15);
    String passwordHashed=passwordEncoder.encode("pass");


    @BeforeEach
    void setup(){
        loginRequest1=new LoginRequest("erhfeurf","difier", UserType.SHOPPER);
        shopperToLogin=new Shopper("Kelly","Morrison","hi@gmail","09273673","cats","abc",UserType.SHOPPER,userID);
        driverToLogin=new Driver("Kelly","Morrison","hi@gmail","09273673","cats","abc",UserType.DRIVER,userID);
        adminToLogin=new Admin("Kelly","Morrison","hi@gmail","09273673","cats","abc",UserType.ADMIN,userID);
        customerToLogin=new Customer("Kelly","Morrison","hi@gmail","09273673","cats","abc",UserType.CUSTOMER,userID);

    }

    @AfterEach
    void teardown(){
        shopperRepo.deleteAll();
        customerRepo.deleteAll();
        adminRepo.deleteAll();
        driverRepo.deleteAll();
    }

    @Test
    @Description("Tests for when login is submited with a null request object- exception should be thrown")
    @DisplayName("When request object is not specificed")
    void UnitTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.loginUser(null));
        assertEquals("LoginRequest is null", thrown.getMessage());
    }

    @Test
    @Description("Tests for when login is submited with a null parameter for email in request object- exception should be thrown")
    @DisplayName("When request parameter object is not specificed")
    void UnitTest_testingEmailParameterNullRequestObject(){
       loginRequest1.setEmail(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.loginUser(loginRequest1));
        assertEquals("Email in LoginRequest is null", thrown.getMessage());

    }

    @Test
    @Description("Tests for when login is submited with a null parameter for password in request object- exception should be thrown")
    @DisplayName("When request parameter object is not specificed")
    void UnitTest_testingPasswordParameterNullRequestObject(){
        loginRequest1.setPassword(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.loginUser(loginRequest1));
        assertEquals("Password in LoginRequest is null", thrown.getMessage());

    }
    @Test
    @Description("Tests for when login is submited with a null parameter for userType in request object- exception should be thrown")
    @DisplayName("When request parameter object is not specificed")
    void UnitTest_testingUserTypeParameterNullRequestObject(){
        loginRequest1.setUserType(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.loginUser(loginRequest1));
        assertEquals("UserType in LoginRequest is null", thrown.getMessage());

    }

    @Test
    @Description("Tests for whether request object was created properly")
    @DisplayName("Request correctly created")
    void UnitTest_RequestCreatedCorrectly(){
        LoginRequest loginRequest=new LoginRequest("hi","as",UserType.SHOPPER);
        assertNotNull(loginRequest);
        assertEquals("hi",loginRequest.getEmail());
        assertEquals("as",loginRequest.getPassword());
        assertEquals(UserType.SHOPPER,loginRequest.getUserType());
    }

    @Test
    @Description("Test for driver, if driver does not exist")
    @DisplayName("DriverDoesNotExistException for login")
    void UnitTest_DriverDoesNotExist(){
        loginRequest1.setUserType(UserType.DRIVER);
        Throwable thrown = Assertions.assertThrows(DriverDoesNotExistException.class, ()-> userService.loginUser(loginRequest1));
        assertEquals("Driver does not exist", thrown.getMessage());
    }

    @Test
    @Description("Test for shopper, if shopper does not exist")
    @DisplayName("ShopperDoesNotExistException for login")
    void UnitTest_ShopperDoesNotExist(){
        Throwable thrown = Assertions.assertThrows(ShopperDoesNotExistException.class, ()-> userService.loginUser(loginRequest1));
        assertEquals("Shopper does not exist", thrown.getMessage());
    }

    @Test
    @Description("Test for customer, if customer does not exist")
    @DisplayName("CustomerDoesNotExistException for login")
    void UnitTest_CustomerDoesNotExist(){
        loginRequest1.setUserType(UserType.CUSTOMER);
        Throwable thrown = Assertions.assertThrows(CustomerDoesNotExistException.class, ()-> userService.loginUser(loginRequest1));
        assertEquals("Customer does not exist", thrown.getMessage());
    }

    @Test
    @Description("Test for admin, if admin does not exist")
    @DisplayName("AdminDoesNotExistException for login")
    void UnitTest_AdminDoesNotExist(){
        loginRequest1.setUserType(UserType.ADMIN);
        Throwable thrown = Assertions.assertThrows(AdminDoesNotExistException.class, ()-> userService.loginUser(loginRequest1));
        assertEquals("Admin does not exist", thrown.getMessage());
    }

    @Test
    @Description("Test for driver, if driver exists but password is wrong")
    @DisplayName("InvalidCredentialsException for driver login")
    void UnitTest_DriverInvalidCredentials(){

        loginRequest1.setUserType(UserType.DRIVER);
        loginRequest1.setEmail("hi@gmail");
        driverToLogin.setPassword(passwordHashed);
        Mockito.when(driverRepo.findDriverByEmail(Mockito.any())).thenReturn(driverToLogin);
        Throwable thrown = Assertions.assertThrows(InvalidCredentialsException.class, ()-> userService.loginUser(loginRequest1));
        assertEquals("Password is incorrect", thrown.getMessage());
    }

    @Test
    @Description("Test for shopper, if shopper exists but password is wrong")
    @DisplayName("InvalidCredentialsException for shopper login")
    void UnitTest_ShopperInvalidCredentials(){
        loginRequest1.setUserType(UserType.SHOPPER);
        loginRequest1.setEmail("hi@gmail");
        shopperToLogin.setPassword(passwordHashed);
        Mockito.when(shopperRepo.findByEmail(Mockito.any())).thenReturn(java.util.Optional.ofNullable(shopperToLogin));
        Throwable thrown = Assertions.assertThrows(InvalidCredentialsException.class, ()-> userService.loginUser(loginRequest1));
        assertEquals("Password is incorrect", thrown.getMessage());
    }

    @Test
    @Description("Test for admin, if admin exists but password is wrong")
    @DisplayName("InvalidCredentialsException for admin login")
    void UnitTest_AdminInvalidCredentials(){
        loginRequest1.setUserType(UserType.ADMIN);
        loginRequest1.setEmail("hi@gmail");
        adminToLogin.setPassword(passwordHashed);
        Mockito.when(adminRepo.findAdminByEmail(Mockito.any())).thenReturn(adminToLogin);
        Throwable thrown = Assertions.assertThrows(InvalidCredentialsException.class, ()-> userService.loginUser(loginRequest1));
        assertEquals("Password is incorrect", thrown.getMessage());
    }

    @Test
    @Description("Test for customer, if customer exists but password is wrong")
    @DisplayName("InvalidCredentialsException for customer login")
    void UnitTest_CustomerInvalidCredentials(){
        loginRequest1.setUserType(UserType.CUSTOMER);
        loginRequest1.setEmail("hi@gmail");
        customerToLogin.setPassword(passwordHashed);
        //Mockito.when(customerRepo.findByEmail(Mockito.any())).thenReturn(customerToLogin);
        when(customerRepo.findByEmail(Mockito.any())).thenReturn(Optional.ofNullable(customerToLogin));
        Throwable thrown = Assertions.assertThrows(InvalidCredentialsException.class, ()-> userService.loginUser(loginRequest1));
        assertEquals("Password is incorrect", thrown.getMessage());
    }

    @Test
    @Description("Test for customer, if customer exists and is logged in correctly")
    @DisplayName("Customer logged in correctly")
    void UnitTest_CustomerLoggedInCorrectly() throws UserException {
        loginRequest1.setUserType(UserType.CUSTOMER);
        loginRequest1.setEmail("hi@gmail");
        loginRequest1.setPassword("pass");
        customerToLogin.setPassword(passwordHashed);
        customerToLogin.setActivationDate(Calendar.getInstance().getTime());
        //Mockito.when(customerRepo.findByEmail(Mockito.any())).thenReturn(customerToLogin);
        when(customerRepo.findByEmail(Mockito.any())).thenReturn(Optional.ofNullable(customerToLogin));
        LoginResponse response=userService.loginUser(loginRequest1);
        assertEquals(true, response.isSuccess());
        assertNotNull(response.getTimestamp());
        assertEquals("User successfully logged in",response.getMessage());
    }

    @Test
    @Description("Test for admin, if admin exists and is logged in correctly")
    @DisplayName("Admin logged in correctly")
    void UnitTest_AdminLoggedInCorrectly() throws UserException {
        loginRequest1.setUserType(UserType.ADMIN);
        loginRequest1.setEmail("hi@gmail");
        loginRequest1.setPassword("pass");
        adminToLogin.setPassword(passwordHashed);
        Mockito.when(adminRepo.findAdminByEmail(Mockito.any())).thenReturn(adminToLogin);
        LoginResponse response=userService.loginUser(loginRequest1);
        assertEquals(true, response.isSuccess());
        assertNotNull(response.getTimestamp());
        assertEquals("User successfully logged in",response.getMessage());


    }

    @Test
    @Description("Test for shopper, if shopper exists and is logged in correctly")
    @DisplayName("Shopper logged in correctly")
    void UnitTest_ShopperLoggedInCorrectly() throws UserException {
        loginRequest1.setUserType(UserType.SHOPPER);
        loginRequest1.setEmail("hi@gmail");
        loginRequest1.setPassword("pass");
        shopperToLogin.setPassword(passwordHashed);
        shopperToLogin.setActivationDate(Calendar.getInstance().getTime());
        Mockito.when(shopperRepo.findByEmail(Mockito.any())).thenReturn(java.util.Optional.ofNullable(shopperToLogin));
        LoginResponse response=userService.loginUser(loginRequest1);
        assertEquals(true, response.isSuccess());
        assertNotNull(response.getTimestamp());
        assertEquals("User successfully logged in",response.getMessage());


    }

    @Test
    @Description("Test for driver, if driver exists and is logged in correctly")
    @DisplayName("Driver logged in correctly")
    void UnitTest_DriverLoggedInCorrectly() throws UserException {
        loginRequest1.setUserType(UserType.DRIVER);
        loginRequest1.setEmail("hi@gmail");
        loginRequest1.setPassword("pass");
        driverToLogin.setPassword(passwordHashed);
        driverToLogin.setActivationDate(Calendar.getInstance().getTime());
        Mockito.when(driverRepo.findDriverByEmail(Mockito.any())).thenReturn(driverToLogin);
        LoginResponse response=userService.loginUser(loginRequest1);
        assertEquals(true, response.isSuccess());
        assertNotNull(response.getTimestamp());
        assertEquals("User successfully logged in",response.getMessage());


    }


    @Test
    @Description("Test for shopper, if shopper exists but Not Verified")
    @DisplayName("Shopper Not verified")
    void UnitTest_ShopperNotVerified() throws UserException {
        loginRequest1.setUserType(UserType.SHOPPER);
        loginRequest1.setEmail("hi@gmail");
        loginRequest1.setPassword("pass");
        shopperToLogin.setPassword(passwordHashed);
        shopperToLogin.setActivationDate(null);
        Mockito.when(shopperRepo.findByEmail(Mockito.any())).thenReturn(java.util.Optional.ofNullable(shopperToLogin));
        LoginResponse response=userService.loginUser(loginRequest1);
        assertNull(response.getToken());
        assertEquals(false, response.isSuccess());
        assertNotNull(response.getTimestamp());
        assertEquals("Please verify account before logging in",response.getMessage());


    }

    @Test
    @Description("Test for driver, if driver credentials are right but not verified")
    @DisplayName("Driver not verified")
    void UnitTest_DriverNotVerified() throws UserException {
        loginRequest1.setUserType(UserType.DRIVER);
        loginRequest1.setEmail("hi@gmail");
        loginRequest1.setPassword("pass");
        driverToLogin.setPassword(passwordHashed);
        driverToLogin.setActivationDate(null);
        Mockito.when(driverRepo.findDriverByEmail(Mockito.any())).thenReturn(driverToLogin);
        LoginResponse response=userService.loginUser(loginRequest1);
        assertNull(response.getToken());
        assertEquals(false, response.isSuccess());
        assertNotNull(response.getTimestamp());
        assertEquals("Please verify account before logging in",response.getMessage());


    }

    @Test
    @Description("Test for customer, if customer exists but not verified")
    @DisplayName("Customer not verified")
    void UnitTest_CustomerNotVerified() throws UserException {
        loginRequest1.setUserType(UserType.CUSTOMER);
        loginRequest1.setEmail("hi@gmail");
        loginRequest1.setPassword("pass");
        customerToLogin.setPassword(passwordHashed);
        customerToLogin.setActivationDate(null);
        //Mockito.when(customerRepo.findByEmail(Mockito.any())).thenReturn(customerToLogin);
        when(customerRepo.findByEmail(Mockito.any())).thenReturn(Optional.ofNullable(customerToLogin));
        LoginResponse response=userService.loginUser(loginRequest1);
        assertNull(response.getToken());
        assertEquals(false, response.isSuccess());
        assertNotNull(response.getTimestamp());
        assertEquals("Please verify account before logging in",response.getMessage());
    }








}
