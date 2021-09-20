package cs.superleague.user;

import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.user.dataclass.Customer;
import cs.superleague.user.dataclass.GroceryList;
import cs.superleague.user.dataclass.UserType;
import cs.superleague.user.exceptions.InvalidRequestException;
import cs.superleague.user.exceptions.UserDoesNotExistException;
import cs.superleague.user.repos.CustomerRepo;
import cs.superleague.user.requests.ClearShoppingCartRequest;
import cs.superleague.user.responses.ClearShoppingCartResponse;
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
public class ClearShoppingCartUnitTest {

    @Mock
    CustomerRepo customerRepo;

    @InjectMocks
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

    ClearShoppingCartRequest request;
    ClearShoppingCartResponse response;

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
    }

    @AfterEach
    void tearDown(){
    }


    @Test
    @DisplayName("When request object is not specified")
    void UnitTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.clearShoppingCart(null));
        assertEquals("clearShoppingCart Request is null - Could not clear to shopping cart", thrown.getMessage());
    }

    @Test
    @DisplayName("When userID parameter is not specified")
    void UnitTest_testingNullRequestUserIDParameter(){
        request = new ClearShoppingCartRequest(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.clearShoppingCart(request));
        assertEquals("CustomerId is null - could not clear shopping cart", thrown.getMessage());
    }

    @Test
    @DisplayName("When customer with given UserID does not exist")
    void UnitTest_testingInvalidUser(){
        request = new ClearShoppingCartRequest(UUID.randomUUID().toString());
        when(customerRepo.findById(Mockito.any())).thenReturn(null);
        Throwable thrown = Assertions.assertThrows(UserDoesNotExistException.class, ()-> userService.clearShoppingCart(request));
        assertEquals("User with given userID does not exist - could clear cart", thrown.getMessage());
    }

    @Test
    @DisplayName("When valid values are given")
    void UnitTest_testingSuccessfulUpdate(){
        request = new ClearShoppingCartRequest(userID.toString());
        when(customerRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(customer));

        customer.getShoppingCart().clear();

        when(customerRepo.save(customer)).thenReturn(customer);
        try {
            response = userService.clearShoppingCart(request);
            assertEquals("Cart successfully cleared", response.getMessage());
            assertTrue(response.isSuccess());
            assertEquals(0, response.getItems().size());
        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }
}