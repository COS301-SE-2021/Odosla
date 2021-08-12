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
import cs.superleague.user.dataclass.Driver;
import cs.superleague.user.dataclass.GroceryList;
import cs.superleague.user.dataclass.UserType;
import cs.superleague.user.repos.*;
import cs.superleague.user.requests.*;
import cs.superleague.user.responses.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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
    DriverRepo driverRepo;

    @Autowired
    AdminRepo adminRepo;

    @Autowired
    ShopperRepo shopperRepo;

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
    public ResponseEntity<UserResetPasswordResponse> resetPassword(UserResetPasswordRequest body){

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

            setCartCustomer = new Customer("D", "S", "u14254922@tuks.co.za", "0721234567", "", new Date(), "", "", "", true,
                    UserType.CUSTOMER, customerID, deliveryAddress, groceryLists, shoppingCart, null, null);

            itemRepo.save(item1);
            itemRepo.saveAll(shoppingCart);
            groceryListRepo.save(groceryList);
            storeRepo.saveAll(listOfStores);
            customerRepo.save(setCartCustomer);
        }

        UserResetPasswordResponse userMakeGroceryListResponse = new UserResetPasswordResponse();
        HttpStatus status = HttpStatus.OK;

        try{
            ResetPasswordRequest request = new ResetPasswordRequest(body.getEmail(), body.getUserType());

            ResetPasswordResponse response = ServiceSelector.getUserService().resetPassword(request);
            try{
                userMakeGroceryListResponse.setResetCode(response.getResetCode());
                userMakeGroceryListResponse.setMessage(response.getMessage());
                userMakeGroceryListResponse.setSuccess(response.isSuccess());

            }catch(Exception e){
                e.printStackTrace();
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<>(userMakeGroceryListResponse, status);
    }

    @Override
    public ResponseEntity<UserLoginResponse> loginUser(UserLoginRequest body){

        Driver driver=new Driver();
        UUID driverId= UUID.randomUUID();
        String driverEmail="driverEmail@gmail.com";
        String password="Kelcat@01";
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(15);
        String passwordHashed=passwordEncoder.encode(password);

        driver.setDriverID(driverId);
        driver.setEmail(driverEmail);
        driver.setPassword(passwordHashed);
        UserType userType = null;
        driverRepo.save(driver);
        UserLoginResponse response=new UserLoginResponse();
        HttpStatus httpStatus = HttpStatus.OK;
        try {
            if (body.getUserType().equals("DRIVER")) {
                userType = UserType.DRIVER;
            } else if (body.getUserType().equals("CUSTOMER")) {
                userType = UserType.CUSTOMER;
            } else if (body.getUserType().equals("SHOPPER")) {
                userType = UserType.SHOPPER;
            } else if (body.getUserType().equals("ADMIN")) {
                userType = UserType.ADMIN;
            }

            String emailRequest=body.getEmail();
            String passwordRequest=body.getPassword();


            LoginRequest request = new LoginRequest(body.getEmail(), body.getPassword(), userType);

            LoginResponse loginResponse= ServiceSelector.getUserService().loginUser(request);

            try {
                response.setMessage(loginResponse.getMessage());
                response.setToken(loginResponse.getToken());
                response.setSuccess(loginResponse.isSuccess());
                response.setDate(new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(loginResponse.getTimestamp()));

            }catch(Exception e){
                e.printStackTrace();
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        driverRepo.deleteAll();
        shopperRepo.deleteAll();
        customerRepo.deleteAll();
        adminRepo.deleteAll();
        return new ResponseEntity<>(response, httpStatus);

    }

    @Override
    public ResponseEntity<UserRegisterDriverResponse> registerDriver(UserRegisterDriverRequest body) {
        UserRegisterDriverResponse response=new UserRegisterDriverResponse();
        HttpStatus httpStatus=HttpStatus.OK;
        try{

            RegisterDriverRequest request=new RegisterDriverRequest(body.getName(), body.getSurname(), body.getEmail(), body.getPhoneNumber(), body.getPassword());
            RegisterDriverResponse driverResponse=ServiceSelector.getUserService().registerDriver(request);

            try{
                response.setMessage(driverResponse.getMessage());
                response.setDate(new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(driverResponse.getTimestamp()));
                response.setSuccess(driverResponse.isSuccess());
            }catch(Exception e){
                e.printStackTrace();
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(response,httpStatus);
    }

    @Override
    public ResponseEntity<UserRegisterCustomerResponse> registerCustomer(UserRegisterCustomerRequest body) {
        UserRegisterCustomerResponse response=new UserRegisterCustomerResponse();
        HttpStatus httpStatus=HttpStatus.OK;
        try{

            RegisterCustomerRequest request=new RegisterCustomerRequest(body.getName(), body.getSurname(), body.getEmail(), body.getPhoneNumber(), body.getPassword());
            RegisterCustomerResponse customerResponse=ServiceSelector.getUserService().registerCustomer(request);

            try{
                response.setMessage(customerResponse.getMessage());
                response.setDate(new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(customerResponse.getTimestamp()));
                response.setSuccess(customerResponse.isSuccess());
            }catch(Exception e){
                e.printStackTrace();
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(response,httpStatus);
    }

    @Override
    public ResponseEntity<UserRegisterAdminResponse> registerAdmin(UserRegisterAdminRequest body) {
        UserRegisterAdminResponse response=new UserRegisterAdminResponse();
        HttpStatus httpStatus=HttpStatus.OK;
        try{

            RegisterAdminRequest request=new RegisterAdminRequest(body.getName(), body.getSurname(), body.getEmail(), body.getPhoneNumber(), body.getPassword());
            RegisterAdminResponse adminResponse=ServiceSelector.getUserService().registerAdmin(request);

            try{
                response.setMessage(adminResponse.getMessage());
                response.setDate(new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(adminResponse.getTimestamp()));
                response.setSuccess(adminResponse.isSuccess());
            }catch(Exception e){
                e.printStackTrace();
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(response,httpStatus);
    }

    @Override
    public ResponseEntity<UserRegisterShopperResponse> registerShopper(UserRegisterShopperRequest body) {
        UserRegisterShopperResponse response=new UserRegisterShopperResponse();
        HttpStatus httpStatus=HttpStatus.OK;
        try{

            RegisterShopperRequest request=new RegisterShopperRequest(body.getName(), body.getSurname(), body.getEmail(), body.getPhoneNumber(), body.getPassword());
            RegisterShopperResponse shopperResponse=ServiceSelector.getUserService().registerShopper(request);

            try{
                response.setMessage(shopperResponse.getMessage());
                response.setDate(new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(shopperResponse.getTimestamp()));
                response.setSuccess(shopperResponse.isSuccess());
            }catch(Exception e){
                e.printStackTrace();
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(response,httpStatus);
    }

    @Override
    public ResponseEntity<UserAccountVerifyResponse> verifyAccount(UserAccountVerifyRequest body) {
        Driver driver=new Driver();
        driver.setDriverID(UUID.randomUUID());
        driver.setEmail("driverVerifyEmail@gmail.com");
        driver.setActivationCode("verifyCode");
        driverRepo.save(driver);
        UserAccountVerifyResponse response=new UserAccountVerifyResponse();
        HttpStatus status = HttpStatus.OK;

        try{
            UserType userType = null;
            if(body.getUserType().equals("SHOPPER")){
                userType=UserType.SHOPPER;
            } else if(body.getUserType().equals("DRIVER")){
                userType=UserType.DRIVER;
            } else if(body.getUserType().equals("CUSTOMER")){
                userType=UserType.CUSTOMER;
            } else if(body.getUserType().equals("ADMIN")){
                userType=UserType.ADMIN;
            }
            AccountVerifyRequest request = new AccountVerifyRequest(body.getEmail(),body.getActivationCode(),userType);

            AccountVerifyResponse userResponse = ServiceSelector.getUserService().verifyAccount(request);
            try{
                response.setDate(userResponse.getTimestamp().toString());
                response.setMessage(userResponse.getMessage());
                response.setSuccess(userResponse.isSuccess());
                response.setUserType(body.getUserType());

            }catch(Exception e){
                e.printStackTrace();
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        driverRepo.deleteAll();

        return new ResponseEntity<>(response, status);

    }

    @Override
    public ResponseEntity<UserSetCurrentLocationResponse> setCurrentLocation(UserSetCurrentLocationRequest body){
        UserSetCurrentLocationResponse setCurrentLocationResponse = new UserSetCurrentLocationResponse();
        HttpStatus status = HttpStatus.OK;

        try{
            SetCurrentLocationRequest request = new SetCurrentLocationRequest(body.getDriverID(), body.getLongitude().doubleValue(), body.getLatitude().doubleValue(), body.getAddress());

            SetCurrentLocationResponse response = ServiceSelector.getUserService().setCurrentLocation(request);
            try{
                setCurrentLocationResponse.setDate(response.getTimestamp().toString());
                setCurrentLocationResponse.setMessage(response.getMessage());
                setCurrentLocationResponse.setSuccess(response.isSuccess());

            }catch(Exception e){
                e.printStackTrace();
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<>(setCurrentLocationResponse, status);
    }

    @Override
    public ResponseEntity<UserGetCurrentUserResponse> getCurrentUser(UserGetCurrentUserRequest body){

        customerID = UUID.fromString("99134567-9CBC-FEF0-1254-56789ABCDEF0");
        storeID = UUID.fromString("01234567-9CBC-FEF0-1254-56789ABCDEF0");
        groceryListID = UUID.fromString("55534567-9CBC-FEF0-1254-56789ABCDEF0");

        if(!driverRepo.findById(customerID).isPresent()){

            Customer customer = new Customer();
            customer.setCustomerID(customerID);

            customerRepo.save(customer);

        }

        UserGetCurrentUserResponse userGetCurrentUserResponse = new UserGetCurrentUserResponse();
        HttpStatus status = HttpStatus.OK;

        try{
            GetCurrentUserRequest request = new GetCurrentUserRequest(body.getJwTToken());

            GetCurrentUserResponse response = ServiceSelector.getUserService().getCurrentUser(request);
            try{
                userGetCurrentUserResponse.setDate(response.getTimestamp().toString());
                userGetCurrentUserResponse.setMessage(response.getMessage());
                userGetCurrentUserResponse.setSuccess(response.isSuccess());
                userGetCurrentUserResponse.setUser((OneOfuserGetCurrentUserResponseUser) response.getUser());

            }catch(Exception e){
                e.printStackTrace();
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<>(userGetCurrentUserResponse, status);
    }


}
