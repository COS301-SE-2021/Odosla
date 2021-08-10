package cs.superleague.user;

import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.user.dataclass.Customer;
import cs.superleague.user.dataclass.GroceryList;
import cs.superleague.user.dataclass.UserType;
import cs.superleague.user.exceptions.CustomerDoesNotExistException;
import cs.superleague.user.exceptions.InvalidRequestException;
import cs.superleague.user.repos.CustomerRepo;
import cs.superleague.user.requests.RemoveFromCartRequest;
import cs.superleague.user.responses.RemoveFromCartResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RemoveFromCartUnitTest {

    @Mock
    CustomerRepo customerRepo;

    @InjectMocks
    UserServiceImpl userService;

    UUID userID;
    UUID storeID;
    UUID customerID;
    UUID customerIDEmptyCart;

    Customer customerNullShoppingCart;
    Customer customerEmptyCart;
    Customer customer;
    GeoPoint address;
    Item item;

    List<Item> cart = new ArrayList<>();
    List<Item> emptyCart = new ArrayList<>();
    List<GroceryList> groceryLists = new ArrayList<>();

    RemoveFromCartRequest request;
    RemoveFromCartResponse response;

    @BeforeEach
    void setUp(){
        userID = UUID.randomUUID();
        storeID = UUID.randomUUID();
        customerIDEmptyCart = UUID.randomUUID();

        address = new GeoPoint(2.0, 2.0, "2616 Urban Quarters, Hatfield");
        item = new Item("Heinz Tamatoe Sauce","123456","123456",storeID,36.99,1,"description","img/");

        customerNullShoppingCart = new Customer("Harry", "Kane", "kane@spur.com", "0721234569",
                "123Pol*&", new Date(), UUID.randomUUID().toString(),UUID.randomUUID().toString(),
                new Date().toString(), true, UserType.CUSTOMER, userID, address, groceryLists, null, null, null);

        customerEmptyCart = new Customer("Harry", "Kane", "kane@spur.com", "0721234569",
                "123Pol*&", new Date(), UUID.randomUUID().toString(),UUID.randomUUID().toString(),
                new Date().toString(), true, UserType.CUSTOMER, customerIDEmptyCart, address, groceryLists,
                emptyCart, null, null);

        cart.add(item);

        customer = new Customer("Harry", "Kane", "kane@spur.com", "0721234569",
                "123Pol*&", new Date(), UUID.randomUUID().toString(),UUID.randomUUID().toString(),
                new Date().toString(), true, UserType.CUSTOMER, customerID, address, groceryLists,
                cart, null, null);
    }

    @AfterEach
    void tearDown(){
        cart.clear();
    }

    @Test
    @DisplayName("When request object is not specified")
    void UnitTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.removeFromCart(null));
        assertEquals("RemoveFromCart Request is null - Could not remove from cart", thrown.getMessage());
    }

    @Test
    @DisplayName("When customerID is null")
    void UnitTest_testingNull_CustomerID_Attribute(){
        request = new RemoveFromCartRequest(null, "123456");
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.removeFromCart(request));
        assertEquals("CustomerId is null - could not remove from cart", thrown.getMessage());
    }

    @Test
    @DisplayName("When barcode attribute is null")
    void UnitTest_testingNull_Barcode_Attribute(){
        request = new RemoveFromCartRequest(UUID.randomUUID(), null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, () -> userService.removeFromCart(request));
        assertEquals("Barcode is null - could not remove from cart", thrown.getMessage());
    }

    @Test
    @DisplayName("When customer with the provided ID does not exist")
    void UnitTest_testing_InvalidUser(){
        request = new RemoveFromCartRequest(UUID.randomUUID(), "");

        when(customerRepo.findById(Mockito.any())).thenReturn(null);
        Throwable throwable = Assertions.assertThrows((CustomerDoesNotExistException.class), () -> userService.removeFromCart(request));
        assertEquals("User with given userID does not exist - could not remove from cart", throwable.getMessage());
    }

    @Test
    @DisplayName("When does not have a shopping cart")
    void UnitTest_testing_NoShoppingCart(){
        request = new RemoveFromCartRequest(userID, "");

        try {
            when(customerRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(customerNullShoppingCart));
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
    void UnitTest_testing_ShoppingCartEmpty(){
        request = new RemoveFromCartRequest(userID, "");

        try {
            when(customerRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(customerEmptyCart));
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
    void UnitTest_testing_NonMatchingBarcode(){
        request = new RemoveFromCartRequest(userID, "");

        try {
            when(customerRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(customer));
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
    void UnitTest_testing_MatchingBarcode(){
        request = new RemoveFromCartRequest(userID, "123456");

        try {
            when(customerRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(customer));
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
