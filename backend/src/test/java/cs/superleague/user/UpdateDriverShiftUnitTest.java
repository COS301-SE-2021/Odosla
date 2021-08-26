package cs.superleague.user;

import cs.superleague.integration.security.JwtUtil;
import cs.superleague.user.dataclass.Driver;
import cs.superleague.user.dataclass.Shopper;
import cs.superleague.user.dataclass.UserType;
import cs.superleague.user.exceptions.DriverDoesNotExistException;
import cs.superleague.user.exceptions.InvalidRequestException;
import cs.superleague.user.exceptions.UserException;
import cs.superleague.user.repos.DriverRepo;
import cs.superleague.user.repos.ShopperRepo;
import cs.superleague.user.requests.UpdateDriverShiftRequest;
import cs.superleague.user.responses.UpdateDriverShiftResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class UpdateDriverShiftUnitTest {

    @Mock
    DriverRepo driverRepo;

    @Mock
    ShopperRepo shopperRepo;

    @InjectMocks
    private UserServiceImpl userService;

    @InjectMocks
    JwtUtil jwtTokenUtil;

    UpdateDriverShiftRequest request;
    UpdateDriverShiftResponse response;
    UUID driverID= UUID.randomUUID();
    Driver driver;
    Shopper shopper;


    String shopperJWT;
    String driverJWT;

    @BeforeEach
    void setUp() {

        driver=new Driver();
        driver.setDriverID(driverID);
        driver.setOnShift(true);
        driver.setAccountType(UserType.DRIVER);
        driver.setEmail("driver@gmaill.com");

        shopper = new Shopper();
        shopper.setShopperID(UUID.randomUUID());
        shopper.setAccountType(UserType.SHOPPER);
        shopper.setEmail("Email@shhhopper.com");


        driverJWT = jwtTokenUtil.generateJWTTokenDriver(driver);
        shopperJWT = jwtTokenUtil.generateJWTTokenShopper(shopper);

        request=new UpdateDriverShiftRequest(true);
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
    @DisplayName("When onShift parameter is not specified")
    void UnitTest_testingNullRequestOnShiftParameter(){
        request.setOnShift(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.updateDriverShift(request));
        assertEquals("onShift in UpdateDriverShiftRequest is null", thrown.getMessage());
    }

    @Test
    @DisplayName("When driver is already on Shift")
    void UnitTest_testingDriverAlreadyOnShift() throws InvalidRequestException, DriverDoesNotExistException {
        Mockito.when(driverRepo.findDriverByEmail(Mockito.any())).thenReturn(driver);
        response= userService.updateDriverShift(request);
        assertNotNull(response);
        Assertions.assertFalse(response.isSuccess());
        assertNotNull(response.getTimestamp());
        assertEquals("Driver is already on shift",response.getMessage());
    }

    @Test
    @DisplayName("When driver is already not on Shift")
    void UnitTest_testingDriverAlreadyNotOnShift() throws InvalidRequestException, DriverDoesNotExistException {
        request.setOnShift(false);
        driver.setOnShift(false);
        Mockito.when(driverRepo.findDriverByEmail(Mockito.any())).thenReturn(driver);
        response= userService.updateDriverShift(request);
        assertNotNull(response);
        Assertions.assertFalse(response.isSuccess());
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
        Mockito.when(driverRepo.findDriverByEmail(Mockito.any())).thenReturn(driver).thenReturn(newDriver);
        response= userService.updateDriverShift(request);
        assertNotNull(response);
        Assertions.assertFalse(response.isSuccess());
        assertNotNull(response.getTimestamp());
        assertEquals("Couldn't update driver's shift",response.getMessage());
    }

    @Test
    @DisplayName("When driver's shift successfully updated")
    void UnitTest_testingDriverShiftSuccessfullyUpdated() throws InvalidRequestException, DriverDoesNotExistException {
        request.setOnShift(false);
        driver.setOnShift(true);
        Mockito.when(driverRepo.findDriverByEmail(Mockito.any())).thenReturn(driver);
        Mockito.when(driverRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(driver));
        response= userService.updateDriverShift(request);
        assertNotNull(response);
//        assertTrue(response.isSuccess());
        assertNotNull(response.getTimestamp());
        assertEquals("Driver's shift correctly updated",response.getMessage());
    }
}
