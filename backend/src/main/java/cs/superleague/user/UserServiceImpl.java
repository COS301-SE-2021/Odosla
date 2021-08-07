package cs.superleague.user;

import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.dataclass.OrderStatus;
import cs.superleague.payment.exceptions.OrderDoesNotExist;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.shopping.ShoppingService;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.shopping.dataclass.Store;
import cs.superleague.shopping.requests.GetStoresRequest;
import cs.superleague.shopping.responses.GetStoresResponse;
import cs.superleague.user.dataclass.*;
import cs.superleague.user.exceptions.*;
import cs.superleague.user.repos.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import cs.superleague.user.exceptions.*;
import cs.superleague.user.responses.*;
import cs.superleague.user.requests.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.Calendar;

@Service("userServiceImpl")
public class UserServiceImpl implements UserService{

    private final ShopperRepo shopperRepo;
    private final DriverRepo driverRepo;
    private final AdminRepo adminRepo;
    private final CustomerRepo customerRepo;
    private final GroceryListRepo groceryListRepo;
    private final OrderRepo orderRepo;
    private final ShoppingService shoppingService;
    //private final UserService userService;

    @Autowired
    public UserServiceImpl(ShopperRepo shopperRepo, DriverRepo driverRepo, AdminRepo adminRepo, CustomerRepo customerRepo, GroceryListRepo groceryListRepo, OrderRepo orderRepo, @Lazy ShoppingService shoppingService){//, UserService userService) {
        this.shopperRepo = shopperRepo;
        this.driverRepo=driverRepo;
        this.adminRepo=adminRepo;
        this.customerRepo=customerRepo;
        this.groceryListRepo=groceryListRepo;
        this.orderRepo= orderRepo;
        this.shoppingService = shoppingService;
    }

    /**
     *
     * @param request is used to bring in:
     *                OrderID - Id of order that should be found in database
     *
     * completePackagingOrder should:
     *                1.Check if request object is not null else throw InvalidRequestException
     *                2.Check if request object's orderID is null, else throw InvalidRequestException
     *                3.Check if order exists in database, else throw OrderDoesNotExist
     *                4.Set found order's status to AWAITING_COLLECTION
     *                5.Return response object
     * Request Object (CompletePackagingOrderRequest)
     * {
     *                "orderID":"d30e7a98-c918-11eb-b8bc-0242ac130003"
     *                "getNext":true
     *
     * }
     * Response Object
     * {
     *                "success":"true"
     *                "timeStamp":"2021-01-05T11:50:55"
     *                "message":"Order entity with corresponding ID is ready for collection"
     *
     * }
     * @return
     * @throws InvalidRequestException
     * @throws OrderDoesNotExist
     */
    @Override
    public CompletePackagingOrderResponse completePackagingOrder(CompletePackagingOrderRequest request) throws InvalidRequestException, OrderDoesNotExist {
        CompletePackagingOrderResponse response = null;
        if(request != null){

            if(request.getOrderID()==null){
                throw new InvalidRequestException("OrderID is null in CompletePackagingOrderRequest request - could not retrieve order entity");
            }

            Order orderEntity=null;
            try {
                orderEntity = orderRepo.findById(request.getOrderID()).orElse(null);
            }
            catch (Exception e){
                throw new OrderDoesNotExist("Order with ID does not exist in repository - could not get Order entity");
            }

            if(orderEntity==null)
            {
                throw new OrderDoesNotExist("Order with ID does not exist in repository - could not get Order entity");
            }
            orderEntity.setStatus(OrderStatus.AWAITING_COLLECTION);

            Shopper shopperEntity=null;
            try {
                shopperEntity = shopperRepo.findById(orderEntity.getShopperID()).orElse(null);
            }
            catch (Exception e){
                throw new InvalidRequestException("Shopper with ID does not exist in repository - could not get Shopper entity");
            }

            if(shopperEntity==null)
            {
                throw new InvalidRequestException("Shopper with ID does not exist in repository - could not get Shopper entity");
            }

            shopperEntity.setOrdersCompleted(shopperEntity.getOrdersCompleted()+1);

            //TODO check the order type and call the respective user (driver or customer)
            response=new CompletePackagingOrderResponse(true, Calendar.getInstance().getTime(),"Order entity with corresponding ID is ready for collection");
        }
        else{
            throw new InvalidRequestException("CompletePackagingOrderRequest is null - could not fetch order entity");
        }
        return response;

    }

    /**
     *
     * @param request is used to bring in:
     *                OrderID - Order that should be found in database
     *                barcode- the barcode used to identify the item being scanned
     *
     * scanItem should:
     *                1.Check if request object is not null else throw InvalidRequestException
     *                2.Check if request object's ID is null, else throw InvalidRequestException
     *                3.Check if request object's barcode is empty, else throw InvalidRequestException
     *                4.Check if order exists in database, else throw OrderDoesNotExist
     *                5.Use the barcode to find the corresponding item in the order.
     *                6.Return response object
     * Request Object (ScanItemRequest)
     * {
     *                "orderID":"d30e7a98-c918-11eb-b8bc-0242ac130003"
     *                "barcode":"34gd-43232-43fsfs-421fsfs-grw"
     *
     * }
     * Response Object
     * {
     *                "success":"true"
     *                "timeStamp":"2021-01-05T11:50:55"
     *                "message":"Item successfully scanned"
     *
     * }
     * @return
     * @throws InvalidRequestException
     * @throws OrderDoesNotExist
     */
    @Override
    public ScanItemResponse scanItem(ScanItemRequest request) throws InvalidRequestException, OrderDoesNotExist {
        ScanItemResponse response = null;
        if(request != null){

            if(request.getOrderID()==null){
                throw new InvalidRequestException("Order ID is null in ScanItemRequest request - could not retrieve order entity");
            }
            if(request.getBarcode()==null){
                throw new InvalidRequestException("Barcode is null in ScanItemRequest request - could not scan item");
            }

            Order orderEntity=null;

            try {
                orderEntity = orderRepo.findById(request.getOrderID()).orElse(null);
            }
            catch (Exception e){
                throw new OrderDoesNotExist("Order with ID does not exist in repository - could not get Order entity");
            }

            if(orderEntity==null)
            {
                throw new OrderDoesNotExist("Order with ID does not exist in repository - could not get Order entity");
            }

            List<Item> items = orderEntity.getItems();
            boolean itemFound= false;

            for (Item item : items) {
                if (item.getBarcode().equals(request.getBarcode())) {
                    itemFound = true;
                    response = new ScanItemResponse(true, Calendar.getInstance().getTime(), "Item successfully scanned");
                }
            }
            if(itemFound)
            {
                return response;
            }
            else
            {
                throw new InvalidRequestException("Item barcode doesn't match any of the items in the order");
            }
        }
        else{
            throw new InvalidRequestException("ScanItemRequest is null - could not fetch order entity");
        }

    }

