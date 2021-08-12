package cs.superleague.shopping.integration;

import cs.superleague.integration.ServiceSelector;
import cs.superleague.shopping.ShoppingServiceImpl;
import cs.superleague.shopping.dataclass.Catalogue;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.shopping.dataclass.Store;
import cs.superleague.shopping.exceptions.InvalidRequestException;
import cs.superleague.shopping.exceptions.StoreDoesNotExistException;
import cs.superleague.shopping.repos.CatalogueRepo;
import cs.superleague.shopping.repos.ItemRepo;
import cs.superleague.shopping.repos.StoreRepo;
import cs.superleague.shopping.requests.GetStoreOpenRequest;
import cs.superleague.shopping.requests.GetStoresRequest;
import cs.superleague.shopping.requests.UpdateStoreRequest;
import cs.superleague.shopping.responses.GetStoresResponse;
import cs.superleague.shopping.responses.UpdateStoreResponse;
import cs.superleague.user.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class GetStoresIntegrationTest {

    @Autowired
    ShoppingServiceImpl shoppingService;

    //OPTIONAL SERVICES
    @Autowired
    UserServiceImpl userService;

    @Autowired
    StoreRepo storeRepo;

    @Autowired
    CatalogueRepo catalogueRepo;

    @Autowired
    ItemRepo itemRepo;

    /* Requests */
    GetStoresRequest getStoreRequest;

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
        itemRepo.save(i1);
        itemRepo.save(i2);
        itemRepo.save(i3);
        itemRepo.save(i4);

        listOfItems.add(i1);
        listOfItems.add(i2);
        c=new Catalogue(storeUUID1, listOfItems);
        catalogueRepo.save(c);

        listOfItems2.add(i3);
        listOfItems2.add(i4);
        c2 = new Catalogue(storeUUID2, listOfItems2);
        catalogueRepo.save(c2);

        s=new Store(storeUUID1,"Woolworths",c,2,null,null,4,true);
        s2=new Store(storeUUID2,"PnP", c2, 2, null, null, 6,true);

        stores.add(s);
        stores.add(s2);

        storeRepo.save(s);
        storeRepo.save(s2);

    }

    @AfterEach
    void tearDown() {
        storeRepo.deleteAll();
        catalogueRepo.deleteAll();
        itemRepo.deleteAll();
    }

    @Test
    @Description("Tests for when getStores is submitted with a null request object- exception should be thrown")
    @DisplayName("When request object is not specified")
    void IntegrationTest_GetStoreNullRequestObject(){
        getStoreRequest = null;

        assertThrows(cs.superleague.shopping.exceptions.InvalidRequestException.class, ()-> {
            GetStoresResponse getStoresResponse = ServiceSelector.getShoppingService().getStores(getStoreRequest);
        });
    }

    @Test
    @Description("Test for when no stores exist in database")
    @DisplayName("When stores don't exist in database")
    void IntegrationTest_Stores_dont_exist() throws InvalidRequestException {
        storeRepo.deleteAll();

        getStoreRequest = new GetStoresRequest();
        GetStoresResponse response= ServiceSelector.getShoppingService().getStores(getStoreRequest);
        assertNotNull(response);
        assertEquals(false,response.getResponse());
        assertEquals("List of Stores is null",response.getMessage());
        assertEquals(null, response.getStores());


    }

    @Test
    @Description("Test for when Stores exist in database - should return correct store entities")
    @DisplayName("When Stores exist and stores are returned")
    void IntegrationTest_Stores_exist_fetching_store() throws InvalidRequestException {
        getStoreRequest = new GetStoresRequest();
        GetStoresResponse response= ServiceSelector.getShoppingService().getStores(getStoreRequest);
        assertNotNull(response);
        assertEquals(true,response.getResponse());
        assertEquals("List of Stores successfully returned",response.getMessage());

    }

    /** Checking request object is created correctly */
    @Test
    @Description("Tests whether the GetStores request object was created correctly")
    @DisplayName("GetStoresRequest correctly constructed")
    void IntegrationTest_GetStoreRequestConstruction() {
        GetStoresRequest request = new GetStoresRequest();
        assertNotNull(request);
    }
}
