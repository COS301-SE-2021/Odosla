package cs.superleague.user.integration;

import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.shopping.repos.ItemRepo;
import cs.superleague.user.UserServiceImpl;
import cs.superleague.user.dataclass.Customer;
import cs.superleague.user.dataclass.GroceryList;
import cs.superleague.user.dataclass.UserType;
import cs.superleague.user.exceptions.CustomerDoesNotExistException;
import cs.superleague.user.exceptions.InvalidRequestException;
import cs.superleague.user.repos.CustomerRepo;
import cs.superleague.user.repos.GroceryListRepo;
import cs.superleague.user.requests.RemoveFromCartRequest;
import cs.superleague.user.responses.RemoveFromCartResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RemoveFromCartIntegrationTest {

    @Autowired
    CustomerRepo customerRepo;

    @Autowired
    GroceryListRepo groceryListRepo;

    @Autowired
    ItemRepo itemRepo;

    @Autowired
    UserServiceImpl userService;

    UUID userID;
    UUID storeID;
    UUID customerID;
    UUID groceryListID;
    UUID customerIDEmptyCart;

    GroceryList groceryList, groceryListEmptyCart, groceryListNullCart;
    Customer customerNullShoppingCart;
    Customer customerEmptyCart;
    Customer customer;
    GeoPoint address;
    Item item;

    List<Item> cart = new ArrayList<>();
    List<Item> emptyCart = new ArrayList<>();
    List<GroceryList> groceryListsNullCart = new ArrayList<>();
    List<GroceryList> groceryListsEmptyCart = new ArrayList<>();
    List<GroceryList> groceryLists = new ArrayList<>();

    RemoveFromCartRequest request;
    RemoveFromCartResponse response;

    @BeforeEach
    void setUp(){
        userID = UUID.randomUUID();
        customerID = UUID.randomUUID();
        storeID = UUID.randomUUID();
        groceryListID = UUID.randomUUID();
        customerIDEmptyCart = UUID.randomUUID();

        address = new GeoPoint(2.0, 2.0, "2616 Urban Quarters, Hatfield");
        item = new Item("Heinz Tamatoe Sauce","123456","123456",storeID,36.99,1,"description","img/");

        cart.add(item);

        groceryList = new GroceryList(groceryListID, "Seamus' party", cart);
        groceryLists.add(groceryList);
        groceryListEmptyCart = new GroceryList(UUID.randomUUID(), "Seamus' party", cart);
        groceryListsEmptyCart.add(groceryListEmptyCart);
        groceryListNullCart = new GroceryList(UUID.randomUUID(), "Seamus' party", cart);
        groceryListsNullCart.add(groceryListNullCart);

        customerNullShoppingCart = new Customer("Harry", "Kane", "kane@spur.com", "0721234569",
                "123Pol*&", new Date(), UUID.randomUUID().toString(),UUID.randomUUID().toString(),
                new Date().toString(), true, UserType.CUSTOMER, userID, address, groceryListsNullCart, null, null, null);

        customerEmptyCart = new Customer("Harry", "Kane", "kane@spurs.com", "0721234569",
                "123Pol*&", new Date(), UUID.randomUUID().toString(),UUID.randomUUID().toString(),
                new Date().toString(), true, UserType.CUSTOMER, customerIDEmptyCart, address, groceryListsEmptyCart,
                emptyCart, null, null);

        customer = new Customer("Harry", "Kane", "kane@spursy.com", "0721234569",
                "123Pol*&", new Date(), UUID.randomUUID().toString(),UUID.randomUUID().toString(),
                new Date().toString(), true, UserType.CUSTOMER, customerID, address, groceryLists,
                cart, null, null);

        itemRepo.save(item);
        groceryListRepo.save(groceryList);
        groceryListRepo.save(groceryListNullCart);
        groceryListRepo.save(groceryListEmptyCart);
        customerRepo.save(customer);
        customerRepo.save(customerNullShoppingCart);
        customerRepo.save(customerEmptyCart);
    }

    @AfterEach
    void tearDown(){
        customerRepo.deleteAll();
        groceryListRepo.deleteAll();
        //itemRepo.deleteAll();
    }

    @Test
    @DisplayName("When request object is not specified")
    void IntegrationTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.removeFromCart(null));
        assertEquals("RemoveFromCart Request is null - Could not remove from cart", thrown.getMessage());
    }

    @Test
    @DisplayName("When customerID is null")
    void IntegrationTest_testingNull_CustomerID_Attribute(){
        request = new RemoveFromCartRequest(null, "123456");
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.removeFromCart(request));
        assertEquals("CustomerId is null - could not remove from cart", thrown.getMessage());
    }

    @Test
    @DisplayName("When barcode attribute is null")
    void IntegrationTest_testingNull_Barcode_Attribute(){
        request = new RemoveFromCartRequest(UUID.randomUUID(), null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, () -> userService.removeFromCart(request));
        assertEquals("Barcode is null - could not remove from cart", thrown.getMessage());
    }

    @Test
    @DisplayName("When customer with the provided ID does not exist")
    void IntegrationTest_testing_InvalidUser(){
        request = new RemoveFromCartRequest(UUID.randomUUID(), "");

        Throwable throwable = Assertions.assertThrows((CustomerDoesNotExistException.class), () -> userService.removeFromCart(request));
        assertEquals("User with given userID does not exist - could not remove from cart", throwable.getMessage());
    }

    @Test
    @DisplayName("When does not have a shopping cart")
    void IntegrationTest_testing_NoShoppingCart(){
        request = new RemoveFromCartRequest(userID, "");

        try {
            response = userService.removeFromCart(request);
            assertFalse(response.isSuccess());
            assertEquals("There are no items in the cart - Could not remove from cart", response.getMessage());
        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("When shopping cart is Empty")
    void IntegrationTest_testing_ShoppingCartEmpty(){
        request = new RemoveFromCartRequest(userID, "");

        try {
            response = userService.removeFromCart(request);
            assertFalse(response.isSuccess());
            assertEquals("There are no items in the cart - Could not remove from cart", response.getMessage());
        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("When a non-matching barcode is given")
    void IntegrationTest_testing_NonMatchingBarcode(){
        request = new RemoveFromCartRequest(customerID, "");

        try {
            response = userService.removeFromCart(request);
            assertFalse(response.isSuccess());
            assertEquals("Item with given barcode does not exist - Could not remove from cart", response.getMessage());
            assertEquals(1, response.getCart().size()); // unchanged cart
        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("When a matching barcode is given")
    void IntegrationTest_testing_MatchingBarcode(){
        request = new RemoveFromCartRequest(customerID, "123456");

        try {
            response = userService.removeFromCart(request);
            assertTrue(response.isSuccess());
            assertEquals("Item successfully removed from cart", response.getMessage());
            assertEquals(0, response.getCart().size()); // modified cart
        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }
}
