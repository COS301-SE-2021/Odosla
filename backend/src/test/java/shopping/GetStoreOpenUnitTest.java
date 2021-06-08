package shopping;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Description;
import shopping.dataclass.Catalogue;
import shopping.dataclass.Item;
import shopping.dataclass.Store;
import shopping.exceptions.InvalidRequestException;
import shopping.exceptions.StoreClosedException;
import shopping.exceptions.StoreDoesNotExistException;
import shopping.repos.StoreRepo;
import shopping.requests.GetCatalogueRequest;
import shopping.requests.GetStoreByUUIDRequest;
import shopping.requests.GetStoreOpenRequest;
import shopping.responses.GetCatalogueResponse;
import shopping.responses.GetStoreOpenResponse;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetStoreOpenUnitTest {

    @Mock
    private StoreRepo storeRepo;

    @InjectMocks
    private ShoppingServiceImpl shoppingService;

    UUID storeUUID1= UUID.randomUUID();
    Store s;
    Catalogue c;
    Item i1;
    Item i2;
    List<Item> listOfItems=new ArrayList<>();

    @BeforeEach
    void setUp() {
        i1=new Item("Heinz Tomatoe Sauce","123456","123456",storeUUID1,36.99,1,"description","img/");
        i2=new Item("Bar one","012345","012345",storeUUID1,14.99,3,"description","img/");
        listOfItems.add(i1);
        listOfItems.add(i2);
        c=new Catalogue(listOfItems);
        s=new Store(storeUUID1,"Woolworths",c,2,null,null,4,false);
        s.setOpeningTime(7);
        s.setClosingTime(22);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Description("Tests for when getStoreOpen is submitted with a null request object- exception should be thrown")
    @DisplayName("When request object is not specified")
    void UnitTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> shoppingService.getStoreOpen(null));
        assertEquals("The GetStoreOpenRequest parameter is null - Could not set store to open", thrown.getMessage());
    }

    @Test
    @Description("Tests for when storeID in request object is null- exception should be thrown")
    @DisplayName("When request object parameter -storeID - is not specified")
    void UnitTest_testingNull_storeID_Parameter_RequestObject(){
        GetStoreOpenRequest request=new GetStoreOpenRequest(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> shoppingService.getStoreOpen(request));
        assertEquals("The Store ID in GetStoreOpenRequest parameter is null - Could not set store to open", thrown.getMessage());
    }

    @Test
    @Description("Test for when Store with storeID does not exist in database - StoreDoesNotExist Exception should be thrown")
    @DisplayName("When Store with ID doesn't exist")
    void UnitTest_Store_doesnt_exist(){
        GetStoreByUUIDRequest request=new GetStoreByUUIDRequest(storeUUID1);
        when(storeRepo.findById(Mockito.any())).thenReturn(null);
        Throwable thrown = Assertions.assertThrows(StoreDoesNotExistException.class, ()-> shoppingService.getStoreByUUID(request));
        assertEquals("Store with ID does not exist in repository - could not get Store entity", thrown.getMessage());
    }

    @Test
    @Description("Test for when Store with storeID does exist in database - should return correct store entity")
    @DisplayName("When Store with ID does exist")
    void UnitTest_Store_does_exist() throws InvalidRequestException, StoreDoesNotExistException, StoreClosedException {
        GetStoreOpenRequest request=new GetStoreOpenRequest(storeUUID1);
        when(storeRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(s));
        GetStoreOpenResponse response= shoppingService.getStoreOpen(request);
        assertNotNull(response);
        assertEquals(s.getOpen(),response.getOpen());
        assertEquals("Store is now open for business",response.getMessage());
    }

    /** Checking request object is created correctly */
    @Test
    @Description("Tests whether the GetStoreOpen request object was created correctly")
    @DisplayName("GetStoreOpenRequest correctly constructed")
    void UnitTest_GetStoreRequestConstruction() {

        GetStoreOpenRequest request = new GetStoreOpenRequest(storeUUID1);

        assertNotNull(request);
        assertEquals(storeUUID1, request.getStoreID());
    }
}
