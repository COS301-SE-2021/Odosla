package cs.superleague.user;

import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.user.dataclass.Customer;
import cs.superleague.user.dataclass.GroceryList;
import cs.superleague.user.dataclass.UserType;
import cs.superleague.user.exceptions.CustomerDoesNotExistException;
import cs.superleague.user.exceptions.InvalidRequestException;
import cs.superleague.user.exceptions.UserDoesNotExistException;
import cs.superleague.user.repos.CustomerRepo;
import cs.superleague.user.requests.GetShoppingCartRequest;
import cs.superleague.user.responses.GetShoppingCartResponse;
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
public class GetShoppingCartUnitTest {

    @Mock
    CustomerRepo customerRepo;

    @InjectMocks
    private UserServiceImpl userService;

    GroceryList groceryList;
    Customer customer;
    Customer customerNULLCart;
    Customer customerEMPTYCart;
    Item I1;
    Item I2;
    Item item;

    UUID userID;
    UUID groceryListID;
    UUID expectedS1;

    GeoPoint deliveryAddress;

    List<Item> listOfItems = new ArrayList<>();
    List<GroceryList> groceryLists = new ArrayList<>();
    List<Item> shoppingCart = new ArrayList<>();
    List<Item> shoppingCartNULL = new ArrayList<>();
    List<Item> shoppingCartEMPTY = new ArrayList<>();
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

        shoppingCartNULL = null;
        shoppingCart.add(I1);
        shoppingCart.add(I2);

        deliveryAddress = new GeoPoint(2.0, 2.0, "2616 Urban Quarters, Hatfield");

        groceryList = new GroceryList(groceryListID, "Seamus' party", listOfItems);
        groceryLists.add(groceryList);
        customer = new Customer("D", "S", "ds@smallClub.com", "0721234567", "", new Date(), "", "", "", true,
                UserType.CUSTOMER, userID, deliveryAddress, groceryLists, shoppingCart, null, null);
        customerEMPTYCart = new Customer("D", "S", "ds@smallClub.com", "0721234567", "", new Date(), "", "", "", true,
                UserType.CUSTOMER, userID, deliveryAddress, groceryLists, shoppingCartEMPTY, null, null);
        customerNULLCart = new Customer("D", "S", "ds@smallClub.com", "0721234567", "", new Date(), "", "", "", true,
                UserType.CUSTOMER, userID, deliveryAddress, groceryLists, shoppingCartNULL, null, null);
    }

    @AfterEach
    void tearDown(){
    }


    @Test
    @DisplayName("When request object is not specified")
    void UnitTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.getShoppingCart(null));
        assertEquals("GetShoppingCart Request is null - could retrieve shopping cart", thrown.getMessage());
    }

    @Test
    @DisplayName("When userID parameter is not specified")
    void UnitTest_testingNullRequestUserIDParameter(){
        GetShoppingCartRequest request  = new GetShoppingCartRequest(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.getShoppingCart(request));
        assertEquals("UserID is null - could retrieve shopping cart", thrown.getMessage());
    }

    @Test
    @DisplayName("When customer with given UserID does not exist")
    void UnitTest_testingInvalidUser(){
        GetShoppingCartRequest request  = new GetShoppingCartRequest(userID.toString());
        when(customerRepo.findById(Mockito.any())).thenReturn(null);
        Throwable thrown = Assertions.assertThrows(CustomerDoesNotExistException.class, ()-> userService.getShoppingCart(request));
        assertEquals("User with given userID does not exist - could not retrieve shopping cart", thrown.getMessage());
    }

    @Test
    @DisplayName("When a null shoppingCart is returned")
    void UnitTest_ShoppingCartDoesNotExist_NULL(){
        GetShoppingCartRequest request  = new GetShoppingCartRequest(userID.toString());
        when(customerRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(customerNULLCart));
        try{
            GetShoppingCartResponse response = userService.getShoppingCart(request);
            assertEquals("Shopping Cart does not have any items", response.getMessage());
            assertFalse(response.isSuccess());
            assertNull(response.getShoppingCart());
        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("When an empty shoppingCart is returned")
    void UnitTest_ShoppingCartDoesNotExist_EMPTY(){
        GetShoppingCartRequest request  = new GetShoppingCartRequest(userID.toString());
        when(customerRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(customerEMPTYCart));
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
        GetShoppingCartRequest request  = new GetShoppingCartRequest(userID.toString());
        when(customerRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(customer));

        try{
            GetShoppingCartResponse response = userService.getShoppingCart(request);
            assertEquals("Shopping cart successfully retrieved", response.getMessage());
            assertTrue(response.isSuccess());
            assertEquals(shoppingCart, response.getShoppingCart());
        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }
}