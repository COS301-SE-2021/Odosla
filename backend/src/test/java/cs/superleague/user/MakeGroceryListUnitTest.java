package cs.superleague.user;

import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.user.dataclass.Customer;
import cs.superleague.user.dataclass.GroceryList;
import cs.superleague.user.exceptions.InvalidRequestException;
import cs.superleague.user.exceptions.UserDoesNotExistException;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/** Testing use cases with JUnit testing and Mockito */
@ExtendWith(MockitoExtension.class)
public class MakeGroceryListUnitTest {

    @Mock
    CustomerRepo customerRepo;

    @Mock
    GroceryListRepo groceryListRepo;

    @InjectMocks
    private UserServiceImpl userService;

    GroceryList groceryList;
    Customer customer;
    Item I1;
    Item I2;
    Item item;

    UUID userID;
    UUID groceryListID;

    UUID expectedS1=UUID.randomUUID();
    GeoPoint deliveryAddress;
    List<Item> listOfItems=new ArrayList<>();

    @BeforeEach
    void setUp() {
        userID = UUID.randomUUID();
        groceryListID = UUID.randomUUID();

        I1=new Item("Heinz Tamatoe Sauce","123456","123456",expectedS1,36.99,1,"description","img/");
        I2=new Item("Bar one","012345","012345",expectedS1,14.99,3,"description","img/");
        item = null;

        listOfItems.add(I1);
        listOfItems.add(I2);

        deliveryAddress = new GeoPoint(2.0, 2.0, "2616 Urban Quarters, Hatfield");

        customer = new Customer(deliveryAddress);
        groceryList = new GroceryList(groceryListID, "Seamus' Party", listOfItems, userID);
    }

    @AfterEach
    void tearDown(){
    }


    @Test
    @DisplayName("When request object is not specified")
    void UnitTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.MakeGroceryList(null));
        assertEquals("MakeGroceryList Request is null - could not make grocery list", thrown.getMessage());
    }

    @Test
    @DisplayName("When userID parameter is not specified")
    void UnitTest_testingNullRequestUserIDParameter(){
        MakeGroceryListRequest request  = new MakeGroceryListRequest(null, listOfItems, "Seamus' Party");
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.MakeGroceryList(request));
        assertEquals("UserID is null - could not make grocery list", thrown.getMessage());
    }

    @Test
    @DisplayName("When items parameter is not specified")
    void UnitTest_testingNullRequestItemsParameter(){
        MakeGroceryListRequest request  = new MakeGroceryListRequest(userID, item, "Seamus' Party");
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.MakeGroceryList(request));
        assertEquals("Item list empty - could not make the grocery list", thrown.getMessage());
    }

    @Test
    @DisplayName("When items parameter is not specified")
    void UnitTest_testingNullRequestItemsListParameter(){
        MakeGroceryListRequest request  = new MakeGroceryListRequest(userID, item, "Seamus' Party");
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.MakeGroceryList(request));
        assertEquals("Item list empty - could not make the grocery list", thrown.getMessage());
    }

    @Test
    @DisplayName("When name parameter is not specified")
    void UnitTest_testingNullRequestNameParameter(){
        MakeGroceryListRequest request  = new MakeGroceryListRequest(userID, listOfItems, null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.MakeGroceryList(request));
        assertEquals("Grocery List Name is Null - could not make the grocery list", thrown.getMessage());
    }

    @Test
    @DisplayName("When customer with given UserID does not exist")
    void UnitTest_testingInvalidUser(){
        MakeGroceryListRequest request  = new MakeGroceryListRequest(userID, listOfItems, "Seamus' party");
        when(customerRepo.findById(Mockito.any())).thenReturn(null);
        Throwable thrown = Assertions.assertThrows(UserDoesNotExistException.class, ()-> userService.MakeGroceryList(request));
        assertEquals("User with given userID does not exist - could not make the grocery list", thrown.getMessage());
    }

    @Test
    @DisplayName("When groceryList with given name exists")
    void UnitTest_testingExistingGroceryList(){
        MakeGroceryListRequest request  = new MakeGroceryListRequest(userID, listOfItems, "Seamus' party");
        when(customerRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(customer));
        when(groceryListRepo.findGroceryListByNameAndUserID(request.getName(), userID)).thenReturn(groceryList);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.MakeGroceryList(request));
        assertEquals("Grocery List Name exists - could not make the grocery list", thrown.getMessage());
    }

    @Test
    @DisplayName("When the groceryList Creation is successful")
    void UnitTest_testingSuccessfulGroceryListCreation(){
        MakeGroceryListRequest request  = new MakeGroceryListRequest(userID, listOfItems, "Seamus' party");
        when(customerRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(customer));
        when(groceryListRepo.findGroceryListByNameAndUserID(request.getName(), userID)).thenReturn(null);

        try{
            MakeGroceryListResponse response = userService.MakeGroceryList(request);
            assertEquals("Grocery List successfully created", response.getMessage());
            assertTrue(response.isSuccess());
        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }
}