    @Override
    public RegisterCustomerResponse registerCustomer(RegisterCustomerRequest request) throws InvalidRequestException {
        RegisterCustomerResponse response=null;

        if(request!=null){

            boolean isException=false;
            String errorMessage="";

            if(request.getEmail()==null){
                isException=true;
                errorMessage="Email in RegisterCustomerRequest is null";
            }
            else if(request.getName()==null){
                isException=true;
                errorMessage="Name in RegisterCustomerRequest is null";
            }
            else if(request.getPassword() == null){
                isException=true;
                errorMessage="Password in RegisterCustomerRequest is null";
            }
            else if(request.getSurname()==null){
                isException=true;
                errorMessage="Surname in RegisterCustomerRequest is null";
            }
            else if(request.getPhoneNumber()==null){
                isException=true;
                errorMessage="PhoneNumber in RegisterCustomerRequest is null";
            }


            if(isException==true){
                throw new InvalidRequestException(errorMessage);
            }

            String emailRegex = "^(.+)@(.+)$";
            Pattern pattern = Pattern.compile(emailRegex);
            Matcher emailMatcher = pattern.matcher(request.getEmail());

            String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";
            Pattern passwordPattern = Pattern.compile(passwordRegex);
            Matcher passwordMatcher = passwordPattern.matcher(request.getPassword());

            assert customerRepo!=null;

            errorMessage="";
            Boolean isError=false;
            if(!emailMatcher.matches()){
                isError=true;
                errorMessage+="Email is not valid";
            }
            if(!passwordMatcher.matches()){
                isError=true;
                if (errorMessage!=""){
                    errorMessage+=" and ";
                }
                errorMessage+="Password is not valid";
            }

            if(isError) {
                return new RegisterCustomerResponse(false, Calendar.getInstance().getTime(), errorMessage);
            }

            Customer customer;
            customer=customerRepo.findCustomerByEmail(request.getEmail());

            if(customer!=null){
                return new RegisterCustomerResponse(false,Calendar.getInstance().getTime(), "Email has already been used");
            }
            else{

                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(15);
                String passwordHashed = passwordEncoder.encode(request.getPassword());

                String upperAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
                String lowerAlphabet = "abcdefghijklmnopqrstuvwxyz";
                String numbers = "0123456789";
                String alphaNumeric = upperAlphabet + lowerAlphabet + numbers;


                StringBuilder sb = new StringBuilder();
                Random random = new Random();
                int length = 10;

                for (int i = 0; i < length; i++) {
                    int index = random.nextInt(alphaNumeric.length());
                    char randomChar = alphaNumeric.charAt(index);
                    sb.append(randomChar);
                }

                String activationCode = sb.toString();

                UUID userID = UUID.randomUUID();
                long start = System.currentTimeMillis();
                long end = start + 5000;
                Boolean timeout=false;
                Boolean isPresent=false;

                try{
                    customer=customerRepo.findById(userID).orElse(null);
                    isPresent=true;
                    if(customer==null){
                        isPresent=false;
                    }
                }
                catch (NullPointerException e){
                    isPresent=false;
                }

                while(isPresent){
                    userID=UUID.randomUUID();

                    try{
                        customer=customerRepo.findById(userID).orElse(null);
                        isPresent=true;
                        if(customer==null){
                            isPresent=false;
                        }
                    }
                    catch (NullPointerException e){
                        isPresent=false;
                    }

                    if(System.currentTimeMillis() > end) {
                        timeout=true;
                        break;
                    }
                }

                if(timeout==true){
                    return new RegisterCustomerResponse(false,Calendar.getInstance().getTime(), "Timeout occured and couldn't register Customer");
                }

                Customer newCustomer=new Customer(request.getName(),request.getSurname(),request.getEmail(),request.getPhoneNumber(),passwordHashed,activationCode, UserType.CUSTOMER,userID);
                customerRepo.save(newCustomer);

                try{
                    customer=customerRepo.findById(userID).orElse(null);
                    isPresent=true;
                    if(customer==null){
                        isPresent=false;
                    }
                }
                catch (NullPointerException e){
                    isPresent=false;
                }

                if(isPresent){
                    /* send a notification with email */
                    return new RegisterCustomerResponse(true,Calendar.getInstance().getTime(), "Customer succesfully added to database");
                }
                else{
                    return new RegisterCustomerResponse(false,Calendar.getInstance().getTime(), "Could not save Customer to database");
                }
            }

        }
        else{
            throw new InvalidRequestException("Request object can't be null for RegisterCustomerRequest");
        }
    }

