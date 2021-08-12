package cs.superleague.shopping;

import cs.superleague.shopping.dataclass.Catalogue;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.shopping.dataclass.Store;
import cs.superleague.shopping.exceptions.InvalidRequestException;
import cs.superleague.shopping.exceptions.StoreDoesNotExistException;
import cs.superleague.shopping.repos.StoreRepo;
import cs.superleague.shopping.requests.UpdateCatalogueRequest;
import cs.superleague.shopping.requests.UpdateStoreRequest;
import cs.superleague.shopping.responses.UpdateCatalogueResponse;
import cs.superleague.shopping.responses.UpdateStoreResponse;
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
public class UpdateStoreUnitTest {

    @Mock
    private StoreRepo storeRepo;

    @InjectMocks
    private ShoppingServiceImpl shoppingService;

    UUID storeUUID1= UUID.randomUUID();
    Store s;
    Catalogue c;
    Store s2;
    Item i1;
    Item i2;
    Item i3;
    Item i4;
    List<Item> listOfItems=new ArrayList<>();
    List<Item> listOfItems2=new ArrayList<>();

    @BeforeEach
    void setUp() {
        i1=new Item("Heinz Tomato Sauce","123456","123456",storeUUID1,36.99,1,"description","img/");
        i2=new Item("Bar one","012345","012345",storeUUID1,14.99,3,"description","img/");
        i3=new Item("Milk","901234","901234",storeUUID1,30.00,1,"description","img/");
        i4=new Item("Bread","890123","890123",storeUUID1,36.99,1,"description","img/");
        listOfItems.add(i1);
        listOfItems.add(i2);
        c=new Catalogue(storeUUID1, listOfItems);
        listOfItems2.add(i1);
        listOfItems2.add(i2);
        listOfItems2.add(i3);
        listOfItems2.add(i4);
        s=new Store(storeUUID1,"Woolworths",c,2,null,null,4,true);
        s2=new Store(storeUUID1, 7, 21, "PnP", 2,6,true);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Description("Tests for when UpdateStore is submitted with a null request object- exception should be thrown")
    @DisplayName("When request object is not specified")
    void UnitTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> shoppingService.updateStore(null));
        assertEquals("The request object for UpdateStoreRequest is null - Could not update store", thrown.getMessage());
    }

    @Test
    @Description("Tests for whether a request is submitted with a null parameter for store in request object- exception should be thrown")
    @DisplayName("When request object parameter -Store - is not specified")
    void UnitTest_testingNull_store_Parameter_RequestObject(){
        UpdateStoreRequest request=new UpdateStoreRequest(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> shoppingService.updateStore(request));
        assertEquals("The Store object in UpdateStoreRequest parameter is null - Could not update store", thrown.getMessage());
    }

    @Test
    @Description("Test for when Store with storeID does not exist in database - StoreDoesNotExist Exception should be thrown")
    @DisplayName("When Store with ID doesn't exist")
    void UnitTest_Store_doesnt_exist(){
        s2.setStoreID(UUID.randomUUID());
        UpdateStoreRequest request=new UpdateStoreRequest(s2);
        when(storeRepo.findById(Mockito.any())).thenReturn(null);
        Throwable thrown = Assertions.assertThrows(StoreDoesNotExistException.class, ()-> shoppingService.updateStore(request));
        assertEquals("Store with ID does not exist in repository - could not update Store entity", thrown.getMessage());
    }

    @Test
    @Description("Test for when Store with storeID does exist in database - should return correct store entity")
    @DisplayName("When Store with ID does exist and store updates")
    void UnitTest_Store_does_exist_changing_Store() throws InvalidRequestException, StoreDoesNotExistException {
        UpdateStoreRequest request=new UpdateStoreRequest(s2);
        when(storeRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(s));
        UpdateStoreResponse response= shoppingService.updateStore(request);
        assertNotNull(response);
        assertEquals(true,response.getResponse());
        assertEquals("Store updated successfully",response.getMessage());
        assertEquals(storeUUID1, response.getStoreID());
        if(s2.getOpen()!=null){
            assertEquals(s.getOpen(), s2.getOpen());
        }
        if(s2.getOpeningTime()!=-1){
            assertEquals(s.getOpeningTime(), s2.getOpeningTime());
        }
        if(s2.getClosingTime()!=-1){
            assertEquals(s.getClosingTime(), s2.getClosingTime());
        }
        if(s2.getStoreBrand()!=null){
            assertEquals(s.getStoreBrand(), s2.getStoreBrand());
        }
        if(s2.getMaxShoppers()!=-1){
            assertEquals(s.getMaxShoppers(), s2.getMaxShoppers());
        }
        if(s2.getMaxOrders()!=-1){
            assertEquals(s.getMaxOrders(), s2.getMaxOrders());
        }
    }

    /** Checking request object is created correctly */
    @Test
    @Description("Tests whether the UpdateStore request object was created correctly")
    @DisplayName("UpdateStoreRequest correctly constructed")
    void UnitTest_UpdateStoreRequestConstruction() {
        UpdateStoreRequest request = new UpdateStoreRequest(s2);
        assertNotNull(request);
        assertEquals(s2, request.getStore());
    }
}
