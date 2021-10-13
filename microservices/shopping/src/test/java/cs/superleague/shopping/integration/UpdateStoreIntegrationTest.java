package cs.superleague.shopping.integration;

import cs.superleague.shopping.ShoppingServiceImpl;
import cs.superleague.shopping.dataclass.Catalogue;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.shopping.dataclass.Store;
import cs.superleague.shopping.exceptions.InvalidRequestException;
import cs.superleague.shopping.exceptions.StoreDoesNotExistException;
import cs.superleague.shopping.repos.CatalogueRepo;
import cs.superleague.shopping.repos.ItemRepo;
import cs.superleague.shopping.repos.StoreRepo;
import cs.superleague.shopping.requests.UpdateStoreRequest;
import cs.superleague.shopping.responses.UpdateStoreResponse;
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

@SpringBootTest
@Transactional
public class UpdateStoreIntegrationTest {
    @Autowired
    StoreRepo storeRepo;

    @Autowired
    CatalogueRepo catalogueRepo;

    @Autowired
    ItemRepo itemRepo;

    @Autowired
    ShoppingServiceImpl shoppingService;

    /* Requests */
    UpdateStoreRequest updateStoreRequest;

    /* StoreID */
    UUID storeUUID1= UUID.randomUUID();

    /* Store objects, one will be used to update the other */
    Store store;
    Store store2;

    /* Catalogue*/
    Catalogue catalogue1;


    /* items */
    Item item1;
    Item item2;
    Item item3;
    Item item4;

    /* Lists of items */
    List<Item> listOfItems=new ArrayList<>();
    List<Item> listOfItems2=new ArrayList<>();

    @BeforeEach
    void setUp() {
        item1=new Item("Heinz Tomato Sauce","123456","123456",storeUUID1,36.99,1,"description","img/");
        item2=new Item("Bar one","012345","012345",storeUUID1,14.99,3,"description","img/");
        item3=new Item("Milk","901234","901234",storeUUID1,30.00,1,"description","img/");
        item4=new Item("Bread","890123","890123",storeUUID1,36.99,1,"description","img/");
        itemRepo.save(item1);
        itemRepo.save(item2);
        itemRepo.save(item3);
        itemRepo.save(item4);
        listOfItems.add(item1);
        listOfItems.add(item2);
        catalogue1=new Catalogue(storeUUID1, listOfItems);
        catalogueRepo.save(catalogue1);
        listOfItems2.add(item1);
        listOfItems2.add(item2);
        listOfItems2.add(item3);
        listOfItems2.add(item4);
        store=new Store(storeUUID1,"Woolworth's",catalogue1,2,null,null,4,false);
        store.setOpeningTime(9);
        store.setClosingTime(23);
        store2=new Store(storeUUID1, 7, 21, "PnP", 2,6,true, "img");
        storeRepo.save(store);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Description("Tests for when updateStore is submitted with a null request object- exception should be thrown")
    @DisplayName("When request object is not specified")
    void IntegrationTest_UpdateStoreNullRequestObject(){
        updateStoreRequest = null;

        assertThrows(InvalidRequestException.class, ()-> {
            UpdateStoreResponse updateStoreResponse = shoppingService.updateStore(updateStoreRequest);
        });
    }

    @Test
    @Description("Tests for whether a request is submitted with a null parameter for store in request object- exception should be thrown")
    @DisplayName("When request object parameter -store - is not specified")
    void IntegrationTest_testingNull_store_Parameter_RequestObject() {
        updateStoreRequest = new UpdateStoreRequest(null);
        assertThrows(InvalidRequestException.class, ()-> {
            UpdateStoreResponse updateStoreResponse = shoppingService.updateStore(updateStoreRequest);
        });
    }

    @Test
    @Description("Test for when Store with storeID does not exist in database - StoreDoesNotExist Exception should be thrown")
    @DisplayName("When Store with ID doesn't exist")
    void IntegrationTest_Store_doesnt_exist() {
        store2.setStoreID(UUID.randomUUID());
        updateStoreRequest = new UpdateStoreRequest(store2);
        assertThrows(StoreDoesNotExistException.class, ()-> {
            UpdateStoreResponse updateStoreResponse = shoppingService.updateStore(updateStoreRequest);
        });
    }

    @Test
    @Description("Test for when Store with storeID does exist in database - should return correct store entity")
    @DisplayName("When Store with ID does exist and catalogue changes")
    void IntegrationTest_Store_does_exist_updating_store() throws InvalidRequestException, StoreDoesNotExistException {
        updateStoreRequest = new UpdateStoreRequest(store2);
        UpdateStoreResponse response= shoppingService.updateStore(updateStoreRequest);
        assertNotNull(response);
        assertEquals(true,response.getResponse());
        assertEquals("Store updated successfully",response.getMessage());
        assertEquals(storeUUID1, response.getStoreID());
        if(storeRepo.findById(storeUUID1).isPresent()){
            store = storeRepo.findById(storeUUID1).orElse(null);
        }
        assertNotNull(store);

        if(store2.getOpen()!=null){
            assertEquals(store.getOpen(), store2.getOpen());
        }
        if(store2.getOpeningTime()!=-1){
            assertEquals(store.getOpeningTime(), store2.getOpeningTime());
        }
        if(store2.getClosingTime()!=-1){
            assertEquals(store.getClosingTime(), store2.getClosingTime());
        }
        if(store2.getStoreBrand()!=null){
            assertEquals(store.getStoreBrand(), store2.getStoreBrand());
        }
        if(store2.getMaxShoppers()!=-1){
            assertEquals(store.getMaxShoppers(), store2.getMaxShoppers());
        }
        if(store2.getMaxOrders()!=-1){
            assertEquals(store.getMaxOrders(), store2.getMaxOrders());
        }

    }

    /** Checking request object is created correctly */
    @Test
    @Description("Tests whether the UpdateStore request object was created correctly")
    @DisplayName("UpdateStoreRequest correctly constructed")
    void IntegrationTest_UpdateStoreRequestConstruction() {
        updateStoreRequest = new UpdateStoreRequest(store2);
        assertNotNull(updateStoreRequest);
        assertEquals(store2, updateStoreRequest.getStore());
    }
}
