package cs.superleague.user.integration;

import cs.superleague.user.UserServiceImpl;
import cs.superleague.user.dataclass.Customer;
import cs.superleague.user.dataclass.Driver;
import cs.superleague.user.dataclass.Shopper;
import cs.superleague.user.dataclass.UserType;
import cs.superleague.user.exceptions.CustomerDoesNotExistException;
import cs.superleague.user.exceptions.DriverDoesNotExistException;
import cs.superleague.user.exceptions.InvalidRequestException;
import cs.superleague.user.exceptions.ShopperDoesNotExistException;
import cs.superleague.user.repos.AdminRepo;
import cs.superleague.user.repos.CustomerRepo;
import cs.superleague.user.repos.DriverRepo;
import cs.superleague.user.repos.ShopperRepo;
import cs.superleague.user.requests.AccountVerifyRequest;
import cs.superleague.user.responses.AccountVerifyResponse;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.UUID;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class VerifyAccountIntegrationTest {


    @Autowired
    CustomerRepo customerRepo;

    @Autowired
    ShopperRepo shopperRepo;

    @Autowired
    DriverRepo driverRepo;

    @Autowired
    AdminRepo adminRepo;

    @Autowired
    private UserServiceImpl userService;

    AccountVerifyRequest request;

    @BeforeEach
    void setUp(){
        request = new AccountVerifyRequest("a@gmail.com","activationCode", UserType.SHOPPER);
    }

    @AfterEach
    void tearDown(){
        customerRepo.deleteAll();
        shopperRepo.deleteAll();
        driverRepo.deleteAll();
    }

    @Test
    @DisplayName("When request object is not specified")
    void UnitTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.verifyAccount(null));
        assertEquals("AccountVerifyRequest can't be null", thrown.getMessage());
    }

    @Test
    @DisplayName("When request email is not specified")
    void UnitTest_testingNullEmailParameterRequestObject(){
        request.setEmail(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.verifyAccount(request));
        assertEquals("Email can't be null in AccountVerifyRequest", thrown.getMessage());
    }
    @Test
    @DisplayName("When request activationCode is not specified")
    void UnitTest_testingNullActivationCodeParameterRequestObject(){
        request.setActivationCode(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.verifyAccount(request));
        assertEquals("ActivationCode can't be null in AccountVerifyRequest", thrown.getMessage());
    }
    @Test
    @DisplayName("When request userType is not specified")
    void UnitTest_testingNullUserTypeParameterRequestObject(){
        request.setUserType(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.verifyAccount(request));
        assertEquals("UserType can't be null in AccountVerifyRequest", thrown.getMessage());
    }

    @Test
    @DisplayName("When Shopper does not exist")
    void UnitTest_testingShopperDoesNotExist(){
        Throwable thrown = Assertions.assertThrows(ShopperDoesNotExistException.class, ()-> userService.verifyAccount(request));
        System.out.println(thrown);
        assertEquals("Shopper Does Not Exist in database", thrown.getMessage());
    }

    @Test
    @DisplayName("When Driver does not exist")
    void UnitTest_testingDriverDoesNotExist(){
        request.setUserType(UserType.DRIVER);
        Throwable thrown = Assertions.assertThrows(DriverDoesNotExistException.class, ()-> userService.verifyAccount(request));
        System.out.println(thrown);
        assertEquals("Driver does not exist in database", thrown.getMessage());
    }

    @Test
    @DisplayName("When Customer does not exist")
    void UnitTest_testingCustomerDoesNotExist(){
        request.setUserType(UserType.CUSTOMER);
        Throwable thrown = Assertions.assertThrows(CustomerDoesNotExistException.class, ()-> userService.verifyAccount(request));
        System.out.println(thrown);
        assertEquals("Customer does not exist in database", thrown.getMessage());
    }

    @Test
    @DisplayName("When Customer has already activated account")
    void UnitTest_testingCustomerAlreadyActivated() throws Exception {
        request.setUserType(UserType.CUSTOMER);
        Customer customer=new Customer();
        customer.setEmail(request.getEmail());
        customer.setCustomerID(UUID.randomUUID());
        customer.setActivationDate(Calendar.getInstance().getTime());
        customerRepo.save(customer);
        AccountVerifyResponse response= userService.verifyAccount(request);

        assertNotNull(response);
        Assertions.assertNotNull(response.getTimestamp());
        assertEquals(false,response.isSuccess());
        assertEquals(UserType.CUSTOMER,response.getUserType());
        assertEquals("Customer with email '"+request.getEmail()+"' has already activated their Customer account",response.getMessage());

    }
    @Test
    @DisplayName("When Driver has already activated account")
    void UnitTest_testingDriverAlreadyActivated() throws Exception {
        request.setUserType(UserType.DRIVER);
        Driver driver=new Driver();
        driver.setEmail(request.getEmail());
        driver.setDriverID(UUID.randomUUID());
        driver.setActivationDate(Calendar.getInstance().getTime());
        driverRepo.save(driver);
        AccountVerifyResponse response= userService.verifyAccount(request);

        assertNotNull(response);
        Assertions.assertNotNull(response.getTimestamp());
        assertEquals(false,response.isSuccess());
        assertEquals(UserType.DRIVER,response.getUserType());
        assertEquals("Driver with email '"+request.getEmail()+"' has already activated their Driver account",response.getMessage());

    }

    @Test
    @DisplayName("When Shopper has already activated account")
    void UnitTest_testingShopperAlreadyActivated() throws Exception {
        request.setUserType(UserType.SHOPPER);
        Shopper shopper=new Shopper();
        shopper.setEmail(request.getEmail());
        shopper.setShopperID(UUID.randomUUID());
        shopper.setActivationDate(Calendar.getInstance().getTime());
        shopperRepo.save(shopper);
        AccountVerifyResponse response= userService.verifyAccount(request);

        assertNotNull(response);
        Assertions.assertNotNull(response.getTimestamp());
        assertEquals(false,response.isSuccess());
        assertEquals(UserType.SHOPPER,response.getUserType());
        assertEquals("Shopper with email '"+request.getEmail()+"' has already activated their Shopper account",response.getMessage());

    }

    @Test
    @DisplayName("When Shopper activation code doesn't match")
    void UnitTest_testingShopperInvalidActivationCode() throws Exception {
        request.setUserType(UserType.SHOPPER);
        Shopper shopper=new Shopper();
        shopper.setEmail(request.getEmail());
        shopper.setShopperID(UUID.randomUUID());
        shopper.setActivationCode("wrong");
        shopperRepo.save(shopper);
        AccountVerifyResponse response= userService.verifyAccount(request);

        assertNotNull(response);
        Assertions.assertNotNull(response.getTimestamp());
        assertEquals(false,response.isSuccess());
        assertEquals(UserType.SHOPPER,response.getUserType());
        assertEquals("Activation code was incorrect",response.getMessage());

    }

    @Test
    @DisplayName("When Driver activation code doesn't match")
    void UnitTest_testingDriverInvalidActivationCode() throws Exception {
        request.setUserType(UserType.DRIVER);
        Driver driver=new Driver();
        driver.setEmail(request.getEmail());
        driver.setActivationCode("wrong");
        driver.setDriverID(UUID.randomUUID());
        driverRepo.save(driver);
        AccountVerifyResponse response= userService.verifyAccount(request);

        assertNotNull(response);
        Assertions.assertNotNull(response.getTimestamp());
        assertEquals(false,response.isSuccess());
        assertEquals(UserType.DRIVER,response.getUserType());
        assertEquals("Activation code was incorrect",response.getMessage());

    }

    @Test
    @DisplayName("When Customer activation code doesn't match")
    void UnitTest_testingCustomerInvalidActivationCode() throws Exception {
        request.setUserType(UserType.CUSTOMER);
        Customer customer=new Customer();
        customer.setEmail(request.getEmail());
        customer.setCustomerID(UUID.randomUUID());
        customer.setActivationCode("wrong");
        customerRepo.save(customer);
        AccountVerifyResponse response= userService.verifyAccount(request);

        assertNotNull(response);
        Assertions.assertNotNull(response.getTimestamp());
        assertEquals(false,response.isSuccess());
        assertEquals(UserType.CUSTOMER,response.getUserType());
        assertEquals("Activation code was incorrect",response.getMessage());

    }

    @Test
    @DisplayName("When Shopper activation code does match")
    void UnitTest_testingShopperValidActivationCode() throws Exception {
        request.setUserType(UserType.SHOPPER);
        Shopper shopper=new Shopper();
        shopper.setEmail(request.getEmail());
        shopper.setShopperID(UUID.randomUUID());
        shopper.setActivationCode(request.getActivationCode());
        shopperRepo.save(shopper);
        AccountVerifyResponse response= userService.verifyAccount(request);

        assertNotNull(response);
        Assertions.assertNotNull(response.getTimestamp());
        assertEquals(true,response.isSuccess());
        assertEquals(UserType.SHOPPER,response.getUserType());
        assertEquals("Shopper with email '"+request.getEmail()+"' has successfully activated their Shopper account",response.getMessage());

    }

    @Test
    @DisplayName("When Driver activation code does match")
    void UnitTest_testingDriverValidActivationCode() throws Exception {
        request.setUserType(UserType.DRIVER);
        Driver driver=new Driver();
        driver.setEmail(request.getEmail());
        driver.setDriverID(UUID.randomUUID());
        driver.setActivationCode(request.getActivationCode());
        driverRepo.save(driver);
        AccountVerifyResponse response= userService.verifyAccount(request);

        assertNotNull(response);
        Assertions.assertNotNull(response.getTimestamp());
        assertEquals(true,response.isSuccess());
        assertEquals(UserType.DRIVER,response.getUserType());
        assertEquals("Driver with email '"+request.getEmail()+"' has successfully activated their Driver account",response.getMessage());

    }

    @Test
    @DisplayName("When Customer activation code does match")
    void UnitTest_testingCustomerValidActivationCode() throws Exception {
        request.setUserType(UserType.CUSTOMER);
        Customer customer=new Customer();
        customer.setEmail(request.getEmail());
        customer.setActivationCode(request.getActivationCode());
        customer.setCustomerID(UUID.randomUUID());
        customerRepo.save(customer);
        AccountVerifyResponse response= userService.verifyAccount(request);

        assertNotNull(response);
        Assertions.assertNotNull(response.getTimestamp());
        assertEquals(true,response.isSuccess());
        assertEquals(UserType.CUSTOMER,response.getUserType());
        assertEquals("Customer with email '"+request.getEmail()+"' has successfully activated their Customer account",response.getMessage());

    }

}
