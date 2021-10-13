package cs.superleague.user;

import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.shopping.dataclass.Catalogue;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.shopping.dataclass.Store;
import cs.superleague.shopping.responses.GetStoresResponse;
import cs.superleague.user.dataclass.Customer;
import cs.superleague.user.dataclass.Driver;
import cs.superleague.user.dataclass.GroceryList;
import cs.superleague.user.dataclass.UserType;
import cs.superleague.user.exceptions.CustomerDoesNotExistException;
import cs.superleague.user.exceptions.DriverDoesNotExistException;
import cs.superleague.user.exceptions.InvalidRequestException;
import cs.superleague.user.exceptions.UserException;
import cs.superleague.user.repos.CustomerRepo;
import cs.superleague.user.repos.DriverRepo;
import cs.superleague.user.requests.SetCartRequest;
import cs.superleague.user.requests.SetCurrentLocationRequest;
import cs.superleague.user.responses.SetCartResponse;
import cs.superleague.user.responses.SetCurrentLocationResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/** Testing use cases with JUnit testing and Mockito */
@ExtendWith(MockitoExtension.class)
public class SetCurrentLocationUnitTest {

    @Mock
    DriverRepo driverRepo;

    @Mock
    RestTemplate restTemplate;
    @Mock
    RabbitTemplate rabbitTemplate;
    @Value("${paymentPort}")
    private String paymentPort;
    @Value("${paymentHost}")
    private String paymentHost;
    @Value("${deliveryPort}")
    private String deliveryPort;
    @Value("${deliveryHost}")
    private String deliveryHost;
    @Value("${shoppingPort}")
    private String shoppingPort;
    @Value("${shoppingHost}")
    private String shoppingHost;

    @InjectMocks
    private UserServiceImpl userService;

    GroceryList groceryList;
    Customer customer;
    Store store;
    Driver driver;
    Catalogue catalogue;
    Item I1;
    Item I2;

    UUID userID;
    UUID groceryListID;
    UUID expectedS1;

    GeoPoint deliveryAddress;

    List<Item> listOfItems = new ArrayList<>();
    List<String> listOfBarcodes = new ArrayList<>();
    List<GroceryList> groceryLists = new ArrayList<>();
    List<Store> notExistStores = new ArrayList<>();
    List<Store> listOfStores = new ArrayList<>();
    List<Item> shoppingCart = new ArrayList<>();
    List<Item> shoppingCartNULL = new ArrayList<>();

    SetCurrentLocationRequest request;
    SetCurrentLocationResponse response;

    @BeforeEach
    void setUp() {

        userID = UUID.randomUUID();

        driver = new Driver();
        driver.setDriverID(userID);

    }

    @AfterEach
    void tearDown(){
    }


    @Test
    @DisplayName("When request object is not specified")
    void UnitTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.setCurrentLocation(null));
        assertEquals("SetCurrentLocationRequest is null - could not set current location", thrown.getMessage());
    }

    @Test
    @DisplayName("When userID parameter is not specified")
    void UnitTest_testingNullRequestUserIDParameter(){
        request = new SetCurrentLocationRequest(null, 0.0, 0.0, null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.setCurrentLocation(request));
        assertEquals("DriverID attribute is null - could not set current location", thrown.getMessage());
    }

    @Test
    @DisplayName("When longitude parameter is not specified")
    void UnitTest_testingNullRequestLongitudeParameter(){
        request = new SetCurrentLocationRequest(UUID.randomUUID().toString(), 0.0, 0.0, null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.setCurrentLocation(request));
        assertEquals("Longitude attribute is null - could not set current location", thrown.getMessage());
    }

    @Test
    @DisplayName("When latitude parameter is not specified")
    void UnitTest_testingNullRequestLatitudeParameter(){
        request = new SetCurrentLocationRequest(UUID.randomUUID().toString(), 1.00, 0.0, null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.setCurrentLocation(request));
        assertEquals("Latitude attribute is null - could not set current location", thrown.getMessage());
    }

    @Test
    @DisplayName("When address parameter is not specified")
    void UnitTest_testingNullRequestAddressParameter(){
        request = new SetCurrentLocationRequest(UUID.randomUUID().toString(), 1.00, 1.00, null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.setCurrentLocation(request));
        assertEquals("Address attribute is null - could not set current location", thrown.getMessage());
    }

    @Test
    @DisplayName("When driverID given does not exist")
    void UnitTest_testingDriverIDNotExistParameter(){
        request = new SetCurrentLocationRequest(UUID.randomUUID().toString(), 1.00, 1.00, "");
        Throwable thrown = Assertions.assertThrows(DriverDoesNotExistException.class, ()-> userService.setCurrentLocation(request));
        assertEquals("Driver with driverID does not exist in database", thrown.getMessage());
    }

    @Test
    @DisplayName("When address given is empty")
    void UnitTest_testingEmptyAddress(){
        request = new SetCurrentLocationRequest(userID.toString(), 1.00, 1.00, "");
        when(driverRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(driver));

        try{
            response = userService.setCurrentLocation(request);
            assertFalse(response.isSuccess());
            assertEquals("address cannot be empty", response.getMessage());
        }catch(UserException e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("When address given is valid")
    void UnitTest_testingValidAddress(){
        request = new SetCurrentLocationRequest(userID.toString(), 1.00, 1.00, "building");
        when(driverRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(driver));

        try{
            response = userService.setCurrentLocation(request);
            assertTrue(response.isSuccess());
            assertEquals("driver location successfully updated", response.getMessage());
        }catch(UserException e){
            e.printStackTrace();
            fail();
        }
    }
}
