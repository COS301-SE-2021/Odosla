package cs.superleague.user.integration;

import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.user.UserServiceImpl;
import cs.superleague.user.dataclass.Customer;
import cs.superleague.user.exceptions.InvalidRequestException;
import cs.superleague.user.exceptions.UserException;
import cs.superleague.user.repos.CustomerRepo;
import cs.superleague.user.repos.DriverRepo;
import cs.superleague.user.requests.FinalisePasswordResetRequest;
import cs.superleague.user.responses.FinalisePasswordResetResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
public class FinalisePasswordResetIntegrationTest {

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    DriverRepo driverRepo;

    @Autowired
    CustomerRepo customerRepo;

    @Autowired
    private UserServiceImpl userService;

    FinalisePasswordResetResponse response;
    FinalisePasswordResetRequest request;

    UUID userID = UUID.randomUUID();
    Customer customer;

    @BeforeEach
    void setup(){

        Date today = new Date();
        Date expiration = new Date(today.getTime() + (4 * 3600 * 1000));

        customer = new Customer();
        customer.setEmail("levy@smallFC.com");
        customer.setCustomerID(userID);
        customer.setResetExpiration(expiration.toString());
        customer.setResetCode("GcnDne4rFH");

        customerRepo.save(customer);

    }

    @AfterEach
    void teardown(){
        customerRepo.deleteAll();
    }

    @Test
    @DisplayName("When request object is not specified")
    void UnitTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.finalisePasswordReset(null));
        assertEquals("ResetPassword Request is null - Could not final password reset", thrown.getMessage());
    }

    @Test
    @DisplayName("When Email parameter is not specified")
    void UnitTest_testingNullEmailParameter(){
        request = new FinalisePasswordResetRequest(null, "CUSTOMER", "ola", "sho");
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.finalisePasswordReset(request));
        assertEquals("Email in request object is null - Could not finalise password reset", thrown.getMessage());
    }


    @Test
    @DisplayName("When user type parameter is not specified")
    void UnitTest_testingNullUserTypeParameter(){
        request = new FinalisePasswordResetRequest("null", null, "ola", "sho");
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.finalisePasswordReset(request));
        assertEquals("Account Type in request object is null - Could not finalise password reset", thrown.getMessage());
    }


    @Test
    @DisplayName("When reset code parameter is not specified")
    void UnitTest_testingNullResetCodeParameter(){
        request = new FinalisePasswordResetRequest("null", "null", null, "sho");
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.finalisePasswordReset(request));
        assertEquals("Reset code in request object is null - Could not finalise password reset", thrown.getMessage());
    }

    @Test
    @DisplayName("When password parameter is not specified")
    void UnitTest_testingNullPasswordParameter(){
        request = new FinalisePasswordResetRequest("null", "null", "null", null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.finalisePasswordReset(request));
        assertEquals("Password in request object is null - Could not finalise password reset", thrown.getMessage());
    }

    @Test
    @DisplayName("When Invalid Email parameter is given")
    void UnitTest_testingInvalidEmailParameter(){
        request = new FinalisePasswordResetRequest("null", "null", "null", "null");

        try{
            response = userService.finalisePasswordReset(request);
            assertFalse(response.isSuccess());
            assertEquals("Invalid email - Could not finalise password reset", response.getMessage());
        }catch(UserException e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("When Invalid Password parameter is given")
    void UnitTest_testingInvalidPasswordParameter(){
        request = new FinalisePasswordResetRequest("null@nully.com", "null", "null", "null");

        try{
            response = userService.finalisePasswordReset(request);
            assertFalse(response.isSuccess());
            assertEquals("invalid password - Could not finalise password reset", response.getMessage());
        }catch(UserException e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("When Email Not found")
    void UnitTest_testingEmailParameterNotFound(){

        request = new FinalisePasswordResetRequest("null@nully.com", "CUSTOMER", "null", "nulL^666");

        try{
            response = userService.finalisePasswordReset(request);
            Assertions.assertFalse(response.isSuccess());
            assertFalse(response.isSuccess());
            assertEquals("Could not find customer with email - Could not finalise password reset", response.getMessage());
        }catch(UserException e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("When the expiration time has passed")
    void UnitTest_testingWhenResetCodeHasExpired(){

        Date today = new Date();
        Date expiration = new Date(today.getTime() - (4 * 3600 * 1000));

        customer.setResetExpiration(expiration.toString());
        customerRepo.save(customer);
        request = new FinalisePasswordResetRequest("levy@smallFC.com", "CUSTOMER", "null", "nulL^666");


        try{
            response = userService.finalisePasswordReset(request);
            assertFalse(response.isSuccess());
            assertEquals("Reset code expired - Could not finalise password reset", response.getMessage());
        }catch(UserException e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("When the reset codes don't match")
    void UnitTest_testingWhenResetCodesDontMatch(){
        request = new FinalisePasswordResetRequest("levy@smallFC.com", "CUSTOMER", "null", "nulL^666");

        try{
            response = userService.finalisePasswordReset(request);
            assertFalse(response.isSuccess());
            assertEquals("Invalid Reset code given - Could not finalise password reset", response.getMessage());
        }catch(UserException e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("When the reset codes match")
    void UnitTest_testingWhenResetCodesMatch(){
        request = new FinalisePasswordResetRequest("levy@smallFC.com", "CUSTOMER", "GcnDne4rFH", "nulL^666");
        try{
            response = userService.finalisePasswordReset(request);
            assertTrue(response.isSuccess());
            assertEquals("Password reset successful", response.getMessage());
        }catch(UserException e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("When the invalid account type")
    void UnitTest_testingInvalidAccountType(){
        request = new FinalisePasswordResetRequest("levy@smallFC.com", "Student", "GcnDne4rFH", "nulL^666");

        try{
            response = userService.finalisePasswordReset(request);
            assertFalse(response.isSuccess());
            assertEquals("Invalid account type - could not finalise password reset", response.getMessage());
        }catch(UserException e){
            e.printStackTrace();
            fail();
        }
    }
}
