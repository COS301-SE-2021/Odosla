package cs.superleague.user.controller;

import cs.superleague.api.UserApi;
import cs.superleague.integration.ServiceSelector;
import cs.superleague.models.*;
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
import cs.superleague.user.requests.*;
import cs.superleague.user.responses.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
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

    @Override
    public ResponseEntity<UserClearShoppingCartResponse> clearShoppingCart(UserClearShoppingCartRequest body){

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

        UserClearShoppingCartResponse userClearShoppingCartResponse = new UserClearShoppingCartResponse();
        HttpStatus status = HttpStatus.OK;

        try{
            ClearShoppingCartRequest request = new ClearShoppingCartRequest(body.getCustomerID());

            ClearShoppingCartResponse response = ServiceSelector.getUserService().clearShoppingCart(request);
            try{
                userClearShoppingCartResponse.setDate(response.getTimestamp().toString());
                userClearShoppingCartResponse.setMessage(response.getMessage());
                userClearShoppingCartResponse.setSuccess(response.isSuccess());

            }catch(Exception e){
                e.printStackTrace();
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<>(userClearShoppingCartResponse, status);
    }

    @Override
    public ResponseEntity<UserGetShoppingCartResponse> getShoppingCart(UserGetShoppingCartRequest body){

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

        UserGetShoppingCartResponse userGetShoppingCartResponse = new UserGetShoppingCartResponse();
        HttpStatus status = HttpStatus.OK;

        try{
            GetShoppingCartRequest request = new GetShoppingCartRequest(body.getCustomerID());

            GetShoppingCartResponse response = ServiceSelector.getUserService().getShoppingCart(request);
            try{
                userGetShoppingCartResponse.setItems(populateItems(response.getShoppingCart()));
                userGetShoppingCartResponse.setMessage(response.getMessage());
                userGetShoppingCartResponse.setSuccess(response.isSuccess());

            }catch(Exception e){
                e.printStackTrace();
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<>(userGetShoppingCartResponse, status);
    }

    @Override
    public ResponseEntity<UserMakeGroceryListResponse> makeGroceryList(UserMakeGroceryListRequest body){

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

        UserMakeGroceryListResponse makeGroceryListResponse = new UserMakeGroceryListResponse();
        HttpStatus status = HttpStatus.OK;

        try{
            MakeGroceryListRequest request = new MakeGroceryListRequest(body.getCustomerID(), body.getBarcodes(), body.getName());

            MakeGroceryListResponse response = ServiceSelector.getUserService().makeGroceryList(request);
            try{
                makeGroceryListResponse.setDate(response.getTimestamp().toString());
                makeGroceryListResponse.setMessage(response.getMessage());
                makeGroceryListResponse.setSuccess(response.isSuccess());

            }catch(Exception e){
                e.printStackTrace();
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<>(makeGroceryListResponse, status);
    }

    @Override
    public ResponseEntity<UserRemoveFromCartResponse> removeFromCart(UserRemoveFromCartRequest body){

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

        UserRemoveFromCartResponse userRemoveFromCartResponse = new UserRemoveFromCartResponse();
        HttpStatus status = HttpStatus.OK;

        try{
            RemoveFromCartRequest request = new RemoveFromCartRequest(body.getCustomerID(), body.getBarcode());

            RemoveFromCartResponse response = ServiceSelector.getUserService().removeFromCart(request);
            try{
                userRemoveFromCartResponse.setDate(response.getTimestamp().toString());
                userRemoveFromCartResponse.setMessage(response.getMessage());
                userRemoveFromCartResponse.setSuccess(response.isSuccess());

            }catch(Exception e){
                e.printStackTrace();
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<>(userRemoveFromCartResponse, status);
    }

    @Override
    public ResponseEntity<UserRegisterCustomerResponse> registerCustomer(UserRegisterCustomerRequest body){

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

        UserRegisterCustomerResponse userRegisterCustomerResponse = new UserRegisterCustomerResponse();
        HttpStatus status = HttpStatus.OK;

        try{
            RegisterCustomerRequest request = new RegisterCustomerRequest(body.getName(), body.getSurnname(),
                    body.getEmail(), body.getPhoneNumber(), body.getPassword());

            RegisterCustomerResponse response = ServiceSelector.getUserService().registerCustomer(request);
            try{
                userRegisterCustomerResponse.setDate(response.getTimestamp().toString());
                userRegisterCustomerResponse.setMessage(response.getMessage());
                userRegisterCustomerResponse.setSuccess(response.isSuccess());

            }catch(Exception e){
                e.printStackTrace();
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<>(userRegisterCustomerResponse, status);
    }

    //Populate ItemObject list from items returned by use case
    private List<ItemObject> populateItems(List<Item> responseItems) throws NullPointerException{

        List<ItemObject> responseBody = new ArrayList<>();

        for(int i = 0; i < responseItems.size(); i++){

            ItemObject currentItem = new ItemObject();

            currentItem.setName(responseItems.get(i).getName());
            currentItem.setDescription(responseItems.get(i).getDescription());
            currentItem.setBarcode(responseItems.get(i).getBarcode());
            currentItem.setProductId(responseItems.get(i).getProductID());
            currentItem.setStoreId(responseItems.get(i).getStoreID().toString());
            currentItem.setPrice(BigDecimal.valueOf(responseItems.get(i).getPrice()));
            currentItem.setQuantity(responseItems.get(i).getQuantity());
            currentItem.setImageUrl(responseItems.get(i).getImageUrl());

            responseBody.add(currentItem);

        }

        return responseBody;
    }
}
