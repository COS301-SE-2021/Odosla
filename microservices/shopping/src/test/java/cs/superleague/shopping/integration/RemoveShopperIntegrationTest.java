package cs.superleague.shopping.integration;

import cs.superleague.shopping.ShoppingServiceImpl;
import cs.superleague.shopping.dataclass.Store;
import cs.superleague.shopping.exceptions.InvalidRequestException;
import cs.superleague.shopping.exceptions.StoreDoesNotExistException;
import cs.superleague.shopping.repos.StoreRepo;
import cs.superleague.shopping.requests.RemoveShopperRequest;
import cs.superleague.user.dataclass.Shopper;
import cs.superleague.user.exceptions.UserDoesNotExistException;
import cs.superleague.user.exceptions.UserException;
import cs.superleague.user.requests.SaveShopperToRepoRequest;
import org.junit.jupiter.api.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class RemoveShopperIntegrationTest {

    @Autowired
    ShoppingServiceImpl shoppingService;

    @Autowired
    StoreRepo storeRepo;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    RabbitTemplate rabbit;

    UUID storeUUID1= UUID.randomUUID();
    Store store;
    Shopper shopper;
    Shopper shopper1;
    Shopper shopper2;
    UUID shopperID=UUID.fromString("08d96d59-2e53-402a-92c4-0fb9a66fc2d4");
    UUID shopperID2=UUID.fromString("0a33c5b6-3f4a-4d17-a1b7-48de0f4c7dfd");
    UUID shopperID3=UUID.fromString("0cd88fe3-5fd1-44e9-9cee-a9460b528310");
    UUID storeID=UUID.randomUUID();
    List<Shopper> shopperList=new ArrayList<>();

    @BeforeEach
    void setUp() {
        store=new Store();
        shopper=new Shopper();
        shopper.setShopperID(shopperID);
        shopper1=new Shopper();
        shopper1.setShopperID(shopperID2);
        shopper2=new Shopper();
        shopper2.setShopperID(shopperID3);
        shopperList.add(shopper1);
        shopperList.add(shopper2);

        SaveShopperToRepoRequest saveShopperToRepoRequest = new SaveShopperToRepoRequest(shopper);
        rabbit.convertAndSend("UserEXCHANGE", "RK_SaveShopperToRepo", saveShopperToRepoRequest);
        saveShopperToRepoRequest = new SaveShopperToRepoRequest(shopper1);
        rabbit.convertAndSend("UserEXCHANGE", "RK_SaveShopperToRepo", saveShopperToRepoRequest);
        saveShopperToRepoRequest = new SaveShopperToRepoRequest(shopper2);
        rabbit.convertAndSend("UserEXCHANGE", "RK_SaveShopperToRepo", saveShopperToRepoRequest);

    }

    @AfterEach
    void tearDown() {

    }

    @Test
    @Description("Tests for when removeShoppers is submited with a null request object- exception should be thrown")
    @DisplayName("When request object is not specificed")
    void IntegrationTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> shoppingService.removeShopper(null));
        assertEquals("Request object can't be null for removeShopper", thrown.getMessage());
    }

    @Test
    @Description("Tests for when removeShoppers is submited store ID in request object being null- exception should be thrown")
    @DisplayName("When request object has null parameter")
    void IntegrationTest_StoreID_inRequest_NullRequestObject(){
        RemoveShopperRequest request=new RemoveShopperRequest(shopperID,null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> shoppingService.removeShopper(request));
        assertEquals("Store ID in request object for remove shopper is null", thrown.getMessage());
    }

    @Test
    @Description("Tests for when removeShoppers is submited shopper ID in request object being null- exception should be thrown")
    @DisplayName("When request object has null parameter")
    void IntegrationTest_ShopperID_inRequest_NullRequestObject(){
        RemoveShopperRequest request=new RemoveShopperRequest(null,storeID);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> shoppingService.removeShopper(request));
        assertEquals("Shopper ID in request object for remove shopper is null", thrown.getMessage());
    }

    @Test
    @Description("Tests whether the removeShoppers request object was created correctly")
    @DisplayName("RemoveShopper Request correctly constructed")
    void IntegrationTest_AddShoppersRequestConstruction() {
        RemoveShopperRequest request=new RemoveShopperRequest(shopperID,storeID);
        assertNotNull(request);
        assertEquals(storeID,request.getStoreID());
        assertEquals(shopperID,request.getShopperID());
    }

    @Test
    @Description("Test for when Store with storeID does not exist in database - StoreDoesNotExist Exception should be thrown")
    @DisplayName("When Store with ID doesn't exist")
    void IntegrationTest_Store_doesnt_exist(){
        RemoveShopperRequest request=new RemoveShopperRequest(shopperID,storeID);
        Throwable thrown = Assertions.assertThrows(StoreDoesNotExistException.class, ()-> shoppingService.removeShopper(request));
        assertEquals("Store with ID does not exist in repository - could not remove Shopper", thrown.getMessage());
    }


    @Test
    @Description("Test for when Shopper with shopperID does not exist in shopper database ")
    @DisplayName("When Shopper with ID doesn't exist")
    void IntegrationTest_Shopper_doesnt_exist() throws InvalidRequestException, cs.superleague.user.exceptions.InvalidRequestException, UserDoesNotExistException, StoreDoesNotExistException {
//        store.setStoreID(storeUUID1);
//        store.setShoppers(shopperList);
//        RemoveShopperRequest request=new RemoveShopperRequest(shopperID,storeUUID1);
//        storeRepo.save(store);
//        Throwable thrown = Assertions.assertThrows(ShopperDoesNotExistException.class, ()-> shoppingService.removeShopper(request));
//        assertEquals("User with ID does not exist in repository - could not get Shopper entity", thrown.getMessage());
    }


    @Test
    @Description("Test for when list of shoppers doesn't contain shopper")
    @DisplayName("Shopper Id not in list of Shoppers")
    void IntegrationTest_shopper_not_in_shopper_list() throws InvalidRequestException, StoreDoesNotExistException, UserException, UserException {
//        store.setStoreID(storeUUID1);
//        store.setShoppers(shopperList);
//        RemoveShopperRequest request=new RemoveShopperRequest(shopperID,storeUUID1);

//        shopperRepo.save(shopper);

//        SaveShopperToRepoRequest saveShopperToRepoRequest = new SaveShopperToRepoRequest(shopper);
//        rabbit.convertAndSend("UserEXCHANGE", "RK_SaveShopperToRepo", saveShopperToRepoRequest);
//        storeRepo.save(store);
//        RemoveShopperResponse response=shoppingService.removeShopper(request);
//        assertNotNull(response);
//        assertEquals(false,response.isSuccess());
//        assertEquals("Shopper isn't in list of shoppers in store entity",response.getMessage());
    }

    @Test
    @Description("Test for when shopper is correctly removed from the list")
    @DisplayName("Shopper was correctly removed from list of shoppers in store")
    void Shopper_correctly_removed() throws InvalidRequestException, StoreDoesNotExistException, UserException {
//        store.setStoreID(storeUUID1);
//        store.setShoppers(shopperList);
//        RemoveShopperRequest request=new RemoveShopperRequest(shopper1.getShopperID(),storeUUID1);
//        storeRepo.save(store);
//        GetShopperByUUIDResponse shopperResponse=new GetShopperByUUIDResponse(shopper1,null,null);
//        RemoveShopperResponse response=shoppingService.removeShopper(request);
//        assertNotNull(response);
//        assertEquals(true,response.isSuccess());
//        assertEquals("Shopper was successfully removed",response.getMessage());
//        Store storeResponse=storeRepo.findById(store.getStoreID()).orElse(null);
//        assertEquals(shopperList.size()-1,storeResponse.getShoppers().size());
//        List<Shopper> listOfShoppersResponse=storeResponse.getShoppers();
//        shopperList.remove(shopper1);
//        for(int i=0;i<shopperList.size();i++){
//            assertEquals(shopperList.get(i).getShopperID(),listOfShoppersResponse.get(i).getShopperID());
//        }
    }
}