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
import cs.superleague.user.requests.GetShoppingCartRequest;
import cs.superleague.user.responses.GetShoppingCartResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class GetShoppingCartIntegrationTest {

    @Autowired
    CustomerRepo customerRepo;

    @Autowired
    GroceryListRepo groceryListRepo;

    @Autowired
    ItemRepo itemRepo;

    @Autowired
    private UserServiceImpl userService;

    GroceryList groceryList;
    GroceryList groceryListEmptyCart;
    Customer customer;
    Customer customerEMPTYCart;
    Item I1;
    Item I2;
    Item item;

    UUID userID;
    UUID userID_EMPTY;
    UUID groceryListID;
    UUID expectedS1;

    GeoPoint deliveryAddress;

    List<Item> listOfItems = new ArrayList<>();
    List<GroceryList> groceryLists = new ArrayList<>();
    List<GroceryList> groceryListsEmptyCart = new ArrayList<>();
    List<Item> shoppingCart = new ArrayList<>();
    List<Item> shoppingCartEMPTY = new ArrayList<>();

    @BeforeEach
    void setUp() {
        userID = UUID.randomUUID();
        userID_EMPTY = UUID.randomUUID();

        groceryListID = UUID.randomUUID();
        expectedS1 = UUID.randomUUID();

        I1=new Item("Heinz Tamatoe Sauce","123456","123456",expectedS1,36.99,1,"description","img/");
        I2=new Item("Bar one","012345","012345",expectedS1,14.99,3,"description","img/");
        item = null;

        listOfItems.add(I1);
        listOfItems.add(I2);

        shoppingCart.add(I1);
        shoppingCart.add(I2);

        itemRepo.saveAll(shoppingCart);

        deliveryAddress = new GeoPoint(2.0, 2.0, "2616 Urban Quarters, Hatfield");

        groceryList = new GroceryList(groceryListID, "Seamus' party", listOfItems);
        groceryListEmptyCart = new GroceryList(UUID.randomUUID(), "Seamus' party", listOfItems);

        groceryLists.add(groceryList);
        groceryListsEmptyCart.add(groceryListEmptyCart);

        groceryListRepo.saveAll(groceryLists);
        groceryListRepo.saveAll(groceryListsEmptyCart);

        customer = new Customer("D", "S", "ds@smallClub.com", "0721234567", "", new Date(), "", "", "", true,
                UserType.CUSTOMER, userID, deliveryAddress, groceryLists, shoppingCart, null, null);


        customerEMPTYCart = new Customer("D", "S", "ds@smallClub.com", "0721234567", "", new Date(), "", "", "", true,
                UserType.CUSTOMER, userID_EMPTY, deliveryAddress, groceryListsEmptyCart, shoppingCartEMPTY, null, null);


        customerRepo.save(customer);
        customerRepo.save(customerEMPTYCart);
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
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.getShoppingCart(null));
        assertEquals("GetShoppingCart Request is null - could retrieve shopping cart", thrown.getMessage());
    }

    @Test
    @DisplayName("When userID parameter is not specified")
    void IntegrationTest_testingNullRequestUserIDParameter(){
        GetShoppingCartRequest request  = new GetShoppingCartRequest(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.getShoppingCart(request));
        assertEquals("UserID is null - could retrieve shopping cart", thrown.getMessage());
    }

    @Test
    @DisplayName("When customer with given UserID does not exist")
    void IntegrationTest_testingInvalidUser(){
        GetShoppingCartRequest request  = new GetShoppingCartRequest(UUID.randomUUID());
        Throwable thrown = Assertions.assertThrows(UserDoesNotExistException.class, ()-> userService.getShoppingCart(request));
        assertEquals("User with given userID does not exist - could not retrieve shopping cart", thrown.getMessage());
    }

    @Test
    @DisplayName("When an empty shoppingCart is returned")
    void UnitTest_ShoppingCartDoesNotExist_EMPTY(){
        GetShoppingCartRequest request  = new GetShoppingCartRequest(userID_EMPTY);
        try{
            GetShoppingCartResponse response = userService.getShoppingCart(request);
            assertEquals("Shopping Cart does not have any items", response.getMessage());
            assertFalse(response.isSuccess());
            assertEquals(0, response.getShoppingCart().size());
        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("When the groceryList Creation is successful")
    void UnitTest_testingSuccessfulGroceryListCreation(){
        GetShoppingCartRequest request  = new GetShoppingCartRequest(userID);

        try{
            GetShoppingCartResponse response = userService.getShoppingCart(request);
            assertEquals("Shopping cart successfully retrieved", response.getMessage());
            assertTrue(response.isSuccess());
            assertEquals(shoppingCart.containsAll(response.getShoppingCart()), response.getShoppingCart().containsAll(shoppingCart));
        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }
}
