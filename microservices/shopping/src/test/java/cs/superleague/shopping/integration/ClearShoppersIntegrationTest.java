package cs.superleague.shopping.integration;

import cs.superleague.shopping.ShoppingServiceImpl;
import cs.superleague.shopping.dataclass.Store;
import cs.superleague.shopping.exceptions.InvalidRequestException;
import cs.superleague.shopping.exceptions.StoreDoesNotExistException;
import cs.superleague.shopping.repos.StoreRepo;
import cs.superleague.shopping.requests.ClearShoppersRequest;
import cs.superleague.shopping.responses.ClearShoppersResponse;
import cs.superleague.user.dataclass.Shopper;
import cs.superleague.user.exceptions.UserDoesNotExistException;
import cs.superleague.user.requests.SaveShopperToRepoRequest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
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
public class ClearShoppersIntegrationTest {

    @Autowired
    ShoppingServiceImpl shoppingService;
    //OPTIONAL SERVICES

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

        SaveShopperToRepoRequest saveShopperToRepoRequest = new SaveShopperToRepoRequest(shopper1);
        rabbit.convertAndSend("UserEXCHANGE", "RK_SaveShopperToRepo", saveShopperToRepoRequest);
        saveShopperToRepoRequest = new SaveShopperToRepoRequest(shopper2);
        rabbit.convertAndSend("UserEXCHANGE", "RK_SaveShopperToRepo", saveShopperToRepoRequest);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Description("Tests for when clearShoppers is submited with a null request object- exception should be thrown")
    @DisplayName("When request object is not specificed")
    void IntegratioonTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> shoppingService.clearShoppers(null));
        assertEquals("Request object can't be null for clearShoppers", thrown.getMessage());
    }

    @Test
    @Description("Tests for when clearShoppers is submited store ID in request object being null- exception should be thrown")
    @DisplayName("When request object has null parameter")
    void IntegrationTest_StoreID_inRequest_NullRequestObject(){
        ClearShoppersRequest request=new ClearShoppersRequest(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> shoppingService.clearShoppers(request));
        assertEquals("Store ID in request object for clearShoppers can't be null", thrown.getMessage());
    }

    @Test
    @Description("Tests whether the clearShoppers request object was created correctly")
    @DisplayName("GetShoppers correctly constructed")
    void IntegrationTest_GetShoppersRequestConstruction() {
        ClearShoppersRequest request=new ClearShoppersRequest(storeUUID1);
        assertNotNull(request);
        assertEquals(storeUUID1, request.getStoreID());
    }

}
