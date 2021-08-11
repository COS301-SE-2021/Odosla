package cs.superleague.user.integration;

import cs.superleague.user.UserServiceImpl;
import cs.superleague.user.dataclass.Driver;
import cs.superleague.user.exceptions.DriverDoesNotExistException;
import cs.superleague.user.exceptions.InvalidRequestException;
import cs.superleague.user.repos.DriverRepo;
import cs.superleague.user.requests.UpdateDriverDetailsRequest;
import cs.superleague.user.responses.UpdateDriverDetailsResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UpdateDriverDetailsIntegrationTest {

    @Autowired
    DriverRepo driverRepo;

    @Autowired
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
    void tearDown(){
        driverRepo.deleteAll();
    }

    @Test
    @DisplayName("When request object is not specified")
    void IntegrationTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.updateDriverDetails(null));
        assertEquals("UpdateDriver Request is null - Could not update driver", thrown.getMessage());
    }

    @Test
    @DisplayName("When userID parameter is not specified")
    void IntegrationTest_testingNullRequestUserIDParameter(){
        request.setDriverID(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.updateDriverDetails(request));
        assertEquals("DriverId is null - could not update driver", thrown.getMessage());
    }

    @Test
    @DisplayName("Request object created correctly")
    void IntegrationTest_requestObjectCorrectlyCreated(){
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
    void IntegrationTest_testingInvalidUser(){
        Throwable thrown = Assertions.assertThrows(DriverDoesNotExistException.class, ()-> userService.updateDriverDetails(request));
        assertEquals("User with given userID does not exist - could not update driver", thrown.getMessage());
    }

    @Test
    @DisplayName("When an Invalid email is given")
    void IntegrationTest_testingInvalidEmail(){
        request.setEmail("invalid");
        driver.setDriverID(driverId);
        driverRepo.save(driver);

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
    void IntegrationTest_testingInvalidPassword(){

        driver.setDriverID(driverId);
        driverRepo.save(driver);

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
    void IntegrationTest_testingNullUpdates(){
        request = new UpdateDriverDetailsRequest(driverId, null, null, null,
                null, null);

        driver.setDriverID(driverId);
        driverRepo.save(driver);

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
    void IntegrationTest_testingExistingEmailUpdateAttempt(){

        driver.setEmail("validEmail@gmail.com");
        driver.setDriverID(driverId);
        driverRepo.save(driver);

        Driver newDriver=new Driver();
        newDriver.setEmail(request.getEmail());
        newDriver.setDriverID(UUID.randomUUID());
        driverRepo.save(newDriver);

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
    void IntegrationTest_testingSuccessfulUpdate(){

        request.setPassword("validPassword@1");

        driver.setEmail("validEmail@gmail.com");
        driver.setDriverID(driverId);
        driverRepo.save(driver);

        try {
            response = userService.updateDriverDetails(request);
            assertEquals("Driver successfully updated", response.getMessage());
            assertTrue(response.isSuccess());
            assertNotNull(response.getTimestamp());

            /* Ensure driver with same ID's details have been changed */
            Optional<Driver> checkDriver=driverRepo.findById(driverId);
            assertNotNull(checkDriver);
            assertEquals(driverId, checkDriver.get().getDriverID());
            assertEquals(request.getEmail(),checkDriver.get().getEmail());
            assertEquals(request.getName(),checkDriver.get().getName());
            assertEquals(request.getSurname(),checkDriver.get().getSurname());
            assertEquals(request.getPhoneNumber(),checkDriver.get().getPhoneNumber());
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(15);
            passwordEncoder.matches(request.getPassword(),checkDriver.get().getPassword());

        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }
}