    @Override
    public RegisterDriverResponse registerDriver(RegisterDriverRequest request) throws InvalidRequestException {
        RegisterDriverResponse response=null;

        if(request!=null){

            boolean isException=false;
            String errorMessage="";

            if(request.getEmail()==null){
                isException=true;
                errorMessage="Email in RegisterDriverRequest is null";
            }
            else if(request.getName()==null){
                isException=true;
                errorMessage="Name in RegisterDriverRequest is null";
            }
            else if(request.getPassword() == null){
                isException=true;
                errorMessage="Password in RegisterDriverRequest is null";
            }
            else if(request.getSurname()==null){
                isException=true;
                errorMessage="Surname in RegisterDriverRequest is null";
            }
            else if(request.getPhoneNumber()==null){
                isException=true;
                errorMessage="PhoneNumber in RegisterDriverRequest is null";
            }


            if(isException==true){
                throw new InvalidRequestException(errorMessage);
            }

            String emailRegex = "^(.+)@(.+)$";
            Pattern pattern = Pattern.compile(emailRegex);
            Matcher emailMatcher = pattern.matcher(request.getEmail());

            String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";
            Pattern passwordPattern = Pattern.compile(passwordRegex);
            Matcher passwordMatcher = passwordPattern.matcher(request.getPassword());

            assert driverRepo!=null;

            errorMessage="";
            Boolean isError=false;
            if(!emailMatcher.matches()){
                isError=true;
                errorMessage+="Email is not valid";
            }
            if(!passwordMatcher.matches()){
                isError=true;
                if (errorMessage!=""){
                    errorMessage+=" and ";
                }
                errorMessage+="Password is not valid";
            }

            if(isError) {
                return new RegisterDriverResponse(false, Calendar.getInstance().getTime(), errorMessage);
            }

            Driver driver;
            driver=driverRepo.findDriverByEmail(request.getEmail());
            if(driver!=null){
                return new RegisterDriverResponse(false,Calendar.getInstance().getTime(), "Email has already been used");
            }
            else{

                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(15);
                String passwordHashed = passwordEncoder.encode(request.getPassword());

                String upperAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
                String lowerAlphabet = "abcdefghijklmnopqrstuvwxyz";
                String numbers = "0123456789";
                String alphaNumeric = upperAlphabet + lowerAlphabet + numbers;


                StringBuilder sb = new StringBuilder();
                Random random = new Random();
                int length = 10;

                for (int i = 0; i < length; i++) {
                    int index = random.nextInt(alphaNumeric.length());
                    char randomChar = alphaNumeric.charAt(index);
                    sb.append(randomChar);
                }

                String activationCode = sb.toString();

                UUID userID = UUID.randomUUID();

                long start = System.currentTimeMillis();
                long end = start + 5000;
                Boolean timeout=false;
                Boolean isPresent=false;

                try{
                    driver=driverRepo.findById(userID).orElse(null);
                    isPresent=true;
                    if(driver==null){
                        isPresent=false;
                    }
                }
                catch (NullPointerException e){
                    isPresent=false;
                }

                while(isPresent){
                    userID=UUID.randomUUID();

                    try{
                        driver=driverRepo.findById(userID).orElse(null);
                        isPresent=true;
                        if(driver==null){
                            isPresent=false;
                        }
                    }
                    catch (NullPointerException e){
                        isPresent=false;
                    }

                    if(System.currentTimeMillis() > end) {
                        timeout=true;
                        break;
                    }
                }

                if(timeout==true){
                    return new RegisterDriverResponse(false,Calendar.getInstance().getTime(), "Timeout occured and couldn't register driver");
                }

                Driver newDriver=new Driver(request.getName(),request.getSurname(),request.getEmail(),request.getPhoneNumber(),passwordHashed,activationCode, UserType.DRIVER,userID);
                driverRepo.save(newDriver);

                try{
                    driver=driverRepo.findById(userID).orElse(null);
                    isPresent=true;
                    if(driver==null){
                        isPresent=false;
                    }
                }
                catch (NullPointerException e){
                    isPresent=false;
                }

                if(isPresent){
                    /* send a notification with email */
                    return new RegisterDriverResponse(true,Calendar.getInstance().getTime(), "Driver succesfully added to database");
                }
                else{
                    return new RegisterDriverResponse(false,Calendar.getInstance().getTime(), "Could not save Driver to database");
                }
            }

        }
        else{
            throw new InvalidRequestException("Request object can't be null for RegisterDriverRequest");
        }
    }

    @Override
    public RegisterShopperResponse registerShopper(RegisterShopperRequest request) throws InvalidRequestException {
        RegisterShopperResponse response=null;

        if(request!=null){

            boolean isException=false;
            String errorMessage="";

            if(request.getEmail()==null){
                isException=true;
                errorMessage="Email in RegisterShopperRequest is null";
            }
            else if(request.getName()==null){
                isException=true;
                errorMessage="Name in RegisterShopperRequest is null";
            }
            else if(request.getPassword() == null){
                isException=true;
                errorMessage="Password in RegisterShopperRequest is null";
            }
            else if(request.getSurname()==null){
                isException=true;
                errorMessage="Surname in RegisterShopperRequest is null";
            }
            else if(request.getPhoneNumber()==null){
                isException=true;
                errorMessage="PhoneNumber in RegisterShopperRequest is null";
            }


            if(isException==true){
                throw new InvalidRequestException(errorMessage);
            }

            String emailRegex = "^(.+)@(.+)$";
            Pattern pattern = Pattern.compile(emailRegex);
            Matcher emailMatcher = pattern.matcher(request.getEmail());

            String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";
            Pattern passwordPattern = Pattern.compile(passwordRegex);
            Matcher passwordMatcher = passwordPattern.matcher(request.getPassword());

            assert shopperRepo!=null;

            errorMessage="";
            Boolean isError=false;
            if(!emailMatcher.matches()){
                isError=true;
                errorMessage+="Email is not valid";
            }
            if(!passwordMatcher.matches()){
                isError=true;
                if (errorMessage!=""){
                    errorMessage+=" and ";
                }
                errorMessage+="Password is not valid";
            }

            if(isError) {
                return new RegisterShopperResponse(false, Calendar.getInstance().getTime(), errorMessage);
            }

            Shopper shopper;
            shopper=shopperRepo.findShopperByEmail(request.getEmail());
            if(shopper!=null){
                return new RegisterShopperResponse(false,Calendar.getInstance().getTime(), "Email has already been used");
            }
            else{

                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(15);
                String passwordHashed = passwordEncoder.encode(request.getPassword());

                String upperAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
                String lowerAlphabet = "abcdefghijklmnopqrstuvwxyz";
                String numbers = "0123456789";
                String alphaNumeric = upperAlphabet + lowerAlphabet + numbers;


                StringBuilder sb = new StringBuilder();
                Random random = new Random();
                int length = 10;

                for (int i = 0; i < length; i++) {
                    int index = random.nextInt(alphaNumeric.length());
                    char randomChar = alphaNumeric.charAt(index);
                    sb.append(randomChar);
                }

                String activationCode = sb.toString();

                UUID userID = UUID.randomUUID();


                long start = System.currentTimeMillis();
                long end = start + 5000;
                Boolean timeout=false;
                Boolean isPresent=false;

                try{
                    shopper=shopperRepo.findById(userID).orElse(null);
                    isPresent=true;
                    if(shopper==null){
                        isPresent=false;
                    }
                }
                catch (NullPointerException e){
                    isPresent=false;
                }

                while(isPresent){
                    userID=UUID.randomUUID();

                    try{
                        shopper=shopperRepo.findById(userID).orElse(null);
                        isPresent=true;
                        if(shopper==null){
                            isPresent=false;
                        }
                    }
                    catch (NullPointerException e){
                        isPresent=false;
                    }

                    if(System.currentTimeMillis() > end) {
                        timeout=true;
                        break;
                    }
                }

                if(timeout==true){
                    return new RegisterShopperResponse(false,Calendar.getInstance().getTime(), "Timeout occured and couldn't register shopper");
                }


                Shopper newShopper=new Shopper(request.getName(),request.getSurname(),request.getEmail(),request.getPhoneNumber(),passwordHashed,activationCode, UserType.SHOPPER,userID);
                shopperRepo.save(newShopper);

                try{
                    shopper=shopperRepo.findById(userID).orElse(null);
                    isPresent=true;
                    if(shopper==null){
                        isPresent=false;
                    }
                }
                catch (NullPointerException e){
                    isPresent=false;
                }

                if(isPresent){
                    /* send a notification with email */
                    return new RegisterShopperResponse(true,Calendar.getInstance().getTime(), "Shopper succesfully added to database");
                }
                else{
                    return new RegisterShopperResponse(false,Calendar.getInstance().getTime(), "Could not save Shopper to database");
                }
            }

        }
        else{
            throw new InvalidRequestException("Request object can't be null for RegisterShopperRequest");
        }
    }

