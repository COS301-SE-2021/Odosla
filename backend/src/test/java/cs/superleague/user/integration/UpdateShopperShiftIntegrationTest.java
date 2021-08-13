package cs.superleague.user.integration;

import cs.superleague.user.UserServiceImpl;
import cs.superleague.user.dataclass.Shopper;
import cs.superleague.user.exceptions.InvalidRequestException;
import cs.superleague.user.exceptions.ShopperDoesNotExistException;
import cs.superleague.user.repos.ShopperRepo;
import cs.superleague.user.requests.UpdateShopperShiftRequest;
import cs.superleague.user.responses.UpdateShopperShiftResponse;
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
public class UpdateShopperShiftIntegrationTest {


    @Autowired
    ShopperRepo shopperRepo;

    @Autowired
    private UserServiceImpl userService;

    UpdateShopperShiftRequest request;
    UpdateShopperShiftResponse response;
    UUID shopperID= UUID.randomUUID();
    Shopper shopper;

    @BeforeEach
    void setUp() {
        request=new UpdateShopperShiftRequest(shopperID,true);
        shopper=new Shopper();
        shopper.setShopperID(shopperID);
        shopper.setOnShift(true);
    }

    @AfterEach
    void tearDown(){
        shopperRepo.deleteAll();
    }

    @Test
    @DisplayName("When request object is not specified")
    void IntegrationTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.updateShopperShift(null));
        assertEquals("UpdateShopperShiftRequest object is null", thrown.getMessage());
    }

    @Test
    @DisplayName("When shopperID parameter is not specified")
    void IntegrationTest_testingNullRequestShopperIDParameter(){
        request.setShopperID(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.updateShopperShift(request));
        assertEquals("ShopperID in UpdateShopperShiftRequest is null", thrown.getMessage());
    }

    @Test
    @DisplayName("When onShift parameter is not specified")
    void IntegrationTest_testingNullRequestOnShiftParameter(){
        request.setOnShift(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.updateShopperShift(request));
        assertEquals("onShift in UpdateShopperShiftRequest is null", thrown.getMessage());
    }

    @Test
    @DisplayName("When shopper with shopperID does not exist")
    void IntegrationTest_testingShopperDoesNotExist(){
        Throwable thrown = Assertions.assertThrows(ShopperDoesNotExistException.class, ()-> userService.updateShopperShift(request));
        assertEquals("Shopper with shopperID does not exist in database", thrown.getMessage());
    }

    @Test
    @DisplayName("When shopper is already on Shift")
    void IntegrationTest_testingShopperAlreadyOnShift() throws InvalidRequestException, ShopperDoesNotExistException {
        shopperRepo.save(shopper);
        response= userService.updateShopperShift(request);
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNotNull(response.getTimestamp());
        assertEquals("Shopper is already on shift",response.getMessage());
    }

    @Test
    @DisplayName("When shopper is already not on Shift")
    void IntegrationTest_testingShopperAlreadyNotOnShift() throws InvalidRequestException, ShopperDoesNotExistException {
        request.setOnShift(false);
        shopper.setOnShift(false);
        shopperRepo.save(shopper);
        response= userService.updateShopperShift(request);
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNotNull(response.getTimestamp());
        assertEquals("Shopper is already not on shift",response.getMessage());
    }

    @Test
    @DisplayName("When shopper's shift successfully updated")
    void IntegrationTest_testingShopperShiftSuccessfullyUpdated() throws InvalidRequestException, ShopperDoesNotExistException {
        request.setOnShift(false);
        shopper.setOnShift(true);
        shopperRepo.save(shopper);
        response= userService.updateShopperShift(request);
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertNotNull(response.getTimestamp());
        assertEquals("Shopper's shift correctly updated",response.getMessage());
    }
}
