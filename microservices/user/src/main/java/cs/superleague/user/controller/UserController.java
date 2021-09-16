package cs.superleague.user.controller;
import cs.superleague.api.UserApi;
import cs.superleague.models.*;
import cs.superleague.notifications.responses.SendDirectEmailNotificationResponse;
import cs.superleague.user.UserServiceImpl;
import cs.superleague.user.dataclass.*;
import cs.superleague.user.repos.*;
import cs.superleague.user.requests.*;
import cs.superleague.user.responses.*;
import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.shopping.dataclass.Catalogue;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.shopping.dataclass.Store;
import org.apache.http.Header;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@CrossOrigin
@RestController
public class UserController implements UserApi {

    private CustomerRepo customerRepo;

    private GroceryListRepo groceryListRepo;

    private DriverRepo driverRepo;

    private AdminRepo adminRepo;

    private ShopperRepo shopperRepo;

    private UserServiceImpl userService;

    private RestTemplate restTemplate;

    private HttpServletRequest httpServletRequest;

    @Autowired
    public UserController(CustomerRepo customerRepo, GroceryListRepo groceryListRepo,
                          DriverRepo driverRepo, AdminRepo adminRepo, ShopperRepo shopperRepo,
                          UserServiceImpl userService, RestTemplate restTemplate,
                          HttpServletRequest httpServletRequest){
        this.customerRepo = customerRepo;
        this.groceryListRepo = groceryListRepo;
        this.driverRepo = driverRepo;
        this.adminRepo = adminRepo;
        this.shopperRepo = shopperRepo;
        this.userService = userService;
        this.restTemplate = restTemplate;
        this.httpServletRequest = httpServletRequest;

    }

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

