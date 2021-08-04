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
import cs.superleague.user.requests.AddToCartRequest;
import cs.superleague.user.responses.AddToCartResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AddToCartIntegrationTest {

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

    UUID userID;
    UUID groceryListID;
    UUID expectedS1;

    GeoPoint deliveryAddress;

    List<Item> listOfItems = new ArrayList<>();
    List<GroceryList> groceryLists = new ArrayList<>();
    List<Item> shoppingCart = new ArrayList<>();
    List<Item> shoppingCartNULL = new ArrayList<>();

    AddToCartRequest request;
    AddToCartResponse response;

    @BeforeEach
    void setUp() {
        userID = UUID.randomUUID();
        groceryListID = UUID.randomUUID();
        expectedS1 = UUID.randomUUID();

        I1=new Item("Heinz Tamatoe Sauce","123456","123456",expectedS1,36.99,1,"description","img/");
        I2=new Item("Bar one","012345","012345",expectedS1,14.99,3,"description","img/");
        listOfItems.add(I1);
        listOfItems.add(I2);

        shoppingCartNULL = null;
        shoppingCart.add(I1);
        shoppingCart.add(I2);

        deliveryAddress = new GeoPoint(2.0, 2.0, "2616 Urban Quarters, Hatfield");

        groceryList = new GroceryList(groceryListID, "Seamus' party", listOfItems);
        groceryLists.add(groceryList);
        customer = new Customer("D", "S", "ds@smallClub.com", "0721234567", "", new Date(), "", "", "", true,
                UserType.CUSTOMER, userID, deliveryAddress, groceryLists, shoppingCart, null, null);

        itemRepo.saveAll(shoppingCart);
        groceryListRepo.saveAll(groceryLists);
        customerRepo.save(customer);
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
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.addToCart(null));
        assertEquals("addToCart Request is null - Could not add to cart", thrown.getMessage());
    }

    @Test
    @DisplayName("When userID parameter is not specified")
    void IntegrationTest_testingNullRequestUserIDParameter(){
        request = new AddToCartRequest(null, shoppingCart);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.addToCart(request));
        assertEquals("CustomerId is null - could not add to cart", thrown.getMessage());
    }

    @Test
    @DisplayName("When customer with given UserID does not exist")
    void IntegrationTest_testingInvalidUser(){
        request = new AddToCartRequest(UUID.randomUUID(), shoppingCart);
        Throwable thrown = Assertions.assertThrows(UserDoesNotExistException.class, ()-> userService.addToCart(request));
        assertEquals("User with given userID does not exist - could add to cart", thrown.getMessage());
    }

    @Test
    @DisplayName("When item list is null/empty")
    void IntegrationTest_testingNullEmptyItemList(){
        request = new AddToCartRequest(userID, new ArrayList<>());

        try {
            response = userService.addToCart(request);
            assertEquals("Item list empty - could not add to cart", response.getMessage());
            assertFalse(response.isSuccess());
        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("When nonnull update values are given")
    void IntegrationTest_testingSuccessfulUpdate(){
        request = new AddToCartRequest(userID, shoppingCart);

        try {
            response = userService.addToCart(request);
            assertEquals("Items successfully added to cart", response.getMessage());
            assertTrue(response.isSuccess());
        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }
}
