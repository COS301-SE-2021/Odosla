package cs.superleague.user;

import cs.superleague.user.dataclass.Driver;
import cs.superleague.user.dataclass.Shopper;
import cs.superleague.user.exceptions.DriverDoesNotExistException;
import cs.superleague.user.exceptions.InvalidRequestException;
import cs.superleague.user.repos.DriverRepo;
import cs.superleague.user.requests.UpdateDriverDetailsRequest;
import cs.superleague.user.responses.UpdateDriverDetailsResponse;
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
public class UpdateDriverDetailsUnitTest {

    @Mock
    DriverRepo driverRepo;

    @InjectMocks
    private UserServiceImpl userService;

    BCryptPasswordEncoder passwordEncoder;
    UpdateDriverDetailsRequest request;
    UUID driverId=UUID.randomUUID();
    Driver driver;
    Driver driverExisting;
    UpdateDriverDetailsResponse response;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder(15);

        request=new UpdateDriverDetailsRequest("name","surname","email@gmail.com","password","phoneNumber", "currentPassword");
        driver = new Driver();
        driver.setEmail("email@yahoo.com");
        driver.setDriverID(UUID.randomUUID());
        driver.setPassword(passwordEncoder.encode(request.getCurrentPassword()));

        driverExisting = new Driver();
        driverExisting.setDriverID(UUID.randomUUID());
        driverExisting.setEmail("validEmail@gmail.com");
    }

    @AfterEach
    void tearDown(){}

    @Test
    @DisplayName("When request object is not specified")
    void UnitTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.updateDriverDetails(null));
        assertEquals("UpdateDriver Request is null - Could not update driver", thrown.getMessage());
    }

    @Test
    @DisplayName("Request object created correctly")
    void UnitTest_requestObjectCorrectlyCreated(){
        UpdateDriverDetailsRequest req=new UpdateDriverDetailsRequest("n","s","e","pass","pN", "currentPassword");
        assertNotNull(req);
        assertEquals("n",req.getName());
        assertEquals("s",req.getSurname());
        assertEquals("e",req.getEmail());
        assertEquals("pass",req.getPassword());
        assertEquals("pN",req.getPhoneNumber());
        assertEquals("currentPassword", req.getCurrentPassword());
    }

    @Test
    @DisplayName("When driver with given Email does not exist")
    void UnitTest_testingInvalidUser(){
        when(driverRepo.findDriverByEmail(Mockito.any())).thenReturn(null);
        Throwable thrown = Assertions.assertThrows(DriverDoesNotExistException.class, ()-> userService.updateDriverDetails(request));
        assertEquals("User with given email does not exist - could not update driver", thrown.getMessage());
    }

    @Test
    @DisplayName("When an Invalid email is given")
    void UnitTest_testingInvalidEmail(){
        request.setEmail("invalid");
        when(driverRepo.findDriverByEmail(Mockito.any())).thenReturn((driver));

        try {
            response = userService.updateDriverDetails(request);
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

        when(driverRepo.findDriverByEmail(Mockito.any())).thenReturn((driver));
        when(driverRepo.findDriverByEmail(request.getEmail())).thenReturn((null));

        try {
            response = userService.updateDriverDetails(request);
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
        request = new UpdateDriverDetailsRequest( null, null, null,
                null, null, null);
        when(driverRepo.findDriverByEmail(Mockito.any())).thenReturn((driver));

        try {
            response = userService.updateDriverDetails(request);
            assertEquals("Null values submitted - Nothing updated", response.getMessage());
            assertFalse(response.isSuccess());
            assertNotNull(response.getMessage());
        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("When user tries to update to existingEmail")
    void UnitTest_testingExistingEmailUpdateAttempt(){
        driver.setEmail("validEmail@gmail.com");
        when(driverRepo.findDriverByEmail(Mockito.any())).thenReturn((driver));
        when(driverRepo.findDriverByEmail(request.getEmail())).thenReturn(driverExisting);
        try {
            response = userService.updateDriverDetails(request);
            assertEquals("Email is already taken", response.getMessage());
            assertFalse(response.isSuccess());
            assertNotNull(response.getTimestamp());
        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("When nonnull update values are given")
    void UnitTest_testingSuccessfulUpdate(){

        request.setPassword("validPassword@1");
        when(driverRepo.findDriverByEmail(Mockito.any())).thenReturn((driver));
        when(driverRepo.findDriverByEmail(request.getEmail())).thenReturn(null);

        try {
            response = userService.updateDriverDetails(request);
            assertEquals("Driver successfully updated", response.getMessage());
            assertTrue(response.isSuccess());
            assertNotNull(response.getTimestamp());
        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }
}
