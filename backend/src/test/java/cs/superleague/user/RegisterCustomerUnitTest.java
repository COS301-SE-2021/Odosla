package cs.superleague.user;

import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.user.dataclass.Customer;
import cs.superleague.user.dataclass.UserType;
import cs.superleague.user.exceptions.AlreadyExistsException;
import cs.superleague.user.exceptions.InvalidRequestException;
import cs.superleague.user.repos.CustomerRepo;
import cs.superleague.user.requests.RegisterCustomerRequest;
import cs.superleague.user.responses.RegisterCustomerResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/** Testing use cases with JUnit testing and Mockito */
@ExtendWith(MockitoExtension.class)
public class RegisterCustomerUnitTest {

    @Mock
    CustomerRepo customerRepo;

    @InjectMocks
    private UserServiceImpl userService;

    Customer customer;

    UUID userID;

    GeoPoint deliveryAddress;

    List<Customer> customers = new ArrayList<>();

    RegisterCustomerRequest request;

    Calendar today;
    @BeforeEach
    void setUp() {
        today = Calendar.getInstance();
        today.add(Calendar.DATE, 7);
        String expirationDate = new SimpleDateFormat("yyyy-MM-dd").format(today.getTime());

        userID = UUID.randomUUID();

        deliveryAddress = new GeoPoint(2.0, 2.0, "2616 Urban Quarters, Hatfield");

       /* customer = new Customer("D", "S", "DS77", userID, "ds@smallClubUnited.com", "0721234567", "ewtryuj57iuhf",
                today, UUID.randomUUID().toString(), UUID.randomUUID().toString(), expirationDate, true, UserType.CUSTOMER,
                deliveryAddress, null, null);
*/
        customers.add(customer);
    }

    @AfterEach
    void tearDown(){
    }

