package cs.superleague.user.integration;

import cs.superleague.user.UserServiceImpl;
import cs.superleague.user.dataclass.Admin;
import cs.superleague.user.dataclass.Shopper;
import cs.superleague.user.exceptions.InvalidRequestException;
import cs.superleague.user.repos.AdminRepo;
import cs.superleague.user.requests.RegisterAdminRequest;
import cs.superleague.user.responses.RegisterAdminResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class RegisterAdminIntegrationTest {

    @Autowired
    AdminRepo adminRepo;

    @Autowired
    private UserServiceImpl userService;

    RegisterAdminRequest request;
    Admin Admin;
    @BeforeEach
    void setup(){

        request=new RegisterAdminRequest("Name","Surname","Email","PhoneNumber","Password");
    }

    @AfterEach
    void teardown(){
        adminRepo.deleteAll();
    }


    @Test
    @Description("Tests for when RegisterAdmin is submited with a null request object- exception should be thrown")
    @DisplayName("When request object is not specificed")
    void IntegrationTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.registerAdmin(null));
        assertEquals("Request object can't be null for RegisterAdminRequest", thrown.getMessage());
    }

    @Test
    @Description("Tests for when RegisterAdmin is submited with a null email parameter in request object- exception should be thrown")
    @DisplayName("When email parameter in request object is null")
    void IntegrationTest_emailNullParameter(){
        request.setEmail(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.registerAdmin(request));
        assertEquals("Email in RegisterAdminRequest is null", thrown.getMessage());
    }

    @Test
    @Description("Tests for when RegisterAdmin is submited with a null name parameter in request object- exception should be thrown")
    @DisplayName("When name parameter in request object is null")
    void IntegrationTest_nameNullParameter(){
        request.setName(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.registerAdmin(request));
        assertEquals("Name in RegisterAdminRequest is null", thrown.getMessage());
    }

    @Test
    @Description("Tests for when RegisterAdmin is submited with a null Surname parameter in request object- exception should be thrown")
    @DisplayName("When surname parameter in request object is null")
    void IntegrationTest_surnameNullParameter(){
        request.setSurname(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.registerAdmin(request));
        assertEquals("Surname in RegisterAdminRequest is null", thrown.getMessage());
    }

    @Test
    @Description("Tests for when RegisterAdmin is submited with a null Password parameter in request object- exception should be thrown")
    @DisplayName("When Password parameter in request object is null")
    void IntegrationTest_PasswordNullParameter(){
        request.setPassword(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.registerAdmin(request));
        assertEquals("Password in RegisterAdminRequest is null", thrown.getMessage());
    }

    @Test
    @Description("Tests for when RegisterAdmin is submited with a null PhoneNumber parameter in request object- exception should be thrown")
    @DisplayName("When PhoneNumber parameter in request object is null")
    void IntegrationTest_PhoneNumberNullParameter(){
        request.setPhoneNumber(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.registerAdmin(request));
        assertEquals("PhoneNumber in RegisterAdminRequest is null", thrown.getMessage());
    }

    @Test
    @Description("Tests for when RegisterAdmin request is correctly created")
    @DisplayName("Request is created correctly")
    void IntegrationTest_CorrectlyCreatedRequest(){
        RegisterAdminRequest request=new RegisterAdminRequest("name","surname","email","phoneNumber","password");
        assertNotNull(request);
        assertEquals("name",request.getName());
        assertEquals("surname",request.getSurname());
        assertEquals("email",request.getEmail());
        assertEquals("phoneNumber",request.getPhoneNumber());
        assertEquals("password",request.getPassword());
    }

    @Test
    @Description("Tests for when RegisterAdmin is submited with a an inavlid email and/or password")
    @DisplayName("Invalid email and/or password")
    void IntegrationTest_InavlidEmailAndPassword() throws InvalidRequestException {
        RegisterAdminResponse response= userService.registerAdmin(request);
        assertEquals(false,response.isSuccess());
        assertEquals("Email is not valid and Password is not valid",response.getMessage());
        assertNotNull(response.getTimestamp());

        /* When email is valid and not password*/
        String orginialInvalidEmail=request.getEmail();
        request.setEmail("validEmail@gmail.com");
        response= userService.registerAdmin(request);
        assertEquals(false,response.isSuccess());
        assertEquals("Password is not valid",response.getMessage());
        assertNotNull(response.getTimestamp());

        /* When password is valid and not email*/
        request.setEmail(orginialInvalidEmail);
        request.setPassword("validPassword@1");
        response= userService.registerAdmin(request);
        assertEquals(false,response.isSuccess());
        assertEquals("Email is not valid",response.getMessage());
        assertNotNull(response.getTimestamp());
    }
    @Test
    @Description("Tests for when RegisterAdmin is submitted with a email that already exists in database")
    @DisplayName("Email already exists in database")
    void IntegrationTest_EmailAlreadyExists() throws InvalidRequestException {
        request.setEmail("validEmail@gmail.com");
        request.setPassword("validPassword@1");
        Admin Admin=new Admin();
        Admin.setEmail("validEmail@gmail.com");
        Admin.setAdminID(UUID.randomUUID());
        adminRepo.save(Admin);
        RegisterAdminResponse response=userService.registerAdmin(request);

        assertNotNull(response);
        assertEquals(false,response.isSuccess());
        assertEquals("Email has already been used",response.getMessage());
        assertNotNull(response.getTimestamp());
    }



    @Test
    @Description("Tests for when RegisterAdmin is submitted with a valid credentials")
    @DisplayName("Valid Registering")
    void IntegrationTest_ValidRegistration() throws InvalidRequestException {
        request.setEmail("validEmail@gmail.com");
        request.setPassword("validPassword@1");
        RegisterAdminResponse response=userService.registerAdmin(request);

        assertNotNull(response);
        assertEquals(true,response.isSuccess());
        assertEquals("Admin succesfully added to database",response.getMessage());
        assertNotNull(response.getTimestamp());

        Admin adminSaved=adminRepo.findAdminByEmail(request.getEmail());

        assertNotNull(adminSaved);
        assertEquals(request.getName(),adminSaved.getName());
        assertEquals(request.getSurname(),adminSaved.getSurname());
        assertEquals(request.getPhoneNumber(),adminSaved.getPhoneNumber());
        assertEquals(request.getEmail(),adminSaved.getEmail());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(15);
        assertEquals(true,passwordEncoder.matches(request.getPassword(),adminSaved.getPassword()));


    }
}
