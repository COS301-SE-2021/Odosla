package cs.superleague.shopping;

import cs.superleague.shopping.ShoppingServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Description;
import cs.superleague.shopping.dataclass.Catalogue;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.shopping.dataclass.Store;
import cs.superleague.shopping.exceptions.InvalidRequestException;
import cs.superleague.shopping.exceptions.StoreClosedException;
import cs.superleague.shopping.exceptions.StoreDoesNotExistException;
import cs.superleague.shopping.repos.StoreRepo;
import cs.superleague.shopping.requests.GetCatalogueRequest;
import cs.superleague.shopping.requests.GetStoreByUUIDRequest;
import cs.superleague.shopping.requests.GetStoreOpenRequest;
import cs.superleague.shopping.responses.GetCatalogueResponse;
import cs.superleague.shopping.responses.GetStoreOpenResponse;


import java.util.*;

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
        c=new Catalogue(storeUUID1, listOfItems);
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

        Calendar calendar= Calendar.getInstance();
        if(calendar.get(Calendar.HOUR_OF_DAY) >= response.getOpeningTime() && calendar.get(Calendar.HOUR_OF_DAY) < response.getClosingTime())
        {
            assertEquals("Store is now open for business",response.getMessage());
        }
        else
        {
            assertEquals("Store is closed for business",response.getMessage());
        }

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
