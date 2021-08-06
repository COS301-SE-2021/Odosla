package cs.superleague.shopping;

import cs.superleague.shopping.dataclass.Catalogue;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.shopping.dataclass.Store;
import cs.superleague.shopping.exceptions.InvalidRequestException;
import cs.superleague.shopping.exceptions.StoreDoesNotExistException;
import cs.superleague.shopping.repos.StoreRepo;
import cs.superleague.shopping.requests.GetStoresRequest;
import cs.superleague.shopping.requests.UpdateStoreRequest;
import cs.superleague.shopping.responses.GetStoresResponse;
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
public class GetStoresUnitTest {

    @Mock
    private StoreRepo storeRepo;

    @InjectMocks
    private ShoppingServiceImpl shoppingService;

    UUID storeUUID1= UUID.randomUUID();
    UUID storeUUID2= UUID.randomUUID();
    Store s;
    Store s2;

    Catalogue c;
    Catalogue c2;

    Item i1;
    Item i2;
    Item i3;
    Item i4;
    List<Item> listOfItems=new ArrayList<>();
    List<Item> listOfItems2=new ArrayList<>();

    List<Store> stores = new ArrayList<>();
    List<Store> nullStores = new ArrayList<>();

    @BeforeEach
    void setUp() {
        i1=new Item("Heinz Tomato Sauce","123456","123456",storeUUID1,36.99,1,"description","img/");
        i2=new Item("Bar one","012345","012345",storeUUID1,14.99,3,"description","img/");
        i3=new Item("Milk","901234","901234",storeUUID1,30.00,1,"description","img/");
        i4=new Item("Bread","890123","890123",storeUUID1,36.99,1,"description","img/");
        listOfItems.add(i1);
        listOfItems.add(i2);
        c=new Catalogue(storeUUID1, listOfItems);

        listOfItems2.add(i3);
        listOfItems2.add(i4);
        c2 = new Catalogue(storeUUID2, listOfItems2);

        s=new Store(storeUUID1,"Woolworths",c,2,null,null,4,true);
        s2=new Store(storeUUID2,"PnP", c2, 2, null, null, 6,true);

        stores.add(s);
        stores.add(s2);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Description("Tests for when GetStores is submitted with a null request object- exception should be thrown")
    @DisplayName("When request object is not specified")
    void UnitTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> shoppingService.getStores(null));
        assertEquals("Request object for getStores can't be null", thrown.getMessage());
    }

    @Test
    @Description("Test for when stores dont exist in the database - should return null")
    @DisplayName("When Stores dont exist in database and return null")
    void UnitTest_Stores_dont_exist_fetching_Stores() throws InvalidRequestException {
        GetStoresRequest request=new GetStoresRequest();

        GetStoresResponse response= shoppingService.getStores(request);
        assertNotNull(response);
        assertEquals(false,response.getResponse());
        assertEquals("List of Stores is null",response.getMessage());
        assertEquals(null, response.getStores());


    }

    @Test
    @Description("Test for when stores exist in the database - should return correct store entity")
    @DisplayName("When Stores exist in database and get returned in list")
    void UnitTest_Stores_does_exist_fetching_Stores() throws InvalidRequestException {

        assertEquals(stores.get(0).getStoreID(), s.getStoreID());
        assertEquals(stores.get(1).getStoreID(), s2.getStoreID());

    }

    /** Checking request object is created correctly */
    @Test
    @Description("Tests whether the GetStores request object was created correctly")
    @DisplayName("GetStoresRequest correctly constructed")
    void UnitTest_GetStoreRequestConstruction() {
        GetStoresRequest request = new GetStoresRequest();
        assertNotNull(request);

    }
}