   /* @Test
    @DisplayName("When request object is not specified")
    void UnitTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.registerCustomer(null));
        assertEquals("RegisterCustomer Request is null - Could not register user as a customer", thrown.getMessage());
    }

    @Test
    @DisplayName("When userType parameter is not Invalid - Shopper")
    void UnitTest_testingInvalidUserTypeParameter_Shopper(){
        request = new RegisterCustomerRequest("", "", "", "", "", "", today, "", "","",
                true, UserType.SHOPPER, deliveryAddress);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.registerCustomer(request));
        assertEquals("User type is not Customer - Could not register user as a customer", thrown.getMessage());
    }

    @Test
    @DisplayName("When userType parameter is not Invalid - Admin")
    void UnitTest_testingInvalidUserTypeParameter_Admin(){
        request = new RegisterCustomerRequest("", "", "", "", "", "", today, "", "","",
                true, UserType.ADMIN, deliveryAddress);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.registerCustomer(request));
        assertEquals("User type is not Customer - Could not register user as a customer", thrown.getMessage());
    }

    @Test
    @DisplayName("When userType parameter is not Invalid - Driver")
    void UnitTest_testingInvalidUserTypeParameter_Driver(){
        request = new RegisterCustomerRequest("", "", "", "", "", "", today, "", "","",
                true, UserType.DRIVER, deliveryAddress);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.registerCustomer(request));
        assertEquals("User type is not Customer - Could not register user as a customer", thrown.getMessage());
    }

    @Test
    @DisplayName("When userType parameter is not Invalid - Null")
    void UnitTest_testingInvalidUserTypeParameter_Null(){
        request = new RegisterCustomerRequest("", "", "", "", "", "", today, "", "","",
                true, null, deliveryAddress);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.registerCustomer(request));
        assertEquals("User type is not Customer - Could not register user as a customer", thrown.getMessage());
    }

    @Test
    @DisplayName("When name parameter is valid - Null")
    void UnitTest_testingNameParameter_Null(){
        request = new RegisterCustomerRequest(null, "", "", "", "", "", today, "", "","",
                true, UserType.CUSTOMER, deliveryAddress);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.registerCustomer(request));
        assertEquals("Name cannot be null - Registration Failed", thrown.getMessage());
    }

    @Test
    @DisplayName("When surname parameter is valid - Null")
    void UnitTest_testingSurnameParameter_Null(){
        request = new RegisterCustomerRequest("D", null, "", "", "", "", today, "", "","",
                true, UserType.CUSTOMER, deliveryAddress);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.registerCustomer(request));
        assertEquals("Surname cannot be null - Registration Failed", thrown.getMessage());
    }

    @Test
    @DisplayName("When username parameter is valid - Null")
    void UnitTest_testingUsernameParameter_Null(){
        request = new RegisterCustomerRequest("D", "S", null, "", "", "", today, "", "","",
                true, UserType.CUSTOMER, deliveryAddress);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.registerCustomer(request));
        assertEquals("Username cannot be null - Registration Failed", thrown.getMessage());
    }

    @Test
    @DisplayName("When email parameter is valid - Null")
    void UnitTest_testingEmailParameter_Null(){
        request = new RegisterCustomerRequest("D", "S", "DS77", null, "", "", today, "", "","",
                true, UserType.CUSTOMER, deliveryAddress);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.registerCustomer(request));
        assertEquals("Email cannot be null - Registration Failed", thrown.getMessage());
    }

    @Test
    @DisplayName("When phone number parameter is valid - Null")
    void UnitTest_testingPhoneNumberParameter_Null(){
        request = new RegisterCustomerRequest("D", "S", "DS77", "ds@smallClubUnited.com", null, "", today, "", "","",
                true, UserType.CUSTOMER, deliveryAddress);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.registerCustomer(request));
        assertEquals("Phone Number cannot be null - Registration Failed", thrown.getMessage());
    }

    @Test
    @DisplayName("When password parameter is valid - Null")
    void UnitTest_testingPasswordParameter_Null(){
        request = new RegisterCustomerRequest("D", "S", "DS77", "ds@smallClubUnited.com", "0721234567",
                null, today, "", "","",
                true, UserType.CUSTOMER, deliveryAddress);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.registerCustomer(request));
        assertEquals("Password cannot be null - Registration Failed", thrown.getMessage());
    }

    @Test
    @DisplayName("When username parameter exists")
    void UnitTest_testingUsernameExists(){
        request = new RegisterCustomerRequest("D", "S", "DS77", "ds@smallClubUnited.com", "0721234567",
                "ewtryuj57iuhf", today, "", "","",
                true, UserType.CUSTOMER, deliveryAddress);

        when(customerRepo.findAll()).thenReturn(customers);
        Throwable thrown = Assertions.assertThrows(AlreadyExistsException.class, ()-> userService.registerCustomer(request));
        assertEquals("Username already exists - Registration Failed", thrown.getMessage());
    }

    @Test
    @DisplayName("When email parameter exists")
    void UnitTest_testingEmailExists(){
        request = new RegisterCustomerRequest("D", "S", "DS78", "ds@smallClubUnited.com", "0721234567",
                "ewtryuj57iuhf", today, "", "","",
                true, UserType.CUSTOMER, deliveryAddress);

        when(customerRepo.findAll()).thenReturn(customers);
        Throwable thrown = Assertions.assertThrows(AlreadyExistsException.class, ()-> userService.registerCustomer(request));
        assertEquals("Email already exists - Registration Failed", thrown.getMessage());
    }

    @Test
    @DisplayName("When phone number parameter exists")
    void UnitTest_testingPhoneNumberExists(){
        request = new RegisterCustomerRequest("D", "S", "DS78", "ds@smallClub.com", "0721234567",
                "ewtryuj57iuhf", today, "", "","",
                true, UserType.CUSTOMER, deliveryAddress);

        when(customerRepo.findAll()).thenReturn(customers);
        Throwable thrown = Assertions.assertThrows(AlreadyExistsException.class, ()-> userService.registerCustomer(request));
        assertEquals("Phone number already exists - Registration Failed", thrown.getMessage());
    }

    @Test
    @DisplayName("When the email format is Invalid")
    void UnitTest_testingInvalidEmailFormat(){
        request = new RegisterCustomerRequest("D", "S", "DS78", "dsSmallClub.com", "0721234568",
                "ewtryuj57iuhf", today, "", "","",
                true, UserType.CUSTOMER, deliveryAddress);

        when(customerRepo.findAll()).thenReturn(customers);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.registerCustomer(request));
        assertEquals("Invalid email - Registration failed", thrown.getMessage());
    }

    @Test
    @DisplayName("When the registration is successful")
    void UnitTest_SuccessfulRegistration(){
        request = new RegisterCustomerRequest("D", "S", "DS78", "ds@smallClub.com", "0721234568",
                "ewtryuj57iuhf", today, "", "","",
                true, UserType.CUSTOMER, deliveryAddress);

        when(customerRepo.findAll()).thenReturn(customers);

        try{
            RegisterCustomerResponse response = userService.registerCustomer(request);

            assertEquals(request.getName(), response.getCustomer().getName());
            assertEquals("Customer successfully registered", response.getMessage());
            assertTrue(response.isSuccess());
        }catch (Exception e){
            e.printStackTrace();
            fail();
        }
    }*/
}
