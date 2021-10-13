package cs.superleague.shopping.integration;

import cs.superleague.shopping.ShoppingServiceImpl;
import cs.superleague.shopping.dataclass.Store;
import cs.superleague.shopping.exceptions.InvalidRequestException;
import cs.superleague.shopping.exceptions.StoreDoesNotExistException;
import cs.superleague.shopping.repos.CatalogueRepo;
import cs.superleague.shopping.repos.StoreRepo;
import cs.superleague.shopping.requests.UpdateShoppersRequest;
import cs.superleague.shopping.responses.UpdateShoppersResponse;
import cs.superleague.user.dataclass.Shopper;
import cs.superleague.user.requests.SaveShopperToRepoRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

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
    ShoppingServiceImpl shoppingService;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    RabbitTemplate rabbit;

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
    UUID shopperID1=UUID.fromString("08d96d59-2e53-402a-92c4-0fb9a66fc2d4");
    UUID shopperID2=UUID.fromString("0a33c5b6-3f4a-4d17-a1b7-48de0f4c7dfd");
    UUID shopperID3=UUID.fromString("0cd88fe3-5fd1-44e9-9cee-a9460b528310");
    UUID shopperID4=UUID.fromString("102f73ce-1e74-4553-8f37-5e77bb553192");

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

        SaveShopperToRepoRequest saveShopperToRepoRequest = new SaveShopperToRepoRequest(shopper1);
        rabbit.convertAndSend("UserEXCHANGE", "RK_SaveShopperToRepo", saveShopperToRepoRequest);
        saveShopperToRepoRequest = new SaveShopperToRepoRequest(shopper2);
        rabbit.convertAndSend("UserEXCHANGE", "RK_SaveShopperToRepo", saveShopperToRepoRequest);
        saveShopperToRepoRequest = new SaveShopperToRepoRequest(shopper3);
        rabbit.convertAndSend("UserEXCHANGE", "RK_SaveShopperToRepo", saveShopperToRepoRequest);
        saveShopperToRepoRequest = new SaveShopperToRepoRequest(shopper4);
        rabbit.convertAndSend("UserEXCHANGE", "RK_SaveShopperToRepo", saveShopperToRepoRequest);

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
    }

    @Test
    @Description("Tests for when updateShoppers is submitted with a null request object- exception should be thrown")
    @DisplayName("When request object is not specified")
    void IntegrationTest_UpdateShoppersNullRequestObject(){
        updateShoppersRequest = null;

        assertThrows(InvalidRequestException.class, ()-> {
            UpdateShoppersResponse updateShoppersResponse = shoppingService.updateShoppers(updateShoppersRequest);
        });
    }

    @Test
    @Description("Tests for whether a request is submitted with a null parameter for store in request object- exception should be thrown")
    @DisplayName("When request object parameter -store - is not specified")
    void IntegrationTest_testingNull_store_Parameter_RequestObject() {
        updateShoppersRequest = new UpdateShoppersRequest(null, updatedShopperList);
        assertThrows(InvalidRequestException.class, ()-> {
            UpdateShoppersResponse updateShoppersResponse = shoppingService.updateShoppers(updateShoppersRequest);
        });
    }

    @Test
    @Description("Tests for whether a request is submitted with a null parameter for newShoppers in request object- exception should be thrown")
    @DisplayName("When request object parameter -newShoppers - is not specified")
    void IntegrationTest_testingNull_newShoppers_Parameter_RequestObject() {
        updateShoppersRequest = new UpdateShoppersRequest(store, null);
        assertThrows(InvalidRequestException.class, ()-> {
            UpdateShoppersResponse updateShoppersResponse = shoppingService.updateShoppers(updateShoppersRequest);
        });
    }

    @Test
    @Description("Test for when Store with storeID does not exist in database - StoreDoesNotExist Exception should be thrown")
    @DisplayName("When Store with ID doesn't exist")
    void IntegrationTest_Store_doesnt_exist() {
        store.setStoreID(UUID.randomUUID());
        updateShoppersRequest = new UpdateShoppersRequest(store, updatedShopperList);
        assertThrows(StoreDoesNotExistException.class, ()-> {
            UpdateShoppersResponse updateShoppersResponse = shoppingService.updateShoppers(updateShoppersRequest);
        });
    }

    @Test
    @Description("Test for when Store with storeID does exist in database - should return correct store entity")
    @DisplayName("When Store with ID does exist and catalogue changes")
    void IntegrationTest_Store_does_exist_updating_shoppers() throws InvalidRequestException, StoreDoesNotExistException {
        updateShoppersRequest = new UpdateShoppersRequest(store, updatedShopperList);
        UpdateShoppersResponse response= shoppingService.updateShoppers(updateShoppersRequest);
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
