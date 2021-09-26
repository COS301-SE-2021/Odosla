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
import cs.superleague.shopping.requests.UpdateCatalogueRequest;
import cs.superleague.shopping.responses.UpdateCatalogueResponse;
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
public class UpdateCatalogueIntegrationTest {
    @Autowired
    StoreRepo storeRepo;

    @Autowired
    CatalogueRepo catalogueRepo;

    @Autowired
    ItemRepo itemRepo;

    @Autowired
    ShoppingServiceImpl shoppingService;

    /* Requests */
    UpdateCatalogueRequest updateCatalogueRequest;

    /* StoreID */
    UUID storeUUID1= UUID.randomUUID();

    /* Store */
    Store store;

    /* Catalogues to update between */
    Catalogue catalogue1;
    Catalogue catalogue2;

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
        catalogue2=new Catalogue(storeUUID1, listOfItems2);
        catalogueRepo.save(catalogue2);
        store=new Store(storeUUID1,"Woolworths",catalogue1,2,null,null,4,true);
        storeRepo.save(store);
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    @Description("Tests for when updateCatalogue is submitted with a null request object- exception should be thrown")
    @DisplayName("When request object is not specified")
    void IntegrationTest_UpdateCatalogueNullRequestObject(){
        updateCatalogueRequest = null;

        assertThrows(InvalidRequestException.class, ()-> {
            UpdateCatalogueResponse updateCatalogueResponse = shoppingService.updateCatalogue(updateCatalogueRequest);
        });
    }

    @Test
    @Description("Tests for whether a request is submitted with a null parameter for catalogue in request object- exception should be thrown")
    @DisplayName("When request object parameter -catalogue - is not specified")
    void IntegrationTest_testingNull_catalogue_Parameter_RequestObject() {
        updateCatalogueRequest = new UpdateCatalogueRequest(null);
        assertThrows(InvalidRequestException.class, ()-> {
            UpdateCatalogueResponse updateCatalogueResponse = shoppingService.updateCatalogue(updateCatalogueRequest);
        });
    }

    @Test
    @Description("Test for when Store with storeID does not exist in database - StoreDoesNotExist Exception should be thrown")
    @DisplayName("When Store with ID doesn't exist")
    void IntegrationTest_Store_doesnt_exist() {
        catalogue2.setStoreID(UUID.randomUUID());
        updateCatalogueRequest = new UpdateCatalogueRequest(catalogue2);
        assertThrows(StoreDoesNotExistException.class, ()-> {
            UpdateCatalogueResponse updateCatalogueResponse = shoppingService.updateCatalogue(updateCatalogueRequest);
        });
    }

    @Test
    @Description("Test for when Store with storeID does exist in database - should return correct store entity")
    @DisplayName("When Store with ID does exist and catalogue changes")
    void IntegrationTest_Store_does_exist_changing_catalogue() throws InvalidRequestException, StoreDoesNotExistException {
        updateCatalogueRequest=new UpdateCatalogueRequest(catalogue2);
        UpdateCatalogueResponse response= shoppingService.updateCatalogue(updateCatalogueRequest);
        assertNotNull(response);
        assertEquals(true,response.isResponse());
        assertEquals("Catalogue updated for the store",response.getMessage());
        assertEquals(storeUUID1, response.getStoreID());
        if(storeRepo.findById(storeUUID1).isPresent()){
            store = storeRepo.findById(storeUUID1).orElse(null);
        }
        assertNotNull(store);
        assertEquals(store.getStock().getItems().size(), catalogue2.getItems().size());
    }

    @Test
    @Description("Test for when Store with storeID does exist in database - should return correct store entity")
    @DisplayName("When Store with ID does exist but catalogue is not changing")
    void IntegrationTest_Store_does_exist_updating_to_same_catalogue() throws InvalidRequestException, StoreDoesNotExistException {
        updateCatalogueRequest=new UpdateCatalogueRequest(catalogue1);
        UpdateCatalogueResponse response= shoppingService.updateCatalogue(updateCatalogueRequest);
        assertNotNull(response);
        assertEquals(true,response.isResponse());
        assertEquals("Catalogue updated for the store",response.getMessage());
        assertEquals(storeUUID1, response.getStoreID());
        assertEquals(store.getStock(), catalogue1);
    }

    /** Checking request object is created correctly */
    @Test
    @Description("Tests whether the UpdateCatalogue request object was created correctly")
    @DisplayName("UpdateCatalogueRequest correctly constructed")
    void IntegrationTest_UpdateCatalogueRequestConstruction() {
        updateCatalogueRequest = new UpdateCatalogueRequest(catalogue1);
        assertNotNull(updateCatalogueRequest);
        assertEquals(catalogue1, updateCatalogueRequest.getCatalogue());
    }
}
