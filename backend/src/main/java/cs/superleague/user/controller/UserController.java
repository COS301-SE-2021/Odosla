package cs.superleague.user.controller;

import cs.superleague.api.UserApi;
import cs.superleague.integration.ServiceSelector;
import cs.superleague.models.UserSetCartRequest;
import cs.superleague.models.UserSetCartResponse;
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
import cs.superleague.user.repos.CustomerRepo;
import cs.superleague.user.repos.GroceryListRepo;
import cs.superleague.user.requests.SetCartRequest;
import cs.superleague.user.responses.SetCartResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    Store store;
    Catalogue catalogue;

    UUID storeID;
    UUID customerID;
    UUID groceryListID;

    GeoPoint deliveryAddress;

    List<Item> listOfItems = new ArrayList<>();
    List<GroceryList> groceryLists = new ArrayList<>();
    List<Item> shoppingCart = new ArrayList<>();
    List<String> barcodes = new ArrayList<>();
    List<Store> listOfStores = new ArrayList<>();

    @Override
    public ResponseEntity<UserSetCartResponse> setCart(UserSetCartRequest body){

        customerID = UUID.fromString("99134567-9CBC-FEF0-1254-56789ABCDEF0");
        storeID = UUID.fromString("01234567-9CBC-FEF0-1254-56789ABCDEF0");
        groceryListID = UUID.fromString("55534567-9CBC-FEF0-1254-56789ABCDEF0");

        if(!customerRepo.findById(customerID).isPresent()){

            deliveryAddress = new GeoPoint(2.0, 2.0, "2616 Urban Quarters, Hatfield");

            item1 = new Item("Heinz Tamatoe Sauce","123459","123456",storeID,36.99,1,"description","img/");
            item2 = new Item("Bar one","012340","012345",storeID,14.99,3,"description","img/");

            listOfItems.add(item1);

            barcodes.add("123456");
            groceryList = new GroceryList(groceryListID, "Shopping List", listOfItems);
            groceryLists.add(groceryList);

            shoppingCart.add(item2);

            catalogue = new Catalogue(UUID.randomUUID(),listOfItems);
            store = new Store(storeID,"Checkers",catalogue,2,null,null,4,true);
            listOfStores.add(store);

            setCartCustomer = new Customer("D", "S", "ds@smallClub.com", "0721234567", "", new Date(), "", "", "", true,
                    UserType.CUSTOMER, customerID, deliveryAddress, groceryLists, shoppingCart, null, null);

            itemRepo.save(item1);
            itemRepo.saveAll(shoppingCart);
            groceryListRepo.save(groceryList);
            storeRepo.saveAll(listOfStores);
            customerRepo.save(setCartCustomer);
        }

        UserSetCartResponse userSetCartResponse = new UserSetCartResponse();
        HttpStatus status = HttpStatus.OK;

        try{
            SetCartRequest request = new SetCartRequest(body.getCustomerID(), body.getBarcodes());

            System.out.println(barcodes.get(0));

            SetCartResponse response = ServiceSelector.getUserService().setCart(request);
            try{
                userSetCartResponse.setDate(response.getTimestamp().toString());
                userSetCartResponse.setMessage(response.getMessage());
                userSetCartResponse.setSuccess(response.isSuccess());

            }catch(Exception e){
                e.printStackTrace();
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<>(userSetCartResponse, status);
    }
}
