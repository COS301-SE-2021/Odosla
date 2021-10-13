package cs.superleague.user;

import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.shopping.dataclass.Catalogue;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.shopping.dataclass.Store;
import cs.superleague.shopping.responses.GetStoresResponse;
import cs.superleague.user.dataclass.Customer;
import cs.superleague.user.dataclass.GroceryList;
import cs.superleague.user.dataclass.UserType;
import cs.superleague.user.exceptions.CustomerDoesNotExistException;
import cs.superleague.user.exceptions.InvalidRequestException;
import cs.superleague.user.repos.CustomerRepo;
import cs.superleague.user.requests.SetCartRequest;
import cs.superleague.user.responses.SetCartResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/** Testing use cases with JUnit testing and Mockito */
@ExtendWith(MockitoExtension.class)
public class SetCartUnitTest {

    @Mock
    CustomerRepo customerRepo;
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

    SetCartRequest request;
    SetCartResponse response;

    @BeforeEach
    void setUp() {
        userID = UUID.randomUUID();
        groceryListID = UUID.randomUUID();
        expectedS1 = UUID.randomUUID();

        listOfBarcodes.add("123456");

        I1=new Item("Heinz Tamatoe Sauce","123456","123456",expectedS1,36.99,1,"description","img/");
        I2=new Item("Bar one","012345","012345",expectedS1,14.99,3,"description","img/");
        listOfItems.add(I1);
        listOfItems.add(I2);

        catalogue = new Catalogue(UUID.randomUUID(),listOfItems);
        shoppingCartNULL = null;

        deliveryAddress = new GeoPoint(2.0, 2.0, "2616 Urban Quarters, Hatfield");


        store = new Store(expectedS1,"Checkers",catalogue,2,null,null,4,true);
        listOfStores.add(store);

        groceryList = new GroceryList(groceryListID, "Seamus' party", listOfItems);
        groceryLists.add(groceryList);
        customer = new Customer("D", "S", "ds@smallClub.com", "0721234567", "", new Date(), "", "", "", true,
                UserType.CUSTOMER, userID, deliveryAddress, groceryLists, shoppingCart, null, null);
    }

    @AfterEach
    void tearDown(){
    }


    @Test
    @DisplayName("When request object is not specified")
    void UnitTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.setCart(null));
        assertEquals("addToCart Request is null - Could not add to cart", thrown.getMessage());
    }

    @Test
    @DisplayName("When userID parameter is not specified")
    void UnitTest_testingNullRequestUserIDParameter(){
        request = new SetCartRequest(null, listOfBarcodes);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.setCart(request));
        assertEquals("CustomerId is null - could not add to cart", thrown.getMessage());
    }

    @Test
    @DisplayName("When customer with given UserID does not exist")
    void UnitTest_testingInvalidUser(){
        request = new SetCartRequest(UUID.randomUUID().toString(), listOfBarcodes);
        when(customerRepo.findById(Mockito.any())).thenReturn(null);
        Throwable thrown = Assertions.assertThrows(CustomerDoesNotExistException.class, ()-> userService.setCart(request));
        assertEquals("User with given userID does not exist - could add to cart", thrown.getMessage());
    }

    @Test
    @DisplayName("When barcodes list is null/empty")
    void UnitTest_testingNullEmptyItemList(){
        request = new SetCartRequest(userID.toString(), new ArrayList<>());
        when(customerRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(customer));

        try {
            response = userService.setCart(request);
            assertEquals("Item list empty - could not add to cart", response.getMessage());
            assertFalse(response.isSuccess());
        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("When the barcodes do not exist")
    void UnitTest_testing_Barcodes_DoneExist(){
        request = new SetCartRequest(userID.toString(), listOfBarcodes);
        when(customerRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(customer));

        try{
            //when(shoppingService.getStores(Mockito.any())).thenReturn(new GetStoresResponse(true, "", notExistStores));
            Map<String, Object> parts = new HashMap<String, Object>();

            String uriString = "http://"+shoppingHost+":"+shoppingPort+"/shopping/getStores";
            URI uri = new URI(uriString);
            GetStoresResponse getStoresResponse = new GetStoresResponse(true, "", notExistStores);
            ResponseEntity<GetStoresResponse> getStoresResponseEntity = new ResponseEntity<>(getStoresResponse, HttpStatus.OK);
            when(restTemplate.postForEntity(uri, parts, GetStoresResponse.class)).thenReturn(getStoresResponseEntity);

            response = userService.setCart(request);
            assertEquals("Cannot find item with given barcode - could not add to cart", response.getMessage());
            assertFalse(response.isSuccess());
        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("When nonnull update values are given")
    void UnitTest_testingSuccessfulUpdate(){
        request = new SetCartRequest(userID.toString(), listOfBarcodes);
        when(customerRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(customer));

        try {
            //when(shoppingService.getStores(Mockito.any())).thenReturn(new GetStoresResponse(true, "", listOfStores));
            Map<String, Object> parts = new HashMap<String, Object>();

            String uriString = "http://"+shoppingHost+":"+shoppingPort+"/shopping/getStores";
            URI uri = new URI(uriString);
            GetStoresResponse getStoresResponse = new GetStoresResponse(true, "", listOfStores);
            ResponseEntity<GetStoresResponse> getStoresResponseEntity = new ResponseEntity<>(getStoresResponse, HttpStatus.OK);
            when(restTemplate.postForEntity(uri, parts, GetStoresResponse.class)).thenReturn(getStoresResponseEntity);

            response = userService.setCart(request);
            assertEquals("Items successfully added to cart", response.getMessage());
            assertTrue(response.isSuccess());
        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }
}