    @Override
    public RegisterAdminResponse registerAdmin(RegisterAdminRequest request) throws InvalidRequestException {
        RegisterAdminResponse response=null;

        if(request!=null){

            boolean isException=false;
            String errorMessage="";

            if(request.getEmail()==null){
                isException=true;
                errorMessage="Email in RegisterAdminRequest is null";
            }
            else if(request.getName()==null){
                isException=true;
                errorMessage="Name in RegisterAdminRequest is null";
            }
            else if(request.getPassword() == null){
                isException=true;
                errorMessage="Password in RegisterAdminRequest is null";
            }
            else if(request.getSurname()==null){
                isException=true;
                errorMessage="Surname in RegisterAdminRequest is null";
            }
            else if(request.getPhoneNumber()==null){
                isException=true;
                errorMessage="PhoneNumber in RegisterAdminRequest is null";
            }


            if(isException==true){
                throw new InvalidRequestException(errorMessage);
            }

            String emailRegex = "^(.+)@(.+)$";
            Pattern pattern = Pattern.compile(emailRegex);
            Matcher emailMatcher = pattern.matcher(request.getEmail());

            String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";
            Pattern passwordPattern = Pattern.compile(passwordRegex);
            Matcher passwordMatcher = passwordPattern.matcher(request.getPassword());

            assert adminRepo!=null;

            errorMessage="";
            Boolean isError=false;
            if(!emailMatcher.matches()){
                isError=true;
                errorMessage+="Email is not valid";
            }
            if(!passwordMatcher.matches()){
                isError=true;
                if (errorMessage!=""){
                    errorMessage+=" and ";
                }
                errorMessage+="Password is not valid";
            }

            if(isError) {
                return new RegisterAdminResponse(false, Calendar.getInstance().getTime(), errorMessage);
            }

            Admin admin;
            admin=adminRepo.findAdminByEmail(request.getEmail());

            if(admin!=null){
                return new RegisterAdminResponse(false,Calendar.getInstance().getTime(), "Email has already been used");
            }
            else{

                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(15);
                String passwordHashed = passwordEncoder.encode(request.getPassword());

                String upperAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
                String lowerAlphabet = "abcdefghijklmnopqrstuvwxyz";
                String numbers = "0123456789";
                String alphaNumeric = upperAlphabet + lowerAlphabet + numbers;


                StringBuilder sb = new StringBuilder();
                Random random = new Random();
                int length = 10;

                for (int i = 0; i < length; i++) {
                    int index = random.nextInt(alphaNumeric.length());
                    char randomChar = alphaNumeric.charAt(index);
                    sb.append(randomChar);
                }

                String activationCode = sb.toString();

                UUID userID = UUID.randomUUID();


                long start = System.currentTimeMillis();
                long end = start + 5000;
                Boolean timeout=false;
                Boolean isPresent=false;

                try{
                    admin=adminRepo.findById(userID).orElse(null);
                    isPresent=true;
                    if(admin==null){
                        isPresent=false;
                    }
                }
                catch (NullPointerException e){
                    isPresent=false;
                }

                while(isPresent){
                    userID=UUID.randomUUID();

                    try{
                        admin=adminRepo.findById(userID).orElse(null);
                        isPresent=true;
                        if(admin==null){
                            isPresent=false;
                        }
                    }
                    catch (NullPointerException e){
                        isPresent=false;
                    }

                    if(System.currentTimeMillis() > end) {
                        timeout=true;
                        break;
                    }
                }

                if(timeout==true){
                    return new RegisterAdminResponse(false,Calendar.getInstance().getTime(), "Timeout occured and couldn't register Admin");
                }


                Admin newAdmin=new Admin(request.getName(),request.getSurname(),request.getEmail(),request.getPhoneNumber(),passwordHashed,activationCode, UserType.ADMIN,userID);
                adminRepo.save(newAdmin);

                try{
                    admin=adminRepo.findById(userID).orElse(null);
                    isPresent=true;
                    if(admin==null){
                        isPresent=false;
                    }
                }
                catch (NullPointerException e){
                    isPresent=false;
                }
                if(isPresent){
                    /* send a notification with email */
                    return new RegisterAdminResponse(true,Calendar.getInstance().getTime(), "Admin succesfully added to database");
                }
                else{
                    return new RegisterAdminResponse(false,Calendar.getInstance().getTime(), "Could not save Admin to database");
                }
            }

        }
        else{
            throw new InvalidRequestException("Request object can't be null for RegisterAdminRequest");
        }
    }

