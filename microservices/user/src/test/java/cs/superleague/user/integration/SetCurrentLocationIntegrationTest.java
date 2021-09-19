//package cs.superleague.user.integration;
//
//import cs.superleague.payment.dataclass.GeoPoint;
//import cs.superleague.shopping.ShoppingService;
//import cs.superleague.shopping.dataclass.Catalogue;
//import cs.superleague.shopping.dataclass.Item;
//import cs.superleague.shopping.dataclass.Store;
//import cs.superleague.user.UserServiceImpl;
//import cs.superleague.user.dataclass.Customer;
//import cs.superleague.user.dataclass.Driver;
//import cs.superleague.user.dataclass.GroceryList;
//import cs.superleague.user.exceptions.DriverDoesNotExistException;
//import cs.superleague.user.exceptions.InvalidRequestException;
//import cs.superleague.user.exceptions.UserException;
//import cs.superleague.user.repos.DriverRepo;
//import cs.superleague.user.requests.SetCurrentLocationRequest;
//import cs.superleague.user.responses.SetCurrentLocationResponse;
//import org.junit.jupiter.api.*;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.when;
//
//@SpringBootTest
//@Transactional
//public class SetCurrentLocationIntegrationTest {
//
//    @Autowired
//    DriverRepo driverRepo;
//
//
//
//    @Autowired
//    private ShoppingService shoppingService;
//
//    @Autowired
//    private UserServiceImpl userService;
//
//    GroceryList groceryList;
//    Customer customer;
//    Store store;
//    Driver driver;
//    Catalogue catalogue;
//    Item I1;
//    Item I2;
//
//    UUID userID;
//    UUID groceryListID;
//    UUID expectedS1;
//
//    GeoPoint deliveryAddress;
//
//    List<Item> listOfItems = new ArrayList<>();
//    List<String> listOfBarcodes = new ArrayList<>();
//    List<GroceryList> groceryLists = new ArrayList<>();
//    List<Store> notExistStores = new ArrayList<>();
//    List<Store> listOfStores = new ArrayList<>();
//    List<Item> shoppingCart = new ArrayList<>();
//    List<Item> shoppingCartNULL = new ArrayList<>();
//
//    SetCurrentLocationRequest request;
//    SetCurrentLocationResponse response;
//
//    @BeforeEach
//    void setUp() {
//
//        userID = UUID.randomUUID();
//
//        driver = new Driver();
//        driver.setDriverID(userID);
//
//        driverRepo.save(driver);
//
//    }
//
//    @AfterEach
//    void tearDown(){
//    }
//
//
//    @Test
//    @DisplayName("When request object is not specified")
//    void IntegrationTest_testingNullRequestObject(){
//        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.setCurrentLocation(null));
//        assertEquals("SetCurrentLocationRequest is null - could not set current location", thrown.getMessage());
//    }
//
//    @Test
//    @DisplayName("When userID parameter is not specified")
//    void IntegrationTest_testingNullRequestUserIDParameter(){
//        request = new SetCurrentLocationRequest(null, 0.0, 0.0, null);
//        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.setCurrentLocation(request));
//        assertEquals("DriverID attribute is null - could not set current location", thrown.getMessage());
//    }
//
//    @Test
//    @DisplayName("When longitude parameter is not specified")
//    void IntegrationTest_testingNullRequestLongitudeParameter(){
//        request = new SetCurrentLocationRequest(UUID.randomUUID().toString(), 0.0, 0.0, null);
//        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.setCurrentLocation(request));
//        assertEquals("Longitude attribute is null - could not set current location", thrown.getMessage());
//    }
//
//    @Test
//    @DisplayName("When latitude parameter is not specified")
//    void IntegrationTest_testingNullRequestLatitudeParameter(){
//        request = new SetCurrentLocationRequest(UUID.randomUUID().toString(), 1.00, 0.0, null);
//        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.setCurrentLocation(request));
//        assertEquals("Latitude attribute is null - could not set current location", thrown.getMessage());
//    }
//
//    @Test
//    @DisplayName("When address parameter is not specified")
//    void IntegrationTest_testingNullRequestAddressParameter(){
//        request = new SetCurrentLocationRequest(UUID.randomUUID().toString(), 1.00, 1.00, null);
//        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.setCurrentLocation(request));
//        assertEquals("Address attribute is null - could not set current location", thrown.getMessage());
//    }
//
//    @Test
//    @DisplayName("When driverID given does not exist")
//    void IntegrationTest_testingDriverIDNotExistParameter(){
//        request = new SetCurrentLocationRequest(UUID.randomUUID().toString(), 1.00, 1.00, "");
//        Throwable thrown = Assertions.assertThrows(DriverDoesNotExistException.class, ()-> userService.setCurrentLocation(request));
//        assertEquals("Driver with driverID does not exist in database", thrown.getMessage());
//    }
//
//    @Test
//    @DisplayName("When address given is empty")
//    void IntegrationTest_testingEmptyAddress(){
//        request = new SetCurrentLocationRequest(userID.toString(), 1.00, 1.00, "");
//
//        try{
//            response = userService.setCurrentLocation(request);
//            assertFalse(response.isSuccess());
//            assertEquals("address cannot be empty", response.getMessage());
//        }catch(UserException e){
//            e.printStackTrace();
//            fail();
//        }
//    }
//
//    @Test
//    @DisplayName("When address given is valid")
//    void IntegrationTest_testingValidAddress(){
//        request = new SetCurrentLocationRequest(userID.toString(), 1.00, 1.00, "building");
//
//        try{
//            response = userService.setCurrentLocation(request);
//            assertTrue(response.isSuccess());
//            assertEquals("driver location successfully updated", response.getMessage());
//        }catch(UserException e){
//            e.printStackTrace();
//            fail();
//        }
//    }
//}