            Header header = new BasicHeader("Authorization", httpServletRequest.getHeader("Authorization"));
            List<Header> headers = new ArrayList<>();
            headers.add(header);
            CloseableHttpClient httpClient = HttpClients.custom().setDefaultHeaders(headers).build();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));

            SetCartRequest request = new SetCartRequest(body.getCustomerID(), body.getBarcodes());

            System.out.println(barcodes.get(0));

            SetCartResponse response = userService.setCart(request);
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

            ClearShoppingCartResponse response = userService.clearShoppingCart(request);
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

            GetShoppingCartResponse response = userService.getShoppingCart(request);
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

            Header header = new BasicHeader("Authorization", httpServletRequest.getHeader("Authorization"));
            List<Header> headers = new ArrayList<>();
            headers.add(header);
            CloseableHttpClient httpClient = HttpClients.custom().setDefaultHeaders(headers).build();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));

            MakeGroceryListRequest request = new MakeGroceryListRequest(body.getProductIds(), body.getName());

            MakeGroceryListResponse response = userService.makeGroceryList(request);
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

            ResetPasswordResponse response = userService.resetPassword(request);
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

            LoginResponse loginResponse= userService.loginUser(request);

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
            RegisterDriverResponse driverResponse=userService.registerDriver(request);

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
            RegisterCustomerResponse customerResponse=userService.registerCustomer(request);

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
            RegisterAdminResponse adminResponse=userService.registerAdmin(request);

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
            RegisterShopperResponse shopperResponse=userService.registerShopper(request);

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

            AccountVerifyResponse userResponse = userService.verifyAccount(request);
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

            SetCurrentLocationResponse response = userService.setCurrentLocation(request);
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
            UpdateShopperShiftRequest request = new UpdateShopperShiftRequest(body.isOnShift(), UUID.fromString(body.getStoreID()));
            UpdateShopperShiftResponse response1 = userService.updateShopperShift(request);
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

            GetCurrentUserResponse response = userService.getCurrentUser(request);
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
    public ResponseEntity<UserGetCustomerByEmailResponse> getCustomerByEmail(UserGetCustomerByEmailRequest body) {
        UserGetCustomerByEmailResponse getCustomerByEmailResponse=new UserGetCustomerByEmailResponse();
        HttpStatus status = HttpStatus.OK;

        try{
            GetCustomerByEmailRequest request = new GetCustomerByEmailRequest(body.getEmail());

            GetCustomerByEmailResponse response = userService.getCustomerByEmail(request);
            try{
                getCustomerByEmailResponse.setCustomer(populateCustomer(response.getCustomer()));
                getCustomerByEmailResponse.setSuccess(response.isSuccess());

            }catch(Exception e){
                e.printStackTrace();
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<>(getCustomerByEmailResponse, status);
    }

    @Override
    public ResponseEntity<UserGetGroceryListResponse> getGroceryLists(UserGetGroceryListRequest body){

        UserGetGroceryListResponse userGetGroceryListResponse = new UserGetGroceryListResponse();
        HttpStatus status = HttpStatus.OK;

        try{
            GetGroceryListsRequest request = new GetGroceryListsRequest();

            GetGroceryListsResponse response = userService.getGroceryLists(request);
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
    public ResponseEntity<UserGetShopperByEmailResponse> getShopperByEmail(UserGetShopperByEmailRequest body) {
        UserGetShopperByEmailResponse getShopperByEmailResponse=new UserGetShopperByEmailResponse();
        HttpStatus status = HttpStatus.OK;

        try{
            GetShopperByEmailRequest request = new GetShopperByEmailRequest(body.getEmail());

            GetShopperByEmailResponse response = userService.getShopperByEmail(request);
            try{
                getShopperByEmailResponse.setShopper(populateShopper(response.getShopper()));
                getShopperByEmailResponse.setSuccess(response.isSuccess());

            }catch(Exception e){
                e.printStackTrace();
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<>(getShopperByEmailResponse, status);
    }

    @Override
    public ResponseEntity<UserGetShopperByUUIDResponse> getShopperByUUID(UserGetShopperByUUIDRequest body) {
        UserGetShopperByUUIDResponse userGetShopperByUUIDResponse = new UserGetShopperByUUIDResponse();
        HttpStatus status = HttpStatus.OK;
        try {
            GetShopperByUUIDRequest request = new GetShopperByUUIDRequest(UUID.fromString(body.getUserID()));
            GetShopperByUUIDResponse response = userService.getShopperByUUIDRequest(request);
            userGetShopperByUUIDResponse.setShopperEntity(populateShopper(response.getShopper()));
            userGetShopperByUUIDResponse.setMessage(response.getMessage());
            userGetShopperByUUIDResponse.setTimestamp(response.getTimestamp().toString());
        } catch (Exception e){
            userGetShopperByUUIDResponse.setShopperEntity(null);
            userGetShopperByUUIDResponse.setMessage(e.getMessage());
            userGetShopperByUUIDResponse.setTimestamp(Calendar.getInstance().toString());
        }
        return new ResponseEntity<>(userGetShopperByUUIDResponse, status);
    }

    @Override
    public ResponseEntity<UserUpdateDriverShiftResponse> updateDriverShift(UserUpdateDriverShiftRequest body){

        UserUpdateDriverShiftResponse userUpdateDriverShiftResponse = new UserUpdateDriverShiftResponse();
        HttpStatus status = HttpStatus.OK;

        try{
            UpdateDriverShiftRequest request = new UpdateDriverShiftRequest(body.isOnShift());

            UpdateDriverShiftResponse response = userService.updateDriverShift(request);
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
        String activationDate = null;
        if (admin.getActivationDate() != null){
            activationDate = String.valueOf(admin.getActivationDate());
        }
        adminObject.setActivationDate(activationDate);
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
        String activationDate = null;
        if (driver.getActivationDate() != null){
            activationDate = String.valueOf(driver.getActivationDate());
        }
        driverObject.setActivationDate(activationDate);
        driverObject.setActivationCode(driver.getActivationCode());
        driverObject.setResetCode(driver.getResetCode());
        driverObject.setResetExpiration(driver.getResetExpiration());
        driverObject.setAccountType(String.valueOf(driver.getAccountType()));

        if(driver.getDeliveryID() != null)
            driverObject.setDeliveryID(driver.getDeliveryID().toString());

        if(driver.getDriverID()!=null)
        {
            driverObject.setDriverID(String.valueOf(driver.getDriverID()));
        }
        driverObject.setRating(BigDecimal.valueOf(driver.getRating()));
        driverObject.setOnShift(driver.getOnShift());
        driverObject.setDeliveriesCompleted(BigDecimal.valueOf(driver.getDeliveriesCompleted()));
        return driverObject;
    }
    public CustomerObject populateCustomer(Customer customer){
        CustomerObject customerObject = new CustomerObject();
        customerObject.setName(customer.getName());
        customerObject.setSurname(customer.getSurname());
        customerObject.setEmail(customer.getEmail());
        customerObject.setPhoneNumber(customer.getPhoneNumber());
        customerObject.setPassword(customer.getPassword());
        String activationDate = null;
        if (customer.getActivationDate() != null){
            activationDate = String.valueOf(customer.getActivationDate());
        }
        customerObject.setActivationDate(activationDate);
        customerObject.setActivationCode(customer.getActivationCode());
        customerObject.setResetCode(customer.getResetCode());
        customerObject.setResetExpiration(customer.getResetExpiration());
        customerObject.setAccountType(String.valueOf(customer.getAccountType()));
        if(customer.getCustomerID()!=null)
        {
            customerObject.setCustomerID(String.valueOf(customer.getCustomerID()));
        }
        GeoPointObject geoPointObject = null;
        if (customer.getAddress() != null){
            geoPointObject = populateGeoPoint(customer.getAddress());
        }
        customerObject.setAddress(geoPointObject);
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
            String activationDate = null;
            if (shopper.getActivationDate() != null){
                activationDate = String.valueOf(shopper.getActivationDate());
            }
            shopperObject.setActivationDate(activationDate);
            shopperObject.setActivationCode(shopper.getActivationCode());
            shopperObject.setResetCode(shopper.getResetCode());
            shopperObject.setResetExpiration(shopper.getResetExpiration());
            shopperObject.setAccountType(String.valueOf(shopper.getAccountType()));
            shopperObject.setShopperID(shopper.getShopperID().toString());
            if(shopper.getStoreID()!=null)
            {
                shopperObject.setStoreID(shopper.getStoreID().toString());
            }
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

            Header header = new BasicHeader("Authorization", httpServletRequest.getHeader("Authorization"));
            List<Header> headers = new ArrayList<>();
            headers.add(header);
            CloseableHttpClient httpClient = HttpClients.custom().setDefaultHeaders(headers).build();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));

            ScanItemRequest request = new ScanItemRequest(body.getBarcode(), UUID.fromString(body.getOrderID()));

            ScanItemResponse response = userService.scanItem(request);
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

            Header header = new BasicHeader("Authorization", httpServletRequest.getHeader("Authorization"));
            List<Header> headers = new ArrayList<>();
            headers.add(header);
            CloseableHttpClient httpClient = HttpClients.custom().setDefaultHeaders(headers).build();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));

            CompletePackagingOrderRequest request = new CompletePackagingOrderRequest(UUID.fromString(body.getOrderID()), body.isGetNext());

            CompletePackagingOrderResponse response = userService.completePackagingOrder(request);
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

    @Override
    public ResponseEntity<UserCompleteDeliveryResponse> completeDelivery(UserCompleteDeliveryRequest body){

        UserCompleteDeliveryResponse userCompleteDeliveryResponse = new UserCompleteDeliveryResponse();
        HttpStatus status = HttpStatus.OK;

        try{

            Header header = new BasicHeader("Authorization", httpServletRequest.getHeader("Authorization"));
            List<Header> headers = new ArrayList<>();
            headers.add(header);
            CloseableHttpClient httpClient = HttpClients.custom().setDefaultHeaders(headers).build();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));

            CompleteDeliveryRequest request = new CompleteDeliveryRequest(UUID.fromString(body.getOrderID()));

            CompleteDeliveryResponse response = userService.completeDelivery(request);
            try{
                userCompleteDeliveryResponse.setTimestamp(response.getTimestamp().toString());
                userCompleteDeliveryResponse.setMessage(response.getMessage());
                userCompleteDeliveryResponse.setSuccess(response.isSuccess());

            }catch(Exception e){
                e.printStackTrace();
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<>(userCompleteDeliveryResponse, status);
    }

    @Override
    public ResponseEntity<UserGetCustomerByUUIDResponse> getCustomerByUUID(UserGetCustomerByUUIDRequest body) {
        UserGetCustomerByUUIDResponse userGetCustomerByUUIDResponse = new UserGetCustomerByUUIDResponse();
        HttpStatus status = HttpStatus.OK;

        try{
            GetCustomerByUUIDRequest request = new GetCustomerByUUIDRequest(UUID.fromString(body.getUserID()));

            GetCustomerByUUIDResponse response = userService.getCustomerByUUID(request);
            try{
                userGetCustomerByUUIDResponse.setTimestamp(response.getTimestamp().toString());
                userGetCustomerByUUIDResponse.setMessage(response.getMessage());
                userGetCustomerByUUIDResponse.setCustomer(populateCustomer(response.getCustomer()));

            }catch(Exception e){
                e.printStackTrace();
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<>(userGetCustomerByUUIDResponse, status);
    }

    @Override
    public ResponseEntity<UserGetDriverByEmailResponse> getDriverByEmail(UserGetDriverByEmailRequest body) {
        UserGetDriverByEmailResponse getDriverByEmailResponse=new UserGetDriverByEmailResponse();
        HttpStatus status = HttpStatus.OK;

        try{
            GetDriverByEmailRequest request = new GetDriverByEmailRequest(body.getEmail());

            GetDriverByEmailResponse response = userService.getDriverByEmail(request);
            try{
                getDriverByEmailResponse.setDriver(populateDriver(response.getDriver()));
                getDriverByEmailResponse.setSuccess(response.isSuccess());

            }catch(Exception e){
                e.printStackTrace();
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<>(getDriverByEmailResponse, status);
    }

    @Override
    public ResponseEntity<UserGetDriverByUUIDResponse> getDriverByUUID(UserGetDriverByUUIDRequest body) {
        UserGetDriverByUUIDResponse userGetDriverByUUIDResponse = new UserGetDriverByUUIDResponse();
        HttpStatus status = HttpStatus.OK;
        try {
            GetDriverByUUIDRequest request = new GetDriverByUUIDRequest(UUID.fromString(body.getUserID()));
            GetDriverByUUIDResponse response = userService.getDriverByUUID(request);
            userGetDriverByUUIDResponse.setDriver(populateDriver(response.getDriver()));
            userGetDriverByUUIDResponse.setMessage(response.getMessage());
            userGetDriverByUUIDResponse.setTimestamp(response.getTimestamp().toString());
        } catch (Exception e){
            e.printStackTrace();
            userGetDriverByUUIDResponse.setDriver(null);
            userGetDriverByUUIDResponse.setMessage(e.getMessage());
            userGetDriverByUUIDResponse.setTimestamp(Calendar.getInstance().toString());
        }
        return new ResponseEntity<>(userGetDriverByUUIDResponse, status);
    }

    @Override
    public ResponseEntity<UserDriverSetRatingResponse> driverSetRating(UserDriverSetRatingRequest body) {
        UserDriverSetRatingResponse userDriverSetRatingResponse = new UserDriverSetRatingResponse();
        HttpStatus status = HttpStatus.OK;

        try{
            DriverSetRatingRequest request = new DriverSetRatingRequest(UUID.fromString(body.getDriverID()), body.getRating().doubleValue());

            DriverSetRatingResponse response = userService.driverSetRating(request);
            try{
                userDriverSetRatingResponse.setTimestamp(response.getTimestamp().toString());
                userDriverSetRatingResponse.setMessage(response.getMessage());
                userDriverSetRatingResponse.setSuccess(response.isSuccess());

            }catch(Exception e){
                e.printStackTrace();
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<>(userDriverSetRatingResponse, status);
    }

    @Override
    public ResponseEntity<UserGetAdminByEmailResponse> getAdminByEmail(UserGetAdminByEmailRequest body) {
        UserGetAdminByEmailResponse getAdminByEmailResponse = new UserGetAdminByEmailResponse();
        HttpStatus status = HttpStatus.OK;

        try{
            GetAdminByEmailRequest request = new GetAdminByEmailRequest(body.getEmail());

            GetAdminByEmailResponse response = userService.getAdminByEmail(request);
            try{
                getAdminByEmailResponse.setAdmin(populateAdmin(response.getAdmin()));
                getAdminByEmailResponse.setSuccess(response.isSuccess());

            }catch(Exception e){
                e.printStackTrace();
                getAdminByEmailResponse.setAdmin(null);
                getAdminByEmailResponse.setSuccess(false);
            }

        }catch(Exception e){
            e.printStackTrace();
            getAdminByEmailResponse.setAdmin(null);
            getAdminByEmailResponse.setSuccess(false);
        }

        return new ResponseEntity<>(getAdminByEmailResponse, status);
    }

    @Override
    public ResponseEntity<UserGetAdminByUUIDResponse> getAdminByUUID(UserGetAdminByUUIDRequest body) {
        UserGetAdminByUUIDResponse userGetAdminByUUIDResponse = new UserGetAdminByUUIDResponse();
        HttpStatus status = HttpStatus.OK;
        try {
            GetAdminByUUIDRequest request = new GetAdminByUUIDRequest(UUID.fromString(body.getUserID()));
            GetAdminByUUIDResponse response = userService.getAdminByUUID(request);
            userGetAdminByUUIDResponse.setAdmin(populateAdmin(response.getAdmin()));
            userGetAdminByUUIDResponse.setMessage(response.getMessage());
            userGetAdminByUUIDResponse.setTimestamp(response.getTimestamp().toString());
        } catch (Exception e){
            userGetAdminByUUIDResponse.setAdmin(null);
            userGetAdminByUUIDResponse.setMessage(e.getMessage());
            userGetAdminByUUIDResponse.setTimestamp(Calendar.getInstance().toString());
        }
        return new ResponseEntity<>(userGetAdminByUUIDResponse, status);
    }

    @Override
    public ResponseEntity<UserUpdateShopperDetailsResponse> updateShopperDetails(UserUpdateShopperDetailsRequest body) {
        UserUpdateShopperDetailsResponse userUpdateShopperDetailsResponse = new UserUpdateShopperDetailsResponse();
        HttpStatus status = HttpStatus.OK;

        try{
            UpdateShopperDetailsRequest request = new UpdateShopperDetailsRequest(body.getName(),body.getSurname(),body.getEmail(), body.getPhoneNumber(), body.getPassword(),body.getCurrentPassword());

            UpdateShopperDetailsResponse response = userService.updateShopperDetails(request);
            try{
                userUpdateShopperDetailsResponse.setTimestamp(response.getTimestamp().toString());
                userUpdateShopperDetailsResponse.setMessage(response.getMessage());
                userUpdateShopperDetailsResponse.setSuccess(response.isSuccess());
                userUpdateShopperDetailsResponse.setJwtToken(response.getJwtToken());

            }catch(Exception e){
                e.printStackTrace();
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<>(userUpdateShopperDetailsResponse, status);
    }

    @Override
    public ResponseEntity<UserUpdateDriverDetailsResponse> updateDriverDetails(UserUpdateDriverDetailsRequest body) {
        UserUpdateDriverDetailsResponse userUpdateDriverDetailsResponse = new UserUpdateDriverDetailsResponse();
        HttpStatus status = HttpStatus.OK;

        try{
            UpdateDriverDetailsRequest request = new UpdateDriverDetailsRequest(body.getName(),body.getSurname(),body.getEmail(), body.getPhoneNumber(), body.getPassword(),body.getCurrentPassword());

            UpdateDriverDetailsResponse response = userService.updateDriverDetails(request);
            try{
                userUpdateDriverDetailsResponse.setTimestamp(response.getTimestamp().toString());
                userUpdateDriverDetailsResponse.setMessage(response.getMessage());
                userUpdateDriverDetailsResponse.setSuccess(response.isSuccess());
                userUpdateDriverDetailsResponse.setJwtToken(response.getJwtToken());

            }catch(Exception e){
                e.printStackTrace();
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<>(userUpdateDriverDetailsResponse, status);
    }

    @Override
    public ResponseEntity<UserUpdateCustomerDetailsResponse> updateCustomerDetails(UserUpdateCustomerDetailsRequest body) {
        UserUpdateCustomerDetailsResponse userUpdateCustomerDetailsResponse = new UserUpdateCustomerDetailsResponse();
        HttpStatus status = HttpStatus.OK;

        try{
            GeoPoint geoPoint = null;
            if(body.getLatitude() != null && body.getLongitude() != null) {
                geoPoint = new GeoPoint(body.getLatitude().doubleValue(), body.getLongitude().doubleValue(), body.getAddress());
            }
            UpdateCustomerDetailsRequest request = new UpdateCustomerDetailsRequest(body.getName(), body.getSurname(),
                    body.getEmail(), body.getPhoneNumber(), body.getPassword(), geoPoint, body.getCurrentPassword());

            UpdateCustomerDetailsResponse response = userService.updateCustomerDetails(request);
            try{
                userUpdateCustomerDetailsResponse.setTimestamp(response.getTimestamp().toString());
                userUpdateCustomerDetailsResponse.setMessage(response.getMessage());
                userUpdateCustomerDetailsResponse.setSuccess(response.isSuccess());
                userUpdateCustomerDetailsResponse.setJwtToken(response.getJwtToken());

            }catch(Exception e){
                e.printStackTrace();
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<>(userUpdateCustomerDetailsResponse, status);
    }

    @Override
    public ResponseEntity<UserUpdateAdminDetailsResponse> updateAdminDetails(UserUpdateAdminDetailsRequest body) {
        UserUpdateAdminDetailsResponse userUpdateAdminDetailsResponse = new UserUpdateAdminDetailsResponse();
        HttpStatus status = HttpStatus.OK;

        try{
            UpdateAdminDetailsRequest request = new UpdateAdminDetailsRequest(body.getName(),body.getSurname(),body.getEmail(), body.getPhoneNumber(), body.getPassword(),body.getCurrentPassword());

            UpdateAdminDetailsResponse response = userService.updateAdminDetails(request);
            try{
                userUpdateAdminDetailsResponse.setTimestamp(response.getTimestamp().toString());
                userUpdateAdminDetailsResponse.setMessage(response.getMessage());
                userUpdateAdminDetailsResponse.setSuccess(response.isSuccess());
                userUpdateAdminDetailsResponse.setJwtToken(response.getJwtToken());

            }catch(Exception e){
                e.printStackTrace();
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<>(userUpdateAdminDetailsResponse, status);
    }
    public GeoPointObject populateGeoPoint(GeoPoint geoPoint){
        GeoPointObject geoPointObject = new GeoPointObject();
        geoPointObject.setAddress(geoPoint.getAddress());
        geoPointObject.setLatitude(BigDecimal.valueOf(geoPoint.getLatitude()));
        geoPointObject.setLongitude(BigDecimal.valueOf(geoPoint.getLongitude()));
        return geoPointObject;
    }
}
