package cs.superleague.user;

import cs.superleague.user.dataclass.Shopper;
import cs.superleague.user.exceptions.InvalidRequestException;
import cs.superleague.user.repos.ShopperRepo;
import cs.superleague.user.requests.RegisterShopperRequest;
import cs.superleague.user.responses.RegisterShopperResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Description;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class RegisterShopperUnitTest {

    @Mock
    ShopperRepo shopperRepo;

    @InjectMocks
    private UserServiceImpl userService;

    RegisterShopperRequest request;

    @BeforeEach
    void setup(){
        request=new RegisterShopperRequest("Name","Surname","Email","PhoneNumber","Password");
    }

    @AfterEach
    void teardown(){}


    @Test
    @Description("Tests for when RegisterShopper is submited with a null request object- exception should be thrown")
    @DisplayName("When request object is not specificed")
    void UnitTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.registerShopper(null));
        assertEquals("Request object can't be null for RegisterShopperRequest", thrown.getMessage());
    }

    @Test
    @Description("Tests for when RegisterShopper is submited with a null email parameter in request object- exception should be thrown")
    @DisplayName("When email parameter in request object is null")
    void UnitTest_emailNullParameter(){
        request.setEmail(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.registerShopper(request));
        assertEquals("Email in RegisterShopperRequest is null", thrown.getMessage());
    }

    @Test
    @Description("Tests for when RegisterShopper is submited with a null name parameter in request object- exception should be thrown")
    @DisplayName("When name parameter in request object is null")
    void UnitTest_nameNullParameter(){
        request.setName(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.registerShopper(request));
        assertEquals("Name in RegisterShopperRequest is null", thrown.getMessage());
    }

    @Test
    @Description("Tests for when RegisterShopper is submited with a null Surname parameter in request object- exception should be thrown")
    @DisplayName("When surname parameter in request object is null")
    void UnitTest_surnameNullParameter(){
        request.setSurname(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.registerShopper(request));
        assertEquals("Surname in RegisterShopperRequest is null", thrown.getMessage());
    }

    @Test
    @Description("Tests for when RegisterShopper is submited with a null Password parameter in request object- exception should be thrown")
    @DisplayName("When Password parameter in request object is null")
    void UnitTest_PasswordNullParameter(){
        request.setPassword(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.registerShopper(request));
        assertEquals("Password in RegisterShopperRequest is null", thrown.getMessage());
    }

    @Test
    @Description("Tests for when RegisterShopper is submited with a null PhoneNumber parameter in request object- exception should be thrown")
    @DisplayName("When PhoneNumber parameter in request object is null")
    void UnitTest_PhoneNumberNullParameter(){
        request.setPhoneNumber(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.registerShopper(request));
        assertEquals("PhoneNumber in RegisterShopperRequest is null", thrown.getMessage());
    }

    @Test
    @Description("Tests for when RegisterShopper request is correctly created")
    @DisplayName("Request is created correctly")
    void UnitTest_CorrectlyCreatedRequest(){
        RegisterShopperRequest request=new RegisterShopperRequest("name","surname","email","phoneNumber","password");
        assertNotNull(request);
        assertEquals("name",request.getName());
        assertEquals("surname",request.getSurname());
        assertEquals("email",request.getEmail());
        assertEquals("phoneNumber",request.getPhoneNumber());
        assertEquals("password",request.getPassword());
    }

    @Test
    @Description("Tests for when RegisterShopper is submited with a an inavlid email and/or password")
    @DisplayName("Invalid email and/or password")
    void UnitTest_InavlidEmailAndPassword() throws InvalidRequestException {
        RegisterShopperResponse response= userService.registerShopper(request);
        assertEquals(false,response.isSuccess());
        assertEquals("Email is not valid and Password is not valid",response.getMessage());
        assertNotNull(response.getTimestamp());

        /* When email is valid and not password*/
        String orginialInvalidEmail=request.getEmail();
        request.setEmail("validEmail@gmail.com");
        response= userService.registerShopper(request);
        assertEquals(false,response.isSuccess());
        assertEquals("Password is not valid",response.getMessage());
        assertNotNull(response.getTimestamp());

        /* When password is valid and not email*/
        request.setEmail(orginialInvalidEmail);
        request.setPassword("validPassword@1");
        response= userService.registerShopper(request);
        assertEquals(false,response.isSuccess());
        assertEquals("Email is not valid",response.getMessage());
        assertNotNull(response.getTimestamp());
    }
    @Test
    @Description("Tests for when RegisterShopper is submitted with a email that already exists in database")
    @DisplayName("Email already exists in database")
    void UnitTest_EmailAlreadyExists() throws InvalidRequestException {
        request.setEmail("validEmail@gmail.com");
        request.setPassword("validPassword@1");
        Shopper Shopper=new Shopper();
        Mockito.when(shopperRepo.findByEmail(Mockito.any())).thenReturn(java.util.Optional.of(Shopper));
        RegisterShopperResponse response=userService.registerShopper(request);

        assertNotNull(response);
        assertEquals(false,response.isSuccess());
        assertEquals("Email has already been used",response.getMessage());
        assertNotNull(response.getTimestamp());
    }

    @Test
    @Description("Tests for when RegisterShopper is submitted with a valid credentials but UUID for dirverID has been used should timeout after 5 seconds if constantly a UUID used")
    @DisplayName("ShopperID has already been used")
    void UnitTest_ShopperIDAlreadybeenUsed() throws InvalidRequestException {
        request.setEmail("validEmail@gmail.com");
        request.setPassword("validPassword@1");
        Mockito.when(shopperRepo.findByEmail(Mockito.any())).thenReturn(null);
        Shopper Shopper=new Shopper();
        Mockito.when(shopperRepo.findById(Mockito.any())).thenReturn(java.util.Optional.of(Shopper));
        RegisterShopperResponse response=userService.registerShopper(request);

        assertNotNull(response);
        assertEquals(false,response.isSuccess());
        assertEquals("Timeout occured and couldn't register shopper",response.getMessage());
        assertNotNull(response.getTimestamp());
    }

    @Test
    @Description("Tests for when RegisterShopper is submitted with a valid credentials")
    @DisplayName("Valid Registering")
    void UnitTest_ValidRegistration() throws InvalidRequestException {
        request.setEmail("validEmail@gmail.com");
        request.setPassword("validPassword@1");
        Mockito.when(shopperRepo.findByEmail(Mockito.any())).thenReturn(null);
        Shopper Shopper=new Shopper();
        Mockito.when(shopperRepo.findById(Mockito.any())).thenReturn(null).thenReturn(java.util.Optional.of(Shopper));
        RegisterShopperResponse response=userService.registerShopper(request);

        assertNotNull(response);
        assertEquals(true,response.isSuccess());
        assertEquals("Shopper succesfully added to database",response.getMessage());
        assertNotNull(response.getTimestamp());
    }

    @Test
    @Description("Tests for when RegisterShopper is submitted with a valid credentials but Shopper wasn't actually saved to database")
    @DisplayName("Valid Registering without actually being saved to database")
    void UnitTest_ValidRegistrationNotSavedToDatabase() throws InvalidRequestException {
        request.setEmail("validEmail@gmail.com");
        request.setPassword("validPassword@1");
        Mockito.when(shopperRepo.findByEmail(Mockito.any())).thenReturn(null);
        Shopper Shopper=new Shopper();
        Mockito.when(shopperRepo.findById(Mockito.any())).thenReturn(null).thenReturn(null);
        RegisterShopperResponse response=userService.registerShopper(request);

        assertNotNull(response);
        assertEquals(false,response.isSuccess());
        assertEquals("Could not save Shopper to database",response.getMessage());
        assertNotNull(response.getTimestamp());
    }
}
