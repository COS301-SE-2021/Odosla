package cs.superleague.user.integration;

import cs.superleague.user.UserServiceImpl;
import cs.superleague.user.dataclass.Driver;
import cs.superleague.user.exceptions.InvalidRequestException;
import cs.superleague.user.repos.DriverRepo;
import cs.superleague.user.requests.RegisterDriverRequest;
import cs.superleague.user.responses.RegisterDriverResponse;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class RegisterDriverInetgrationTest {

    @Autowired
    DriverRepo driverRepo;

    @Autowired
    private UserServiceImpl userService;

    RegisterDriverRequest request;
    Driver driver;
    @BeforeEach
    void setup(){

        request=new RegisterDriverRequest("Name","Surname","Email","PhoneNumber","Password");
    }

    @AfterEach
    void teardown(){
        driverRepo.deleteAll();
    }


    @Test
    @Description("Tests for when RegisterDriver is submited with a null request object- exception should be thrown")
    @DisplayName("When request object is not specificed")
    void IntegrationTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.registerDriver(null));
        assertEquals("Request object can't be null for RegisterDriverRequest", thrown.getMessage());
    }

    @Test
    @Description("Tests for when RegisterDriver is submited with a null email parameter in request object- exception should be thrown")
    @DisplayName("When email parameter in request object is null")
    void IntegrationTest_emailNullParameter(){
        request.setEmail(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.registerDriver(request));
        assertEquals("Email in RegisterDriverRequest is null", thrown.getMessage());
    }

    @Test
    @Description("Tests for when RegisterDriver is submited with a null name parameter in request object- exception should be thrown")
    @DisplayName("When name parameter in request object is null")
    void IntegrationTest_nameNullParameter(){
        request.setName(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.registerDriver(request));
        assertEquals("Name in RegisterDriverRequest is null", thrown.getMessage());
    }

    @Test
    @Description("Tests for when RegisterDriver is submited with a null Surname parameter in request object- exception should be thrown")
    @DisplayName("When surname parameter in request object is null")
    void IntegrationTest_surnameNullParameter(){
        request.setSurname(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.registerDriver(request));
        assertEquals("Surname in RegisterDriverRequest is null", thrown.getMessage());
    }

    @Test
    @Description("Tests for when RegisterDriver is submited with a null Password parameter in request object- exception should be thrown")
    @DisplayName("When Password parameter in request object is null")
    void IntegrationTest_PasswordNullParameter(){
        request.setPassword(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.registerDriver(request));
        assertEquals("Password in RegisterDriverRequest is null", thrown.getMessage());
    }

    @Test
    @Description("Tests for when RegisterDriver is submited with a null PhoneNumber parameter in request object- exception should be thrown")
    @DisplayName("When PhoneNumber parameter in request object is null")
    void IntegrationTest_PhoneNumberNullParameter(){
        request.setPhoneNumber(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.registerDriver(request));
        assertEquals("PhoneNumber in RegisterDriverRequest is null", thrown.getMessage());
    }

    @Test
    @Description("Tests for when RegisterDriver request is correctly created")
    @DisplayName("Request is created correctly")
    void IntegrationTest_CorrectlyCreatedRequest(){
        RegisterDriverRequest request=new RegisterDriverRequest("name","surname","email","phoneNumber","password");
        assertNotNull(request);
        assertEquals("name",request.getName());
        assertEquals("surname",request.getSurname());
        assertEquals("email",request.getEmail());
        assertEquals("phoneNumber",request.getPhoneNumber());
        assertEquals("password",request.getPassword());
    }

    @Test
    @Description("Tests for when RegisterDriver is submited with a an inavlid email and/or password")
    @DisplayName("Invalid email and/or password")
    void IntegrationTest_InavlidEmailAndPassword() throws InvalidRequestException {
        RegisterDriverResponse response= userService.registerDriver(request);
        assertEquals(false,response.isSuccess());
        assertEquals("Email is not valid and Password is not valid",response.getMessage());
        assertNotNull(response.getTimestamp());

        /* When email is valid and not password*/
        String orginialInvalidEmail=request.getEmail();
        request.setEmail("validEmail@gmail.com");
        response= userService.registerDriver(request);
        assertEquals(false,response.isSuccess());
        assertEquals("Password is not valid",response.getMessage());
        assertNotNull(response.getTimestamp());

        /* When password is valid and not email*/
        request.setEmail(orginialInvalidEmail);
        request.setPassword("validPassword@1");
        response= userService.registerDriver(request);
        assertEquals(false,response.isSuccess());
        assertEquals("Email is not valid",response.getMessage());
        assertNotNull(response.getTimestamp());
    }
    @Test
    @Description("Tests for when RegisterDriver is submitted with a email that already exists in database")
    @DisplayName("Email already exists in database")
    void IntegrationTest_EmailAlreadyExists() throws InvalidRequestException {
        request.setEmail("validEmail@gmail.com");
        request.setPassword("validPassword@1");
        Driver driver=new Driver();
        driver.setEmail("validEmail@gmail.com");
        driver.setDriverID(UUID.randomUUID());
        driverRepo.save(driver);
        RegisterDriverResponse response=userService.registerDriver(request);

        assertNotNull(response);
        assertEquals(false,response.isSuccess());
        assertEquals("Email has already been used",response.getMessage());
        assertNotNull(response.getTimestamp());
    }



    @Test
    @Description("Tests for when RegisterDriver is submitted with a valid credentials")
    @DisplayName("Valid Registering")
    void IntegrationTest_ValidRegistration() throws InvalidRequestException {
        request.setEmail("validEmail@gmail.com");
        request.setPassword("validPassword@1");
        RegisterDriverResponse response=userService.registerDriver(request);

        assertNotNull(response);
        assertEquals(true,response.isSuccess());
        assertEquals("Driver succesfully added to database",response.getMessage());
        assertNotNull(response.getTimestamp());

    }

}
