package cs.superleague.user;

import cs.superleague.user.dataclass.Driver;
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

    UpdateDriverDetailsRequest request;
    UUID driverId=UUID.randomUUID();
    Driver driver;
    UpdateDriverDetailsResponse response;

    @BeforeEach
    void setUp() {
        request=new UpdateDriverDetailsRequest(driverId,"name","surname","email@gmail.com","password","phoneNumber");
        driver=new Driver();
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
    @DisplayName("When userID parameter is not specified")
    void UnitTest_testingNullRequestUserIDParameter(){
        request.setDriverID(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.updateDriverDetails(request));
        assertEquals("DriverId is null - could not update driver", thrown.getMessage());
    }

    @Test
    @DisplayName("Request object created correctly")
    void UnitTest_requestObjectCorrectlyCreated(){
        UpdateDriverDetailsRequest req=new UpdateDriverDetailsRequest(driverId,"n","s","e","pass","pN");
        assertNotNull(req);
        assertEquals(driverId,req.getDriverID());
        assertEquals("n",req.getName());
        assertEquals("s",req.getSurname());
        assertEquals("e",req.getEmail());
        assertEquals("pass",req.getPassword());
        assertEquals("pN",req.getPhoneNumber());
    }

    @Test
    @DisplayName("When driver with given UserID does not exist")
    void UnitTest_testingInvalidUser(){
        when(driverRepo.findById(Mockito.any())).thenReturn(null);
        Throwable thrown = Assertions.assertThrows(DriverDoesNotExistException.class, ()-> userService.updateDriverDetails(request));
        assertEquals("User with given userID does not exist - could not update driver", thrown.getMessage());
    }

    @Test
    @DisplayName("When an Invalid email is given")
    void UnitTest_testingInvalidEmail(){
        request.setEmail("invalid");
        when(driverRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(driver));

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

        when(driverRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(driver));

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
        request = new UpdateDriverDetailsRequest(driverId, null, null, null,
                null, null);
        when(driverRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(driver));

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
        when(driverRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(driver));
        when(driverRepo.findDriverByEmail(Mockito.any())).thenReturn(driver);
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
        when(driverRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(driver));

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
