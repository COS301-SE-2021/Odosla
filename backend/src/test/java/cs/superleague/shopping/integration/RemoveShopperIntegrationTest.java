package cs.superleague.shopping.integration;

import cs.superleague.integration.ServiceSelector;
import cs.superleague.shopping.ShoppingServiceImpl;
import cs.superleague.shopping.dataclass.Store;
import cs.superleague.shopping.exceptions.InvalidRequestException;
import cs.superleague.shopping.exceptions.StoreDoesNotExistException;
import cs.superleague.shopping.repos.CatalogueRepo;
import cs.superleague.shopping.repos.ItemRepo;
import cs.superleague.shopping.repos.StoreRepo;
import cs.superleague.shopping.requests.RemoveShopperRequest;
import cs.superleague.shopping.responses.RemoveShopperResponse;
import cs.superleague.user.UserServiceImpl;
import cs.superleague.user.dataclass.Shopper;
import cs.superleague.user.exceptions.UserDoesNotExistException;
import cs.superleague.user.repos.ShopperRepo;
import cs.superleague.user.responses.GetShopperByUUIDResponse;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
public class RemoveShopperIntegrationTest {

    @Autowired
    ShoppingServiceImpl shoppingService;

    //OPTIONAL SERVICES
    @Autowired
    UserServiceImpl userService;

    @Autowired
    StoreRepo storeRepo;

    @Autowired
    ShopperRepo shopperRepo;

    UUID storeUUID1= UUID.randomUUID();
    Store store;
    Shopper shopper;
    Shopper shopper1;
    Shopper shopper2;
    UUID shopperID=UUID.randomUUID();
    UUID shopperID2=UUID.randomUUID();
    UUID shopperID3=UUID.randomUUID();
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
        shopperRepo.save(shopper1);
        shopperRepo.save(shopper2);
    }

    @AfterEach
    void tearDown() {

        storeRepo.deleteAll();
        shopperRepo.deleteAll();
    }

    @Test
    @Description("Tests for when removeShoppers is submited with a null request object- exception should be thrown")
    @DisplayName("When request object is not specificed")
    void IntegrationTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> ServiceSelector.getShoppingService().removeShopper(null));
        assertEquals("Request object can't be null for removeShopper", thrown.getMessage());
    }

    @Test
    @Description("Tests for when removeShoppers is submited store ID in request object being null- exception should be thrown")
    @DisplayName("When request object has null parameter")
    void IntegrationTest_StoreID_inRequest_NullRequestObject(){
        RemoveShopperRequest request=new RemoveShopperRequest(shopperID,null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> ServiceSelector.getShoppingService().removeShopper(request));
        assertEquals("Store ID in request object for remove shopper is null", thrown.getMessage());
    }

    @Test
    @Description("Tests for when removeShoppers is submited shopper ID in request object being null- exception should be thrown")
    @DisplayName("When request object has null parameter")
    void IntegrationTest_ShopperID_inRequest_NullRequestObject(){
        RemoveShopperRequest request=new RemoveShopperRequest(null,storeID);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> ServiceSelector.getShoppingService().removeShopper(request));
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
        Throwable thrown = Assertions.assertThrows(StoreDoesNotExistException.class, ()-> ServiceSelector.getShoppingService().removeShopper(request));
        assertEquals("Store with ID does not exist in repository - could not remove Shopper", thrown.getMessage());
    }


    @Test
    @Description("Test for when Shopper with shopperID does not exist in shopper database ")
    @DisplayName("When Shopper with ID doesn't exist")
    void IntegrationTest_Shopper_doesnt_exist() throws InvalidRequestException, cs.superleague.user.exceptions.InvalidRequestException, UserDoesNotExistException, StoreDoesNotExistException {
        store.setStoreID(storeUUID1);
        store.setShoppers(shopperList);
        RemoveShopperRequest request=new RemoveShopperRequest(shopperID,storeUUID1);
        storeRepo.save(store);
        Throwable thrown = Assertions.assertThrows(UserDoesNotExistException.class, ()-> ServiceSelector.getShoppingService().removeShopper(request));
        assertEquals("User with ID does not exist in repository - could not get Shopper entity", thrown.getMessage());
    }


    @Test
    @Description("Test for when list of shoppers doesn't contain shopper")
    @DisplayName("Shopper Id not in list of Shoppers")
    void IntegrationTest_shopper_not_in_shopper_list() throws InvalidRequestException, StoreDoesNotExistException, cs.superleague.user.exceptions.InvalidRequestException, UserDoesNotExistException {
        store.setStoreID(storeUUID1);
        store.setShoppers(shopperList);
        RemoveShopperRequest request=new RemoveShopperRequest(shopperID,storeUUID1);
        shopperRepo.save(shopper);
        storeRepo.save(store);
        RemoveShopperResponse response=ServiceSelector.getShoppingService().removeShopper(request);
        assertNotNull(response);
        assertEquals(false,response.isSuccess());
        assertEquals("Shopper isn't in list of shoppers in store entity",response.getMessage());
    }

    @Test
    @Description("Test for when shopper is correctly removed from the list")
    @DisplayName("Shopper was correctly removed from list of shoppers in store")
    void Shopper_correctly_removed() throws InvalidRequestException, StoreDoesNotExistException, cs.superleague.user.exceptions.InvalidRequestException, UserDoesNotExistException {
        store.setStoreID(storeUUID1);
        store.setShoppers(shopperList);
        RemoveShopperRequest request=new RemoveShopperRequest(shopper1.getShopperID(),storeUUID1);
        storeRepo.save(store);
        GetShopperByUUIDResponse shopperResponse=new GetShopperByUUIDResponse(shopper1,null,null);
        RemoveShopperResponse response=ServiceSelector.getShoppingService().removeShopper(request);
        assertNotNull(response);
        assertEquals(true,response.isSuccess());
        assertEquals("Shopper was successfully removed",response.getMessage());
        Store storeResponse=storeRepo.findById(store.getStoreID()).orElse(null);
        assertEquals(shopperList.size()-1,storeResponse.getShoppers().size());
        List<Shopper> listOfShoppersResponse=storeResponse.getShoppers();
        shopperList.remove(shopper1);
        for(int i=0;i<shopperList.size();i++){
            assertEquals(shopperList.get(i).getShopperID(),listOfShoppersResponse.get(i).getShopperID());
        }
    }
}
