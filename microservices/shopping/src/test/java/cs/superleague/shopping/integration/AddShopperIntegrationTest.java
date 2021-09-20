//package cs.superleague.shopping.integration;
//
//import cs.superleague.shopping.ShoppingServiceImpl;
//import cs.superleague.shopping.dataclass.Store;
//import cs.superleague.shopping.exceptions.InvalidRequestException;
//import cs.superleague.shopping.exceptions.StoreDoesNotExistException;
//import cs.superleague.shopping.repos.StoreRepo;
//import cs.superleague.shopping.requests.AddShopperRequest;
//import cs.superleague.shopping.responses.AddShopperResponse;
//import cs.superleague.user.dataclass.Shopper;
//import cs.superleague.user.exceptions.ShopperDoesNotExistException;
//import cs.superleague.user.exceptions.UserException;
//import cs.superleague.user.requests.SaveShopperToRepoRequest;
//import org.junit.jupiter.api.*;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Description;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//@SpringBootTest
//@Transactional
//public class AddShopperIntegrationTest {
//
//    @Autowired
//    ShoppingServiceImpl shoppingService;
//
//    @Autowired
//    StoreRepo storeRepo;
//
//    @Autowired
//    RestTemplate restTemplate;
//
//    @Autowired
//    RabbitTemplate rabbit;
//
//    UUID storeUUID1= UUID.randomUUID();
//    Store store;
//    Shopper shopper;
//    Shopper shopper1;
//    Shopper shopper2;
//    UUID shopperID=UUID.randomUUID();
//    UUID shopperID2=UUID.randomUUID();
//    UUID shopperID3=UUID.randomUUID();
//    UUID storeID=UUID.randomUUID();
//    List<Shopper> shopperList=new ArrayList<>();
//
//    @BeforeEach
//    void setUp() {
//        store=new Store();
//        shopper=new Shopper();
//        shopper.setShopperID(shopperID);
//        shopper1=new Shopper();
//        shopper1.setShopperID(shopperID2);
//        shopper2=new Shopper();
//        shopper2.setShopperID(shopperID3);
//        shopperList.add(shopper1);
//        shopperList.add(shopper2);
//
//        SaveShopperToRepoRequest saveShopperToRepoRequest = new SaveShopperToRepoRequest(shopper1);
//        rabbit.convertAndSend("UserEXCHANGE", "RK_SaveShopperToRepo", saveShopperToRepoRequest);
//        saveShopperToRepoRequest = new SaveShopperToRepoRequest(shopper2);
//        rabbit.convertAndSend("UserEXCHANGE", "RK_SaveShopperToRepo", saveShopperToRepoRequest);
//    }
//
//    @AfterEach
//    void tearDown() {
//
//    }
//
//    @Test
//    @Description("Tests for when addShoppers is submited with a null request object- exception should be thrown")
//    @DisplayName("When request object is not specificed")
//    void IntegrationTest_testingNullRequestObject(){
//        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> shoppingService.addShopper(null));
//        assertEquals("Request object can't be null for addShopper", thrown.getMessage());
//    }
//
//    @Test
//    @Description("Tests for when addShoppers is submited store ID in request object being null- exception should be thrown")
//    @DisplayName("When request object has null parameter")
//    void IntegrationTest_StoreID_inRequest_NullRequestObject(){
//        AddShopperRequest request=new AddShopperRequest(shopperID,null);
//        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> shoppingService.addShopper(request));
//        assertEquals("Store ID in request object for add shopper is null", thrown.getMessage());
//    }
//    @Test
//    @Description("Tests for when addShoppers is submited shopper ID in request object being null- exception should be thrown")
//    @DisplayName("When request object has null parameter")
//    void IntegrationTest_ShopperID_inRequest_NullRequestObject(){
//        AddShopperRequest request=new AddShopperRequest(null,storeID);
//        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> shoppingService.addShopper(request));
//        assertEquals("Shopper ID in request object for add shopper is null", thrown.getMessage());
//    }
//
//    @Test
//    @Description("Tests whether the addShoppers request object was created correctly")
//    @DisplayName("AddShopper request correctly constructed")
//    void IntegrationTest_AddShoppersRequestConstruction() {
//        AddShopperRequest request=new AddShopperRequest(shopperID,storeID);
//        assertNotNull(request);
//        assertEquals(storeID,request.getStoreID());
//        assertEquals(shopperID,request.getShopperID());
//    }
//
//    @Test
//    @Description("Test for when Store with storeID does not exist in database - StoreDoesNotExist Exception should be thrown")
//    @DisplayName("When Store with ID doesn't exist")
//    void IntegrationTest_Store_doesnt_exist(){
//        AddShopperRequest request=new AddShopperRequest(shopperID,storeID);
//        Throwable thrown = Assertions.assertThrows(StoreDoesNotExistException.class, ()-> shoppingService.addShopper(request));
//        assertEquals("Store with ID does not exist in repository - could not add Shopper", thrown.getMessage());
//    }
//
//    @Test
//    @Description("Test for when Shopper with shopperID does not exist in shopper database ")
//    @DisplayName("When Shopper with ID doesn't exist")
//    void IntegrationTest_Shopper_doesnt_exist() throws InvalidRequestException, cs.superleague.user.exceptions.InvalidRequestException, StoreDoesNotExistException {
//        store.setStoreID(storeUUID1);
//        store.setShoppers(shopperList);
//        AddShopperRequest request=new AddShopperRequest(shopperID,storeUUID1);
//        storeRepo.save(store);
//        Throwable thrown = Assertions.assertThrows(ShopperDoesNotExistException.class, ()-> shoppingService.addShopper(request));
//        assertEquals("User with ID does not exist in repository - could not get Shopper entity",thrown.getMessage());
//    }
//
//
//    @Test
//    @Description("Test for when list of shoppers already has Shopper in its list")
//    @DisplayName("Shopper Id already in list of Shoppers")
//    void Store_already_contains_shopper() throws InvalidRequestException, StoreDoesNotExistException, UserException {
//        store.setStoreID(storeUUID1);
//        store.setShoppers(shopperList);
//        AddShopperRequest request=new AddShopperRequest(shopper1.getShopperID(),storeUUID1);
//        storeRepo.save(store);
//        AddShopperResponse response= shoppingService.addShopper(request);
//        assertNotNull(response);
//        assertEquals(false,response.isSuccess());
//        assertEquals("Shopper already is in listOfShoppers",response.getMessage());
//    }
//
//    @Test
//    @Description("Test for when shopper is correctly added to the list")
//    @DisplayName("Shopper was correctly added list of shoppers in store")
//    void Shopper_correctly_added() throws InvalidRequestException, StoreDoesNotExistException, UserException {
//        store.setStoreID(storeUUID1);
//        store.setShoppers(shopperList);
//        AddShopperRequest request=new AddShopperRequest(shopperID,storeUUID1);
//        storeRepo.save(store);
//        SaveShopperToRepoRequest saveShopperToRepoRequest = new SaveShopperToRepoRequest(shopper);
//        rabbit.convertAndSend("UserEXCHANGE", "RK_SaveShopperToRepo", saveShopperToRepoRequest);
//        int size=shopperList.size();
//        AddShopperResponse response = shoppingService.addShopper(request);
//        assertNotNull(response);
//        assertEquals(true,response.isSuccess());
//        assertEquals("Shopper was successfully added",response.getMessage());
//        Store store=storeRepo.findById(storeUUID1).orElse(null);
//        assertEquals(size+1,store.getShoppers().size());
//        shopperList.add(shopper);
//        List<Shopper> shopperList1=store.getShoppers();
//        for(int i=0;i<shopperList.size();i++){
//            assertEquals(shopperList.get(i).getShopperID(),shopperList1.get(i).getShopperID());
//        }
//    }
//}