    @Value("${jwt.secret}")
    private final String secret = "theOdoslaSuperLeagueProjectKeyForEncrptionMustBeCertainNumberOFBitsIReallyDontKnowHowToMakeThisStringLonger";
    @Override
    public LoginResponse loginUser(LoginRequest request) throws UserException {
        LoginResponse response=null;

        if(request==null){
            throw new InvalidRequestException("LoginRequest is null");
        }


        Boolean isException=false;
        String errorMessage="";

        if(request.getEmail()==null){
            isException=true;
            errorMessage="Email in LoginRequest is null";
        }
        else if(request.getPassword()==null){

            isException=true;
            errorMessage="Password in LoginRequest is null";
        }
        else if(request.getUserType()==null){

            isException=true;
            errorMessage="UserType in LoginRequest is null";
        }

        if(isException){
            throw new InvalidRequestException(errorMessage);
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(15);
        UUID userID=null;
        Shopper shopperUser=null;
        Customer customerUser=null;
        Admin adminUser=null;
        Driver driverUser=null;
        switch (request.getUserType()){
            case DRIVER:
                assert driverRepo!=null;
                Driver driverToLogin=driverRepo.findDriverByEmail(request.getEmail());
                if (driverToLogin==null){
                    throw new DriverDoesNotExistException("Driver does not exist");
                }
                else if(!passwordEncoder.matches(request.getPassword(),driverToLogin.getPassword())){
                    throw new InvalidCredentialsException("Password is incorrect");
                }
                userID=driverToLogin.getDriverID();
                driverUser=driverToLogin;
                break;

            case SHOPPER:
                assert shopperRepo!=null;
                Shopper shopperToLogin=shopperRepo.findShopperByEmail(request.getEmail());
                if(shopperToLogin==null){
                    throw new ShopperDoesNotExistException("Shopper does not exist");
                }
                else if(!passwordEncoder.matches(request.getPassword(),shopperToLogin.getPassword())){
                    throw new InvalidCredentialsException("Password is incorrect");
                }
                userID=shopperToLogin.getShopperID();
                shopperUser=shopperToLogin;
                break;

            case ADMIN:
                assert adminRepo!=null;
                Admin adminToLogin=adminRepo.findAdminByEmail(request.getEmail());
                if(adminToLogin==null){
                    throw new AdminDoesNotExistException("Admin does not exist");
                }
                else if(!passwordEncoder.matches(request.getPassword(),adminToLogin.getPassword())){
                    throw new InvalidCredentialsException("Password is incorrect");
                }
                userID=adminToLogin.getAdminID();
                adminUser=adminToLogin;
                break;

            case CUSTOMER:
                assert customerRepo!=null;
                Customer customerToLogin=customerRepo.findCustomerByEmail(request.getEmail());
                if(customerToLogin==null){
                    throw new CustomerDoesNotExistException("Customer does not exist");
                }
                else if(!passwordEncoder.matches(request.getPassword(),customerToLogin.getPassword())){
                    throw new InvalidCredentialsException("Password is incorrect");
                }
                userID=customerToLogin.getCustomerID();
                customerUser=customerToLogin;
                break;
        }

        Map<String, Object> head = new HashMap<>();
        head.put("typ", "JWT");
        Map<String, Object> claims = new HashMap<>();
        claims.put("UUID", userID.toString());
        String JWTToken = Jwts.builder().setHeader(head).setClaims(claims).setSubject(request.getEmail()).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 5 * 60 * 60 * 1000)).signWith(SignatureAlgorithm.HS512, secret).compact();
        response=new LoginResponse(JWTToken,true,Calendar.getInstance().getTime(), "User successfully logged in");

