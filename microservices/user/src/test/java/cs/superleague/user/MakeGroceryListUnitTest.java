package cs.superleague.user;

import cs.superleague.integration.security.JwtUtil;
import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.shopping.dataclass.Catalogue;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.shopping.dataclass.Store;
import cs.superleague.shopping.responses.GetStoresResponse;
import cs.superleague.user.dataclass.Customer;
import cs.superleague.user.dataclass.GroceryList;
import cs.superleague.user.dataclass.Shopper;
import cs.superleague.user.dataclass.UserType;
import cs.superleague.user.exceptions.CustomerDoesNotExistException;
import cs.superleague.user.exceptions.InvalidRequestException;
import cs.superleague.user.repos.CustomerRepo;
import cs.superleague.user.repos.GroceryListRepo;
import cs.superleague.user.repos.ShopperRepo;
import cs.superleague.user.requests.MakeGroceryListRequest;
import cs.superleague.user.responses.MakeGroceryListResponse;
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
public class MakeGroceryListUnitTest {
    @Mock
    RestTemplate restTemplate;
    @Mock
    RabbitTemplate rabbitTemplate;
    @Value("${shoppingPort}")
    private String shoppingPort;
    @Value("${shoppingHost}")
    private String shoppingHost;

    @Mock
    CustomerRepo customerRepo;

    @Mock
    GroceryListRepo groceryListRepo;

    @Mock
    ShopperRepo shopperRepo;


    @InjectMocks
    private UserServiceImpl userService;

    @InjectMocks
    JwtUtil jwtTokenUtil;

    GroceryList groceryList;
    Customer customer;
    Shopper shopper;
    Item I1;
    Item I2;
    Item item;
    Catalogue catalogue;
    Catalogue notExistCatalogue;
    Store store;

    UUID customerID;
    UUID shopperID;
    UUID groceryListID;
    UUID expectedS1;


    String customerEmail="customer@Email.com";
    String shopperEmail="shopper@Email.com";

    String jwtTokenCustomer;
    String jwtTokenShopper;

    GeoPoint deliveryAddress;

    List<Item> listOfItems = new ArrayList<>();
    List<Item> notExistItems = new ArrayList<>();
    List<String> listOfBarcodes = new ArrayList<>();
    List<Store> listOfStores = new ArrayList<>();
    List<Store> notExistStores = new ArrayList<>();
    List<GroceryList> groceryLists = new ArrayList<>();
    @BeforeEach
    void setUp() {

        customerID = UUID.randomUUID();
        shopperID = UUID.randomUUID();
        groceryListID = UUID.randomUUID();
        expectedS1 = UUID.randomUUID();

        shopper = new Shopper();
        shopper.setShopperID(shopperID);
        shopper.setName("JJ");
        shopper.setAccountType(UserType.SHOPPER);
        shopper.setEmail("customer@gmailp.com");

        groceryList = new GroceryList(groceryListID, "Seamus' party", listOfItems);
        groceryLists.add(groceryList);

        customer = new Customer();
        customer.setCustomerID(customerID);
        customer.setName("Pep");
        customer.setEmail("customer@gmaill.com");
        customer.setGroceryLists(groceryLists);
        customer.setAccountType(UserType.CUSTOMER);
        customer.setGroceryLists(groceryLists);

        jwtTokenCustomer=jwtTokenUtil.generateJWTTokenCustomer(customer);
        jwtTokenShopper=jwtTokenUtil.generateJWTTokenShopper(shopper);

        listOfBarcodes.add("123456");

        I1=new Item("Heinz Tamatoe Sauce","123456","123456",expectedS1,36.99,1,"description","img/");
        I2=new Item("Bar one","012345","012345",expectedS1,14.99,3,"description","img/");
        item = null;

        listOfItems.add(I1);
        notExistItems.add(I2);

        catalogue = new Catalogue(UUID.randomUUID(),listOfItems);
        notExistCatalogue = new Catalogue(UUID.randomUUID(), notExistItems);
        store = new Store(expectedS1,"Checkers",catalogue,2,null,null,4,true);
        listOfStores.add(store);
        notExistStores.add(new Store(expectedS1,"Checkers",notExistCatalogue,2,null,null,4,true));
    }

    @AfterEach
    void tearDown(){
    }


    @Test
    @DisplayName("When request object is not specified")
    void UnitTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.makeGroceryList(null));
        assertEquals("MakeGroceryList Request is null - could not make grocery list", thrown.getMessage());
    }

    @Test
    @DisplayName("When barcodes parameter is not specified")
    void UnitTest_testingNullRequestBarcodesParameter(){
        MakeGroceryListRequest request  = new MakeGroceryListRequest(null, "Seamus' Party");
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.makeGroceryList(request));
        assertEquals("Barcodes list empty - could not make the grocery list", thrown.getMessage());
    }

    @Test
    @DisplayName("When name parameter is not specified")
    void UnitTest_testingNullRequestNameParameter(){
        MakeGroceryListRequest request  = new MakeGroceryListRequest( listOfBarcodes, null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.makeGroceryList(request));
        assertEquals("Grocery List Name is Null - could not make the grocery list", thrown.getMessage());
    }


    @Test
    @DisplayName("When the barcodes do not exist")
    void UnitTest_testing_Barcodes_DoneExist(){
        MakeGroceryListRequest request  = new MakeGroceryListRequest(listOfBarcodes, "Seamus' bachelor party");
        when(customerRepo.findByEmail(Mockito.any())).thenReturn(Optional.ofNullable(customer));

        try{
            //when(shoppingService.getStores(Mockito.any())).thenReturn(new GetStoresResponse(true, "", notExistStores));
            Map<String, Object> parts = new HashMap<String, Object>();
            String uriString = "http://"+shoppingHost+":"+shoppingPort+"/shopping/getStores";
            URI uri = new URI(uriString);
            GetStoresResponse getStoresResponse = new GetStoresResponse(true, "", notExistStores);
            ResponseEntity<GetStoresResponse> getStoresResponseEntity = new ResponseEntity<>(getStoresResponse, HttpStatus.OK);
            when(restTemplate.postForEntity(uri, parts, GetStoresResponse.class)).thenReturn(getStoresResponseEntity);

            MakeGroceryListResponse response = userService.makeGroceryList(request);
            assertEquals("Cannot find item with given productID - could not make the grocery list", response.getMessage());
            assertFalse(response.isSuccess());
        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("When the groceryList Creation is successful")
    void UnitTest_testingSuccessfulGroceryListCreation(){
        MakeGroceryListRequest request  = new MakeGroceryListRequest(listOfBarcodes, "Seamus' bachelor party");
        when(customerRepo.findByEmail(Mockito.any())).thenReturn(Optional.ofNullable(customer));
        try{
            //when(shoppingService.getStores(Mockito.any())).thenReturn(new GetStoresResponse(true, "", listOfStores));
            Map<String, Object> parts = new HashMap<String, Object>();
            String uriString = "http://"+shoppingHost+":"+shoppingPort+"/shopping/getStores";
            URI uri = new URI(uriString);
            GetStoresResponse getStoresResponse = new GetStoresResponse(true, "", listOfStores);
            ResponseEntity<GetStoresResponse> getStoresResponseEntity = new ResponseEntity<>(getStoresResponse, HttpStatus.OK);
            when(restTemplate.postForEntity(uri, parts, GetStoresResponse.class)).thenReturn(getStoresResponseEntity);

            MakeGroceryListResponse response = userService.makeGroceryList(request);
            assertEquals("Grocery List successfully created", response.getMessage());
            assertTrue(response.isSuccess());
        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }
}