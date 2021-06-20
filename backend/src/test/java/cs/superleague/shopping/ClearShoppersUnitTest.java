package cs.superleague.shopping;

import cs.superleague.user.exceptions.UserDoesNotExistException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Description;
import cs.superleague.shopping.ShoppingServiceImpl;
import cs.superleague.shopping.dataclass.*;
import cs.superleague.shopping.exceptions.*;
import cs.superleague.shopping.requests.*;
import cs.superleague.shopping.responses.*;
import cs.superleague.shopping.repos.StoreRepo;
import cs.superleague.user.dataclass.Shopper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClearShoppersUnitTest {

    @Mock
    private StoreRepo storeRepo;

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
        shopper.setId(shopperID);
        shopper1=new Shopper();
        shopper1.setId(shopperID2);
        shopper2=new Shopper();
        shopper2.setId(shopperID3);
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

    @Test
    @Description("Test for when Store with storeID does not exist in database - StoreDoesNotExist Exception should be thrown")
    @DisplayName("When Store with ID doesn't exist")
    void UnitTest_Store_doesnt_exist(){
        ClearShoppersRequest request=new ClearShoppersRequest(storeUUID1);
        when(storeRepo.findById(Mockito.any())).thenReturn(null);
        Throwable thrown = Assertions.assertThrows(StoreDoesNotExistException.class, ()-> shoppingService.clearShoppers(request));
        assertEquals("Store with ID does not exist in repository - could not clear shoppers", thrown.getMessage());
    }
    @Test
    @Description("Test for when store is return with list of shoppers being null")
    @DisplayName("List of Shoppers in Store entity is null")
    void UnitTest_listOfShoppers_isNull() throws InvalidRequestException, cs.superleague.user.exceptions.InvalidRequestException, UserDoesNotExistException, StoreDoesNotExistException {
        store.setStoreID(storeUUID1);
        store.setShoppers(null);
        ClearShoppersRequest request=new ClearShoppersRequest(storeUUID1);
        when(storeRepo.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(store));
        ClearShoppersResponse response=shoppingService.clearShoppers(request);
        assertEquals(false,response.isSuccess());
        assertEquals("List of shoppers is null",response.getMessage());
    }

    @Test
    @Description("Test for when clear shoppers was sucessful")
    @DisplayName("Sucessfully cleared shoppers")
    void UnitTest_successfully_clearedShoppers() throws InvalidRequestException, cs.superleague.user.exceptions.InvalidRequestException, UserDoesNotExistException, StoreDoesNotExistException {
        store.setStoreID(storeUUID1);
        store.setShoppers(shopperList);
        ClearShoppersRequest request=new ClearShoppersRequest(storeUUID1);
        when(storeRepo.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(store));
        ClearShoppersResponse response=shoppingService.clearShoppers(request);
        assertEquals(true,response.isSuccess());
        assertEquals("List of Shopper successfuly cleared",response.getMessage());
    }


}
