package cs.superleague.user.integration;

import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.shopping.repos.ItemRepo;
import cs.superleague.user.UserServiceImpl;
import cs.superleague.user.dataclass.Customer;
import cs.superleague.user.dataclass.GroceryList;
import cs.superleague.user.dataclass.UserType;
import cs.superleague.user.exceptions.InvalidRequestException;
import cs.superleague.user.exceptions.UserDoesNotExistException;
import cs.superleague.user.repos.CustomerRepo;
import cs.superleague.user.repos.GroceryListRepo;
import cs.superleague.user.requests.MakeGroceryListRequest;
import cs.superleague.user.responses.MakeGroceryListResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ComponentScan(basePackages = {"cs.superleague.user.repos"})
public class MakeGroceryListIntegrationTest {

    @Autowired
    CustomerRepo customerRepo;

    @Autowired
    GroceryListRepo groceryListRepo;

    @Autowired
    ItemRepo itemRepo;

    @Autowired
    private UserServiceImpl userService;

    GroceryList groceryList;
    Customer customer;
    Item I1;
    Item I2;
    Item item;

    UUID userID;
    UUID groceryListID;
    UUID expectedS1;

    GeoPoint deliveryAddress;

    List<Item> listOfItems = new ArrayList<>();
    List<GroceryList> groceryLists = new ArrayList<>();
    @BeforeEach
    void setUp() {
        userID = UUID.randomUUID();
        groceryListID = UUID.randomUUID();
        expectedS1 = UUID.randomUUID();

        I1=new Item("Heinz Tamatoe Sauce","123456","123456",expectedS1,36.99,1,"description","img/");
        I2=new Item("Bar one","012345","012345",expectedS1,14.99,3,"description","img/");
        item = null;

        listOfItems.add(I1);
        listOfItems.add(I2);

        listOfItems = itemRepo.saveAll(listOfItems);

        deliveryAddress = new GeoPoint(2.0, 2.0, "2616 Urban Quarters, Hatfield");

        groceryList = new GroceryList(groceryListID, "Seamus' party", listOfItems);
        groceryLists.add(groceryList);

        groceryLists = groceryListRepo.saveAll(groceryLists);

        customer = new Customer("D", "S", "ds@smallClub.com", "0721234567", "", new Date(), "", "", "", true,
                UserType.CUSTOMER, userID, deliveryAddress, groceryLists, listOfItems, null, null);

        customer = customerRepo.save(customer);
    }

    @AfterEach
    void tearDown(){
        customerRepo.deleteAll();
        groceryListRepo.deleteAll();
        itemRepo.deleteAll();
    }


    @Test
    @DisplayName("When request object is not specified")
    void IntegrationTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.makeGroceryList(null));
        assertEquals("MakeGroceryList Request is null - could not make grocery list", thrown.getMessage());
    }

    @Test
    @DisplayName("When userID parameter is not specified")
    void IntegrationTest_testingNullRequestUserIDParameter(){
        MakeGroceryListRequest request  = new MakeGroceryListRequest(null, null, "Seamus' Party");
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.makeGroceryList(request));
        assertEquals("UserID is null - could not make grocery list", thrown.getMessage());
    }

    @Test
    @DisplayName("When items parameter is not specified")
    void IntegrationTest_testingNullRequestItemsParameter(){
        MakeGroceryListRequest request  = new MakeGroceryListRequest(userID, null, "Seamus' Party");
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.makeGroceryList(request));
        assertEquals("Item list empty - could not make the grocery list", thrown.getMessage());
    }

    @Test
    @DisplayName("When items parameter is not specified")
    void IntegrationTest_testingNullRequestItemsListParameter(){
        MakeGroceryListRequest request  = new MakeGroceryListRequest(userID, null, "Seamus' Party");
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.makeGroceryList(request));
        assertEquals("Item list empty - could not make the grocery list", thrown.getMessage());
    }

    @Test
    @DisplayName("When name parameter is not specified")
    void IntegrationTest_testingNullRequestNameParameter(){
        MakeGroceryListRequest request  = new MakeGroceryListRequest(userID, null, null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.makeGroceryList(request));
        assertEquals("Grocery List Name is Null - could not make the grocery list", thrown.getMessage());
    }

    @Test
    @DisplayName("When customer with given UserID does not exist")
    void IntegrationTest_testingInvalidUser(){
        MakeGroceryListRequest request  = new MakeGroceryListRequest(UUID.randomUUID(), null, "Seamus' party");
        Throwable thrown = Assertions.assertThrows(UserDoesNotExistException.class, ()-> userService.makeGroceryList(request));
        assertEquals("User with given userID does not exist - could not make the grocery list", thrown.getMessage());
    }

    @Test
    @DisplayName("When groceryList with given name exists")
    void IntegrationTest_testingExistingGroceryList(){
        MakeGroceryListRequest request  = new MakeGroceryListRequest(userID, null, "Seamus' party");
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.makeGroceryList(request));
        assertEquals("Grocery List Name exists - could not make the grocery list", thrown.getMessage());
    }

    @Test
    @DisplayName("When the groceryList Creation is successful")
    void IntegrationTest_testingSuccessfulGroceryListCreation(){
        MakeGroceryListRequest request  = new MakeGroceryListRequest(userID, null, "Seamus' bachelor party");

        try{
            MakeGroceryListResponse response = userService.makeGroceryList(request);
            assertEquals("Grocery List successfully created", response.getMessage());
            assertTrue(response.isSuccess());
        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }
}
