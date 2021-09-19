package cs.superleague.user;

import cs.superleague.integration.security.JwtUtil;
import cs.superleague.shopping.dataclass.Store;
import cs.superleague.shopping.exceptions.StoreDoesNotExistException;
import cs.superleague.shopping.responses.GetStoreByUUIDResponse;
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
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class UpdateShopperShiftUnitTest {

    @Mock
    ShopperRepo shopperRepo;
    @Mock
    RestTemplate restTemplate;
    @Mock
    RabbitTemplate rabbitTemplate;
    @Value("${paymentPort}")
    private String paymentPort;
    @Value("${paymentHost}")
    private String paymentHost;
    @Value("${deliveryPort}")
    private String deliveryPort;
    @Value("${deliveryHost}")
    private String deliveryHost;
    @Value("${shoppingPort}")
    private String shoppingPort;
    @Value("${shoppingHost}")
    private String shoppingHost;
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
        request=new UpdateShopperShiftRequest(true, storeID);
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
    void UnitTest_testingShopperAlreadyOnShift() throws InvalidRequestException, ShopperDoesNotExistException, StoreDoesNotExistException, URISyntaxException {
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
    void UnitTest_testingShopperAlreadyNotOnShift() throws InvalidRequestException, ShopperDoesNotExistException, StoreDoesNotExistException, URISyntaxException {
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
    void UnitTest_testingShopperCouldntBeUpdated() throws InvalidRequestException, ShopperDoesNotExistException, StoreDoesNotExistException, URISyntaxException {
        request.setOnShift(false);
        shopper.setOnShift(true);
        Shopper newShopper = new Shopper();
        newShopper.setShopperID(shopperID);
        newShopper.setOnShift(true);
        Mockito.when(shopperRepo.findByEmail(Mockito.any())).thenReturn(java.util.Optional.ofNullable(shopper));
        //Mockito.when(storeRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(store));
        Map<String, Object> parts = new HashMap<String, Object>();
        parts.put("storeID", request.getStoreID());
        String uriString = "http://"+shoppingHost+":"+shoppingPort+"/shopping/getStoreByUUID";
        URI uri = new URI(uriString);
        GetStoreByUUIDResponse getStoreByUUIDResponse = new GetStoreByUUIDResponse(store, new Date(), "");
        ResponseEntity<GetStoreByUUIDResponse> getStoreByUUIDResponseEntity = new ResponseEntity<>(getStoreByUUIDResponse, HttpStatus.OK);
        Mockito.when(restTemplate.postForEntity(uri, parts, GetStoreByUUIDResponse.class)).thenReturn(getStoreByUUIDResponseEntity);

        Mockito.when(shopperRepo.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(shopper)).thenReturn(java.util.Optional.of(newShopper));
        response= userService.updateShopperShift(request);
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNotNull(response.getTimestamp());
        assertEquals("Couldn't update shopper's shift",response.getMessage());
    }

    @Test
    @DisplayName("When shopper's shift successfully updated to false")
    void UnitTest_testingShopperShiftSuccesfullyUpdatedToFalse() throws InvalidRequestException, ShopperDoesNotExistException, StoreDoesNotExistException, URISyntaxException {
        request.setOnShift(false);
        shopper.setOnShift(true);
        Mockito.when(shopperRepo.findByEmail(Mockito.any())).thenReturn(java.util.Optional.ofNullable(shopper));
        Mockito.when(shopperRepo.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(shopper));
        //Mockito.when(storeRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(store));
        Map<String, Object> parts = new HashMap<String, Object>();
        parts.put("storeID", request.getStoreID());
        String uriString = "http://"+shoppingHost+":"+shoppingPort+"/shopping/getStoreByUUID";
        URI uri = new URI(uriString);
        GetStoreByUUIDResponse getStoreByUUIDResponse = new GetStoreByUUIDResponse(store, new Date(), "");
        ResponseEntity<GetStoreByUUIDResponse> getStoreByUUIDResponseEntity = new ResponseEntity<>(getStoreByUUIDResponse, HttpStatus.OK);
        Mockito.when(restTemplate.postForEntity(uri, parts, GetStoreByUUIDResponse.class)).thenReturn(getStoreByUUIDResponseEntity);

        response= userService.updateShopperShift(request);
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertNotNull(response.getTimestamp());
        assertEquals("Shopper's shift correctly updated",response.getMessage());
    }

    @Test
    @DisplayName("When shopper's shift successfully updated to true")
    void UnitTest_testingShopperShiftSuccesfullyUpdatedToTrue() throws InvalidRequestException, ShopperDoesNotExistException, StoreDoesNotExistException, URISyntaxException {
        request.setOnShift(true);
        shopper.setOnShift(false);
        Mockito.when(shopperRepo.findByEmail(Mockito.any())).thenReturn(java.util.Optional.ofNullable(shopper));
        Mockito.when(shopperRepo.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(shopper));
        //Mockito.when(storeRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(store));
        Map<String, Object> parts = new HashMap<String, Object>();
        parts.put("storeID", request.getStoreID());
        String uriString = "http://"+shoppingHost+":"+shoppingPort+"/shopping/getStoreByUUID";
        URI uri = new URI(uriString);
        GetStoreByUUIDResponse getStoreByUUIDResponse = new GetStoreByUUIDResponse(store, new Date(), "");
        ResponseEntity<GetStoreByUUIDResponse> getStoreByUUIDResponseEntity = new ResponseEntity<>(getStoreByUUIDResponse, HttpStatus.OK);
        Mockito.when(restTemplate.postForEntity(uri, parts, GetStoreByUUIDResponse.class)).thenReturn(getStoreByUUIDResponseEntity);

        response= userService.updateShopperShift(request);
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertNotNull(response.getTimestamp());
        assertEquals("Shopper's shift correctly updated",response.getMessage());
    }

    @Test
    @DisplayName("When invalid ShopID is passed in")
    void UnitTest_InvalidShopIDPassedInRequestObject() throws URISyntaxException {
        request.setOnShift(false);
        shopper.setOnShift(true);
        Mockito.when(shopperRepo.findByEmail(Mockito.any())).thenReturn(java.util.Optional.ofNullable(shopper));
        Mockito.when(shopperRepo.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(shopper));
        //Mockito.when(storeRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(null));
        Map<String, Object> parts = new HashMap<String, Object>();
        parts.put("storeID", request.getStoreID());
        String uriString = "http://"+shoppingHost+":"+shoppingPort+"/shopping/getStoreByUUID";
        URI uri = new URI(uriString);
        GetStoreByUUIDResponse getStoreByUUIDResponse = new GetStoreByUUIDResponse(null, new Date(), "");
        ResponseEntity<GetStoreByUUIDResponse> getStoreByUUIDResponseEntity = new ResponseEntity<>(getStoreByUUIDResponse, HttpStatus.OK);
        Mockito.when(restTemplate.postForEntity(uri, parts, GetStoreByUUIDResponse.class)).thenReturn(getStoreByUUIDResponseEntity);

        Throwable thrown = Assertions.assertThrows(StoreDoesNotExistException.class, ()-> response= userService.updateShopperShift(request));
        assertEquals(thrown.getMessage(), "Store is not saved in database.");
    }
}
