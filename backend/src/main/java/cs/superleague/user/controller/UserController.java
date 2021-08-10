package cs.superleague.user.controller;

import cs.superleague.api.UserApi;
import cs.superleague.models.UserSetCartRequest;
import cs.superleague.models.UserSetCartResponse;
import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.shopping.ShoppingService;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.shopping.repos.CatalogueRepo;
import cs.superleague.shopping.repos.ItemRepo;
import cs.superleague.shopping.repos.StoreRepo;
import cs.superleague.user.UserServiceImpl;
import cs.superleague.user.dataclass.Customer;
import cs.superleague.user.dataclass.GroceryList;
import cs.superleague.user.dataclass.UserType;
import cs.superleague.user.repos.CustomerRepo;
import cs.superleague.user.repos.GroceryListRepo;
import cs.superleague.user.responses.SetCartResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

@CrossOrigin
@RestController
public class UserController implements UserApi {

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

    Customer setCartCustomer;
    GroceryList groceryList;
    Item item1;
    Item item2;

    UUID storeID;
    UUID customerID;
    UUID groceryListID;

    GeoPoint deliveryAddress;

    @Override
    public ResponseEntity<UserSetCartResponse> setCart(UserSetCartRequest body){
        customerID = UUID.fromString("99134567-9CBC-FEF0-1254-56789ABCDEF0");
        groceryListID = UUID.fromString("55534567-9CBC-FEF0-1254-56789ABCDEF0");
        groceryListID = UUID.fromString("55534567-9CBC-FEF0-1254-56789ABCDEF0");

        deliveryAddress = deliveryAddress = new GeoPoint(2.0, 2.0, "2616 Urban Quarters, Hatfield");

//        item1 = new Item("Heinz Tamatoe Sauce","123456","123456",expectedS1,36.99,1,"description","img/");
//        item2 = new Item("Bar one","012345","012345",expectedS1,14.99,3,"description","img/");

        groceryList = new(groceryListID, "Shopping List", );

//        setCartCustomer = new Customer("D", "S", "ds@smallClub.com", "0721234567", "", new Date(), "", "", "", true,
//                UserType.CUSTOMER, customerID, deliveryAddress, groceryLists, shoppingCart, null, null);

        return null;
    }
}
