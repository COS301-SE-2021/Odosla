package cs.superleague.user;

import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.user.dataclass.Customer;
import cs.superleague.user.dataclass.GroceryList;
import cs.superleague.user.dataclass.Shopper;
import cs.superleague.user.dataclass.UserType;
import cs.superleague.user.exceptions.AlreadyExistsException;
import cs.superleague.user.exceptions.InvalidRequestException;
import cs.superleague.user.exceptions.UserDoesNotExistException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import cs.superleague.user.repos.CustomerRepo;
import cs.superleague.user.repos.GroceryListRepo;
import cs.superleague.user.repos.ShopperRepo;
import cs.superleague.user.requests.*;
import cs.superleague.user.responses.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("userServiceImpl")
public class UserServiceImpl implements UserService{

    private final ShopperRepo shopperRepo;
    private final CustomerRepo customerRepo;
    private final GroceryListRepo groceryListRepo;
    //private final UserService userService;

    @Autowired
    public UserServiceImpl(ShopperRepo shopperRepo, CustomerRepo customerRepo,
                       GroceryListRepo groceryListRepo){//, UserService userService) {
        this.shopperRepo = shopperRepo;
        this.customerRepo = customerRepo;
        this.groceryListRepo = groceryListRepo;
        //this.userService = userService;
    }

    @Override //unfinished
    public CompletePackagingOrderResponse completePackagingOrder(CompletePackagingOrderRequest request) throws InvalidRequestException {

        // Checking for valid and appropriately populated request

        boolean invalidReq = false;
        String invalidMessage = "";

        if (request == null){
            invalidReq = true;
            invalidMessage = "Invalid request: null value received";
        } else if (request.getOrderID() == null){
            invalidReq = true;
            invalidMessage = "Invalid request: no orderID received";
        }

        // // Get order by ID // //
        //Order updatedOrder = <paymentService>.getOrder(request.getOrderID());

        // if (updatedOrder.getStatus != OrderStatus.PACKING){
        //  invalidReq = true;
        //  invalidMessage = "Invalid request: incorrect order status;
        //}

        if (invalidReq) throw new InvalidRequestException(invalidMessage);


        // // Update the order status and create time // //
        //.setStatus(OrderStatus.AWAITING_COLLECTION);

        // <paymentService>.updateOrder(updatedOrder);


        //unfinished
        CompletePackagingOrderResponse response = new CompletePackagingOrderResponse();
        return response;

    }

    @Override
    public ScanItemResponse scanItem(ScanItemRequest request) {
        return null;
    }

    @Override
    public GetShopperByUUIDResponse getShopperByUUIDRequest(GetShopperByUUIDRequest request) throws InvalidRequestException, UserDoesNotExistException {
        GetShopperByUUIDResponse response=null;
        if(request != null){

            if(request.getUserID()==null){
                throw new InvalidRequestException("UserID is null in GetShopperByUUIDRequest request - could not return shopper entity");
            }

            Shopper shopperEntity=null;
            try {
                shopperEntity = shopperRepo.findById(request.getUserID()).orElse(null);
            }catch(Exception e){}
            if(shopperEntity==null) {
                throw new UserDoesNotExistException("User with ID does not exist in repository - could not get Shopper entity");
            }
            response=new GetShopperByUUIDResponse(shopperEntity, Calendar.getInstance().getTime(),"Shopper entity with corresponding user id was returned");
        }
        else{
            throw new InvalidRequestException("GetShopperByUUID request is null - could not return Shopper entity");
        }
        return response;
    }

    @Override
    public MakeGroceryListResponse MakeGroceryList(MakeGroceryListRequest request) throws InvalidRequestException, UserDoesNotExistException{
        UUID userID;
        String name;
        String message;
        Optional<Customer> customerOptional;
        Customer customer;
        GroceryList groceryList;

        if(request == null){
            throw new InvalidRequestException("MakeGroceryList Request is null - could not make grocery list");
        }

        if(request.getUserID() == null){
            throw new InvalidRequestException("UserID is null - could not make grocery list");
        }

        if(request.getItems() == null || request.getItems().isEmpty()){
            throw new InvalidRequestException("Item list empty - could not make the grocery list");
        }

        if(request.getName() == null){
            throw new InvalidRequestException("Grocery List Name is Null - could not make the grocery list");
        }

        userID = request.getUserID();
        customerOptional = customerRepo.findById(userID);
        if(customerOptional == null || !customerOptional.isPresent()){
            throw new UserDoesNotExistException("User with given userID does not exist - could not make the grocery list");
        }

        customer = customerOptional.get();
        name = request.getName();
        for (GroceryList list: customer.getGroceryLists()) { // if name exists throw exception
            if(list.getName().equals(name)){
                throw new InvalidRequestException("Grocery List Name exists - could not make the grocery list");
            }
        }

        groceryList = new GroceryList(UUID.randomUUID(), name, request.getItems());
        message = "Grocery List successfully created";

        groceryList = groceryListRepo.save(groceryList);
        customer.getGroceryLists().add(groceryList);
        customerRepo.save(customer);

        return new MakeGroceryListResponse(groceryList, true, message);
    }

