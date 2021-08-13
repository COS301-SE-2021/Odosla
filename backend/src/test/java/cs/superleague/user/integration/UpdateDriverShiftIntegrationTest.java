package cs.superleague.user.integration;

import cs.superleague.user.UserServiceImpl;
import cs.superleague.user.dataclass.Driver;
import cs.superleague.user.exceptions.DriverDoesNotExistException;
import cs.superleague.user.exceptions.InvalidRequestException;
import cs.superleague.user.repos.DriverRepo;
import cs.superleague.user.requests.UpdateDriverShiftRequest;
import cs.superleague.user.responses.UpdateDriverShiftResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class UpdateDriverShiftIntegrationTest {


    @Autowired
    DriverRepo driverRepo;

    @Autowired
    private UserServiceImpl userService;

    UpdateDriverShiftRequest request;
    UpdateDriverShiftResponse response;
    UUID driverID= UUID.randomUUID();
    Driver driver;

    @BeforeEach
    void setUp() {
        request=new UpdateDriverShiftRequest(driverID,true);
        driver=new Driver();
        driver.setDriverID(driverID);
        driver.setOnShift(true);
    }

    @AfterEach
    void tearDown(){
        driverRepo.deleteAll();
    }

    @Test
    @DisplayName("When request object is not specified")
    void IntegrationTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.updateDriverShift(null));
        assertEquals("UpdateDriverShiftRequest object is null", thrown.getMessage());
    }

    @Test
    @DisplayName("When driverID parameter is not specified")
    void IntegrationTest_testingNullRequestDriverIDParameter(){
        request.setDriverID(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.updateDriverShift(request));
        assertEquals("DriverID in UpdateDriverShiftRequest is null", thrown.getMessage());
    }

    @Test
    @DisplayName("When onShift parameter is not specified")
    void IntegrationTest_testingNullRequestOnShiftParameter(){
        request.setOnShift(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.updateDriverShift(request));
        assertEquals("onShift in UpdateDriverShiftRequest is null", thrown.getMessage());
    }

    @Test
    @DisplayName("When driver with driverID does not exist")
    void IntegrationTest_testingDriverDoesNotExist(){
        Throwable thrown = Assertions.assertThrows(DriverDoesNotExistException.class, ()-> userService.updateDriverShift(request));
        assertEquals("Driver with driverID does not exist in database", thrown.getMessage());
    }

    @Test
    @DisplayName("When driver is already on Shift")
    void IntegrationTest_testingDriverAlreadyOnShift() throws InvalidRequestException, DriverDoesNotExistException {
        driverRepo.save(driver);
        response= userService.updateDriverShift(request);
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNotNull(response.getTimestamp());
        assertEquals("Driver is already on shift",response.getMessage());
    }

    @Test
    @DisplayName("When driver is already not on Shift")
    void IntegrationTest_testingDriverAlreadyNotOnShift() throws InvalidRequestException, DriverDoesNotExistException {
        request.setOnShift(false);
        driver.setOnShift(false);
        driverRepo.save(driver);
        response= userService.updateDriverShift(request);
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNotNull(response.getTimestamp());
        assertEquals("Driver is already not on shift",response.getMessage());
    }

    @Test
    @DisplayName("When driver's shift successfully updated")
    void IntegrationTest_testingDriverShiftSuccessfullyUpdated() throws InvalidRequestException, DriverDoesNotExistException {
        request.setOnShift(false);
        driver.setOnShift(true);
        driverRepo.save(driver);
        response= userService.updateDriverShift(request);
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertNotNull(response.getTimestamp());
        assertEquals("Driver's shift correctly updated",response.getMessage());
    }
}
