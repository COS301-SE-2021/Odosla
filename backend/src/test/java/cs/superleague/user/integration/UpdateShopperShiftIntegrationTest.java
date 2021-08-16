package cs.superleague.user.integration;

import cs.superleague.integration.security.JwtUtil;
import cs.superleague.shopping.dataclass.Store;
import cs.superleague.shopping.exceptions.StoreDoesNotExistException;
import cs.superleague.shopping.repos.StoreRepo;
import cs.superleague.user.UserServiceImpl;
import cs.superleague.user.dataclass.Shopper;
import cs.superleague.user.exceptions.InvalidRequestException;
import cs.superleague.user.exceptions.ShopperDoesNotExistException;
import cs.superleague.user.repos.ShopperRepo;
import cs.superleague.user.requests.UpdateShopperShiftRequest;
import cs.superleague.user.responses.UpdateShopperShiftResponse;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
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
    StoreRepo storeRepo;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    JwtUtil jwtTokenUtil;

    UpdateShopperShiftRequest request;
    UpdateShopperShiftResponse response;
    UUID shopperID= UUID.randomUUID();
    Shopper shopper;
    String shopperJWT;
    Store store;
    UUID storeID = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        store = new Store();
        store.setStoreID(storeID);
        shopper=new Shopper();
        shopper.setShopperID(shopperID);
        shopper.setEmail("hello@gmail.com");
        shopper.setOnShift(true);
        shopperJWT = jwtTokenUtil.generateJWTTokenShopper(shopper);
        request=new UpdateShopperShiftRequest(shopperJWT,true, storeID);
        storeRepo.save(store);
    }

    @AfterEach
    void tearDown(){
        storeRepo.deleteAll();
        shopperRepo.deleteAll();

    }

    @Test
    @DisplayName("When request object is not specified")
    void IntegrationTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.updateShopperShift(null));
        assertEquals("UpdateShopperShiftRequest object is null", thrown.getMessage());
    }

    @Test
    @DisplayName("When jwtToken parameter is not specified")
    void IntegrationTest_testingNullRequestJWTTokenParameter(){
        request.setJwtToken(null);
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
    @DisplayName("When storeID parameter is not specified")
    void IntegrationTest_testingNullRequestStoreIDParameter(){
        request.setStoreID(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.updateShopperShift(request));
        assertEquals("StoreID in UpdateShopperShiftRequest is null", thrown.getMessage());
    }

    @Test
    @DisplayName("When shopper with shopperID does not exist")
    void IntegrationTest_testingShopperDoesNotExist(){
        Throwable thrown = Assertions.assertThrows(ShopperDoesNotExistException.class, ()-> userService.updateShopperShift(request));
        assertEquals("Shopper with shopperID does not exist in database", thrown.getMessage());
    }

    @Test
    @DisplayName("When shopper is already on Shift")
    void IntegrationTest_testingShopperAlreadyOnShift() throws InvalidRequestException, ShopperDoesNotExistException, StoreDoesNotExistException {
        shopperRepo.save(shopper);
        response= userService.updateShopperShift(request);
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNotNull(response.getTimestamp());
        assertEquals("Shopper is already on shift",response.getMessage());
    }

    @Test
    @DisplayName("When shopper is already not on Shift")
    void IntegrationTest_testingShopperAlreadyNotOnShift() throws InvalidRequestException, ShopperDoesNotExistException, StoreDoesNotExistException {
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
    @DisplayName("When shopper's shift successfully changed to false")
    void IntegrationTest_testingShopperShiftSuccessfullyUpdatedToFalse() throws InvalidRequestException, ShopperDoesNotExistException, StoreDoesNotExistException {
        request.setOnShift(false);
        shopper.setOnShift(true);
        shopperRepo.save(shopper);
        response= userService.updateShopperShift(request);
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertNotNull(response.getTimestamp());
        assertEquals("Shopper's shift correctly updated",response.getMessage());
        Optional<Store> store = storeRepo.findById(storeID);
        assertFalse(store.get().getShoppers().contains(shopper));
    }

    @Test
    @DisplayName("When shopper's shift successfully changed to true")
    void IntegrationTest_testingShopperShiftSuccessfullyUpdatedToTrue() throws InvalidRequestException, ShopperDoesNotExistException, StoreDoesNotExistException {
        request.setOnShift(true);
        shopper.setOnShift(false);
        shopperRepo.save(shopper);
        response= userService.updateShopperShift(request);
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertNotNull(response.getTimestamp());
        assertEquals("Shopper's shift correctly updated",response.getMessage());
        Optional<Store> store = storeRepo.findById(storeID);
        Optional<Shopper> shopper = shopperRepo.findById(shopperID);
        assertTrue(store.get().getShoppers().get(0).getShopperID().equals(shopperID));
    }


    @Test
    @DisplayName("When the shop isn't in the database")
    void IntegrationTest_ShopIsNotInTheDataBase() {
        request.setStoreID(UUID.randomUUID());
        request.setOnShift(false);
        shopper.setOnShift(true);
        shopperRepo.save(shopper);
        Throwable thrown = Assertions.assertThrows(StoreDoesNotExistException.class, ()-> userService.updateShopperShift(request));
        assertEquals(thrown.getMessage(), "Store is not saved in database.");
    }
}
