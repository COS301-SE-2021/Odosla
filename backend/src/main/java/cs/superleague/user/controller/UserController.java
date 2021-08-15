package cs.superleague.user.controller;

import cs.superleague.api.UserApi;
import cs.superleague.integration.ServiceSelector;
import cs.superleague.models.*;
import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.shopping.ShoppingService;
import cs.superleague.shopping.dataclass.Catalogue;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.shopping.dataclass.Store;
import cs.superleague.shopping.repos.CatalogueRepo;
import cs.superleague.shopping.repos.ItemRepo;
import cs.superleague.shopping.repos.StoreRepo;
import cs.superleague.user.UserServiceImpl;
import cs.superleague.user.dataclass.*;
import cs.superleague.user.repos.*;
import cs.superleague.user.requests.*;
import cs.superleague.user.responses.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    OrderRepo orderRepo;

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
        if (responseItems == null){
            return null;
        }

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

        UserMakeGroceryListResponse makeGroceryListResponse = new UserMakeGroceryListResponse();
        HttpStatus status = HttpStatus.OK;

        try{
            MakeGroceryListRequest request = new MakeGroceryListRequest(body.getJwTToken(), body.getProductIds(), body.getName());

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

        UserType userType=null;
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
    public ResponseEntity<UserUpdateShopperShiftResponse> updateShopperShift(UserUpdateShopperShiftRequest body) {

        UserUpdateShopperShiftResponse response = new UserUpdateShopperShiftResponse();
        HttpStatus status = HttpStatus.OK;
        try {
            UpdateShopperShiftRequest request = new UpdateShopperShiftRequest(body.getJwtToken(), body.isOnShift());
            UpdateShopperShiftResponse response1 = ServiceSelector.getUserService().updateShopperShift(request);
            try {
                response.setMessage(response1.getMessage());
                response.setSuccess(Boolean.valueOf(response1.isSuccess()));
                response.setTimestamp(String.valueOf(response1.getTimestamp().getTime()));
            }catch (Exception e){
                e.printStackTrace();
                response.setMessage(e.getMessage());
                response.setSuccess(false);
                response.setTimestamp(String.valueOf(Calendar.getInstance().getTime()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setMessage(e.getMessage());
            response.setSuccess(false);
            response.setTimestamp(String.valueOf(Calendar.getInstance().getTime()));
        }
        return new ResponseEntity<>(response, status);
    }

    @Override
    public ResponseEntity<UserGetCurrentUserResponse> getCurrentUser(UserGetCurrentUserRequest body){


        UserGetCurrentUserResponse userGetCurrentUserResponse = new UserGetCurrentUserResponse();
        HttpStatus status = HttpStatus.OK;

        try{
            GetCurrentUserRequest request = new GetCurrentUserRequest(body.getJwTToken());

            GetCurrentUserResponse response = ServiceSelector.getUserService().getCurrentUser(request);
            try{
                userGetCurrentUserResponse.setDate(response.getTimestamp().toString());
                userGetCurrentUserResponse.setMessage(response.getMessage());
                userGetCurrentUserResponse.setSuccess(response.isSuccess());
                if (response.getUser().getAccountType() == UserType.CUSTOMER){
                    Customer customer = (Customer) response.getUser();
                    CustomerObject customerObject = populateCustomer(customer);
                    userGetCurrentUserResponse.setUser(customerObject);
                }else if (response.getUser().getAccountType() == UserType.ADMIN){
                    Admin admin = (Admin) response.getUser();
                    AdminObject adminObject = populateAdmin(admin);
                    userGetCurrentUserResponse.setUser(adminObject);
                }else if (response.getUser().getAccountType() == UserType.DRIVER){
                    Driver driver = (Driver) response.getUser();
                    DriverObject driverObject = populateDriver(driver);
                    userGetCurrentUserResponse.setUser(driverObject);
                } else if (response.getUser().getAccountType() == UserType.SHOPPER){
                    Shopper shopper = (Shopper) response.getUser();
                    ShopperObject shopperObject = populateShopper(shopper);
                    userGetCurrentUserResponse.setUser(shopperObject);
                }else{
                    userGetCurrentUserResponse.setMessage("User type is invalid.");
                    userGetCurrentUserResponse.setSuccess(false);
                }

            }catch(Exception e){
                e.printStackTrace();
                userGetCurrentUserResponse.setSuccess(false);
                userGetCurrentUserResponse.setMessage(e.getMessage());
            }

        }catch(Exception e){
            e.printStackTrace();
            userGetCurrentUserResponse.setSuccess(false);
            userGetCurrentUserResponse.setMessage(e.getMessage());
        }

        return new ResponseEntity<>(userGetCurrentUserResponse, status);
    }

    @Override
    public ResponseEntity<UserGetGroceryListResponse> getGroceryLists(UserGetGroceryListRequest body){

        UserGetGroceryListResponse userGetGroceryListResponse = new UserGetGroceryListResponse();
        HttpStatus status = HttpStatus.OK;

        try{
            GetGroceryListsRequest request = new GetGroceryListsRequest(body.getJwTToken());

            GetGroceryListsResponse response = ServiceSelector.getUserService().getGroceryLists(request);
            try{
                userGetGroceryListResponse.setTimestamp(response.getTimestamp().toString());
                userGetGroceryListResponse.setMessage(response.getMessage());
                userGetGroceryListResponse.setSuccess(response.isSuccess());
                userGetGroceryListResponse.setGroceryLists(populateGroceryList(response.getGroceryLists()));

            }catch(Exception e){
                e.printStackTrace();
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<>(userGetGroceryListResponse, status);
    }

    @Override
    public ResponseEntity<UserUpdateDriverShiftResponse> updateDriverShift(UserUpdateDriverShiftRequest body){

        UserUpdateDriverShiftResponse userUpdateDriverShiftResponse = new UserUpdateDriverShiftResponse();
        HttpStatus status = HttpStatus.OK;

        try{
            UpdateDriverShiftRequest request = new UpdateDriverShiftRequest(body.getJwtToken(), body.isOnShift());

            UpdateDriverShiftResponse response = ServiceSelector.getUserService().updateDriverShift(request);
            try{
                userUpdateDriverShiftResponse.setTimestamp(response.getTimestamp().toString());
                userUpdateDriverShiftResponse.setMessage(response.getMessage());
                userUpdateDriverShiftResponse.setSuccess(response.isSuccess());

            }catch(Exception e){
                e.printStackTrace();
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<>(userUpdateDriverShiftResponse, status);
    }

    //Helper for getCurrentUser
    public AdminObject populateAdmin(Admin admin){
        AdminObject adminObject = new AdminObject();
        adminObject.setName(admin.getName());
        adminObject.setSurname(admin.getSurname());
        adminObject.setEmail(admin.getEmail());
        adminObject.setPhoneNumber(admin.getPhoneNumber());
        adminObject.setPassword(admin.getPassword());
        adminObject.setActivationDate(String.valueOf(admin.getActivationDate()));
        adminObject.setActivationCode(admin.getActivationCode());
        adminObject.setResetCode(admin.getResetCode());
        adminObject.setResetExpiration(admin.getResetExpiration());
        adminObject.setAccountType(String.valueOf(admin.getAccountType()));
        adminObject.setAdminID(String.valueOf(admin.getAdminID()));
        return adminObject;
    }
    public DriverObject populateDriver(Driver driver){
        DriverObject driverObject = new DriverObject();
        driverObject.setName(driver.getName());
        driverObject.setSurname(driver.getSurname());
        driverObject.setEmail(driver.getEmail());
        driverObject.setPhoneNumber(driver.getPhoneNumber());
        driverObject.setPassword(driver.getPassword());
        driverObject.setActivationDate(String.valueOf(driver.getActivationDate()));
        driverObject.setActivationCode(driver.getActivationCode());
        driverObject.setResetCode(driver.getResetCode());
        driverObject.setResetExpiration(driver.getResetExpiration());
        driverObject.setAccountType(String.valueOf(driver.getAccountType()));
        driverObject.setDriverID(String.valueOf(driver.getDriverID()));
        driverObject.setRating(BigDecimal.valueOf(driver.getRating()));
        driverObject.setOnShift(driver.getOnShift());
        return driverObject;
    }
    public CustomerObject populateCustomer(Customer customer){
        CustomerObject customerObject = new CustomerObject();
        customerObject.setName(customer.getName());
        customerObject.setSurname(customer.getSurname());
        customerObject.setEmail(customer.getEmail());
        customerObject.setPhoneNumber(customer.getPhoneNumber());
        customerObject.setPassword(customer.getPassword());
        customerObject.setActivationDate(String.valueOf(customer.getActivationDate()));
        customerObject.setActivationCode(customer.getActivationCode());
        customerObject.setResetCode(customer.getResetCode());
        customerObject.setResetExpiration(customer.getResetExpiration());
        customerObject.setAccountType(String.valueOf(customer.getAccountType()));
        customerObject.setCustomerID(String.valueOf(customer.getCustomerID()));
        customerObject.setAddress(String.valueOf(customer.getAddress()));
        List<GroceryListObject> groceryListObjectList = populateGroceryList(customer.getGroceryLists());
        customerObject.setGroceryLists(groceryListObjectList);
        List<ItemObject> itemObjects = populateItems(customer.getShoppingCart());
        customerObject.setShoppingCart(itemObjects);
        customerObject.setPreference(customer.getPreference());
        customerObject.setWallet(customer.getWallet());
        return customerObject;
    }
    public ShopperObject populateShopper(Shopper shopper){

        ShopperObject shopperObject = new ShopperObject();
        try {
            shopperObject.setName(shopper.getName());
            shopperObject.setSurname(shopper.getSurname());
            shopperObject.setEmail(shopper.getEmail());
            shopperObject.setPhoneNumber(shopper.getPhoneNumber());
            shopperObject.setPassword(shopper.getPassword());
            shopperObject.setActivationDate(String.valueOf(shopper.getActivationDate()));
            shopperObject.setActivationCode(shopper.getActivationCode());
            shopperObject.setResetCode(shopper.getResetCode());
            shopperObject.setResetExpiration(shopper.getResetExpiration());
            shopperObject.setAccountType(String.valueOf(shopper.getAccountType()));
            shopperObject.setShopperID(shopper.getShopperID().toString());
            shopperObject.setStoreID(shopper.getStoreID().toString());
            shopperObject.setOrdersCompleted(shopper.getOrdersCompleted());
            shopperObject.setOnShift(shopper.getOnShift());
            shopperObject.setIsActive(shopper.isActive());

        }catch(Exception e)
        {
            e.printStackTrace();
        }

        return shopperObject;
    }
    public List<GroceryListObject> populateGroceryList(List<GroceryList> groceryList){
        List<GroceryListObject> groceryListObjectList = new ArrayList<>();
        if (groceryList == null){
            return null;
        }
        for (int i = 0; i < groceryList.size(); i++){
            GroceryListObject groceryListObject = new GroceryListObject();
            groceryListObject.setGroceryListID(String.valueOf(groceryList.get(i).getGroceryListID()));
            List<ItemObject> itemObjects = populateItems(groceryList.get(i).getItems());
            groceryListObject.setItems(itemObjects);
            groceryListObject.setName(groceryList.get(i).getName());
            groceryListObjectList.add(groceryListObject);
        }
        return groceryListObjectList;
    }
    @Override
    public ResponseEntity<UserScanItemResponse> scanItem(UserScanItemRequest body){

        UserScanItemResponse userScanItemResponse = new UserScanItemResponse();
        HttpStatus status = HttpStatus.OK;

        try{
            ScanItemRequest request = new ScanItemRequest(body.getBarcode(), UUID.fromString(body.getOrderID()));

            ScanItemResponse response = ServiceSelector.getUserService().scanItem(request);
            try{
                userScanItemResponse.setDate(response.getTimestamp().toString());
                userScanItemResponse.setMessage(response.getMessage());
                userScanItemResponse.setSuccess(response.isSuccess());

            }catch(Exception e){
                e.printStackTrace();
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<>(userScanItemResponse, status);
    }

    @Override
    public ResponseEntity<UserCompletePackagingOrderResponse> completePackagingOrder(UserCompletePackagingOrderRequest body){

        UserCompletePackagingOrderResponse userCompletePackagingOrderResponse = new UserCompletePackagingOrderResponse();
        HttpStatus status = HttpStatus.OK;

        try{
            CompletePackagingOrderRequest request = new CompletePackagingOrderRequest(UUID.fromString(body.getOrderID()), body.isGetNext());

            CompletePackagingOrderResponse response = ServiceSelector.getUserService().completePackagingOrder(request);
            try{
                userCompletePackagingOrderResponse.setDate(response.getTimestamp().toString());
                userCompletePackagingOrderResponse.setMessage(response.getMessage());
                userCompletePackagingOrderResponse.setSuccess(response.isSuccess());

            }catch(Exception e){
                e.printStackTrace();
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<>(userCompletePackagingOrderResponse, status);
    }
}
