package cs.superleague.user.integration;

import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.shopping.ShoppingService;
import cs.superleague.shopping.dataclass.Catalogue;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.shopping.dataclass.Store;
import cs.superleague.shopping.repos.CatalogueRepo;
import cs.superleague.shopping.repos.ItemRepo;
import cs.superleague.shopping.repos.StoreRepo;
import cs.superleague.user.UserServiceImpl;
import cs.superleague.user.dataclass.Customer;
import cs.superleague.user.dataclass.GroceryList;
import cs.superleague.user.dataclass.UserType;
import cs.superleague.user.exceptions.CustomerDoesNotExistException;
import cs.superleague.user.exceptions.InvalidRequestException;
import cs.superleague.user.repos.CustomerRepo;
import cs.superleague.user.repos.GroceryListRepo;
import cs.superleague.user.requests.SetCartRequest;
import cs.superleague.user.responses.SetCartResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class SetCartIntegrationTest {

    @Autowired
    CustomerRepo customerRepo;

    @Autowired
    GroceryListRepo groceryListRepo;

    @Autowired
    ItemRepo itemRepo;

    @Autowired
    StoreRepo storeRepo;

    @Autowired
    CatalogueRepo catalogueRepo;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private ShoppingService shoppingService;

    GroceryList groceryList;
    Customer customer;
    Store store;
    Catalogue catalogue;
    Item I1;
    Item I2;

    UUID userID;
    UUID groceryListID;
    UUID expectedS1;

    GeoPoint deliveryAddress;

    List<Item> listOfItems = new ArrayList<>();
    List<String> listOfBarcodes = new ArrayList<>();
    List<GroceryList> groceryLists = new ArrayList<>();
    List<Item> shoppingCart = new ArrayList<>();
    List<Item> shoppingCartNULL = new ArrayList<>();
    List<Store> listOfStores = new ArrayList<>();

    SetCartRequest request;
    SetCartResponse response;

    @BeforeEach
    void setUp() {
        userID = UUID.randomUUID();
        groceryListID = UUID.randomUUID();
        expectedS1 = UUID.randomUUID();

        listOfBarcodes.add("123456");
        listOfBarcodes.add("012345");

        I1=new Item("Heinz Tamatoe Sauce","123456","123456",expectedS1,36.99,1,"description","img/");
        I2=new Item("Bar one","012345","012345",expectedS1,14.99,3,"description","img/");
        listOfItems.add(I1);
        listOfItems.add(I2);

        shoppingCartNULL = null;
        shoppingCart.add(I1);
        shoppingCart.add(I2);

        deliveryAddress = new GeoPoint(2.0, 2.0, "2616 Urban Quarters, Hatfield");

        catalogue = new Catalogue(UUID.randomUUID(),listOfItems);
        store = new Store(expectedS1,"Checkers",catalogue,2,null,null,4,true);
        listOfStores.add(store);

        groceryList = new GroceryList(groceryListID, "Seamus' party", listOfItems);
        groceryLists.add(groceryList);
        customer = new Customer("D", "S", "ds@smallClub.com", "0721234567", "", new Date(), "", "", "", true,
                UserType.CUSTOMER, userID, deliveryAddress, groceryLists, shoppingCart, null, null);

        itemRepo.saveAll(listOfItems);
        catalogueRepo.save(catalogue);
        storeRepo.saveAll(listOfStores);
        groceryListRepo.saveAll(groceryLists);
        customerRepo.save(customer);
    }

    @AfterEach
    void tearDown(){
        customerRepo.deleteAll();
        groceryListRepo.deleteAll();
        storeRepo.deleteAll();
        catalogueRepo.deleteAll();
    }


    @Test
    @DisplayName("When request object is not specified")
    void IntegrationTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.setCart(null));
        assertEquals("addToCart Request is null - Could not add to cart", thrown.getMessage());
    }

    @Test
    @DisplayName("When userID parameter is not specified")
    void IntegrationTest_testingNullRequestUserIDParameter(){
        request = new SetCartRequest(null, listOfBarcodes);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.setCart(request));
        assertEquals("CustomerId is null - could not add to cart", thrown.getMessage());
    }

    @Test
    @DisplayName("When customer with given UserID does not exist")
    void IntegrationTest_testingInvalidUser(){
        request = new SetCartRequest(UUID.randomUUID(), listOfBarcodes);
        Throwable thrown = Assertions.assertThrows(CustomerDoesNotExistException.class, ()-> userService.setCart(request));
        assertEquals("User with given userID does not exist - could add to cart", thrown.getMessage());
    }

    @Test
    @DisplayName("When barcodes list is null/empty")
    void IntegrationTest_testingNullEmptyItemList(){
        request = new SetCartRequest(userID, new ArrayList<>());

        try {
            response = userService.setCart(request);
            assertEquals("Item list empty - could not add to cart", response.getMessage());
            assertFalse(response.isSuccess());
        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("When the barcodes do not exist")
    void IntegrationTest_testing_Barcodes_Does_Not_Exist(){
        List<String> randomBarcodes = new ArrayList<>();
        randomBarcodes.add("1234777");
        request = new SetCartRequest(userID, randomBarcodes);

        try{
            response = userService.setCart(request);
            assertEquals("Cannot find item with given barcode - could not add to cart", response.getMessage());
            assertFalse(response.isSuccess());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("When nonnull update values are given")
    void IntegrationTest_testingSuccessfulUpdate(){
        request = new SetCartRequest(userID, listOfBarcodes);

        try {
            response = userService.setCart(request);
            assertEquals("Items successfully added to cart", response.getMessage());
            assertTrue(response.isSuccess());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
