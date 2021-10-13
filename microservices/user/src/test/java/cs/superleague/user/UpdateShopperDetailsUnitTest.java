package cs.superleague.user;

import cs.superleague.integration.security.JwtUtil;
import cs.superleague.user.dataclass.Shopper;
import cs.superleague.user.exceptions.InvalidRequestException;
import cs.superleague.user.exceptions.ShopperDoesNotExistException;
import cs.superleague.user.repos.ShopperRepo;
import cs.superleague.user.requests.UpdateShopperDetailsRequest;
import cs.superleague.user.responses.UpdateShopperDetailsResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateShopperDetailsUnitTest {

    @Mock
    ShopperRepo shopperRepo;

    @InjectMocks
    private UserServiceImpl userService;

    @InjectMocks
    private JwtUtil jwtUtil;

    BCryptPasswordEncoder passwordEncoder;
    UpdateShopperDetailsRequest request;
    UUID shopperId= UUID.randomUUID();
    Shopper shopper;
    Shopper shopperExistingEmail;
    UpdateShopperDetailsResponse response;
    @BeforeEach
    void setUp(){
        passwordEncoder = new BCryptPasswordEncoder(15);

        request = new UpdateShopperDetailsRequest("name","surname","email@gmail.com","phoneNumber","password", "confirmedPassword");
        shopper = new Shopper();
        shopper.setEmail("email@yahoo.com");
        shopper.setShopperID(UUID.randomUUID());
        shopper.setPassword(passwordEncoder.encode(request.getCurrentPassword()));

        shopperExistingEmail = new Shopper();
        shopperExistingEmail.setEmail("validEmail@gmail.com");

    }

    @AfterEach
    void tearDown(){}

    @Test
    @DisplayName("When request object is not specified")
    void UnitTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.updateShopperDetails(null));
        assertEquals("UpdateShopper Request is null - Could not update shopper", thrown.getMessage());
    }

    @Test
    @DisplayName("Request object created correctly")
    void UnitTest_requestObjectCorrectlyCreated(){
        UUID storeID=UUID.randomUUID();
        UpdateShopperDetailsRequest req=new UpdateShopperDetailsRequest("n","s","e","pN","pass", "currentPassword");
        assertNotNull(req);
        assertEquals("n",req.getName());
        assertEquals("s",req.getSurname());
        assertEquals("e",req.getEmail());
        assertEquals("pass",req.getPassword());
        assertEquals("pN",req.getPhoneNumber());
        assertEquals("currentPassword", req.getCurrentPassword());
    }

    @Test
    @DisplayName("When shopper with given Email does not exist")
    void UnitTest_testingInvalidUser(){
        when(shopperRepo.findByEmail(Mockito.any())).thenReturn(null);
        Throwable thrown = Assertions.assertThrows(ShopperDoesNotExistException.class, ()-> userService.updateShopperDetails(request));
        assertEquals("User with the given email does not exist - could not update shopper", thrown.getMessage());
    }

    @Test
    @DisplayName("When an Invalid email is given")
    void UnitTest_testingInvalidEmail(){
        request.setEmail("invalid");
        when(shopperRepo.findByEmail(Mockito.any())).thenReturn(Optional.ofNullable(shopper));

        try {
            response = userService.updateShopperDetails(request);
            assertEquals("Email is not valid", response.getMessage());
            assertFalse(response.isSuccess());
            assertNotNull(response.getTimestamp());
        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("When an Invalid password is given")
    void UnitTest_testingInvalidPassword(){

        when(shopperRepo.findByEmail(Mockito.any())).thenReturn(Optional.ofNullable(shopper));
        when(shopperRepo.findByEmail(request.getEmail())).thenReturn(Optional.ofNullable(null));

        try {
            response = userService.updateShopperDetails(request);
            assertEquals("Password is not valid", response.getMessage());
            assertFalse(response.isSuccess());
            assertNotNull(response.getTimestamp());
        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("When null update values are given")
    void UnitTest_testingNullUpdates(){
        request = new UpdateShopperDetailsRequest(null, null, null,
                null, null, null);
        when(shopperRepo.findByEmail(Mockito.any())).thenReturn(Optional.ofNullable(shopper));

        try {
            response = userService.updateShopperDetails(request);
            assertEquals("Null values submitted - Nothing updated", response.getMessage());
            assertFalse(response.isSuccess());
            assertNotNull(response.getMessage());
        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }


    @Test
    @DisplayName("When nonnull update values are given")
    void UnitTest_testingSuccessfulUpdate(){

        request.setPassword("validPassword@1");
        when(shopperRepo.findByEmail(Mockito.any())).thenReturn(Optional.ofNullable(shopper));
        when(shopperRepo.findByEmail(request.getEmail())).thenReturn(Optional.ofNullable(null));

        try {
            response = userService.updateShopperDetails(request);
            assertEquals("Shopper successfully updated", response.getMessage());
            assertTrue(response.isSuccess());
            assertNotNull(response.getTimestamp());
        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }
}
