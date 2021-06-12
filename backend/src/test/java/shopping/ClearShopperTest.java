package shopping;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Description;
import shopping.dataclass.Store;
import shopping.exceptions.InvalidRequestException;
import shopping.exceptions.StoreDoesNotExistException;
import shopping.repos.StoreRepo;
import shopping.requests.ClearShoppersRequest;
import shopping.requests.GetShoppersRequest;
import shopping.responses.GetShoppersResponse;
import user.dataclass.Shopper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClearShopperTest {

    @Mock
    private StoreRepo storeRepo;

    @InjectMocks
    private ShoppingServiceImpl shoppingService;
    UUID storeUUID1= UUID.randomUUID();
    Store store;
    Shopper shopper1;
    Shopper shopper2;
    List<Shopper> shopperList=new ArrayList<>();
    @BeforeEach
    void setUp() {
        store=new Store();
        shopper1=new Shopper();
        shopper2=new Shopper();
        shopperList.add(shopper1);
        shopperList.add(shopper2);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Description("Tests for when clearShoppers is submited with a null request object- exception should be thrown")
    @DisplayName("When request object is not specificed")
    void UnitTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> shoppingService.clearShoppers(null));
        assertEquals("Request object can't be null for clearShoppers", thrown.getMessage());
    }

    @Test
    @Description("Tests for when clearShoppers is submited store ID in request object being null- exception should be thrown")
    @DisplayName("When request object has null parameter")
    void UnitTest_StoreID_inRequest_NullRequestObject(){
        ClearShoppersRequest request=new ClearShoppersRequest(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> shoppingService.clearShoppers(request));
        assertEquals("Store ID in request object for clearShoppers can't be null", thrown.getMessage());
    }

    @Test
    @Description("Tests whether the clearShoppers request object was created correctly")
    @DisplayName("GetShoppers correctly constructed")
    void UnitTest_GetShoppersRequestConstruction() {
        ClearShoppersRequest request=new ClearShoppersRequest(storeUUID1);
        assertNotNull(request);
        assertEquals(storeUUID1, request.getStoreID());
    }

//    @Test
//    @Description("Test for when Store with storeID does not exist in database - StoreDoesNotExist Exception should be thrown")
//    @DisplayName("When Store with ID doesn't exist")
//    void UnitTest_Store_doesnt_exist(){
//        GetShoppersRequest request=new GetShoppersRequest(storeUUID1);
//        when(storeRepo.findById(Mockito.any())).thenReturn(null);
//        Throwable thrown = Assertions.assertThrows(StoreDoesNotExistException.class, ()-> shoppingService.getShoppers(request));
//        assertEquals("Store with ID does not exist in repository - could not get Shoppers", thrown.getMessage());
//    }
//
//    @Test
//    @Description("Test for when list of shoppers is null")
//    @DisplayName("List of Shoppers is null")
//    void Stores_shoppers_null() throws InvalidRequestException, StoreDoesNotExistException {
//        store.setStoreID(storeUUID1);
//        store.setShoppers(null);
//        GetShoppersRequest request=new GetShoppersRequest(storeUUID1);
//        when(storeRepo.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(store));
//        GetShoppersResponse response=shoppingService.getShoppers(request);
//        assertNotNull(response);
//        assertEquals(null,response.getListOfShoppers());
//        assertEquals(false,response.isSuccess());
//        assertEquals("List of Shoppers is null",response.getMessage());
//    }
//
//    @Test
//    @Description("Test for when list of shoppers is correct")
//    @DisplayName("Correct shoppers list")
//    void Correct_shoppers_list() throws InvalidRequestException, StoreDoesNotExistException {
//        store.setStoreID(storeUUID1);
//        store.setShoppers(shopperList);
//        GetShoppersRequest request=new GetShoppersRequest(storeUUID1);
//        when(storeRepo.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(store));
//        GetShoppersResponse response=shoppingService.getShoppers(request);
//        assertNotNull(response);
//        assertEquals(shopperList,response.getListOfShoppers());
//        assertEquals(true,response.isSuccess());
//        assertEquals("List of Shoppers successfully returned",response.getMessage());
//    }

}