    @Override
    public GetShoppingCartResponse getShoppingCart(GetShoppingCartRequest request) throws InvalidRequestException, UserDoesNotExistException{
        UUID userID;
        Optional<Customer> customerOptional;
        Customer customer;
        List<Item> shoppingCart;
        String message;
        boolean success;

        if(request == null){
            throw new InvalidRequestException("GetShoppingCart Request is null - could retrieve shopping cart");
        }

        if(request.getUserID() == null){
            throw new InvalidRequestException("UserID is null - could retrieve shopping cart");
        }

        userID = request.getUserID();
        customerOptional = customerRepo.findById(userID);
        if(customerOptional == null || !customerOptional.isPresent()){
            throw new UserDoesNotExistException("User with given userID does not exist - could not retrieve shopping cart");
        }

        customer = customerOptional.get();
        shoppingCart = customer.getShoppingCart();

        if(shoppingCart == null || shoppingCart.isEmpty()){
            message = "Shopping Cart does not have any items";
            success = false;
        }else{
            message = "Shopping cart successfully retrieved";
            success = true;
        }

        return new GetShoppingCartResponse(shoppingCart, message, success);
    }

    @Override
    public RegisterCustomerResponse registerCustomer(RegisterCustomerRequest request) throws InvalidRequestException, AlreadyExistsException {

        List<GroceryList> groceryLists = new ArrayList<>();
        List<Item> shoppingCart = new ArrayList<>();
        List<Customer> customers;
        GeoPoint address = null;
        Customer customer;
        String name, surname, username, email, phoneNum, password;

        if(request == null){
            throw new InvalidRequestException("RegisterCustomer Request is null - Could not register user as a customer");
        }

        if(request.getAccountType() != UserType.CUSTOMER){
            throw new InvalidRequestException("User type is not Customer - Could not register user as a customer");
        }

        if(request.getAddress() != null){
            address = request.getAddress();
        }

        name = request.getName();
        surname = request.getSurname();
        username = request.getUsername();
        email = request.getEmail();
        phoneNum = request.getPhoneNumber();
        password = request.getPassword();

        validRegisterDetails(name, surname, username, email, phoneNum, password);

        customers = customerRepo.findAll();

        for (Customer c: customers) {
            if(c.getUsername().equals(username)){
                throw new AlreadyExistsException("Username already exists - Registration Failed");
            }

            if(c.getEmail().equals(email)){
                throw new AlreadyExistsException("Email already exists - Registration Failed");
            }

            if(c.getPhoneNumber().equals(phoneNum)){
                throw new AlreadyExistsException("Phone number already exists - Registration Failed");
            }
        }

        if(!emailRegex(email)){
            throw new InvalidRequestException("Invalid email - Registration failed");
        }

        password = hashPassword(password);

        Calendar today = Calendar.getInstance();
        today.add(Calendar.DATE, 7);
        String expirationDate = new SimpleDateFormat("yyyy-MM-dd").format(today.getTime());

        customer = new Customer(name, surname, username, UUID.randomUUID(), email, phoneNum, password,
                today, UUID.randomUUID().toString(), UUID.randomUUID().toString(), expirationDate,
                request.isActive(), request.getAccountType(), address, groceryLists, shoppingCart);

        customerRepo.save(customer);

        return new RegisterCustomerResponse(customer, true, "Customer successfully registered");
    }

    /* helper */
    private void validRegisterDetails(String name, String surname, String username, String email,
             String phoneNum, String password) throws InvalidRequestException{

        if(name == null){
            throw new InvalidRequestException("Name cannot be null - Registration Failed");
        }

        if(surname == null){
            throw new InvalidRequestException("Surname cannot be null - Registration Failed");
        }

        if(username == null){
            throw new InvalidRequestException("Username cannot be null - Registration Failed");
        }

        if(email == null){
            throw new InvalidRequestException("Email cannot be null - Registration Failed");
        }

        if(phoneNum == null){
            throw new InvalidRequestException("Phone Number cannot be null - Registration Failed");
        }

        if(password == null){
            throw new InvalidRequestException("Password cannot be null - Registration Failed");
        }
    }

    private boolean emailRegex(String email){
        String emailRegex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    private String hashPassword(String password){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(15);
        return passwordEncoder.encode(password);
    }
}