        switch (request.getUserType()){
            case SHOPPER:
                assert shopperRepo!=null;
                if(shopperUser!=null) {
                shopperUser.setActive(true);
                shopperRepo.save(shopperUser);
                }
            case DRIVER:
                assert driverRepo!=null;
                if(driverUser!=null) {
                    driverUser.setActive(true);
                    driverRepo.save(driverUser);
                }
            case CUSTOMER:
                assert customerRepo!=null;
                if(customerUser!=null) {
                    customerUser.setActive(true);
                    customerRepo.save(customerUser);
                }
            case ADMIN:
                assert adminRepo!=null;
                if(adminUser!=null) {
                    adminUser.setActive(true);
                    adminRepo.save(adminUser);
                }

        }
        return response;
    }

    @Override
    public AccountVerifyResponse verifyAccount(AccountVerifyRequest request) throws Exception {
        AccountVerifyResponse response=null;

        if(request!=null){

            Boolean isException=false;
            String errorMessage="";

            if(request.getEmail()==null){
                isException=true;
                errorMessage="Email can't be null in AccountVerfiyRequest";
            }
            else if(request.getActivationCode()==null){
                isException=true;
                errorMessage="ActivationCode can't be null in AccountVerfiyRequest";
            }
            else if (request.getUserType()==null){
                isException=true;
                errorMessage="UserType can't be null in AccountVerfiyRequest";
            }

            if(isException){
                throw new InvalidRequestException(errorMessage);
            }


            switch (request.getUserType()){

                case SHOPPER:
                    assert shopperRepo!=null;
                    Shopper shopperToVerify=shopperRepo.findShopperByEmail(request.getEmail());
                    if(shopperToVerify==null){
                        throw new ShopperDoesNotExistException("Shopper Does Not Exist in database");
                    }
                    if(shopperToVerify.getActivationDate()==null){

                        if(shopperToVerify.getActivationCode().equals(request.getActivationCode())){
                            shopperToVerify.setActivationDate(Calendar.getInstance().getTime());
                            shopperRepo.save(shopperToVerify);
                            return new AccountVerifyResponse(true,Calendar.getInstance().getTime(),"Shopper with email '"+request.getEmail()+"' has successfully activated their Shopper account" );
                        }
                        else return new AccountVerifyResponse(false,Calendar.getInstance().getTime(), "Activation code was incorrect");
                    }
                    else return new AccountVerifyResponse(false,Calendar.getInstance().getTime(), "Shopper with email '"+request.getEmail()+"' has already activated their Shopper account");

                case DRIVER:
                    assert driverRepo!=null;
                    Driver driverToVerify=driverRepo.findDriverByEmail(request.getEmail());
                    if(driverToVerify==null){
                        throw new DriverDoesNotExistException("Driver does not exist in database");
                    }
                    if(driverToVerify.getActivationDate()==null){

                        if(driverToVerify.getActivationCode().equals(request.getActivationCode())){
                            driverToVerify.setActivationDate(Calendar.getInstance().getTime());
                            driverRepo.save(driverToVerify);
                            return new AccountVerifyResponse(true,Calendar.getInstance().getTime(),"Driver with email '"+request.getEmail()+"' has successfully activated their Driver account");
                        }
                        else return new AccountVerifyResponse(false,Calendar.getInstance().getTime(), "Activation code was incorrect" );
                    }
                    else return new AccountVerifyResponse(false,Calendar.getInstance().getTime(), "Driver with email '"+request.getEmail()+"'  has already activated their Driver account");

                case CUSTOMER:
                    assert customerRepo!=null;
                    Customer customerToVerify=customerRepo.findCustomerByEmail(request.getEmail());
                    if(customerToVerify==null){
                        throw new CustomerDoesNotExistException("Customer does not exist in database");
                    }
                    if(customerToVerify.getActivationDate()==null){

                        if(customerToVerify.getActivationCode().equals(request.getActivationCode())){
                            customerToVerify.setActivationDate(Calendar.getInstance().getTime());
                            customerRepo.save(customerToVerify);
                            return new AccountVerifyResponse(true,Calendar.getInstance().getTime(),"Customer with email '"+request.getEmail()+"' has successfully activated their Customer account");
                        }
                        else return new AccountVerifyResponse(false,Calendar.getInstance().getTime(), "Activation code was incorrect" );
                    }
                    else return new AccountVerifyResponse(false,Calendar.getInstance().getTime(), "Customer with email '"+request.getEmail()+"'  has already activated their Customer account");

                case ADMIN:
                    assert adminRepo!=null;
                    Admin adminToVerify=adminRepo.findAdminByEmail(request.getEmail());
                    if(adminToVerify==null){
                        throw new AdminDoesNotExistException("Admin does not exist in database");
                    }

                    if(adminToVerify.getActivationDate()==null){

                        if(adminToVerify.getActivationCode().equals(request.getActivationCode())){
                            adminToVerify.setActivationDate(Calendar.getInstance().getTime());
                            adminRepo.save(adminToVerify);
                            return new AccountVerifyResponse(true,Calendar.getInstance().getTime(), "Admin with email '"+request.getEmail()+"' has successfully activated their Admin account");
                        }
                        else return new AccountVerifyResponse(false,Calendar.getInstance().getTime(),"Activation code was incorrect" );

                    }
                    else return new AccountVerifyResponse(false,Calendar.getInstance().getTime(), "Admin with email '"+request.getEmail()+"'  has already activated their Admin account");
            }
        }
        else{
            throw new InvalidRequestException("AccountVerifyRequest can't be null");
        }
        return response;
    }

    /**
     *
     * @param request is used to bring in:
     *                userID - user ID to fetch the corresponding shopper from the database
     *  GetShoppersByUUID should:
     *                1.Check if request object is not null else throw InvalidRequestException
     *                2.Check if request object's ID is null, else throw InvalidRequestException
     *                3.Check if shopper exists in database, else throw ShopperDoesNotExist
     *                5.Return response object
     * Request object (GetShopperByUUIDRequest)
     * {
     *               "userID": "7fa06899-98e5-43a0-b4d0-9dbc8e29f74a"
     *
     * }
     *
     * Response object (GetShopperByUUIDResponse)
     * {
     *                "shopperEntity": shopperEntity
     *                "timeStamp":"2021-01-05T11:50:55"
     *                "message":"Shopper entity with corresponding user id was returned"
     * }
     * @return
     * @throws InvalidRequestException
     * @throws ShopperDoesNotExistException
     */
    @Override
    public GetShopperByUUIDResponse getShopperByUUIDRequest(GetShopperByUUIDRequest request) throws InvalidRequestException, ShopperDoesNotExistException {
        GetShopperByUUIDResponse response=null;
        if(request != null){

            if(request.getUserID()==null){
                throw new InvalidRequestException("UserID is null in GetShopperByUUIDRequest request - could not return shopper entity");
            }

            Shopper shopperEntity=null;
            try {
                shopperEntity = shopperRepo.findById(request.getUserID()).orElse(null);
            }catch(Exception e){
                throw new ShopperDoesNotExistException("User with ID does not exist in repository - could not get Shopper entity");
            }
            if(shopperEntity==null) {
                throw new ShopperDoesNotExistException("User with ID does not exist in repository - could not get Shopper entity");
            }
            response=new GetShopperByUUIDResponse(shopperEntity, Calendar.getInstance().getTime(),"Shopper entity with corresponding user id was returned");
        }
        else{
            throw new InvalidRequestException("GetShopperByUUID request is null - could not return Shopper entity");
        }
        return response;
    }

    /**
     *
     * @param request is used to bring in:
     *                userID - Order that should be found in database
     *                barcodes- list of the barcode used to identify the items to place in the groceryList
     *                name - the name of the grocery list to be created
     *
     * makeGroceryList should:
     *                1.Check if request object is not null else throw InvalidRequestException
     *                2.Check if request object's ID is null, else throw InvalidRequestException
     *                3.Check if request object's barcode list is empty, else throw InvalidRequestException
     *                4.Check if request object's name is null, else throw InvalidRequestException
     *                5.Check if customer exists in database, else throw CustomerDoesNotExistException
     *                5.Use the barcodes to find the corresponding items in the database.
     *                6.Return response object
     * Request Object (makeGroceryListRequest)
     * {
     *                "userID":"d30e7a98-c918-11eb-b8bc-0242ac130003"
     *                "barcode":["34gd-43232-43fsfs-421fsfs-grw", "34gd-43232-43fsfs-421fsfs-grx"]
     *                "name": "grocery list name"
     *
     * }
     * Response Object
     * {
     *                "success":"true"
     *                "timeStamp":"2021-01-05T11:50:55"
     *                "message": "Grocery List successfully created"
     *
     * }
     * @return
     * @throws InvalidRequestException
     * @throws CustomerDoesNotExistException
     */
    @Override
    public MakeGroceryListResponse makeGroceryList(MakeGroceryListRequest request) throws InvalidRequestException, CustomerDoesNotExistException{
        UUID userID;
        String name, message;
        Customer customer = null;
        GroceryList groceryList;
        GetStoresResponse response = null;
        List<Item> items = new ArrayList<>(), groceryListItems = new ArrayList<>();

        if(request == null){
            throw new InvalidRequestException("MakeGroceryList Request is null - could not make grocery list");
        }

        if(request.getUserID() == null){
            throw new InvalidRequestException("UserID is null - could not make grocery list");
        }

        if(request.getBarcodes() == null || request.getBarcodes().isEmpty()){
            throw new InvalidRequestException("Barcodes list empty - could not make the grocery list");
        }

        if(request.getName() == null){
            throw new InvalidRequestException("Grocery List Name is Null - could not make the grocery list");
        }

        userID = request.getUserID();
        try {
            customer = customerRepo.findById(userID).orElse(null);
        }catch(Exception e){}

        if(customer == null){
            throw new CustomerDoesNotExistException("User with given userID does not exist - could not make the grocery list");
        }

        name = request.getName();
        for (GroceryList list: customer.getGroceryLists()) { // if name exists return false
            if(list.getName().equals(name)){
                message = "Grocery List Name exists - could not make the grocery list";
                return new MakeGroceryListResponse(false, message, new Date());
            }
        }

        try {
            response = shoppingService.getStores(new GetStoresRequest());
        }catch (Exception e){
            e.printStackTrace();
        }

        if(response == null || !response.getResponse()){
            message = "Cannot find items - could not make the grocery list";
            return new MakeGroceryListResponse(false, message, new Date());
        }

        for (Store store: response.getStores()) {
            items.addAll(store.getStock().getItems());
        }

        for (String barcode: request.getBarcodes()) {
            for (Item item: items) {
                if(item.getBarcode().equals(barcode)){
                    groceryListItems.add(item);
                }
            }
        }

        if(groceryListItems.isEmpty()){
            message = "Cannot find item with given barcode - could not make the grocery list";
            return new MakeGroceryListResponse(false, message, new Date());
        }

        groceryList = new GroceryList(UUID.randomUUID(), name, groceryListItems);
        message = "Grocery List successfully created";

        groceryListRepo.save(groceryList);
        customer.getGroceryLists().add(groceryList);
        customerRepo.save(customer);

        return new MakeGroceryListResponse(true, message, new Date());
    }

    /**
     *
     * @param request is used to bring in:
     *                userID - Order that should be found in database
     *                barcodes- list of the barcode used to identify the items to place in the groceryList
     *                name - the name of the grocery list to be created
     *
     * makeGroceryList should:
     *                1.Check if request object is not null else throw InvalidRequestException
     *                2.Check if customer exists in database, else throw CustomerDoesNotExistException
     *                4.Return response object
     * Request Object (makeGroceryListRequest)
     * {
     *                "userID":"d30e7a98-c918-11eb-b8bc-0242ac130003"
     *
     * }
     * Response Object
     * {
     *                "success":"true"
     *                "timeStamp":"2021-01-05T11:50:55"
     *                "message": "Shopping cart successfully retrieved"
     *
     * }
     * @return
     * @throws InvalidRequestException
     * @throws CustomerDoesNotExistException
     */
    @Override
    public GetShoppingCartResponse getShoppingCart(GetShoppingCartRequest request) throws InvalidRequestException, CustomerDoesNotExistException{
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
            throw new CustomerDoesNotExistException("User with given userID does not exist - could not retrieve shopping cart");
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

    /**
     *
     * @param request is used to bring in:
     *                customerID - Customer that should be found in database
     *                name - the name of the customer that they want changed to
     *                surname - the surname of the customer that they want changed to
     *                email - the email of the customer that they want changed to
     *                password - the password of the customer that they want changed to
     *
     * UpdateCustomerDetailsRequest should:
     *                1.Check if request object is not null else throw InvalidRequestException
     *                2.Check if request object's ID is null, else throw InvalidRequestException
     *                3.Check if customer exists in database, else throw CustomerDoesNotExistException
     *                4.Return response object
     * Request Object (makeGroceryListRequest)
     * {
     *                "userID":"d30e7a98-c918-11eb-b8bc-0242ac130003"
     *                "name": "Harold"
     *                "surname": "Mbalula"
     *                "email": "mbalula@gmail.com"
     *                "password": "$%^&*INJHBGVFYRdr&3"
     *                "phoneNumber": "0712345678"
     * }
     * Response Object
     * {
     *                "success":"true"
     *                "timeStamp":"2021-01-05T11:50:55"
     *                "message": "Customer successfully updated"
     *
     * }
     * @return
     * @throws InvalidRequestException
     * @throws CustomerDoesNotExistException
     */
    @Override
    public UpdateCustomerDetailsResponse updateCustomerDetails(UpdateCustomerDetailsRequest request) throws InvalidRequestException, CustomerDoesNotExistException{

        String message;
        UUID customerID;
        Customer customer = null;
        boolean success;
        boolean emptyUpdate = true;

        if(request == null){
            throw new InvalidRequestException("UpdateCustomer Request is null - Could not update customer");
        }

        if(request.getCustomerID() == null){
            throw new InvalidRequestException("CustomerId is null - could not update customer");
        }

        customerID = request.getCustomerID();
        try {
            customer = customerRepo.findById(customerID).orElse(null);
        }catch(Exception e){}

        if(customer == null){
            throw new CustomerDoesNotExistException("User with given userID does not exist - could not update customer");
        }

        // authentication ??

        if(request.getName() != null && !Objects.equals(request.getName(), customer.getName())){
            emptyUpdate = false;
            customer.setName(request.getName());
        }

        if(request.getSurname() != null && !request.getSurname().equals(customer.getSurname())){
            emptyUpdate = false;
            customer.setSurname(request.getSurname());
        }

        if(request.getEmail() != null && !request.getEmail().equals(customer.getEmail())){
            emptyUpdate = false;
            if(!emailRegex(request.getEmail())){
                message = "Email is not valid";
                return new UpdateCustomerDetailsResponse(message, false, new Date());
            }else{
                if(customerRepo.findCustomerByEmail(request.getEmail()) != null){
                    message = "Email is already taken";
                    return new UpdateCustomerDetailsResponse(message, false, new Date());
                }
                customer.setEmail(request.getEmail());
            }
        }

        if(request.getPassword() != null){
            emptyUpdate = false;
            if(passwordRegex(request.getPassword())){
                customer.setPassword(request.getPassword());
            }else{
                message = "Password is not valid";
                return new UpdateCustomerDetailsResponse(message, false, new Date());
            }
        }

        if(request.getPhoneNumber() != null && !request.getPhoneNumber().equals(customer.getPhoneNumber())){
            emptyUpdate = false;
            customer.setPhoneNumber(request.getPhoneNumber());
        }

        if(request.getAddress() != null && !request.getAddress().equals(customer.getAddress())){
            emptyUpdate = false;
            customer.setAddress(request.getAddress());
        }

        customerRepo.save(customer);

        if(emptyUpdate){
            success = false;
            message = "Null values submitted - Nothing updated";
        }else {
            success = true;
            message = "Customer successfully updated";
        }

        return new UpdateCustomerDetailsResponse(message, success, new Date());
    }

    /**
     *
     * @param request is used to bring in:
     *                customerID - Customer that should be found in database
     *                barcodes- list of the barcode of iiems to add to cart
     *                name - the name of the grocery list to be created
     *
     * makeGroceryList should:
     *                1.Check if request object is not null else throw InvalidRequestException
     *                2.Check if customer exists in database, else throw CustomerDoesNotExistException
     *                3.Return response object
     * Request Object (makeGroceryListRequest)
     * {
     *                "userID":"d30e7a98-c918-11eb-b8bc-0242ac130003"
     *                "barcodes":["34gd-43232-43fsfs-421fsfs-grw", "34gd-43232-43fsfs-421fsfs-grx"]
     *                "name": "grocery list name"
     *
     * }
     * Response Object
     * {
     *                "success":"true"
     *                "timeStamp":"2021-01-05T11:50:55"
     *                "message": "Grocery List successfully created"
     *
     * }
     * @return
     * @throws InvalidRequestException
     * @throws CustomerDoesNotExistException
     */
    @Override
    public SetCartResponse setCart(SetCartRequest request) throws InvalidRequestException, CustomerDoesNotExistException{

        UUID customerID;
        Customer customer;
        String message = "Items successfully added to cart";
        Optional<Customer> customerOptional;
        GetStoresResponse response = null;
        List<Item> items = new ArrayList<>();
        List<Item> cart = new ArrayList<>();


        if(request == null){
            throw new InvalidRequestException("addToCart Request is null - Could not add to cart");
        }

        if(request.getCustomerID() == null){
            throw new InvalidRequestException("CustomerId is null - could not add to cart");
        }

        customerID = request.getCustomerID();
        customerOptional = customerRepo.findById(customerID);
        if(customerOptional == null || !customerOptional.isPresent()){
            throw new CustomerDoesNotExistException("User with given userID does not exist - could add to cart");
        }

        customer = customerOptional.get();

        if(request.getBarcodes() == null || request.getBarcodes().isEmpty()){
            message = "Item list empty - could not add to cart";
            return new SetCartResponse(message, false, new Date());
        }

        try {
            response = shoppingService.getStores(new GetStoresRequest());
        }catch (Exception e){
            e.printStackTrace();
        }

        if(response == null || !response.getResponse()){
            message = "Cannot find items - could not add to cart";
            return new SetCartResponse(message, false, new Date());
        }

        for (Store store: response.getStores()) {
            items.addAll(store.getStock().getItems());
        }

        for (String barcode: request.getBarcodes()) {
            for (Item item: items) {
                if(item.getBarcode().equals(barcode)){
                    cart.add(item);
                }
            }
        }

        if(cart.isEmpty()){
            message = "Cannot find item with given barcode - could not add to cart";
            return new SetCartResponse(message, false, new Date());
        }

//        customer.getShoppingCart().addAll(cart);
        customer.setShoppingCart(cart);

//        Customer c = new Customer("name", "surname", "name@email.com", "1111111111",
//                "tetetE$4", new Date(), "fsdfghg", "safdf",
//                "adg", true, UserType.CUSTOMER, UUID.randomUUID(), null, customer.getGroceryLists(), null, null, null);
        customerRepo.save(customer);
//        customerRepo.save(new Customer("", "", "",))
//        customerRepo.save(c);

        return new SetCartResponse(message, true, new Date());
    }

    @Override
    public ClearShoppingCartResponse clearShoppingCart(ClearShoppingCartRequest request) throws InvalidRequestException, UserDoesNotExistException{

        UUID customerID;
        Customer customer;
        String message = "Cart successfully cleared";
        Optional<Customer> customerOptional;

        if(request == null){
            throw new InvalidRequestException("clearShoppingCart Request is null - Could not clear to shopping cart");
        }

        if(request.getCustomerID() == null){
            throw new InvalidRequestException("CustomerId is null - could not clear shopping cart");
        }

        customerID = request.getCustomerID();
        customerOptional = customerRepo.findById(customerID);
        if(customerOptional == null || !customerOptional.isPresent()){
            throw new UserDoesNotExistException("User with given userID does not exist - could clear cart");
        }

        customer = customerOptional.get();

        customer.getShoppingCart().clear();

        customer = customerRepo.save(customer);

        return new ClearShoppingCartResponse(customer.getShoppingCart(), message, true, new Date());
    }

    @Override
    public RemoveFromCartResponse removeFromCart(RemoveFromCartRequest request) throws InvalidRequestException, CustomerDoesNotExistException{

        String message;
        Customer customer = null;
        List<Item> cart;

        if(request == null){
            throw new InvalidRequestException("RemoveFromCart Request is null - Could not remove from cart");
        }

        if(request.getCustomerID() == null){
            throw new InvalidRequestException("CustomerId is null - could not remove from cart");
        }

        if(request.getBarcode() == null){
            throw new InvalidRequestException("Barcode is null - could not remove from cart");
        }

        try {
            customer = customerRepo.findById(request.getCustomerID()).orElse(null);
        }catch(Exception e){}

        if(customer == null){
            throw new CustomerDoesNotExistException("User with given userID does not exist - could not remove from cart");
        }

        cart = customer.getShoppingCart();

        if(cart == null || cart.isEmpty()){
            message = "There are no items in the cart - Could not remove from cart";
            return new RemoveFromCartResponse(cart, message, false, new Date());
        }

        for (Item item: cart) {
            if(item.getBarcode().equals(request.getBarcode())){
                cart.remove(item);
                message = "Item successfully removed from cart";
                return new RemoveFromCartResponse(cart, message, true, new Date());
            }
        }

        message = "Item with given barcode does not exist - Could not remove from cart";
        return new RemoveFromCartResponse(cart, message, false, new Date());
    }

    /* helper */
    private void validRegisterDetails(String name, String surname, String email,
                                      String phoneNum, String password) throws InvalidRequestException{

        if(name == null){
            throw new InvalidRequestException("Name cannot be null - Registration Failed");
        }

        if(surname == null){
            throw new InvalidRequestException("Surname cannot be null - Registration Failed");
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

    private boolean passwordRegex(String password){
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";
        Pattern passwordPattern = Pattern.compile(passwordRegex);
        Matcher passwordMatcher = passwordPattern.matcher(password);

        return passwordMatcher.matches();
    }

    private String hashPassword(String password){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(20);
        return passwordEncoder.encode(password);
    }

    private String setActivationCode(){
        String upperAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerAlphabet = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String alphaNumeric = upperAlphabet + lowerAlphabet + numbers;

        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        int length = 10;

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(alphaNumeric.length());
            char randomChar = alphaNumeric.charAt(index);
            sb.append(randomChar);
        }

        return sb.toString();
    }

}