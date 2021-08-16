package cs.superleague.user;

import cs.superleague.integration.security.JwtUtil;
import cs.superleague.shopping.dataclass.Store;
import cs.superleague.shopping.exceptions.StoreDoesNotExistException;
import cs.superleague.shopping.repos.StoreRepo;
import cs.superleague.user.dataclass.Shopper;
import cs.superleague.user.exceptions.InvalidRequestException;
import cs.superleague.user.exceptions.ShopperDoesNotExistException;
import cs.superleague.user.repos.ShopperRepo;
import cs.superleague.user.requests.UpdateShopperShiftRequest;
import cs.superleague.user.responses.UpdateShopperShiftResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class UpdateShopperShiftUnitTest {

    @Mock
    ShopperRepo shopperRepo;

    @Mock
    StoreRepo storeRepo;

    @InjectMocks
    private UserServiceImpl userService;

    @InjectMocks
    JwtUtil jwtTokenUtil;

    UpdateShopperShiftRequest request;
    UpdateShopperShiftResponse response;
    UUID shopperID= UUID.randomUUID();
    Shopper shopper;
    String shopperJWT;
    Store store;
    UUID storeID;

    @BeforeEach
    void setUp() {
        storeID = UUID.randomUUID();
        store = new Store();
        store.setStoreID(storeID);
        shopper=new Shopper();
        shopper.setEmail("hello@gmail.com");
        shopper.setShopperID(shopperID);
        shopper.setOnShift(true);
        shopperJWT = jwtTokenUtil.generateJWTTokenShopper(shopper);
        request=new UpdateShopperShiftRequest(shopperJWT,true, storeID);
    }

    @AfterEach
    void tearDown(){}

    @Test
    @DisplayName("When request object is not specified")
    void UnitTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.updateShopperShift(null));
        assertEquals("UpdateShopperShiftRequest object is null", thrown.getMessage());
    }

    @Test
    @DisplayName("When jwtToken parameter is not specified")
    void UnitTest_testingNullRequestJwtTokenParameter(){
        request.setJwtToken(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.updateShopperShift(request));
        assertEquals("ShopperID in UpdateShopperShiftRequest is null", thrown.getMessage());
    }

    @Test
    @DisplayName("When onShift parameter is not specified")
    void UnitTest_testingNullRequestOnShiftParameter(){
        request.setOnShift(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.updateShopperShift(request));
        assertEquals("onShift in UpdateShopperShiftRequest is null", thrown.getMessage());
    }

    @Test
    @DisplayName("When storeID parameter is not specified")
    void UnitTest_testingNullRequestStoreIDParameter(){
        request.setStoreID(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.updateShopperShift(request));
        assertEquals("StoreID in UpdateShopperShiftRequest is null", thrown.getMessage());
    }

    @Test
    @DisplayName("When shopper with shopperID does not exist")
    void UnitTest_testingShopperDoesNotExist(){
        Mockito.when(shopperRepo.findByEmail(Mockito.any())).thenReturn(java.util.Optional.ofNullable(null));
        Throwable thrown = Assertions.assertThrows(ShopperDoesNotExistException.class, ()-> userService.updateShopperShift(request));
        assertEquals("Shopper with shopperID does not exist in database", thrown.getMessage());
    }

    @Test
    @DisplayName("When shopper is already on Shift")
    void UnitTest_testingShopperAlreadyOnShift() throws InvalidRequestException, ShopperDoesNotExistException, StoreDoesNotExistException {
        Mockito.when(shopperRepo.findByEmail(Mockito.any())).thenReturn(java.util.Optional.ofNullable(shopper));
        Mockito.when(shopperRepo.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(shopper));
        response= userService.updateShopperShift(request);
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNotNull(response.getTimestamp());
        assertEquals("Shopper is already on shift",response.getMessage());
    }

    @Test
    @DisplayName("When shopper is already not on Shift")
    void UnitTest_testingShopperAlreadyNotOnShift() throws InvalidRequestException, ShopperDoesNotExistException, StoreDoesNotExistException {
        request.setOnShift(false);
        shopper.setOnShift(false);
        Mockito.when(shopperRepo.findByEmail(Mockito.any())).thenReturn(java.util.Optional.ofNullable(shopper));
        Mockito.when(shopperRepo.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(shopper));
        response= userService.updateShopperShift(request);
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNotNull(response.getTimestamp());
        assertEquals("Shopper is already not on shift",response.getMessage());
    }

    @Test
    @DisplayName("When shopper couldn't be updated on database")
    void UnitTest_testingShopperCouldntBeUpdated() throws InvalidRequestException, ShopperDoesNotExistException, StoreDoesNotExistException {
        request.setOnShift(false);
        shopper.setOnShift(true);
        Shopper newShopper = new Shopper();
        newShopper.setShopperID(shopperID);
        newShopper.setOnShift(true);
        Mockito.when(shopperRepo.findByEmail(Mockito.any())).thenReturn(java.util.Optional.ofNullable(shopper));
        Mockito.when(storeRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(store));
        Mockito.when(shopperRepo.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(shopper)).thenReturn(java.util.Optional.of(newShopper));
        response= userService.updateShopperShift(request);
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNotNull(response.getTimestamp());
        assertEquals("Couldn't update shopper's shift",response.getMessage());
    }

    @Test
    @DisplayName("When shopper's shift successfully updated to false")
    void UnitTest_testingShopperShiftSuccesfullyUpdatedToFalse() throws InvalidRequestException, ShopperDoesNotExistException, StoreDoesNotExistException {
        request.setOnShift(false);
        shopper.setOnShift(true);
        Mockito.when(shopperRepo.findByEmail(Mockito.any())).thenReturn(java.util.Optional.ofNullable(shopper));
        Mockito.when(shopperRepo.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(shopper));
        Mockito.when(storeRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(store));
        response= userService.updateShopperShift(request);
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertNotNull(response.getTimestamp());
        assertEquals("Shopper's shift correctly updated",response.getMessage());
    }

    @Test
    @DisplayName("When shopper's shift successfully updated to true")
    void UnitTest_testingShopperShiftSuccesfullyUpdatedToTrue() throws InvalidRequestException, ShopperDoesNotExistException, StoreDoesNotExistException {
        request.setOnShift(true);
        shopper.setOnShift(false);
        Mockito.when(shopperRepo.findByEmail(Mockito.any())).thenReturn(java.util.Optional.ofNullable(shopper));
        Mockito.when(shopperRepo.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(shopper));
        Mockito.when(storeRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(store));
        response= userService.updateShopperShift(request);
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertNotNull(response.getTimestamp());
        assertEquals("Shopper's shift correctly updated",response.getMessage());
    }

    @Test
    @DisplayName("When invalid ShopID is passed in")
    void UnitTest_InvalidShopIDPassedInRequestObject(){
        request.setOnShift(false);
        shopper.setOnShift(true);
        Mockito.when(shopperRepo.findByEmail(Mockito.any())).thenReturn(java.util.Optional.ofNullable(shopper));
        Mockito.when(shopperRepo.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(shopper));
        Mockito.when(storeRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(null));
        Throwable thrown = Assertions.assertThrows(StoreDoesNotExistException.class, ()-> response= userService.updateShopperShift(request));
        assertEquals(thrown.getMessage(), "Store is not saved in database.");
    }
}
