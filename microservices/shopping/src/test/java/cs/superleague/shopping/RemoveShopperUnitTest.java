package cs.superleague.shopping;

import cs.superleague.shopping.dataclass.Store;
import cs.superleague.shopping.exceptions.InvalidRequestException;
import cs.superleague.shopping.exceptions.StoreDoesNotExistException;
import cs.superleague.shopping.repos.StoreRepo;
import cs.superleague.shopping.requests.RemoveShopperRequest;
import cs.superleague.shopping.responses.RemoveShopperResponse;
import cs.superleague.user.dataclass.Shopper;
import cs.superleague.user.exceptions.UserException;
import cs.superleague.user.responses.GetShopperByUUIDResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RemoveShopperUnitTest {

    @Value("${paymentHost}")
    private String paymentHost;
    @Value("${paymentPort}")
    private String paymentPort;
    @Value("${userHost}")
    private String userHost;
    @Value("${userPort}")
    private String userPort;

    @Mock
    private StoreRepo storeRepo;

    @Mock
    RestTemplate restTemplate;

    @Mock
    RabbitTemplate rabbit;

    @InjectMocks
    private ShoppingServiceImpl shoppingService;

    UUID storeUUID1= UUID.randomUUID();
    Store store;
    Shopper shopper;
    Shopper shopper1;
    Shopper shopper2;
    UUID shopperID=UUID.randomUUID();
    UUID shopperID2=UUID.randomUUID();
    UUID shopperID3=UUID.randomUUID();
    UUID storeID=UUID.randomUUID();
    List<Shopper> shopperList=new ArrayList<>();

    @BeforeEach
    void setUp() {
        store=new Store();
        shopper=new Shopper();
        shopper.setShopperID(shopperID);
        shopper1=new Shopper();
        shopper1.setShopperID(shopperID2);
        shopper2=new Shopper();
        shopper2.setShopperID(shopperID3);
        shopperList.add(shopper1);
        shopperList.add(shopper2);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Description("Tests for when removeShoppers is submited with a null request object- exception should be thrown")
    @DisplayName("When request object is not specificed")
    void UnitTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> shoppingService.removeShopper(null));
        assertEquals("Request object can't be null for removeShopper", thrown.getMessage());
    }

    @Test
    @Description("Tests for when removeShoppers is submited store ID in request object being null- exception should be thrown")
    @DisplayName("When request object has null parameter")
    void UnitTest_StoreID_inRequest_NullRequestObject(){
        RemoveShopperRequest request=new RemoveShopperRequest(shopperID,null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> shoppingService.removeShopper(request));
        assertEquals("Store ID in request object for remove shopper is null", thrown.getMessage());
    }
    @Test
    @Description("Tests for when removeShoppers is submited shopper ID in request object being null- exception should be thrown")
    @DisplayName("When request object has null parameter")
    void UnitTest_ShopperID_inRequest_NullRequestObject(){
        RemoveShopperRequest request=new RemoveShopperRequest(null,storeID);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> shoppingService.removeShopper(request));
        assertEquals("Shopper ID in request object for remove shopper is null", thrown.getMessage());
    }

    @Test
    @Description("Tests whether the removeShoppers request object was created correctly")
    @DisplayName("RemoveShopper Request correctly constructed")
    void UnitTest_AddShoppersRequestConstruction() {
        RemoveShopperRequest request=new RemoveShopperRequest(shopperID,storeID);
        assertNotNull(request);
        assertEquals(storeID,request.getStoreID());
        assertEquals(shopperID,request.getShopperID());
    }

    @Test
    @Description("Test for when Store with storeID does not exist in database - StoreDoesNotExist Exception should be thrown")
    @DisplayName("When Store with ID doesn't exist")
    void UnitTest_Store_doesnt_exist(){
        RemoveShopperRequest request=new RemoveShopperRequest(shopperID,storeID);
        when(storeRepo.findById(Mockito.any())).thenReturn(null);
        Throwable thrown = Assertions.assertThrows(StoreDoesNotExistException.class, ()-> shoppingService.removeShopper(request));
        assertEquals("Store with ID does not exist in repository - could not remove Shopper", thrown.getMessage());
    }

    @Test
    @Description("Test for when store is return with list of shoppers being null")
    @DisplayName("List of Shoppers in Store entity is null")
    void UnitTest_listOfShoppers_isNull() throws InvalidRequestException, UserException, StoreDoesNotExistException {
        store.setStoreID(storeUUID1);
        store.setShoppers(null);
        RemoveShopperRequest request=new RemoveShopperRequest(shopperID,storeID);
        when(storeRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(store));
        RemoveShopperResponse response=shoppingService.removeShopper(request);
        assertEquals(false,response.isSuccess());
        assertEquals("list of Shoppers is null",response.getMessage());
    }

    @Test
    @Description("Test for when Shopper with shopperID does not exist in shopper database ")
    @DisplayName("When Shopper with ID doesn't exist")
    void UnitTest_Shopper_doesnt_exist() throws InvalidRequestException, UserException, StoreDoesNotExistException, URISyntaxException {
        store.setStoreID(storeUUID1);
        store.setShoppers(shopperList);
        RemoveShopperRequest request=new RemoveShopperRequest(shopperID,storeUUID1);
        when(storeRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(store));

        GetShopperByUUIDResponse shopperResponse=new GetShopperByUUIDResponse(shopper1,null,null);

        String stringUri = "http://"+userHost+":"+userPort+"/user/getShopperByUUID";
        URI uri = new URI(stringUri);

        Map<String, Object> parts = new HashMap<String, Object>();
        parts.put("userID", shopper.getShopperID().toString());

        ResponseEntity<GetShopperByUUIDResponse> getShopperByUUIDResponseResponseEntity =
                new ResponseEntity<>(null, HttpStatus.OK);

        Mockito.when(restTemplate.postForEntity(stringUri, parts, GetShopperByUUIDResponse.class))
                .thenReturn(getShopperByUUIDResponseResponseEntity);

//        when(userService.getShopperByUUIDRequest(Mockito.any())).thenThrow(new cs.superleague.user.exceptions.InvalidRequestException("User does not exist in database"));
        Throwable thrown = Assertions.assertThrows(cs.superleague.user.exceptions.InvalidRequestException.class, ()-> shoppingService.removeShopper(request));
        assertEquals("User does not exist in database", thrown.getMessage());
    }


    @Test
    @Description("Test for when list of shoppers doesn't contain shopper")
    @DisplayName("Shopper Id not in list of Shoppers")
    void UnitTest_shopper_not_in_shopper_list() throws InvalidRequestException, StoreDoesNotExistException, UserException {
        store.setStoreID(storeUUID1);
        store.setShoppers(shopperList);
        RemoveShopperRequest request=new RemoveShopperRequest(shopperID,storeUUID1);
        when(storeRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(store));
        GetShopperByUUIDResponse shopperResponse=new GetShopperByUUIDResponse(shopper,null,null);

        String stringUri = "http://"+userHost+":"+userPort+"/user/getShopperByUUID";

        Map<String, Object> parts = new HashMap<String, Object>();
        parts.put("userID", shopper.getShopperID().toString());

        ResponseEntity<GetShopperByUUIDResponse> getShopperByUUIDResponseResponseEntity =
                new ResponseEntity<>(shopperResponse, HttpStatus.OK);

        Mockito.when(restTemplate.postForEntity(stringUri, parts, GetShopperByUUIDResponse.class))
                .thenReturn(getShopperByUUIDResponseResponseEntity);

//        when(userService.getShopperByUUIDRequest(Mockito.any())).thenReturn(shopperResponse);
        RemoveShopperResponse response=shoppingService.removeShopper(request);
        assertNotNull(response);
        assertEquals(false,response.isSuccess());
        assertEquals("Shopper isn't in list of shoppers in store entity",response.getMessage());
    }

    @Test
    @Description("Test for when shopper is correctly removed from the list")
    @DisplayName("Shopper was correctly removed from list of shoppers in store")
    void Shopper_correctly_removed() throws InvalidRequestException, StoreDoesNotExistException, UserException {
        store.setStoreID(storeUUID1);
        store.setShoppers(shopperList);
        RemoveShopperRequest request=new RemoveShopperRequest(shopper1.getShopperID(),storeUUID1);
        when(storeRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(store));
        GetShopperByUUIDResponse shopperResponse=new GetShopperByUUIDResponse(shopper1,null,null);

        String stringUri = "http://"+userHost+":"+userPort+"/user/getShopperByUUID";

        Map<String, Object> parts = new HashMap<String, Object>();
        parts.put("userID", shopper1.getShopperID().toString());

        ResponseEntity<GetShopperByUUIDResponse> getShopperByUUIDResponseResponseEntity =
                new ResponseEntity<>(shopperResponse, HttpStatus.OK);

        Mockito.when(restTemplate.postForEntity(stringUri, parts, GetShopperByUUIDResponse.class))
                .thenReturn(getShopperByUUIDResponseResponseEntity);

//        when(userService.getShopperByUUIDRequest(Mockito.any())).thenReturn(shopperResponse);
        RemoveShopperResponse response=shoppingService.removeShopper(request);
        assertNotNull(response);
        assertEquals(true,response.isSuccess());
        assertEquals("Shopper was successfully removed",response.getMessage());
    }
}
