package cs.superleague.user;

import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.dataclass.OrderStatus;
import cs.superleague.payment.exceptions.OrderDoesNotExist;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.user.dataclass.Customer;
import cs.superleague.user.exceptions.InvalidRequestException;
import cs.superleague.user.exceptions.UserException;
import cs.superleague.user.repos.CustomerRepo;
import cs.superleague.user.repos.DriverRepo;
import cs.superleague.user.requests.CollectOrderRequest;
import cs.superleague.user.requests.ResetPasswordRequest;
import cs.superleague.user.responses.CollectOrderResponse;
import cs.superleague.user.responses.ResetPasswordResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ResetPasswordUnitTest {

    @Mock
    OrderRepo orderRepo;

    @Mock
    DriverRepo driverRepo;

    @Mock
    CustomerRepo customerRepo;

    @InjectMocks
    private UserServiceImpl userService;

    ResetPasswordResponse response;
    ResetPasswordRequest request;

    UUID userID = UUID.randomUUID();
    Customer customer;

    @BeforeEach
    void setup(){

        customer = new Customer();
        customer.setEmail("levy@smallFC.com");
        customer.setCustomerID(userID);

    }

    @AfterEach
    void teardown(){}

    @Test
    @DisplayName("When request object is not specified")
    void UnitTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.resetPassword(null));
        assertEquals("ResetPassword Request is null - Could not reset password", thrown.getMessage());
    }

    @Test
    @DisplayName("When Email parameter is not specified")
    void UnitTest_testingNullEmailParameter(){
        request = new ResetPasswordRequest(null, "CUSTOMER");
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.resetPassword(request));
        assertEquals("Email in request object is null - Could not reset password", thrown.getMessage());
    }

    @Test
    @DisplayName("When AccountType parameter is not specified")
    void UnitTest_testingNullAccountTypeParameter(){
        request = new ResetPasswordRequest("lol", null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.resetPassword(request));
        assertEquals("Account Type in request object is null - Could not reset password", thrown.getMessage());
    }

    @Test
    @DisplayName("When Invalid Email parameter is not specified")
    void UnitTest_testingInvalidEmailParameter(){
        request = new ResetPasswordRequest("lol", "CUSTOMER");

        try{
            response = userService.resetPassword(request);
            assertFalse(response.isSuccess());
            assertEquals(null, response.getResetCode());
            assertEquals("Invalid email - Could not reset", response.getMessage());
        }catch(UserException e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("When Email Not found")
    void UnitTest_testingEmailParameterNotFound(){
        request = new ResetPasswordRequest("levy@smallFC.com", "CUSTOMER");
        when(customerRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(null));


        try{
            response = userService.resetPassword(request);
            Assertions.assertFalse(response.isSuccess());
            assertEquals(null, response.getResetCode());
            assertEquals("Could not find customer with email - Could not reset", response.getMessage());
        }catch(UserException e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("When user type parameter is invalid")
    void UnitTest_testingInvalidAccountTypeParameter(){
        request = new ResetPasswordRequest("levy@smallFC.com", "STUDENT");

        try{
            response = userService.resetPassword(request);
            Assertions.assertFalse(response.isSuccess());
            Assertions.assertNotNull(response.getResetCode());
            assertEquals("Account type given does not exist - Could not reset password", response.getMessage());
        }catch(UserException e){
            e.printStackTrace();
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("When Email found")
    void UnitTest_testingEmailParameterFound(){
        request = new ResetPasswordRequest("levy@smallFC.com", "CUSTOMER");
        when(customerRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(customer));

        try{
            response = userService.resetPassword(request);
            Assertions.assertTrue(response.isSuccess());
            assertNotNull(response.getResetCode());
            assertNotNull(response.getMessage());
        }catch(UserException e){
            e.printStackTrace();
            Assertions.fail();
        }
    }
}
