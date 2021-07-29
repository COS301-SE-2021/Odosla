package cs.superleague.shopping;

import cs.superleague.shopping.dataclass.Catalogue;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.shopping.dataclass.Store;
import cs.superleague.shopping.exceptions.InvalidRequestException;
import cs.superleague.shopping.exceptions.StoreDoesNotExistException;
import cs.superleague.shopping.repos.StoreRepo;
import cs.superleague.shopping.requests.UpdateCatalogueRequest;
import cs.superleague.shopping.requests.UpdateShoppersRequest;
import cs.superleague.shopping.responses.UpdateCatalogueResponse;
import cs.superleague.shopping.responses.UpdateShoppersResponse;
import cs.superleague.user.dataclass.Shopper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Description;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateShoppersUnitTest {

    @Mock
    private StoreRepo storeRepo;

    @InjectMocks
    private ShoppingServiceImpl shoppingService;

    UUID storeUUID1= UUID.randomUUID();
    Store s;
    Shopper shopper1;
    Shopper shopper2;
    Shopper shopper3;
    Shopper shopper4;
    UUID shopperID1=UUID.randomUUID();
    UUID shopperID2=UUID.randomUUID();
    UUID shopperID3=UUID.randomUUID();
    UUID shopperID4=UUID.randomUUID();

    List<Shopper> shopperList=new ArrayList<>();
    List<Shopper> updatedShopperList=new ArrayList<>();

    @BeforeEach
    void setUp() {

        s=new Store();
        s.setStoreID(storeUUID1);

        shopper1=new Shopper();
        shopper1.setId(shopperID1);
        shopper2=new Shopper();
        shopper2.setId(shopperID2);
        shopper3=new Shopper();
        shopper3.setId(shopperID3);
        shopper4=new Shopper();
        shopper4.setId(shopperID4);

        shopperList.add(shopper1);
        shopperList.add(shopper2);

        updatedShopperList.add(shopper3);
        updatedShopperList.add(shopper4);

        s.setShoppers(shopperList);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Description("Tests for when UpdateShoppers is submitted with a null request object- exception should be thrown")
    @DisplayName("When request object is not specified")
    void UnitTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> shoppingService.updateShoppers(null));
        assertEquals("The request object for UpdateShoppersRequest is null - Could not update shoppers", thrown.getMessage());
    }

    @Test
    @Description("Tests for whether a request is submitted with a null parameter for shop in request object- exception should be thrown")
    @DisplayName("When request object parameter -shop - is not specified")
    void UnitTest_testingNull_shop_Parameter_RequestObject(){
        UpdateShoppersRequest request=new UpdateShoppersRequest(null, updatedShopperList);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> shoppingService.updateShoppers(request));
        assertEquals("The Store object in UpdateShoppersRequest parameter is null - Could not update shoppers", thrown.getMessage());
    }

    @Test
    @Description("Tests for whether a request is submitted with a null parameter for newShoppers in request object- exception should be thrown")
    @DisplayName("When request object parameter -newShoppers - is not specified")
    void UnitTest_testingNull_newShoppers_Parameter_RequestObject(){
        UpdateShoppersRequest request=new UpdateShoppersRequest(s, null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> shoppingService.updateShoppers(request));
        assertEquals("The newShoppers object in UpdateShoppersRequest parameter is null - Could not update shoppers", thrown.getMessage());
    }

    @Test
    @Description("Test for when Store with storeID does not exist in database - StoreDoesNotExist Exception should be thrown")
    @DisplayName("When Store with ID doesn't exist")
    void UnitTest_Store_doesnt_exist(){
        s.setStoreID(UUID.randomUUID());
        UpdateShoppersRequest request=new UpdateShoppersRequest(s, updatedShopperList);
        when(storeRepo.findById(Mockito.any())).thenReturn(null);
        Throwable thrown = Assertions.assertThrows(StoreDoesNotExistException.class, ()-> shoppingService.updateShoppers(request));
        assertEquals("Store with ID does not exist in repository - could not update Shoppers entity", thrown.getMessage());
    }

    @Test
    @Description("Test for when Store with storeID does exist in database - should return correct store entity")
    @DisplayName("When Store with ID does exist and shoppers list changes")
    void UnitTest_Store_does_exist_changing_shoppers() throws InvalidRequestException, StoreDoesNotExistException {
        UpdateShoppersRequest request=new UpdateShoppersRequest(s, updatedShopperList);
        when(storeRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(s));
        UpdateShoppersResponse response= shoppingService.updateShoppers(request);
        assertNotNull(response);
        assertEquals(true,response.getResponse());
        assertEquals("Shoppers updated successfully",response.getMessage());
        assertEquals(storeUUID1, response.getStoreID());
        assertEquals(s.getShoppers(), updatedShopperList);
    }

    /** Checking request object is created correctly */
    @Test
    @Description("Tests whether the UpdateShoppers request object was created correctly")
    @DisplayName("UpdateShoppersRequest correctly constructed")
    void UnitTest_UpdateShoppersRequestConstruction() {
        UpdateShoppersRequest request=new UpdateShoppersRequest(s, updatedShopperList);
        assertNotNull(request);
        assertEquals(updatedShopperList, request.getNewShoppers());
    }
}
