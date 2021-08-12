package cs.superleague.shopping.integration;

import cs.superleague.integration.ServiceSelector;
import cs.superleague.shopping.dataclass.Catalogue;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.shopping.dataclass.Store;
import cs.superleague.shopping.exceptions.InvalidRequestException;
import cs.superleague.shopping.exceptions.StoreDoesNotExistException;
import cs.superleague.shopping.repos.CatalogueRepo;
import cs.superleague.shopping.repos.ItemRepo;
import cs.superleague.shopping.repos.StoreRepo;
import cs.superleague.shopping.requests.UpdateShoppersRequest;
import cs.superleague.shopping.requests.UpdateStoreRequest;
import cs.superleague.shopping.responses.UpdateShoppersResponse;
import cs.superleague.shopping.responses.UpdateStoreResponse;
import cs.superleague.user.dataclass.Shopper;
import cs.superleague.user.repos.ShopperRepo;
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
public class UpdateShoppersIntegrationTest {
    @Autowired
    StoreRepo storeRepo;

    @Autowired
    CatalogueRepo catalogueRepo;

    @Autowired
    ShopperRepo shopperRepo;

    /* Requests */
    UpdateShoppersRequest updateShoppersRequest;

    /* StoreID */
    UUID storeUUID1= UUID.randomUUID();

    /* Store objects, one will be used to update the other */
    Store store;

    /* Shopper objects, two will be used to update the others in their respective lists*/
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

        shopper1=new Shopper();
        shopper1.setShopperID(shopperID1);
        shopper2=new Shopper();
        shopper2.setShopperID(shopperID2);
        shopper3=new Shopper();
        shopper3.setShopperID(shopperID3);
        shopper4=new Shopper();
        shopper4.setShopperID(shopperID4);

        shopperRepo.save(shopper1);
        shopperRepo.save(shopper2);
        shopperRepo.save(shopper3);
        shopperRepo.save(shopper4);

        shopperList.add(shopper1);
        shopperList.add(shopper2);

        updatedShopperList.add(shopper3);
        updatedShopperList.add(shopper4);

        store=new Store();
        store.setStoreID(storeUUID1);
        store.setShoppers(shopperList);

        storeRepo.save(store);
    }

    @AfterEach
    void tearDown() {
        storeRepo.deleteAll();
        catalogueRepo.deleteAll();
        shopperRepo.deleteAll();
    }

    @Test
    @Description("Tests for when updateShoppers is submitted with a null request object- exception should be thrown")
    @DisplayName("When request object is not specified")
    void IntegrationTest_UpdateShoppersNullRequestObject(){
        updateShoppersRequest = null;

        assertThrows(cs.superleague.shopping.exceptions.InvalidRequestException.class, ()-> {
            UpdateShoppersResponse updateShoppersResponse = ServiceSelector.getShoppingService().updateShoppers(updateShoppersRequest);
        });
    }

    @Test
    @Description("Tests for whether a request is submitted with a null parameter for store in request object- exception should be thrown")
    @DisplayName("When request object parameter -store - is not specified")
    void IntegrationTest_testingNull_store_Parameter_RequestObject() {
        updateShoppersRequest = new UpdateShoppersRequest(null, updatedShopperList);
        assertThrows(cs.superleague.shopping.exceptions.InvalidRequestException.class, ()-> {
            UpdateShoppersResponse updateShoppersResponse = ServiceSelector.getShoppingService().updateShoppers(updateShoppersRequest);
        });
    }

    @Test
    @Description("Tests for whether a request is submitted with a null parameter for newShoppers in request object- exception should be thrown")
    @DisplayName("When request object parameter -newShoppers - is not specified")
    void IntegrationTest_testingNull_newShoppers_Parameter_RequestObject() {
        updateShoppersRequest = new UpdateShoppersRequest(store, null);
        assertThrows(cs.superleague.shopping.exceptions.InvalidRequestException.class, ()-> {
            UpdateShoppersResponse updateShoppersResponse = ServiceSelector.getShoppingService().updateShoppers(updateShoppersRequest);
        });
    }

    @Test
    @Description("Test for when Store with storeID does not exist in database - StoreDoesNotExist Exception should be thrown")
    @DisplayName("When Store with ID doesn't exist")
    void IntegrationTest_Store_doesnt_exist() {
        store.setStoreID(UUID.randomUUID());
        updateShoppersRequest = new UpdateShoppersRequest(store, updatedShopperList);
        assertThrows(cs.superleague.shopping.exceptions.StoreDoesNotExistException.class, ()-> {
            UpdateShoppersResponse updateShoppersResponse = ServiceSelector.getShoppingService().updateShoppers(updateShoppersRequest);
        });
    }

    @Test
    @Description("Test for when Store with storeID does exist in database - should return correct store entity")
    @DisplayName("When Store with ID does exist and catalogue changes")
    void IntegrationTest_Store_does_exist_updating_shoppers() throws InvalidRequestException, StoreDoesNotExistException {
        updateShoppersRequest = new UpdateShoppersRequest(store, updatedShopperList);
        UpdateShoppersResponse response= ServiceSelector.getShoppingService().updateShoppers(updateShoppersRequest);
        assertNotNull(response);
        assertEquals(true,response.getResponse());
        assertEquals("Shoppers updated successfully",response.getMessage());
        assertEquals(storeUUID1, response.getStoreID());
        if(storeRepo.findById(storeUUID1).isPresent()){
            store = storeRepo.findById(storeUUID1).orElse(null);
        }
        assertNotNull(store);
        assertEquals(store.getShoppers().get(0).getShopperID(), updateShoppersRequest.getNewShoppers().get(0).getShopperID());
        assertEquals(store.getShoppers().get(1).getShopperID(), updateShoppersRequest.getNewShoppers().get(1).getShopperID());


    }

    /** Checking request object is created correctly */
    @Test
    @Description("Tests whether the UpdateShoppers request object was created correctly")
    @DisplayName("UpdateShoppersRequest correctly constructed")
    void IntegrationTest_UpdateShoppersRequestConstruction() {
        updateShoppersRequest = new UpdateShoppersRequest(store, updatedShopperList);
        assertNotNull(updateShoppersRequest);
        assertEquals(store, updateShoppersRequest.getStore());
        assertEquals(updatedShopperList, updateShoppersRequest.getNewShoppers());
    }
}
