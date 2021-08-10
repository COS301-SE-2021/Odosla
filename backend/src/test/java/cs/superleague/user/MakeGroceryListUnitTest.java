package cs.superleague.user;

import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.shopping.ShoppingService;
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
import cs.superleague.user.repos.GroceryListRepo;
import cs.superleague.user.requests.MakeGroceryListRequest;
import cs.superleague.user.responses.MakeGroceryListResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/** Testing use cases with JUnit testing and Mockito */
@ExtendWith(MockitoExtension.class)
public class MakeGroceryListUnitTest {

    @Mock
    CustomerRepo customerRepo;

    @Mock
    GroceryListRepo groceryListRepo;

    @Mock
    private ShoppingService shoppingService;

    @InjectMocks
    private UserServiceImpl userService;

    GroceryList groceryList;
    Customer customer;
    Item I1;
    Item I2;
    Item item;
    Catalogue catalogue;
    Catalogue notExistCatalogue;
    Store store;

    UUID userID;
    UUID groceryListID;
    UUID expectedS1;

    GeoPoint deliveryAddress;

    List<Item> listOfItems = new ArrayList<>();
    List<Item> notExistItems = new ArrayList<>();
    List<String> listOfBarcodes = new ArrayList<>();
    List<Store> listOfStores = new ArrayList<>();
    List<Store> notExistStores = new ArrayList<>();
    List<GroceryList> groceryLists = new ArrayList<>();
    @BeforeEach
    void setUp() {
        userID = UUID.randomUUID();
        groceryListID = UUID.randomUUID();
        expectedS1 = UUID.randomUUID();

        listOfBarcodes.add("123456");

        I1=new Item("Heinz Tamatoe Sauce","123456","123456",expectedS1,36.99,1,"description","img/");
        I2=new Item("Bar one","012345","012345",expectedS1,14.99,3,"description","img/");
        item = null;

        listOfItems.add(I1);
        notExistItems.add(I2);

        deliveryAddress = new GeoPoint(2.0, 2.0, "2616 Urban Quarters, Hatfield");

        catalogue = new Catalogue(UUID.randomUUID(),listOfItems);
        notExistCatalogue = new Catalogue(UUID.randomUUID(), notExistItems);
        store = new Store(expectedS1,"Checkers",catalogue,2,null,null,4,true);
        listOfStores.add(store);
        notExistStores.add(new Store(expectedS1,"Checkers",notExistCatalogue,2,null,null,4,true));

        groceryList = new GroceryList(groceryListID, "Seamus' party", listOfItems);
        groceryLists.add(groceryList);
        customer = new Customer("D", "S", "ds@smallClub.com", "0721234567", "", new Date(), "", "", "", true,
                UserType.CUSTOMER, userID, deliveryAddress, groceryLists, listOfItems, null, null);
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
    @DisplayName("When userID parameter is not specified")
    void UnitTest_testingNullRequestUserIDParameter(){
        MakeGroceryListRequest request  = new MakeGroceryListRequest(null, listOfBarcodes, "Seamus' Party");
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.makeGroceryList(request));
        assertEquals("UserID is null - could not make grocery list", thrown.getMessage());
    }

    @Test
    @DisplayName("When barcodes parameter is not specified")
    void UnitTest_testingNullRequestBarcodesParameter(){
        MakeGroceryListRequest request  = new MakeGroceryListRequest(userID, null, "Seamus' Party");
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.makeGroceryList(request));
        assertEquals("Barcodes list empty - could not make the grocery list", thrown.getMessage());
    }

    @Test
    @DisplayName("When name parameter is not specified")
    void UnitTest_testingNullRequestNameParameter(){
        MakeGroceryListRequest request  = new MakeGroceryListRequest(userID, listOfBarcodes, null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.makeGroceryList(request));
        assertEquals("Grocery List Name is Null - could not make the grocery list", thrown.getMessage());
    }

    @Test
    @DisplayName("When customer with given UserID does not exist")
    void UnitTest_testingInvalidUser(){
        MakeGroceryListRequest request  = new MakeGroceryListRequest(userID, listOfBarcodes, "Seamus' party");
        when(customerRepo.findById(Mockito.any())).thenReturn(null);
        Throwable thrown = Assertions.assertThrows(CustomerDoesNotExistException.class, ()-> userService.makeGroceryList(request));
        assertEquals("User with given userID does not exist - could not make the grocery list", thrown.getMessage());
    }

    @Test
    @DisplayName("When groceryList with given name exists")
    void UnitTest_testingExistingGroceryList(){
        MakeGroceryListRequest request  = new MakeGroceryListRequest(userID, listOfBarcodes, "Seamus' party");
        when(customerRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(customer));

        try{
            MakeGroceryListResponse response = userService.makeGroceryList(request);
            assertFalse(response.isSuccess());
            assertEquals("Grocery List Name exists - could not make the grocery list", response.getMessage());
        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("When the barcodes do not exist")
    void UnitTest_testing_Barcodes_DoneExist(){
        MakeGroceryListRequest request  = new MakeGroceryListRequest(userID, listOfBarcodes, "Seamus' bachelor party");
        when(customerRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(customer));

        try{
            when(shoppingService.getStores(Mockito.any())).thenReturn(new GetStoresResponse(true, "", notExistStores));
            MakeGroceryListResponse response = userService.makeGroceryList(request);
            assertEquals("Cannot find item with given barcode - could not make the grocery list", response.getMessage());
            assertFalse(response.isSuccess());
        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("When the groceryList Creation is successful")
    void UnitTest_testingSuccessfulGroceryListCreation(){
        MakeGroceryListRequest request  = new MakeGroceryListRequest(userID, listOfBarcodes, "Seamus' bachelor party");
        when(customerRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(customer));

        try{
            when(shoppingService.getStores(Mockito.any())).thenReturn(new GetStoresResponse(true, "", listOfStores));
            MakeGroceryListResponse response = userService.makeGroceryList(request);
            assertEquals("Grocery List successfully created", response.getMessage());
            assertTrue(response.isSuccess());
        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }
}