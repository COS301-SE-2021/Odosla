package cs.superleague.shopping.integration;

import cs.superleague.integration.ServiceSelector;
import cs.superleague.shopping.ShoppingServiceImpl;
import cs.superleague.shopping.dataclass.Store;
import cs.superleague.shopping.exceptions.InvalidRequestException;
import cs.superleague.shopping.exceptions.StoreDoesNotExistException;
import cs.superleague.shopping.repos.StoreRepo;
import cs.superleague.shopping.requests.GetShoppersRequest;
import cs.superleague.shopping.responses.GetShoppersResponse;
import cs.superleague.user.UserServiceImpl;
import cs.superleague.user.dataclass.Shopper;
import cs.superleague.user.repos.ShopperRepo;
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
public class GetShoppersIntegrationTest {


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
    @Description("Tests for when getShoppers is submited with a null request object- exception should be thrown")
    @DisplayName("When request object is not specificed")
    void IntegrationTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> ServiceSelector.getShoppingService().getShoppers(null));
        assertEquals("Request object for get Shoppers can't be null", thrown.getMessage());
    }

    @Test
    @Description("Tests for when getShoppers is submited store ID in request object being null- exception should be thrown")
    @DisplayName("When request object has null parameter")
    void IntegrationTest_StoreID_inRequest_NullRequestObject(){
        GetShoppersRequest request=new GetShoppersRequest(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> ServiceSelector.getShoppingService().getShoppers(request));
        assertEquals("Store ID in request object can't be null", thrown.getMessage());
    }

    @Test
    @Description("Tests whether the GetShoppers request object was created correctly")
    @DisplayName("GetShoppers correctly constructed")
    void IntegrationTest_GetShoppersRequestConstruction() {

        GetShoppersRequest request=new GetShoppersRequest(storeUUID1);
        assertNotNull(request);
        assertEquals(storeUUID1, request.getStoreID());
    }

    @Test
    @Description("Test for when Store with storeID does not exist in database - StoreDoesNotExist Exception should be thrown")
    @DisplayName("When Store with ID doesn't exist")
    void IntegrationTest_Store_doesnt_exist(){
        GetShoppersRequest request=new GetShoppersRequest(storeUUID1);
        Throwable thrown = Assertions.assertThrows(StoreDoesNotExistException.class, ()-> ServiceSelector.getShoppingService().getShoppers(request));
        assertEquals("Store with ID does not exist in repository - could not get Shoppers", thrown.getMessage());
    }


    @Test
    @Description("Test for when list of shoppers is correct")
    @DisplayName("Correct shoppers list")
    void Correct_shoppers_list() throws InvalidRequestException, StoreDoesNotExistException {
        store.setStoreID(storeUUID1);
        store.setShoppers(shopperList);
        GetShoppersRequest request=new GetShoppersRequest(storeUUID1);
        storeRepo.save(store);
        GetShoppersResponse response=ServiceSelector.getShoppingService().getShoppers(request);
        assertNotNull(response);
        assertEquals(true,response.isSuccess());
        assertEquals("List of Shoppers successfully returned",response.getMessage());
        Store storeResponse=storeRepo.findById(store.getStoreID()).orElse(null);
        for (int i=0;i<shopperList.size();i++){
            assertEquals(shopperList.get(i).getShopperID(),storeResponse.getShoppers().get(i).getShopperID());
        }
    }



}
