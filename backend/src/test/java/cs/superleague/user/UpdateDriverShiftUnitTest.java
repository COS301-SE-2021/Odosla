package cs.superleague.user;

import cs.superleague.user.dataclass.Driver;
import cs.superleague.user.exceptions.DriverDoesNotExistException;
import cs.superleague.user.exceptions.InvalidRequestException;
import cs.superleague.user.repos.DriverRepo;
import cs.superleague.user.requests.UpdateDriverShiftRequest;
import cs.superleague.user.responses.UpdateDriverShiftResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class UpdateDriverShiftUnitTest {

    @Mock
    DriverRepo driverRepo;

    @InjectMocks
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
    void tearDown(){}

    @Test
    @DisplayName("When request object is not specified")
    void UnitTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.updateDriverShift(null));
        assertEquals("UpdateDriverShiftRequest object is null", thrown.getMessage());
    }

    @Test
    @DisplayName("When driverID parameter is not specified")
    void UnitTest_testingNullRequestDriverIDParameter(){
        request.setDriverID(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.updateDriverShift(request));
        assertEquals("DriverID in UpdateDriverShiftRequest is null", thrown.getMessage());
    }

    @Test
    @DisplayName("When onShift parameter is not specified")
    void UnitTest_testingNullRequestOnShiftParameter(){
        request.setOnShift(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.updateDriverShift(request));
        assertEquals("onShift in UpdateDriverShiftRequest is null", thrown.getMessage());
    }

    @Test
    @DisplayName("When driver with driverID does not exist")
    void UnitTest_testingDriverDoesNotExist(){
        Mockito.when(driverRepo.findById(Mockito.any())).thenReturn(null);
        Throwable thrown = Assertions.assertThrows(DriverDoesNotExistException.class, ()-> userService.updateDriverShift(request));
        assertEquals("Driver with driverID does not exist in database", thrown.getMessage());
    }

    @Test
    @DisplayName("When driver is already on Shift")
    void UnitTest_testingDriverAlreadyOnShift() throws InvalidRequestException, DriverDoesNotExistException {
        Mockito.when(driverRepo.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(driver));
        response= userService.updateDriverShift(request);
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNotNull(response.getTimestamp());
        assertEquals("Driver is already on shift",response.getMessage());
    }

    @Test
    @DisplayName("When driver is already not on Shift")
    void UnitTest_testingDriverAlreadyNotOnShift() throws InvalidRequestException, DriverDoesNotExistException {
        request.setOnShift(false);
        driver.setOnShift(false);
        Mockito.when(driverRepo.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(driver));
        response= userService.updateDriverShift(request);
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNotNull(response.getTimestamp());
        assertEquals("Driver is already not on shift",response.getMessage());
    }

    @Test
    @DisplayName("When driver couldn't be updated on database")
    void UnitTest_testingDriverCouldntBeUpdated() throws InvalidRequestException, DriverDoesNotExistException {
        request.setOnShift(false);
        driver.setOnShift(true);
        Driver newDriver = new Driver();
        newDriver.setDriverID(driverID);
        newDriver.setOnShift(true);
        Mockito.when(driverRepo.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(driver)).thenReturn(java.util.Optional.of(newDriver));
        response= userService.updateDriverShift(request);
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNotNull(response.getTimestamp());
        assertEquals("Couldn't update driver's shift",response.getMessage());
    }

    @Test
    @DisplayName("When driver's shift successfully updated")
    void UnitTest_testingDriverShiftSuccesfullyUpdated() throws InvalidRequestException, DriverDoesNotExistException {
        request.setOnShift(false);
        driver.setOnShift(true);
        Mockito.when(driverRepo.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(driver));
        response= userService.updateDriverShift(request);
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertNotNull(response.getTimestamp());
        assertEquals("Driver's shift correctly updated",response.getMessage());
    }
}
