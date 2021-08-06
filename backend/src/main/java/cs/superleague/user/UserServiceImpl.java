package cs.superleague.user;

import cs.superleague.shopping.dataclass.Item;
import cs.superleague.user.dataclass.*;
import cs.superleague.user.exceptions.*;
import cs.superleague.user.repos.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import cs.superleague.user.responses.*;
import cs.superleague.user.requests.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("userServiceImpl")
@PropertySource(value = {"classpath:application.properties"})
public class UserServiceImpl implements UserService{

    private final ShopperRepo shopperRepo;
    private final DriverRepo driverRepo;
    private final AdminRepo adminRepo;
    private final CustomerRepo customerRepo;
    private final GroceryListRepo groceryListRepo;
    //private final UserService userService;

    @Autowired
    public UserServiceImpl(ShopperRepo shopperRepo, DriverRepo driverRepo, AdminRepo adminRepo, CustomerRepo customerRepo, GroceryListRepo groceryListRepo){//, UserService userService) {
        this.shopperRepo = shopperRepo;
        this.driverRepo=driverRepo;
        this.adminRepo=adminRepo;
        this.customerRepo = customerRepo;
        this.groceryListRepo = groceryListRepo;
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
                errorMessage="Phone number in RegisterCustomerRequest is null";
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

            if(customerRepo.findByEmail(request.getEmail())){
                return new RegisterCustomerResponse(false,Calendar.getInstance().getTime(), "Email has already been used");
            }
            else{

                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(20);
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

                while(customerRepo.findById(userID).isPresent()){
                    userID=UUID.randomUUID();
                }

                Customer newCustomer=new Customer(request.getName(),request.getSurname(),request.getEmail(),request.getPhoneNumber(),passwordHashed,activationCode, UserType.CUSTOMER,userID);
                customerRepo.save(newCustomer);

                if(customerRepo.findById(userID).isPresent()){
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
                errorMessage="Phone number in RegisterDriverRequest is null";
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

            if(driverRepo.findByEmail(request.getEmail())){
                return new RegisterDriverResponse(false,Calendar.getInstance().getTime(), "Email has already been used");
            }
            else{

                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(20);
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

                while(driverRepo.findById(userID).isPresent()){
                    userID=UUID.randomUUID();
                }

                Driver newDriver=new Driver(request.getName(),request.getSurname(),request.getEmail(),request.getPhoneNumber(),passwordHashed,activationCode, UserType.DRIVER,userID);
                driverRepo.save(newDriver);

                if(driverRepo.findById(userID).isPresent()){
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
                errorMessage="Phone number in RegisterShopperRequest is null";
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

            if(shopperRepo.findByEmail(request.getEmail())){
                return new RegisterShopperResponse(false,Calendar.getInstance().getTime(), "Email has already been used");
            }
            else{

                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(20);
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

                while(shopperRepo.findById(userID).isPresent()){
                    userID=UUID.randomUUID();
                }

                Shopper newShopper=new Shopper(request.getName(),request.getSurname(),request.getEmail(),request.getPhoneNumber(),passwordHashed,activationCode, UserType.SHOPPER,userID);
                shopperRepo.save(newShopper);

                if(driverRepo.findById(userID).isPresent()){
                    /* send a notification with email */
                    return new RegisterShopperResponse(true,Calendar.getInstance().getTime(), "Shopper succesfully added to database");
                }
                else{
                    return new RegisterShopperResponse(false,Calendar.getInstance().getTime(), "Could not save Shopper to database");
                }
            }

        }
        else{
            throw new InvalidRequestException("Request object can't be null for RegisterDriverRequest");
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
                errorMessage="Phone number in RegisterAdminRequest is null";
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

            if(adminRepo.findByEmail(request.getEmail())){
                return new RegisterAdminResponse(false,Calendar.getInstance().getTime(), "Email has already been used");
            }
            else{

                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(20);
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

                while(adminRepo.findById(userID).isPresent()){
                    userID=UUID.randomUUID();
                }

                Admin newAdmin=new Admin(request.getName(),request.getSurname(),request.getEmail(),request.getPhoneNumber(),passwordHashed,activationCode, UserType.ADMIN,userID);
                adminRepo.save(newAdmin);

                if(adminRepo.findById(userID).isPresent()){
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
    private final String secret = "odosla";
    @Override
    public LoginResponse loginUser(LoginRequest request) throws UserException {
        LoginResponse response=null;
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(20);
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
                shopperUser.setActive(true);
                shopperRepo.save(shopperUser);
            case DRIVER:
                assert driverRepo!=null;
                driverUser.setActive(true);
                driverRepo.save(driverUser);
            case CUSTOMER:
                assert customerRepo!=null;
                customerUser.setActive(true);
                customerRepo.save(customerUser);
            case ADMIN:
                assert adminRepo!=null;
                adminUser.setActive(true);
                adminRepo.save(adminUser);

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

    @Override
    public MakeGroceryListResponse makeGroceryList(MakeGroceryListRequest request) throws InvalidRequestException, UserDoesNotExistException{
        UUID userID;
        String name;
        String message;
        Customer customer = null;
        GroceryList groceryList;
        List<Item> items = new ArrayList<>();

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
            throw new UserDoesNotExistException("User with given userID does not exist - could not make the grocery list");
        }

        name = request.getName();
        for (GroceryList list: customer.getGroceryLists()) { // if name exists return false
            if(list.getName().equals(name)){
                message = "Grocery List Name exists - could not make the grocery list";
                return new MakeGroceryListResponse(false, message, new Date());
            }
        }

        for (String barcode: request.getBarcodes()) {
            // getItemByBarcode(barcode);

            if(true) { // found
                // add item to items list
            }else{
                message = "Cannot find item with given barcode - could not make the grocery list";
                return new MakeGroceryListResponse(false, message, new Date());
            }
        }

        groceryList = new GroceryList(UUID.randomUUID(), name, items);
        message = "Grocery List successfully created";

        groceryList = groceryListRepo.save(groceryList);
        customer.getGroceryLists().add(groceryList);
        customerRepo.save(customer);

        return new MakeGroceryListResponse(true, message, new Date());
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


}