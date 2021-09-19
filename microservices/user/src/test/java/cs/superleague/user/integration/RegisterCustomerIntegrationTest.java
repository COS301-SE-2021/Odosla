//package cs.superleague.user.integration;
//
//import cs.superleague.user.UserServiceImpl;
//import cs.superleague.user.dataclass.Customer;
//import cs.superleague.user.exceptions.InvalidRequestException;
//import cs.superleague.user.repos.CustomerRepo;
//import cs.superleague.user.requests.RegisterCustomerRequest;
//import cs.superleague.user.responses.RegisterCustomerResponse;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Description;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Calendar;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@Transactional
//public class RegisterCustomerIntegrationTest {
//
//    @Autowired
//    CustomerRepo customerRepo;
//
//    @Autowired
//    private UserServiceImpl userService;
//
//    RegisterCustomerRequest request;
//
//    Calendar today;
//    @BeforeEach
//    void setUp() {
//        request=new RegisterCustomerRequest("Name","Surname","Email","PhoneNumber","Password");
//    }
//
//    @AfterEach
//    void tearDown(){
//        customerRepo.deleteAll();
//    }
//
//
//    @Test
//    @Description("Tests for when RegisterCustomer is submited with a null request object- exception should be thrown")
//    @DisplayName("When request object is not specificed")
//    void IntegrationTest_testingNullRequestObject(){
//        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.registerCustomer(null));
//        assertEquals("Request object can't be null for RegisterCustomerRequest", thrown.getMessage());
//    }
//
//    @Test
//    @Description("Tests for when RegisterCustomer is submited with a null email parameter in request object- exception should be thrown")
//    @DisplayName("When email parameter in request object is null")
//    void IntegrationTest_emailNullParameter(){
//        request.setEmail(null);
//        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.registerCustomer(request));
//        assertEquals("Email in RegisterCustomerRequest is null", thrown.getMessage());
//    }
//
//    @Test
//    @Description("Tests for when RegisterCustomer is submited with a null name parameter in request object- exception should be thrown")
//    @DisplayName("When name parameter in request object is null")
//    void IntegrationTest_nameNullParameter(){
//        request.setName(null);
//        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.registerCustomer(request));
//        assertEquals("Name in RegisterCustomerRequest is null", thrown.getMessage());
//    }
//
//    @Test
//    @Description("Tests for when RegisterCustomer is submited with a null Surname parameter in request object- exception should be thrown")
//    @DisplayName("When surname parameter in request object is null")
//    void IntegrationTest_surnameNullParameter(){
//        request.setSurname(null);
//        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.registerCustomer(request));
//        assertEquals("Surname in RegisterCustomerRequest is null", thrown.getMessage());
//    }
//
//    @Test
//    @Description("Tests for when RegisterCustomer is submited with a null Password parameter in request object- exception should be thrown")
//    @DisplayName("When Password parameter in request object is null")
//    void IntegrationTest_PasswordNullParameter(){
//        request.setPassword(null);
//        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.registerCustomer(request));
//        assertEquals("Password in RegisterCustomerRequest is null", thrown.getMessage());
//    }
//
//    @Test
//    @Description("Tests for when RegisterCustomer is submited with a null PhoneNumber parameter in request object- exception should be thrown")
//    @DisplayName("When PhoneNumber parameter in request object is null")
//    void IntegrationTest_PhoneNumberNullParameter(){
//        request.setPhoneNumber(null);
//        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.registerCustomer(request));
//        assertEquals("PhoneNumber in RegisterCustomerRequest is null", thrown.getMessage());
//    }
//
//    @Test
//    @Description("Tests for when RegisterCustomer request is correctly created")
//    @DisplayName("Request is created correctly")
//    void IntegrationTest_CorrectlyCreatedRequest(){
//        RegisterCustomerRequest request=new RegisterCustomerRequest("name","surname","email","phoneNumber","password");
//        assertNotNull(request);
//        assertEquals("name",request.getName());
//        assertEquals("surname",request.getSurname());
//        assertEquals("email",request.getEmail());
//        assertEquals("phoneNumber",request.getPhoneNumber());
//        assertEquals("password",request.getPassword());
//    }
//
//    @Test
//    @Description("Tests for when RegisterCustomer is submited with a an inavlid email and/or password")
//    @DisplayName("Invalid email and/or password")
//    void IntegrationTest_InavlidEmailAndPassword() throws InvalidRequestException {
//        RegisterCustomerResponse response= userService.registerCustomer(request);
//        assertEquals(false,response.isSuccess());
//        assertEquals("Email is not valid and Password is not valid",response.getMessage());
//        assertNotNull(response.getTimestamp());
//
//        /* When email is valid and not password*/
//        String orginialInvalidEmail=request.getEmail();
//        request.setEmail("validEmail@gmail.com");
//        response= userService.registerCustomer(request);
//        assertEquals(false,response.isSuccess());
//        assertEquals("Password is not valid",response.getMessage());
//        assertNotNull(response.getTimestamp());
//
//        /* When password is valid and not email*/
//        request.setEmail(orginialInvalidEmail);
//        request.setPassword("validPassword@1");
//        response= userService.registerCustomer(request);
//        assertEquals(false,response.isSuccess());
//        assertEquals("Email is not valid",response.getMessage());
//        assertNotNull(response.getTimestamp());
//    }
//    @Test
//    @Description("Tests for when RegisterCustomer is submitted with a email that already exists in database")
//    @DisplayName("Email already exists in database")
//    void IntegrationTest_EmailAlreadyExists() throws InvalidRequestException {
//        request.setEmail("validEmail@gmail.com");
//        request.setPassword("validPassword@1");
//        Customer Customer=new Customer();
//        Customer.setEmail("validEmail@gmail.com");
//        Customer.setCustomerID(UUID.randomUUID());
//        customerRepo.save(Customer);
//        RegisterCustomerResponse response=userService.registerCustomer(request);
//
//        assertNotNull(response);
//        assertEquals(false,response.isSuccess());
//        assertEquals("Email has already been used",response.getMessage());
//        assertNotNull(response.getTimestamp());
//    }
//
//
//
//    @Test
//    @Description("Tests for when RegisterCustomer is submitted with a valid credentials")
//    @DisplayName("Valid Registering")
//    void IntegrationTest_ValidRegistration() throws InvalidRequestException {
//        request.setEmail("validEmail@gmail.com");
//        request.setPassword("validPassword@1");
//        RegisterCustomerResponse response=userService.registerCustomer(request);
//
//        assertNotNull(response);
//        assertEquals(true,response.isSuccess());
//        assertEquals("Customer succesfully added to database",response.getMessage());
//        assertNotNull(response.getTimestamp());
//
//        Customer customerSaved=customerRepo.findByEmail(request.getEmail()).orElse(null);
//
//        assertNotNull(customerSaved);
//        assertEquals(request.getName(),customerSaved.getName());
//        assertEquals(request.getSurname(),customerSaved.getSurname());
//        assertEquals(request.getPhoneNumber(),customerSaved.getPhoneNumber());
//        assertEquals(request.getEmail(),customerSaved.getEmail());
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(15);
//        assertEquals(true,passwordEncoder.matches(request.getPassword(),customerSaved.getPassword()));
//
//    }
//}
