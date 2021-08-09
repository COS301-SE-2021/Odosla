package cs.superleague.user;

import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.user.dataclass.Customer;
import cs.superleague.user.dataclass.Customer;
import cs.superleague.user.dataclass.UserType;
import cs.superleague.user.exceptions.AlreadyExistsException;
import cs.superleague.user.exceptions.InvalidRequestException;
import cs.superleague.user.repos.CustomerRepo;
import cs.superleague.user.requests.RegisterCustomerRequest;
import cs.superleague.user.requests.RegisterCustomerRequest;
import cs.superleague.user.responses.RegisterCustomerResponse;
import cs.superleague.user.responses.RegisterCustomerResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Description;

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

    RegisterCustomerRequest request;

    @BeforeEach
    void setup(){
        request=new RegisterCustomerRequest("Name","Surname","Email","PhoneNumber","Password");
    }

    @AfterEach
    void teardown(){}


    @Test
    @Description("Tests for when RegisterCustomer is submited with a null request object- exception should be thrown")
    @DisplayName("When request object is not specificed")
    void UnitTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.registerCustomer(null));
        assertEquals("Request object can't be null for RegisterCustomerRequest", thrown.getMessage());
    }

    @Test
    @Description("Tests for when RegisterCustomer is submited with a null email parameter in request object- exception should be thrown")
    @DisplayName("When email parameter in request object is null")
    void UnitTest_emailNullParameter(){
        request.setEmail(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.registerCustomer(request));
        assertEquals("Email in RegisterCustomerRequest is null", thrown.getMessage());
    }

    @Test
    @Description("Tests for when RegisterCustomer is submited with a null name parameter in request object- exception should be thrown")
    @DisplayName("When name parameter in request object is null")
    void UnitTest_nameNullParameter(){
        request.setName(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.registerCustomer(request));
        assertEquals("Name in RegisterCustomerRequest is null", thrown.getMessage());
    }

    @Test
    @Description("Tests for when RegisterCustomer is submited with a null Surname parameter in request object- exception should be thrown")
    @DisplayName("When surname parameter in request object is null")
    void UnitTest_surnameNullParameter(){
        request.setSurname(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.registerCustomer(request));
        assertEquals("Surname in RegisterCustomerRequest is null", thrown.getMessage());
    }

    @Test
    @Description("Tests for when RegisterCustomer is submited with a null Password parameter in request object- exception should be thrown")
    @DisplayName("When Password parameter in request object is null")
    void UnitTest_PasswordNullParameter(){
        request.setPassword(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.registerCustomer(request));
        assertEquals("Password in RegisterCustomerRequest is null", thrown.getMessage());
    }

    @Test
    @Description("Tests for when RegisterCustomer is submited with a null PhoneNumber parameter in request object- exception should be thrown")
    @DisplayName("When PhoneNumber parameter in request object is null")
    void UnitTest_PhoneNumberNullParameter(){
        request.setPhoneNumber(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.registerCustomer(request));
        assertEquals("PhoneNumber in RegisterCustomerRequest is null", thrown.getMessage());
    }

    @Test
    @Description("Tests for when RegisterCustomer request is correctly created")
    @DisplayName("Request is created correctly")
    void UnitTest_CorrectlyCreatedRequest(){
        RegisterCustomerRequest request=new RegisterCustomerRequest("name","surname","email","phoneNumber","password");
        assertNotNull(request);
        assertEquals("name",request.getName());
        assertEquals("surname",request.getSurname());
        assertEquals("email",request.getEmail());
        assertEquals("phoneNumber",request.getPhoneNumber());
        assertEquals("password",request.getPassword());
    }

    @Test
    @Description("Tests for when RegisterCustomer is submited with a an inavlid email and/or password")
    @DisplayName("Invalid email and/or password")
    void UnitTest_InavlidEmailAndPassword() throws InvalidRequestException {
        RegisterCustomerResponse response= userService.registerCustomer(request);
        assertEquals(false,response.isSuccess());
        assertEquals("Email is not valid and Password is not valid",response.getMessage());
        assertNotNull(response.getTimestamp());

        /* When email is valid and not password*/
        String orginialInvalidEmail=request.getEmail();
        request.setEmail("validEmail@gmail.com");
        response= userService.registerCustomer(request);
        assertEquals(false,response.isSuccess());
        assertEquals("Password is not valid",response.getMessage());
        assertNotNull(response.getTimestamp());

        /* When password is valid and not email*/
        request.setEmail(orginialInvalidEmail);
        request.setPassword("validPassword@1");
        response= userService.registerCustomer(request);
        assertEquals(false,response.isSuccess());
        assertEquals("Email is not valid",response.getMessage());
        assertNotNull(response.getTimestamp());
    }
    @Test
    @Description("Tests for when RegisterCustomer is submitted with a email that already exists in database")
    @DisplayName("Email already exists in database")
    void UnitTest_EmailAlreadyExists() throws InvalidRequestException {
        request.setEmail("validEmail@gmail.com");
        request.setPassword("validPassword@1");
        Customer Customer=new Customer();
        Mockito.when(customerRepo.findCustomerByEmail(Mockito.any())).thenReturn(Customer);
        RegisterCustomerResponse response=userService.registerCustomer(request);

        assertNotNull(response);
        assertEquals(false,response.isSuccess());
        assertEquals("Email has already been used",response.getMessage());
        assertNotNull(response.getTimestamp());
    }

    @Test
    @Description("Tests for when RegisterCustomer is submitted with a valid credentials but UUID for dirverID has been used should timeout after 5 seconds if constantly a UUID used")
    @DisplayName("CustomerID has already been used")
    void UnitTest_CustomerIDAlreadybeenUsed() throws InvalidRequestException {
        request.setEmail("validEmail@gmail.com");
        request.setPassword("validPassword@1");
        Mockito.when(customerRepo.findCustomerByEmail(Mockito.any())).thenReturn(null);
        Customer Customer=new Customer();
        Mockito.when(customerRepo.findById(Mockito.any())).thenReturn(java.util.Optional.of(Customer));
        RegisterCustomerResponse response=userService.registerCustomer(request);

        assertNotNull(response);
        assertEquals(false,response.isSuccess());
        assertEquals("Timeout occured and couldn't register Customer",response.getMessage());
        assertNotNull(response.getTimestamp());
    }

    @Test
    @Description("Tests for when RegisterCustomer is submitted with a valid credentials")
    @DisplayName("Valid Registering")
    void UnitTest_ValidRegistration() throws InvalidRequestException {
        request.setEmail("validEmail@gmail.com");
        request.setPassword("validPassword@1");
        Mockito.when(customerRepo.findCustomerByEmail(Mockito.any())).thenReturn(null);
        Customer Customer=new Customer();
        Mockito.when(customerRepo.findById(Mockito.any())).thenReturn(null).thenReturn(java.util.Optional.of(Customer));
        RegisterCustomerResponse response=userService.registerCustomer(request);

        assertNotNull(response);
        assertEquals(true,response.isSuccess());
        assertEquals("Customer succesfully added to database",response.getMessage());
        assertNotNull(response.getTimestamp());
    }

    @Test
    @Description("Tests for when RegisterCustomer is submitted with a valid credentials but Customer wasn't actually saved to database")
    @DisplayName("Valid Registering without actually being saved to database")
    void UnitTest_ValidRegistrationNotSavedToDatabase() throws InvalidRequestException {
        request.setEmail("validEmail@gmail.com");
        request.setPassword("validPassword@1");
        Mockito.when(customerRepo.findCustomerByEmail(Mockito.any())).thenReturn(null);
        Customer Customer=new Customer();
        Mockito.when(customerRepo.findById(Mockito.any())).thenReturn(null).thenReturn(null);
        RegisterCustomerResponse response=userService.registerCustomer(request);

        assertNotNull(response);
        assertEquals(false,response.isSuccess());
        assertEquals("Could not save Customer to database",response.getMessage());
        assertNotNull(response.getTimestamp());
    }
}
