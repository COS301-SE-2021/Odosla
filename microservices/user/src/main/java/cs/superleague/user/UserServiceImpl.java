package cs.superleague.user;
import cs.superleague.integration.security.CurrentUser;
import cs.superleague.integration.security.JwtUtil;
import cs.superleague.notifications.responses.SendDirectEmailNotificationResponse;
import cs.superleague.payment.dataclass.*;
import cs.superleague.user.dataclass.*;
import cs.superleague.user.exceptions.*;
import cs.superleague.user.repos.*;
import cs.superleague.user.requests.*;
import cs.superleague.user.responses.*;
import cs.superleague.delivery.responses.CreateDeliveryResponse;
import cs.superleague.notifications.requests.SendDirectEmailNotificationRequest;
import cs.superleague.payment.exceptions.OrderDoesNotExist;
import cs.superleague.payment.requests.SaveOrderRequest;
import cs.superleague.payment.responses.GetOrderResponse;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.shopping.dataclass.Store;
import cs.superleague.shopping.exceptions.StoreDoesNotExistException;
import cs.superleague.shopping.responses.GetStoreByUUIDResponse;
import cs.superleague.shopping.responses.GetStoresResponse;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cs.superleague.user.dataclass.UserType.CUSTOMER;

@Service("userServiceImpl")
public class UserServiceImpl implements UserService{

    /* TO DO:
    Complete packaging
    Scan Item*/
    @Value("${paymentPort}")
    private String paymentPort;
    @Value("${paymentHost}")
    private String paymentHost;
    @Value("${deliveryPort}")
    private String deliveryPort;
    @Value("${deliveryHost}")
    private String deliveryHost;
    @Value("${shoppingPort}")
    private String shoppingPort;
    @Value("${shoppingHost}")
    private String shoppingHost;
    @Value("${notificationsHost}")
    private String notificationHost;
    @Value("${notificationsPort}")
    private String notificationPort;

    private final ShopperRepo shopperRepo;
    private final DriverRepo driverRepo;
    private final AdminRepo adminRepo;
    private final CustomerRepo customerRepo;
    private final GroceryListRepo groceryListRepo;
    private CurrentUser currentUser;
    private final JwtUtil jwtUtil;
    //private final UserService userService;
    private RabbitTemplate rabbit;
    private final RestTemplate restTemplate;
    @Autowired
    public UserServiceImpl(ShopperRepo shopperRepo, DriverRepo driverRepo, AdminRepo adminRepo, CustomerRepo customerRepo, GroceryListRepo groceryListRepo, JwtUtil jwtUtil, RabbitTemplate rabbit, RestTemplate restTemplate){//, UserService userService) {
        this.shopperRepo = shopperRepo;
        this.driverRepo=driverRepo;
        this.adminRepo=adminRepo;
        this.customerRepo=customerRepo;
        this.groceryListRepo=groceryListRepo;
        this.jwtUtil = jwtUtil;
        this.rabbit=rabbit;
        this.restTemplate = restTemplate;
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
    public CompletePackagingOrderResponse completePackagingOrder(CompletePackagingOrderRequest request) throws InvalidRequestException, OrderDoesNotExist, cs.superleague.delivery.exceptions.InvalidRequestException {
        CompletePackagingOrderResponse response = null;
        if(request != null){
            if(request.getOrderID()==null){
                throw new InvalidRequestException("OrderID is null in CompletePackagingOrderRequest request - could not retrieve order entity");
            }



            Order orderEntity=null;
            try {

                Map<String, Object> parts = new HashMap<String, Object>();
                parts.put("orderID", request.getOrderID());

                ResponseEntity<GetOrderResponse> useCaseResponseEntity = restTemplate.postForEntity("http://"+paymentHost+":"+paymentPort+"/payment/getOrder", parts, GetOrderResponse.class);
                GetOrderResponse getOrderResponse = useCaseResponseEntity.getBody();
                orderEntity = getOrderResponse.getOrder();
            }
            catch (Exception e){
                throw new OrderDoesNotExist("Order with ID does not exist in repository - could not get Order entity");
            }

            if(orderEntity==null)
            {
                throw new OrderDoesNotExist("Order with ID does not exist in repository - could not get Order entity");
            }

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

            if(shopperRepo!=null)
                shopperRepo.save(shopperEntity);

            orderEntity.setStatus(OrderStatus.AWAITING_COLLECTION);

            if(orderEntity.getType().equals(OrderType.DELIVERY))
            {

                Map<String, Object> parts = new HashMap<String, Object>();

                parts.put("orderID", orderEntity.getOrderID());
                parts.put("customerID", orderEntity.getUserID());
                parts.put("storeID", orderEntity.getStoreID());
                parts.put("timeOfDelivery", null);
                parts.put("placeOfDelivery", orderEntity.getDeliveryAddress());
                ResponseEntity<CreateDeliveryResponse> useCaseResponseEntity = restTemplate.postForEntity("http://"+deliveryHost+":"+deliveryPort+"/delivery/createDelivery", parts, CreateDeliveryResponse.class);
                CreateDeliveryResponse createDeliveryResponse = useCaseResponseEntity.getBody();
            }

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
                //orderEntity = orderRepo.findById(request.getOrderID()).orElse(null);

                Map<String, Object> parts = new HashMap<String, Object>();
                parts.put("orderID", request.getOrderID());

                ResponseEntity<GetOrderResponse> useCaseResponseEntity = restTemplate.postForEntity("http://"+paymentHost+":"+paymentPort+"/payment/getOrder", parts, GetOrderResponse.class);
                GetOrderResponse getOrderResponse = useCaseResponseEntity.getBody();
                orderEntity = getOrderResponse.getOrder();
            }
            catch (Exception e){
                throw new OrderDoesNotExist("Order with ID does not exist in repository - could not get Order entity");
            }

            if(orderEntity==null)
            {
                throw new OrderDoesNotExist("Order with ID does not exist in repository - could not get Order entity");
            }

            List<CartItem> items = orderEntity.getCartItems();
            boolean itemFound= false;

            for (CartItem item : items) {
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

            Customer customer=null;
            try {
                customer=customerRepo.findByEmail(request.getEmail()).orElse(null);
            }catch (Exception e){}


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

                Customer newCustomer=new Customer(request.getName(),request.getSurname(),request.getEmail(),request.getPhoneNumber(),passwordHashed,activationCode, CUSTOMER,userID);
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
                    /*
                    HashMap<String, String> properties = new HashMap<>();
                    properties.put("Subject", "Registration for Odosla");
                    properties.put("Email", request.getEmail());
                    SendDirectEmailNotificationRequest request1 = new SendDirectEmailNotificationRequest("Please use the following activation code to activate your account " + activationCode,properties);
                    rabbit.convertAndSend("NotificationsEXCHANGE", "RK_SendDirectEmailNotification", request1);
                    */
                    HashMap<String, String> properties = new HashMap<>();
                    properties.put("Subject", "Registration for Odosla");
                    properties.put("Email", request.getEmail());
                    Map<String, Object> parts = new HashMap<String, Object>();
                    parts.put("message", "Please use the following activation code to activate your account " + activationCode);
                    parts.put("properties", properties);
                    ResponseEntity<SendDirectEmailNotificationResponse> getStoresResponseEntity = restTemplate.postForEntity("http://"+notificationHost+":"+notificationPort+"/notification/sendDirectEmailNotification", parts, SendDirectEmailNotificationResponse.class);

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
//                    HashMap<String, String> properties = new HashMap<>();
//                    properties.put("Subject", "Registration for Odosla");
//                    properties.put("Email", request.getEmail());
//                    SendDirectEmailNotificationRequest request1 = new SendDirectEmailNotificationRequest("Please use the following activation code to activate your account " + activationCode,properties);
//                    rabbit.convertAndSend("NotificationsEXCHANGE", "RK_SendDirectEmailNotification", request1);
                    HashMap<String, String> properties = new HashMap<>();
                    properties.put("Subject", "Registration for Odosla");
                    properties.put("Email", request.getEmail());
                    Map<String, Object> parts = new HashMap<String, Object>();
                    parts.put("message", "Please use the following activation code to activate your account " + activationCode);
                    parts.put("properties", properties);
                    ResponseEntity<SendDirectEmailNotificationResponse> getStoresResponseEntity = restTemplate.postForEntity("http://"+notificationHost+":"+notificationPort+"/notification/sendDirectEmailNotification", parts, SendDirectEmailNotificationResponse.class);
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

            Shopper shopper=null;
            try {
                shopper=shopperRepo.findByEmail(request.getEmail()).orElse(null);
            }catch (Exception e){}

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
//                    HashMap<String, String> properties = new HashMap<>();
//                    properties.put("Subject", "Registration for Odosla");
//                    properties.put("Email", request.getEmail());
//                    SendDirectEmailNotificationRequest request1 = new SendDirectEmailNotificationRequest("Please use the following activation code to activate your account " + activationCode,properties);
//                    rabbit.convertAndSend("NotificationsEXCHANGE", "RK_SendDirectEmailNotification", request1);
                    HashMap<String, String> properties = new HashMap<>();
                    properties.put("Subject", "Registration for Odosla");
                    properties.put("Email", request.getEmail());
                    Map<String, Object> parts = new HashMap<String, Object>();
                    parts.put("message", "Please use the following activation code to activate your account " + activationCode);
                    parts.put("properties", properties);
                    ResponseEntity<SendDirectEmailNotificationResponse> getStoresResponseEntity = restTemplate.postForEntity("http://"+notificationHost+":"+notificationPort+"/notification/sendDirectEmailNotification", parts, SendDirectEmailNotificationResponse.class);
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
//                    HashMap<String, String> properties = new HashMap<>();
//                    properties.put("Subject", "Registration for Odosla");
//                    properties.put("Email", request.getEmail());
//                    SendDirectEmailNotificationRequest request1 = new SendDirectEmailNotificationRequest("Please use the following activation code to activate your account " + activationCode,properties);
//                    rabbit.convertAndSend("NotificationsEXCHANGE", "RK_SendDirectEmailNotification", request1);
                    HashMap<String, String> properties = new HashMap<>();
                    properties.put("Subject", "Registration for Odosla");
                    properties.put("Email", request.getEmail());
                    Map<String, Object> parts = new HashMap<String, Object>();
                    parts.put("message", "Please use the following activation code to activate your account " + activationCode);
                    parts.put("properties", properties);
                    ResponseEntity<SendDirectEmailNotificationResponse> getStoresResponseEntity = restTemplate.postForEntity("http://"+notificationHost+":"+notificationPort+"/notification/sendDirectEmailNotification", parts, SendDirectEmailNotificationResponse.class);
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
                } else if(driverToLogin.getActivationDate()==null || driverToLogin.getActivationDate().after(Calendar.getInstance().getTime())){
                    return new LoginResponse(null,false,Calendar.getInstance().getTime(),"Please verify account before logging in");
                }
                userID=driverToLogin.getDriverID();
                driverUser=driverToLogin;
                break;

            case SHOPPER:
                assert shopperRepo!=null;
                Shopper shopperToLogin=shopperRepo.findByEmail(request.getEmail()).orElse(null);
                if(shopperToLogin==null){
                    throw new ShopperDoesNotExistException("Shopper does not exist");
                }
                else if(!passwordEncoder.matches(request.getPassword(),shopperToLogin.getPassword())){
                    throw new InvalidCredentialsException("Password is incorrect");
                } else if(shopperToLogin.getActivationDate()==null || shopperToLogin.getActivationDate().after(Calendar.getInstance().getTime())){
                    return new LoginResponse(null,false,Calendar.getInstance().getTime(),"Please verify account before logging in");
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
                Customer customerToLogin=customerRepo.findByEmail(request.getEmail()).orElse(null);
                if(customerToLogin==null){
                    throw new CustomerDoesNotExistException("Customer does not exist");
                }
                else if(!passwordEncoder.matches(request.getPassword(),customerToLogin.getPassword())){
                    throw new InvalidCredentialsException("Password is incorrect");
                } else if(customerToLogin.getActivationDate()==null || customerToLogin.getActivationDate().after(Calendar.getInstance().getTime())){
                    return new LoginResponse(null,false,Calendar.getInstance().getTime(),"Please verify account before logging in");
                }
                userID=customerToLogin.getCustomerID();
                customerUser=customerToLogin;
                break;
        }


        String jwtToken="";
        switch (request.getUserType()){
            case SHOPPER:
                assert shopperRepo!=null;
                if(shopperUser!=null) {
                    jwtToken=jwtUtil.generateJWTTokenShopper(shopperUser);
                }
            case DRIVER:
                assert driverRepo!=null;
                if(driverUser!=null) {
                    jwtToken=jwtUtil.generateJWTTokenDriver(driverUser);
                }
            case CUSTOMER:
                assert customerRepo!=null;
                if(customerUser!=null) {
                    jwtToken=jwtUtil.generateJWTTokenCustomer(customerUser);
                }
            case ADMIN:
                assert adminRepo!=null;
                if(adminUser!=null) {
                    jwtToken=jwtUtil.generateJWTTokenAdmin(adminUser);
                }

                if(jwtToken==""){
                    return new LoginResponse(null, false, Calendar.getInstance().getTime(), "Couldn't generate JWTToken for user");
                }

                response=new LoginResponse(jwtToken,true,Calendar.getInstance().getTime(), "User successfully logged in");

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
                errorMessage="Email can't be null in AccountVerifyRequest";
            }
            else if(request.getActivationCode()==null){
                isException=true;
                errorMessage="ActivationCode can't be null in AccountVerifyRequest";
            }
            else if (request.getUserType()==null){
                isException=true;
                errorMessage="UserType can't be null in AccountVerifyRequest";
            }

            if(isException){
                throw new InvalidRequestException(errorMessage);
            }


            switch (request.getUserType()){

                case SHOPPER:
                    assert shopperRepo!=null;
                    Shopper shopperToVerify;
                    try {
                        shopperToVerify = shopperRepo.findByEmail(request.getEmail()).orElse(null);
                    }catch(Exception e){
                        shopperToVerify=null;
                    }

                    if(shopperToVerify==null){
                        throw new ShopperDoesNotExistException("Shopper Does Not Exist in database");
                    }
                    if(shopperToVerify.getActivationDate()==null){

                        if(shopperToVerify.getActivationCode().equals(request.getActivationCode())){
                            shopperToVerify.setActivationDate(new Date());
                            shopperRepo.save(shopperToVerify);
                            return new AccountVerifyResponse(true,Calendar.getInstance().getTime(),"Shopper with email '"+request.getEmail()+"' has successfully activated their Shopper account", UserType.SHOPPER);
                        }
                        else return new AccountVerifyResponse(false,Calendar.getInstance().getTime(), "Activation code was incorrect", UserType.SHOPPER);
                    }
                    else return new AccountVerifyResponse(false,Calendar.getInstance().getTime(), "Shopper with email '"+request.getEmail()+"' has already activated their Shopper account", UserType.SHOPPER);

                case DRIVER:
                    assert driverRepo!=null;
                    Driver driverToVerify;
                    try {
                        driverToVerify = driverRepo.findDriverByEmail(request.getEmail());
                    }catch (Exception e) {
                        driverToVerify=null;
                    }
                    if(driverToVerify==null){
                        throw new DriverDoesNotExistException("Driver does not exist in database");
                    }
                    if(driverToVerify.getActivationDate()==null){

                        if(driverToVerify.getActivationCode().equals(request.getActivationCode())){
                            driverToVerify.setActivationDate(new Date());
                            driverRepo.save(driverToVerify);
                            return new AccountVerifyResponse(true,Calendar.getInstance().getTime(),"Driver with email '"+request.getEmail()+"' has successfully activated their Driver account", UserType.DRIVER);
                        }
                        else return new AccountVerifyResponse(false,Calendar.getInstance().getTime(), "Activation code was incorrect", UserType.DRIVER);
                    }
                    else return new AccountVerifyResponse(false,Calendar.getInstance().getTime(), "Driver with email '"+request.getEmail()+"' has already activated their Driver account", UserType.DRIVER);

                case CUSTOMER:
                    assert customerRepo!=null;
                    Customer customerToVerify;
                     try {
                         customerToVerify = customerRepo.findByEmail(request.getEmail()).orElse(null);
                     }catch (Exception e) {
                         customerToVerify=null;
                     }
                    if(customerToVerify==null){
                        throw new CustomerDoesNotExistException("Customer does not exist in database");
                    }
                    if(customerToVerify.getActivationDate()==null){

                        if(customerToVerify.getActivationCode().equals(request.getActivationCode())){
                            customerToVerify.setActivationDate(new Date());
                            customerRepo.save(customerToVerify);
                            return new AccountVerifyResponse(true,Calendar.getInstance().getTime(),"Customer with email '"+request.getEmail()+"' has successfully activated their Customer account", UserType.CUSTOMER);
                        }
                        else return new AccountVerifyResponse(false,Calendar.getInstance().getTime(), "Activation code was incorrect", UserType.CUSTOMER);
                    }
                    else return new AccountVerifyResponse(false,Calendar.getInstance().getTime(), "Customer with email '"+request.getEmail()+"' has already activated their Customer account", UserType.CUSTOMER);

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

    @Override
    public UpdateShopperDetailsResponse updateShopperDetails(UpdateShopperDetailsRequest request) throws UserException {

        String message;
        Shopper shopper;
        boolean success;
        String jwtToken = null;
        boolean emptyUpdate = true;
        Optional<Shopper> shopperOptional;

        if(request == null){
            throw new InvalidRequestException("UpdateShopper Request is null - Could not update shopper");
        }

        currentUser = new CurrentUser();
        shopperOptional = shopperRepo.findByEmail(currentUser.getEmail());
        if(shopperOptional == null || !shopperOptional.isPresent()){
            throw new ShopperDoesNotExistException("User with the given email does not exist - could not update shopper");
        }

        shopper = shopperOptional.get();

        if(request.getName() != null && !Objects.equals(request.getName(), shopper.getName())){
            emptyUpdate = false;
            shopper.setName(request.getName());
        }

        if(request.getSurname() != null && !request.getSurname().equals(shopper.getSurname())){
            emptyUpdate = false;
            shopper.setSurname(request.getSurname());
        }

        if(request.getEmail() != null && !request.getEmail().equals(shopper.getEmail())){
            emptyUpdate = false;
            if(!emailRegex(request.getEmail())){
                message = "Email is not valid";
                return new UpdateShopperDetailsResponse(message, false, new Date(),null);
            }

            if(shopperRepo.findByEmail(request.getEmail()).isPresent()){
                message = "Email is already taken";
                return new UpdateShopperDetailsResponse(message, false, new Date(),null);
            }

            JwtUtil jwtUtil = new JwtUtil();
            shopper.setEmail(request.getEmail());
            jwtToken=jwtUtil.generateJWTTokenShopper(shopper);
        }

        if(request.getPassword() != null){
            emptyUpdate = false;

            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(15);
            String currentPassword = request.getCurrentPassword();
            String currentPasswordCheck = shopper.getPassword();

            if(!passwordEncoder.matches(currentPassword,currentPasswordCheck)){
                return new UpdateShopperDetailsResponse("Incorrect password",false,Calendar.getInstance().getTime(),null);
            }
            if(passwordRegex(request.getPassword())){
                passwordEncoder = new BCryptPasswordEncoder(15);
                String passwordHashed = passwordEncoder.encode(request.getPassword());
                shopper.setPassword(passwordHashed);
            }else{
                message = "Password is not valid";
                return new UpdateShopperDetailsResponse(message, false, new Date(),null);
            }
        }

        if(request.getPhoneNumber() != null && !request.getPhoneNumber().equals(shopper.getPhoneNumber())){
            emptyUpdate = false;
            shopper.setPhoneNumber(request.getPhoneNumber());
        }

        shopperRepo.save(shopper);

        if(emptyUpdate){
            success = false;
            message = "Null values submitted - Nothing updated";
        }else {
            success = true;
            message = "Shopper successfully updated";
        }

        return new UpdateShopperDetailsResponse(message, success, new Date(),jwtToken);
    }

    @Override
    public UpdateAdminDetailsResponse updateAdminDetails(UpdateAdminDetailsRequest request) throws UserException{
        String message;
        Admin admin;
        boolean success;
        boolean emptyUpdate = true;
        String JWTToken = null;

        if(request == null){
            throw new InvalidRequestException("UpdateAdmin Request is null - Could not update admin");
        }

        currentUser = new CurrentUser();

        admin = adminRepo.findAdminByEmail(currentUser.getEmail());
        if(admin == null){
            throw new AdminDoesNotExistException("User with given Email does not exist - could not update admin");
        }

        if(request.getName() != null && !Objects.equals(request.getName(), admin.getName())){
            emptyUpdate = false;
            admin.setName(request.getName());
        }

        if(request.getSurname() != null && !request.getSurname().equals(admin.getSurname())){
            emptyUpdate = false;
            admin.setSurname(request.getSurname());
        }

        if(request.getEmail() != null && !request.getEmail().equals(admin.getEmail())){
            emptyUpdate = false;
            if(!emailRegex(request.getEmail())){
                message = "Email is not valid";
                return new UpdateAdminDetailsResponse(message, false, new Date(), null);
            }else{
                if(adminRepo.findAdminByEmail(request.getEmail()) != null){
                    message = "Email is already taken";
                    return new UpdateAdminDetailsResponse(message, false, new Date(), null);
                }
                admin.setEmail(request.getEmail());

                JwtUtil jwtUtil = new JwtUtil();
                JWTToken = jwtUtil.generateJWTTokenAdmin(admin);
            }
        }

        if(request.getPassword() != null){
            emptyUpdate = false;

            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(15);
            String currentPassword = request.getCurrentPassword();
            String currentPasswordCheck = admin.getPassword();

            if(!passwordEncoder.matches(currentPassword,currentPasswordCheck)){
                return new UpdateAdminDetailsResponse("Incorrect password",false,Calendar.getInstance().getTime(),null);
            }
            if(passwordRegex(request.getPassword())){
                passwordEncoder = new BCryptPasswordEncoder(15);
                String passwordHashed = passwordEncoder.encode(request.getPassword());
                admin.setPassword(passwordHashed);
            }else{
                message = "Password is not valid";
                return new UpdateAdminDetailsResponse(message, false, new Date(),null);
            }
        }

        if(request.getPhoneNumber() != null && !request.getPhoneNumber().equals(admin.getPhoneNumber())){
            emptyUpdate = false;
            admin.setPhoneNumber(request.getPhoneNumber());
        }

        adminRepo.save(admin);

        if(emptyUpdate){
            success = false;
            message = "Null values submitted - Nothing updated";
        }else {
            success = true;
            message = "Admin successfully updated";
        }

        return new UpdateAdminDetailsResponse(message, success, new Date(), JWTToken);
    }

    @Override
    public UpdateDriverDetailsResponse updateDriverDetails(UpdateDriverDetailsRequest request) throws UserException {
        String message;
        UUID driverID;
        Driver driver;
        boolean success;
        boolean emptyUpdate = true;
        Optional<Driver> driverOptional;
        String JWTToken = null;

        if (request == null) {
            throw new InvalidRequestException("UpdateDriver Request is null - Could not update driver");
        }

        CurrentUser currentUser = new CurrentUser();

        driver = driverRepo.findDriverByEmail(currentUser.getEmail());
        if (driver == null) {
            throw new DriverDoesNotExistException("User with given email does not exist - could not update driver");
        }

        if (request.getName() != null && !Objects.equals(request.getName(), driver.getName())) {
            emptyUpdate = false;
            driver.setName(request.getName());
        }

        if (request.getSurname() != null && !request.getSurname().equals(driver.getSurname())) {
            emptyUpdate = false;
            driver.setSurname(request.getSurname());
        }

        if (request.getEmail() != null && !request.getEmail().equals(driver.getEmail())) {
            emptyUpdate = false;
            if (!emailRegex(request.getEmail())) {
                message = "Email is not valid";
                return new UpdateDriverDetailsResponse(message, false, new Date(), null);
            } else {
                if (driverRepo.findDriverByEmail(request.getEmail()) != null) {
                    message = "Email is already taken";
                    return new UpdateDriverDetailsResponse(message, false, new Date(), null);
                }
                driver.setEmail(request.getEmail());
                JwtUtil jwtUtil = new JwtUtil();
                JWTToken = jwtUtil.generateJWTTokenDriver(driver);
            }
        }

        if (request.getPassword() != null) {
            emptyUpdate = false;

            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(15);
            String currentPassword = request.getCurrentPassword();
            String currentPasswordCheck = driver.getPassword();

            if(!passwordEncoder.matches(currentPassword,currentPasswordCheck)){
                return new UpdateDriverDetailsResponse("Incorrect password",false,Calendar.getInstance().getTime(),null);
            }
            if(passwordRegex(request.getPassword())){
                passwordEncoder = new BCryptPasswordEncoder(15);
                String passwordHashed = passwordEncoder.encode(request.getPassword());
                driver.setPassword(passwordHashed);
            }else{
                message = "Password is not valid";
                return new UpdateDriverDetailsResponse(message, false, new Date(),null);
            }
        }

        if (request.getPhoneNumber() != null && !request.getPhoneNumber().equals(driver.getPhoneNumber())) {
            emptyUpdate = false;
            driver.setPhoneNumber(request.getPhoneNumber());
        }

        driverRepo.save(driver);

        if (emptyUpdate) {
            success = false;
            message = "Null values submitted - Nothing updated";
        } else {
            success = true;
            message = "Driver successfully updated";
        }

        return new UpdateDriverDetailsResponse(message, success, new Date(), JWTToken);
    }

    @Override
    public GetCurrentUserResponse getCurrentUser(GetCurrentUserRequest request) throws InvalidRequestException {
        GetCurrentUserResponse response=null;
        if(request!=null) {

            if(request.getJWTToken()==null){
                throw new InvalidRequestException("JWTToken in GetCurrentUserRequest is null");
            }

            String jwtToken = request.getJWTToken();
            jwtToken = jwtToken.replace("Bearer ","");
            String userType = jwtUtil.extractUserType(jwtToken);
            String email = jwtUtil.extractEmail(jwtToken);
            User user = null;
            switch(userType){
                case "CUSTOMER":
                    //assert customerRepo!=null;
                    try {
                        user=(Customer)customerRepo.findByEmail(email).orElse(null);
                    }catch (Exception e){}
                    break;
                case "SHOPPER":
                    //assert shopperRepo!=null;
                    try {
                        user=(Shopper) shopperRepo.findByEmail(email).orElse(null);
                    }catch (Exception e){}

                    break;
                case "ADMIN":
                    //assert adminRepo!=null;
                    try {
                        user=(Admin) adminRepo.findAdminByEmail(email);
                    }catch (Exception e){}

                    break;
                case "DRIVER":
                    //assert driverRepo!=null;
                    try {
                        user=(Driver) driverRepo.findDriverByEmail(email);
                    }catch (Exception e){}

            }
            if(user!=null){
                response=new GetCurrentUserResponse(user,true,Calendar.getInstance().getTime(),"User successfully returned");
            }else{
                response=new GetCurrentUserResponse(null,false,Calendar.getInstance().getTime(),"User could not be returned");
            }
        }
        else{
            throw new InvalidRequestException("GetCurrentUserRequest object is null");
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

        if(request.getProductIds() == null || request.getProductIds().isEmpty()){
            throw new InvalidRequestException("Barcodes list empty - could not make the grocery list");
        }

        if(request.getName() == null){
            throw new InvalidRequestException("Grocery List Name is Null - could not make the grocery list");
        }

        currentUser=new CurrentUser();

        customer = customerRepo.findByEmail(currentUser.getEmail()).orElse(null);

        if(customer == null){
            throw new CustomerDoesNotExistException("Customer does not exist");
        }
        name = request.getName();
        for (GroceryList list: customer.getGroceryLists()) { // if name exists return false
            if(list.getName().equals(name)){
                message = "Grocery List Name exists - could not make the grocery list";
                return new MakeGroceryListResponse(false, message, new Date());
            }
        }

        Map<String, Object> parts = new HashMap<String, Object>();

        ResponseEntity<GetStoresResponse> getStoresResponseEntity = restTemplate.postForEntity("http://"+shoppingHost+":"+shoppingPort+"/shopping/getStores", parts, GetStoresResponse.class);

        GetStoresResponse getStoresResponse = getStoresResponseEntity.getBody();

        response=getStoresResponse;

        if(response == null || !response.getResponse()){
            message = "Cannot find items - could not make the grocery list";
            return new MakeGroceryListResponse(false, message, new Date());
        }

        for (Store store: response.getStores()) {
            if(store.getStock()!=null)
            items.addAll(store.getStock().getItems());
        }

        for (String productId: request.getProductIds()) {
            for (Item item: items) {
                if(item.getProductID().equals(productId)){
                    groceryListItems.add(item);
                }
            }
        }

        if(groceryListItems.isEmpty()){
            message = "Cannot find item with given productID - could not make the grocery list";
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

        userID = UUID.fromString(request.getUserID());
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
        Customer customer = null;
        boolean success;
        boolean emptyUpdate = true;
        String JWTToken = null;

        if(request == null){
            throw new InvalidRequestException("UpdateCustomer Request is null - Could not update customer");
        }

        CurrentUser currentUser = new CurrentUser();

        try {
            customer = customerRepo.findByEmail(currentUser.getEmail()).orElse(null);
        }catch(Exception e){}

        if(customer == null){
            throw new CustomerDoesNotExistException("User with given Email does not exist - could not update customer");
        }

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
                return new UpdateCustomerDetailsResponse(message, false, new Date(), null);
            }else{
                if(customerRepo.findByEmail(request.getEmail()).isPresent()){
                    message = "Email is already taken";
                    return new UpdateCustomerDetailsResponse(message, false, new Date(), null);
                }
                customer.setEmail(request.getEmail());
                JwtUtil jwtUtil = new JwtUtil();
                JWTToken = jwtUtil.generateJWTTokenCustomer(customer);
            }
        }

        if(request.getPassword() != null){
            emptyUpdate = false;

            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(15);
            String currentPassword = request.getCurrentPassword();
            String currentPasswordCheck = customer.getPassword();

            if(!passwordEncoder.matches(currentPassword,currentPasswordCheck)){
                return new UpdateCustomerDetailsResponse("Incorrect password",false,Calendar.getInstance().getTime(),null);
            }

            if(passwordRegex(request.getPassword())){
                passwordEncoder = new BCryptPasswordEncoder(15);
                String passwordHashed = passwordEncoder.encode(request.getPassword());
                customer.setPassword(passwordHashed);
            }else{
                message = "Password is not valid";
                return new UpdateCustomerDetailsResponse(message, false, new Date(),null);
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

        return new UpdateCustomerDetailsResponse(message, success, new Date(), JWTToken);
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

        customerID = UUID.fromString(request.getCustomerID());
        customerOptional = customerRepo.findById(customerID);
        if(customerOptional == null || !customerOptional.isPresent()){
            throw new CustomerDoesNotExistException("User with given userID does not exist - could add to cart");
        }

        customer = customerOptional.get();

        if(request.getBarcodes() == null || request.getBarcodes().isEmpty()){
            message = "Item list empty - could not add to cart";
            return new SetCartResponse(message, false, new Date());
        }

        Map<String, Object> parts = new HashMap<String, Object>();

        try {
            ResponseEntity<GetStoresResponse> getStoresResponseEntity = restTemplate.postForEntity("http://"+shoppingHost+":"+shoppingPort+"/shopping/getStores", parts, GetStoresResponse.class);
            GetStoresResponse getStoresResponse = getStoresResponseEntity.getBody();
            response=getStoresResponse;
        }catch (Exception e){
            e.printStackTrace();
        }

        if(response == null || !response.getResponse()){
            message = "Cannot find items - could not add to cart";
            return new SetCartResponse(message, false, new Date());
        }

        for (Store store: response.getStores()) {
            if(store.getStock().getItems()!=null)
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

        customer.setShoppingCart(cart);

        customerRepo.save(customer);

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

        customerID = UUID.fromString(request.getCustomerID());
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
   public CollectOrderResponse collectOrder(CollectOrderRequest request) throws OrderDoesNotExist, InvalidRequestException {

        CollectOrderResponse response;

        Boolean isException=false;
        String errorMessage="";

        if(request==null){
            isException=true;
            errorMessage="CollectOrderRequest object is null";

        }else if(request.getOrderID()==null){
            isException=true;
            errorMessage="OrderID in CollectOrderRequest object is null";
        }

        if(isException){
            throw new InvalidRequestException(errorMessage);
        }

        //Get Order By UUID
        //Optional<Order> currentOrder= orderRepo.findById(request.getOrderID());

       Map<String, Object> parts = new HashMap<String, Object>();
       parts.put("orderID", request.getOrderID());

       ResponseEntity<GetOrderResponse> useCaseResponseEntity = restTemplate.postForEntity("http://"+paymentHost+":"+paymentPort+"/payment/getOrder", parts, GetOrderResponse.class);
       GetOrderResponse getOrderResponse = useCaseResponseEntity.getBody();
       Order currentOrder = getOrderResponse.getOrder();

        if(currentOrder==null){
            throw new OrderDoesNotExist("Order does not exist in database");
        }

        Order order=currentOrder;
        order.setStatus(OrderStatus.DELIVERY_COLLECTED);

        /* Send notification to User saying order has been collected */

        //orderRepo.save(order);
        SaveOrderRequest saveOrderRequest = new SaveOrderRequest(order);
        rabbit.convertAndSend("PaymentEXCHANGE", "RK_SaveOrderToRepo", saveOrderRequest);

        /* Checking that order with same ID is now in DELIVERY_COLLECTED status */
        response=new CollectOrderResponse(true,Calendar.getInstance().getTime(),"Order successfully been collected and status has been changed");
        return response;
    }

    @Override
    public CompleteDeliveryResponse completeDelivery(CompleteDeliveryRequest request) throws OrderDoesNotExist, InvalidRequestException {

        CompleteDeliveryResponse response;

        Boolean isException=false;
        String errorMessage="";

        if(request==null){
            isException=true;
            errorMessage="CompleteDeliveryRequest object is null";

        }else if(request.getOrderID()==null){
            isException=true;
            errorMessage="OrderID in CompleteDeliveryRequest object is null";
        }

        if(isException){
            throw new InvalidRequestException(errorMessage);
        }

        //Order order= orderRepo.findById(request.getOrderID()).orElse(null);

        Map<String, Object> parts = new HashMap<String, Object>();
        parts.put("orderID", request.getOrderID());

        ResponseEntity<GetOrderResponse> useCaseResponseEntity = restTemplate.postForEntity("http://"+paymentHost+":"+paymentPort+"/payment/getOrder", parts, GetOrderResponse.class);
        GetOrderResponse getOrderResponse = useCaseResponseEntity.getBody();
        Order order = getOrderResponse.getOrder();

        if(order==null){
            throw new OrderDoesNotExist("Order does not exist in database");
        }

        order.setStatus(OrderStatus.DELIVERED);
        //orderRepo.save(order);
        SaveOrderRequest saveOrderRequest = new SaveOrderRequest(order);
        rabbit.convertAndSend("PaymentEXCHANGE", "RK_SaveOrderToRepo", saveOrderRequest);
        /* Checking that order with same ID is now in DELIVERY_COLLECTED status */
        //Order currentOrder= orderRepo.findById(request.getOrderID()).orElse(null);

        Driver driver = driverRepo.findById(order.getDriverID()).orElse(null);
        if(driver!=null)
        {
            int numDeliveries=0;
            numDeliveries= driver.getDeliveriesCompleted()+1;
            driver.setDeliveriesCompleted(numDeliveries);
            driverRepo.save(driver);
        }
        else
        {
           throw new InvalidRequestException("Driver isn't set in order");
        }
        response=new CompleteDeliveryResponse(true,Calendar.getInstance().getTime(),"Order successfully been delivered and status has been changed");
        return response;
    }

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
            customer = customerRepo.findById(UUID.fromString(request.getCustomerID())).orElse(null);
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

    @Override
    public UpdateShopperShiftResponse updateShopperShift(UpdateShopperShiftRequest request) throws InvalidRequestException, ShopperDoesNotExistException, StoreDoesNotExistException {
        UpdateShopperShiftResponse response;
        if (request == null){
            throw new InvalidRequestException("UpdateShopperShiftRequest object is null");
        }
        if (request.getOnShift() == null){
            throw new InvalidRequestException("onShift in UpdateShopperShiftRequest is null");
        }
        if (request.getStoreID() == null){
            throw new InvalidRequestException("StoreID in UpdateShopperShiftRequest is null");
        }
        currentUser=new CurrentUser();
        Shopper shopper1 = shopperRepo.findByEmail(currentUser.getEmail()).orElse(null);
        if (shopper1 == null){
            throw new ShopperDoesNotExistException("Shopper with shopperID does not exist in database");
        }
        Optional<Shopper> shopper=shopperRepo.findById(shopper1.getShopperID());

        if(shopper==null || !shopper.isPresent()){
            throw new ShopperDoesNotExistException("Shopper with shopperID does not exist in database");
        }

        if(shopper.get().getOnShift()==request.getOnShift()){
            String message="";
            if(request.getOnShift()==true){
                message="Shopper is already on shift";
            }
            else if(request.getOnShift()==false){
                message="Shopper is already not on shift";
            }
            response=new UpdateShopperShiftResponse(false,Calendar.getInstance(),message);
        }
        else{

            Map<String, Object> parts = new HashMap<String, Object>();
            parts.put("storeID", request.getStoreID());
            ResponseEntity<GetStoreByUUIDResponse> getStoreByUUIDResponseEntity = restTemplate.postForEntity("http://"+shoppingHost+":"+shoppingPort+"/shopping/getStoreByUUID", parts, GetStoreByUUIDResponse.class);
            GetStoreByUUIDResponse getStoreByUUIDResponse = getStoreByUUIDResponseEntity.getBody();
            Store store = getStoreByUUIDResponse.getStore();
            if (store == null){
                throw new StoreDoesNotExistException("Store is not saved in database.");
            }
            shopper.get().setOnShift(request.getOnShift());
            shopper.get().setStoreID(request.getStoreID());
            shopperRepo.save(shopper.get());
            if (request.getOnShift() == true){
                if (store.getShoppers() == null){
                    store.setShoppers(new ArrayList<>());
                }
                store.getShoppers().add(shopper.get());
            }else{
                List<Shopper> shoppersAtStore = store.getShoppers();
                if (shoppersAtStore == null){
                    store.setShoppers(new ArrayList<>());
                }else {
                    Shopper foundShopper = null;
                    if (shoppersAtStore != null) {
                        for (Shopper s : shoppersAtStore) {
                            if (s.getShopperID().compareTo(shopper.get().getShopperID()) == 0) {
                                foundShopper= s;
                            }
                        }
                        shoppersAtStore.remove(foundShopper);
                    }
                    store.setShoppers(shoppersAtStore);
                }
            }
            /* Save in store */


            /*Check updates have happened */
            shopper=shopperRepo.findById(shopper1.getShopperID());

            if(shopper==null || !shopper.isPresent()|| shopper.get().getOnShift()!=request.getOnShift()){
                response=new UpdateShopperShiftResponse(false,Calendar.getInstance(),"Couldn't update shopper's shift");
            }
            else{

                response=new UpdateShopperShiftResponse(true,Calendar.getInstance(),"Shopper's shift correctly updated");
            }
        }
        return response;
    }

    @Override
    public UpdateDriverShiftResponse updateDriverShift(UpdateDriverShiftRequest request) throws InvalidRequestException, DriverDoesNotExistException {
        UpdateDriverShiftResponse response;

        Boolean isException=false;
        String errorMessage="";

        if(request==null){
            isException=true;
            errorMessage="UpdateDriverShiftRequest object is null";
        }else if(request.getOnShift()==null){
            isException=true;
            errorMessage="onShift in UpdateDriverShiftRequest is null";
        }

        if(isException){
            throw new InvalidRequestException(errorMessage);
        }

        currentUser=new CurrentUser();
        Driver driver;
        try {
            driver= driverRepo.findDriverByEmail(currentUser.getEmail());
        }catch(NullPointerException e){
            driver=null;
        }

        if(driver == null){
            throw new DriverDoesNotExistException("Driver does not exist");
        }

        if(driver.getOnShift()==request.getOnShift()){
            String message="";
            if(request.getOnShift()==true){
                message="Driver is already on shift";
            }
            else if(request.getOnShift()==false){
                message="Driver is already not on shift";
            }
            response=new UpdateDriverShiftResponse(false,Calendar.getInstance().getTime(),message);
        }
        else{
            driver.setOnShift(request.getOnShift());
            driverRepo.save(driver);

            /*Check updates have happened */
            Optional<Driver> driverOptional = driverRepo.findById(driver.getDriverID());

            if(driverOptional==null || !driverOptional.isPresent()|| driverOptional.get().getOnShift()!=request.getOnShift()){
                response=new UpdateDriverShiftResponse(false,Calendar.getInstance().getTime(),"Couldn't update driver's shift");
            }
            else{
                response=new UpdateDriverShiftResponse(true,Calendar.getInstance().getTime(),"Driver's shift correctly updated");
            }
        }
        return response;

    }

    @Override
    public ResetPasswordResponse resetPassword(ResetPasswordRequest request) throws UserException{
        User user;
        String email;
        String userType;
        String resetCode;
        String passwordResetMessage;

        if(request == null){
            throw new InvalidRequestException("ResetPassword Request is null - Could not reset password");
        }

        if(request.getEmail() == null){
            throw new InvalidRequestException("Email in request object is null - Could not reset password");
        }

        if(request.getUserType() == null){
            throw new InvalidRequestException("Account Type in request object is null - Could not reset password");
        }

        email = request.getEmail();
        userType = request.getUserType();

        if(!emailRegex(email)){
            return new ResetPasswordResponse(null, "Invalid email - Could not reset", false);
        }

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

        resetCode = sb.toString();

        Map<String, String> properties = new HashMap<>();
        properties.put("Type","RESETPASSWORD");
        properties.put("Subject","Odosla");
        properties.put("UserType", userType);

        Customer customer = null;

        if(userType.equals("CUSTOMER")){

            try {
                customer = customerRepo.findByEmail(email).orElse(null);
            }catch (Exception e){}


            if(customer == null){
                return new ResetPasswordResponse(null, "Could not find customer with email - Could not reset", false);
            }

            Date today = new Date();
            Date expiration = new Date(today.getTime() + (4 * 3600 * 1000));

            user = customer;
            user.setResetCode(resetCode);
            user.setResetExpiration(expiration.toString());

            customerRepo.save((Customer)user);

            passwordResetMessage="\nPassword reset has been accepted.\n Please use the following code before " + user.getResetExpiration()+" to change your password.\n\n code: "+resetCode;
//            SendDirectEmailNotificationRequest request1=new SendDirectEmailNotificationRequest(passwordResetMessage,properties);
//            rabbit.convertAndSend("NotificationsEXCHANGE", "RK_SendDirectEmailNotification", request1);
            Map<String, Object> parts = new HashMap<String, Object>();
            parts.put("message", passwordResetMessage);
            parts.put("properties", properties);
            ResponseEntity<SendDirectEmailNotificationResponse> getStoresResponseEntity = restTemplate.postForEntity("http://"+notificationHost+":"+notificationPort+"/notification/sendDirectEmailNotification", parts, SendDirectEmailNotificationResponse.class);
//             {
//                rabbit.convertAndSend("NotificationsEXCHANGE", "RK_SendDirectEmailNotification", request1);
//            }catch (Exception e){
//                e.printStackTrace();
//            }

        }
        else if(userType.equals("SHOPPER")){
            Shopper shopper = null;
            try {
                shopper= shopperRepo.findByEmail(email).orElse(null);
            }
            catch (Exception e){}

            if(shopper == null){
                return new ResetPasswordResponse(null, "Could not find shopper with email - Could not reset", false);
            }

            properties.put("UserID", shopper.getShopperID().toString());

            Date today = new Date();
            Date expiration = new Date(today.getTime() + (4 * 3600 * 1000));

            user = shopper;
            user.setResetCode(resetCode);
            user.setResetExpiration(expiration.toString());

            shopperRepo.save((Shopper)user);

            passwordResetMessage="\nPassword reset has been accepted.\n Please use the following code before " + user.getResetExpiration()+" to change your password.\n\n code: "+resetCode;

//            SendDirectEmailNotificationRequest request1=new SendDirectEmailNotificationRequest(passwordResetMessage,properties);
//            rabbit.convertAndSend("NotificationsEXCHANGE", "RK_SendDirectEmailNotification", request1);
            Map<String, Object> parts = new HashMap<String, Object>();
            parts.put("message", passwordResetMessage);
            parts.put("properties", properties);
            ResponseEntity<SendDirectEmailNotificationResponse> getStoresResponseEntity = restTemplate.postForEntity("http://"+notificationHost+":"+notificationPort+"/notification/sendDirectEmailNotification", parts, SendDirectEmailNotificationResponse.class);
//            try {
//                //rabbit.convertAndSend("NotificationsEXCHANGE", "RK_SendDirectEmailNotification", request1);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
        }
        else if(userType.equals("DRIVER")){
            Driver driver = driverRepo.findDriverByEmail(email);

            if(driver == null){
                return new ResetPasswordResponse(null, "Could not find driver with email - Could not reset", false);
            }

            properties.put("UserID", driver.getDriverID().toString());

            Date today = new Date();
            Date expiration = new Date(today.getTime() + (4 * 3600 * 1000));

            user = driver;
            user.setResetCode(resetCode);
            user.setResetExpiration(expiration.toString());

            driverRepo.save((Driver) user);

            passwordResetMessage="\nPassword reset has been accepted.\n Please use the following code before " + user.getResetExpiration()+" to change your password.\n\n code: "+resetCode;

//            SendDirectEmailNotificationRequest request1=new SendDirectEmailNotificationRequest(passwordResetMessage,properties);
//            rabbit.convertAndSend("NotificationsEXCHANGE", "RK_SendDirectEmailNotification", request1);
//            try {
//                rabbit.convertAndSend("NotificationsEXCHANGE", "RK_SendDirectEmailNotification", request1);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
            Map<String, Object> parts = new HashMap<String, Object>();
            parts.put("message", passwordResetMessage);
            parts.put("properties", properties);
            ResponseEntity<SendDirectEmailNotificationResponse> getStoresResponseEntity = restTemplate.postForEntity("http://"+notificationHost+":"+notificationPort+"/notification/sendDirectEmailNotification", parts, SendDirectEmailNotificationResponse.class);

        }else if(userType.equals("ADMIN")){
            Admin admin = adminRepo.findAdminByEmail(email);

            if(admin == null){
                return new ResetPasswordResponse(null, "Could not find customer with email - Could not reset", false);
            }

            properties.put("UserID", admin.getAdminID().toString());

            Date today = new Date();
            Date expiration = new Date(today.getTime() + (4 * 3600 * 1000));

            user = admin;
            user.setResetCode(resetCode);
            user.setResetExpiration(expiration.toString());

            adminRepo.save((Admin)user);

            passwordResetMessage="\nPassword reset has been accepted.\n Please use the following code before " + user.getResetExpiration()+" to change your password.\n\n code: "+resetCode;
//            SendDirectEmailNotificationRequest request1=new SendDirectEmailNotificationRequest(passwordResetMessage,properties);
//            rabbit.convertAndSend("NotificationsEXCHANGE", "RK_SendDirectEmailNotification", request1);
//            try {
//                rabbit.convertAndSend("NotificationsEXCHANGE", "RK_SendDirectEmailNotification", request1);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
            Map<String, Object> parts = new HashMap<String, Object>();
            parts.put("message", passwordResetMessage);
            parts.put("properties", properties);
            ResponseEntity<SendDirectEmailNotificationResponse> getStoresResponseEntity = restTemplate.postForEntity("http://"+notificationHost+":"+notificationPort+"/notification/sendDirectEmailNotification", parts, SendDirectEmailNotificationResponse.class);

        }

        return new ResetPasswordResponse(resetCode, "Account type given does not exist - Could not reset password", false);
    }

    @Override
    public FinalisePasswordResetResponse finalisePasswordReset(FinalisePasswordResetRequest request) throws UserException{

        User user;
        String email;
        String userType;
        String password;
        SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);

        if(request == null){
            throw new InvalidRequestException("ResetPassword Request is null - Could not final password reset");
        }

        if(request.getEmail() == null){
            throw new InvalidRequestException("Email in request object is null - Could not finalise password reset");
        }

        if(request.getUserType() == null){
            throw new InvalidRequestException("Account Type in request object is null - Could not finalise password reset");
        }

        if(request.getResetCode() == null){
            throw new InvalidRequestException("Reset code in request object is null - Could not finalise password reset");
        }

        if(request.getPassword() == null){
            throw new InvalidRequestException("Password in request object is null - Could not finalise password reset");
        }

        email = request.getEmail();
        userType = request.getUserType();
        password = request.getPassword();

        if(!emailRegex(email)){
            return new FinalisePasswordResetResponse("Invalid email - Could not finalise password reset", false, new Date());
        }

        if(!passwordRegex(password)){
            return new FinalisePasswordResetResponse("invalid password - Could not finalise password reset", false, new Date());
        }

        if(userType.equals("CUSTOMER")){

            Customer customer = null;
            try {
                customer = customerRepo.findByEmail(email).orElse(null);
            }catch (Exception e){}

            if(customer == null){
                return new FinalisePasswordResetResponse("Could not find customer with email - Could not finalise password reset", false, new Date());
            }

            user = customer;

            try {
                Date expiryDate = format.parse(user.getResetExpiration());
                if (!new Date().before(expiryDate)){
                    return new FinalisePasswordResetResponse("Reset code expired - Could not finalise password reset", false, new Date());
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            if(!request.getResetCode().equals(user.getResetCode())){
                return new FinalisePasswordResetResponse("Invalid Reset code given - Could not finalise password reset", false, new Date());
            }

            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(15);
            String passwordHashed = passwordEncoder.encode(password);
            user.setPassword(passwordHashed);

            customerRepo.save((Customer) user);

//            Notification emailNotification = ServiceSelector.
            return new FinalisePasswordResetResponse("Password reset successful", true, new Date());
        }else if(userType.equals("SHOPPER")){

            Shopper shopper = null;

            try {
                shopper = shopperRepo.findByEmail(email).orElse(null);
            }catch (Exception e){}

            if(shopper == null){
                return new FinalisePasswordResetResponse("Could not find shopper with email - Could not final password reset", false, new Date());
            }

            user = shopper;

            try {
                Date expiryDate = format.parse(user.getResetExpiration());
                if (!new Date().before(expiryDate)){
                    return new FinalisePasswordResetResponse("Reset code expired - Could not final password reset", false, new Date());
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            if(request.getResetCode().equals(user.getResetCode())){
                return new FinalisePasswordResetResponse("Invalid Reset code given - Could not final password reset", false, new Date());
            }

            password = hashPassword(password);
            user.setPassword(password);

            shopperRepo.save((Shopper) user);

//            Notification emailNotification = ServiceSelector.
            return new FinalisePasswordResetResponse("Password reset successful", true, new Date());
        }else if(userType.equals("ADMIN")){
            Admin admin = adminRepo.findAdminByEmail(email);

            if(admin == null){
                return new FinalisePasswordResetResponse("Could not find admin with email - Could not finalise password reset", false, new Date());
            }

            user = admin;

            try {
                Date expiryDate = format.parse(user.getResetExpiration());
                if (!new Date().before(expiryDate)){
                    return new FinalisePasswordResetResponse("Reset code expired - Could not finalise password reset", false, new Date());
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            if(request.getResetCode().equals(user.getResetCode())){
                return new FinalisePasswordResetResponse("Invalid Reset code given - Could not finalise password reset", false, new Date());
            }

            password = hashPassword(password);
            user.setPassword(password);

            adminRepo.save((Admin) user);

//            Notification emailNotification = ServiceSelector.
            return new FinalisePasswordResetResponse("Password reset successful", true, new Date());
        }else if(userType.equals("DRIVER")){
            Driver driver = driverRepo.findDriverByEmail(email);

            if(driver == null){
                return new FinalisePasswordResetResponse("Could not find driver with email - Could not finalise password reset", false, new Date());
            }

            user = driver;

            try {
                Date expiryDate = format.parse(user.getResetExpiration());
                if (!new Date().before(expiryDate)){
                    return new FinalisePasswordResetResponse("Reset code expired - Could not finalise password reset", false, new Date());
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            if(request.getResetCode().equals(user.getResetCode())){
                return new FinalisePasswordResetResponse("Invalid Reset code given - Could not finalise password reset", false, new Date());
            }

            password = hashPassword(password);
            user.setPassword(password);

            driverRepo.save((Driver) user);

//            Notification emailNotification = ServiceSelector.
            return new FinalisePasswordResetResponse("Password reset successful", true, new Date());
        }

        return new FinalisePasswordResetResponse("Invalid account type - could not finalise password reset", false, new Date());
    }

    @Override
    public ResendActivationCodeResponse resendActivationCode(ResendActivationCodeRequest request) throws UserException {
        return null;
    }

    @Override
    public SetCurrentLocationResponse setCurrentLocation(SetCurrentLocationRequest request) throws UserException{

        UUID driverID;
        Driver driver;
        Optional<Driver> driverOptional;
        GeoPoint geoPoint;

        if(request == null){
            throw new InvalidRequestException("SetCurrentLocationRequest is null - could not set current location");
        }

        if(request.getDriverID() == null){
            throw new InvalidRequestException("DriverID attribute is null - could not set current location");
        }

        if(request.getLongitude() == 0){
            throw new InvalidRequestException("Longitude attribute is null - could not set current location");
        }

        if(request.getLatitude() == 0){
            throw new InvalidRequestException("Latitude attribute is null - could not set current location");
        }

        if(request.getAddress() == null){
            throw new InvalidRequestException("Address attribute is null - could not set current location");
        }

        driverID = UUID.fromString(request.getDriverID());
        driverOptional = driverRepo.findById(driverID);

        if(driverOptional == null || !driverOptional.isPresent()){
            throw new DriverDoesNotExistException("Driver with driverID does not exist in database");
        }

        if(request.getAddress().equals("")){
            return new SetCurrentLocationResponse("address cannot be empty", false, new Date());
        }

        driver = driverOptional.get();

        geoPoint = new GeoPoint(request.getLatitude(), request.getLongitude(), request.getAddress());

        driver.setCurrentAddress(geoPoint);

        driverRepo.save(driver);

        return new SetCurrentLocationResponse("driver location successfully updated", true, new Date());
    }

    @Override
    public GetAdminsResponse getAdmins(GetAdminsRequest request) throws Exception{

        String message = "Admins successfully returned";
        List<Admin> users = new ArrayList<>();

        if(request == null) {
            throw new InvalidRequestException("GetAdmins request is null - could not return admins");
        }

        users.addAll(adminRepo.findAll());

        if(users.isEmpty()){
            message = "Could not retrieve Admins";
            return new GetAdminsResponse(users, false, message, new Date());
        }

        return new GetAdminsResponse(users, true, message, new Date());
    }

    @Override
    public GetCustomersResponse getCustomers(GetCustomersRequest request) throws Exception{

        String message = "Customers successfully returned";
        List<Customer> users = new ArrayList<>();

        if(request == null) {
            throw new InvalidRequestException("GetCustomers request is null - could not return customers");
        }

        users.addAll(customerRepo.findAll());

        if(users.isEmpty()){
            message = "Could not retrieve customers";
            return new GetCustomersResponse(users, false, message, new Date());
        }

        return new GetCustomersResponse(users, true, message, new Date());
    }

    @Override
    public GetDriversResponse getDrivers(GetDriversRequest request) throws Exception{

        String message = "Drivers successfully returned";
        List<Driver> users = new ArrayList<>();

        if(request == null) {
            throw new InvalidRequestException("GetDrivers request is null - could not return drivers");
        }

        users.addAll(driverRepo.findAll());

        if(users.isEmpty()){
            message = "Could not retrieve drivers";
            return new GetDriversResponse(users, false, message, new Date());
        }

        return new GetDriversResponse(users, true, message, new Date());
    }

    @Override
    public GetShoppersResponse getShoppers(GetShoppersRequest request) throws Exception{

        String message = "Shoppers successfully returned";
        List<Shopper> users = new ArrayList<>();

        if(request == null) {
            throw new InvalidRequestException("GetShoppers request is null - could not return shoppers");
        }

        users.addAll(shopperRepo.findAll());

        if(users.isEmpty()){
            message = "Could not retrieve shoppers";
            return new GetShoppersResponse(users, false, message, new Date());
        }

        return new GetShoppersResponse(users, true, message, new Date());
    }

    @Override
    public GetGroceryListsResponse getGroceryLists(GetGroceryListsRequest request) throws UserException{

        Customer customer = null;
        String message;
        if(request == null){
            throw new InvalidRequestException("GetGroceryList request is null - could not return groceryList");
        }

        currentUser=new CurrentUser();

        customer = customerRepo.findByEmail(currentUser.getEmail()).orElse(null);

        if(customer == null){
            throw new CustomerDoesNotExistException("Customer does not exist");
        }

        message = "Grocery list successfully retrieved";
        return new GetGroceryListsResponse(customer.getGroceryLists(), true, new Date(), message);
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

    /**
     *
     * @param request is used to bring in:
     *                userID - user ID to fetch the corresponding shopper from the database
     *  GetCustomerUUID should:
     *                1.Check if request object is not null else throw InvalidRequestException
     *                2.Check if request object's ID is null, else throw InvalidRequestException
     *                3.Check if customer exists in database, else throw CustomerDoesNotExist
     *                5.Return response object
     * Request object (GetCustomerUUIDRequest)
     * {
     *               "userID": "7fa06899-98e5-43a0-b4d0-9dbc8e29f74a"
     *
     * }
     *
     * Response object (GetCustomerUUIDResponse)
     * {
     *                "customer": customer
     *                "timeStamp":"2021-01-05T11:50:55"
     *                "message":"Customer with corresponding user id was returned"
     * }
     * @return
     * @throws InvalidRequestException
     * @throws CustomerDoesNotExistException
     */
    @Override
    public GetCustomerByUUIDResponse getCustomerByUUID(GetCustomerByUUIDRequest request) throws InvalidRequestException, CustomerDoesNotExistException {
        GetCustomerByUUIDResponse response=null;
        if(request != null){

            if(request.getUserID()==null){
                throw new InvalidRequestException("UserID is null in GetCustomerByUUIDRequest request - could not return customer entity");
            }

            Customer customer=null;
            try {
                customer = customerRepo.findById(request.getUserID()).orElse(null);
            }catch(Exception e){
                throw new CustomerDoesNotExistException("User with ID does not exist in repository - could not get customer entity");
            }
            if(customer==null) {
                throw new CustomerDoesNotExistException("User with ID does not exist in repository - could not get customer entity");
            }
            response=new GetCustomerByUUIDResponse(customer, Calendar.getInstance().getTime(),"Customer entity with corresponding user id was returned");
        }
        else{
            throw new InvalidRequestException("GetCustomerByUUID request is null - could not return customer entity");
        }
        return response;
    }

    @Override
    public GetDriverByUUIDResponse getDriverByUUID(GetDriverByUUIDRequest request) throws UserException {
        GetDriverByUUIDResponse response=null;
        if(request != null){

            if(request.getUserID()==null){
                throw new InvalidRequestException("UserID is null in GetDriverByUUIDRequest request - could not return driver entity");
            }

            Driver driver=null;
            try {
                driver = driverRepo.findById(request.getUserID()).orElse(null);
            }catch(Exception e){
                throw new CustomerDoesNotExistException("User with ID does not exist in repository - could not get driver entity");
            }
            if(driver==null) {
                throw new CustomerDoesNotExistException("User with ID does not exist in repository - could not get driver entity");
            }
            response=new GetDriverByUUIDResponse(driver, Calendar.getInstance().getTime(),"Driver entity with corresponding user id was returned");
        }
        else{
            throw new InvalidRequestException("GetDriverByUUID request is null - could not return driver entity");
        }
        return response;
    }

    @Override
    public GetAdminByUUIDResponse getAdminByUUID(GetAdminByUUIDRequest request) throws UserException {
        GetAdminByUUIDResponse response=null;
        if(request != null){

            if(request.getUserID()==null){
                throw new InvalidRequestException("UserID is null in GetAdminByUUIDRequest request - could not return admin entity");
            }

            Admin admin=null;
            try {
                admin = adminRepo.findById(request.getUserID()).orElse(null);
            }catch(Exception e){
                throw new CustomerDoesNotExistException("User with ID does not exist in repository - could not get admin entity");
            }
            if(admin==null) {
                throw new CustomerDoesNotExistException("User with ID does not exist in repository - could not get admin entity");
            }
            response=new GetAdminByUUIDResponse(admin, Calendar.getInstance().getTime(),"Admin entity with corresponding user id was returned");
        }
        else{
            throw new InvalidRequestException("GetAdminByUUID request is null - could not return admin entity");
        }
        return response;
    }

    /**
     *
     * @param request is used to bring in:
     *                driverID - driver ID to fetch the corresponding driver from the database
     *                rating- rating given by the customer
     *  driverSetRating should:
     *                1.Check if request object is not null else throw InvalidRequestException
     *                2.Check if request object's ID is null, else throw InvalidRequestException
     *                3.Check if driver exists in database, else throw DriverDoesNotExist
     *                4.Calculate the rating of the driver and set it
     *                5.Return response object
     * Request object (DriverSetRatingRequest)
     * {
     *               "driverID": "7fa06899-98e5-43a0-b4d0-9dbc8e29f74a",
     *               "rating": 4
     *
     * }
     *
     * Response object (DriverSetRatingResponse)
     * {
     *                "success": true
     *                "timeStamp":"2021-01-05T11:50:55"
     *                "message":"DriverSetRating request is null - could not set rating"
     * }
     * @return
     * @throws InvalidRequestException
     * @throws DriverDoesNotExistException
     */
    @Override
    public DriverSetRatingResponse driverSetRating(DriverSetRatingRequest request) throws InvalidRequestException, DriverDoesNotExistException
    {
        DriverSetRatingResponse response=null;
        if(request != null){

            if(request.getDriverID()==null){
                throw new InvalidRequestException("Driver ID is null in request object - could not find driver");
            }

            if(request.getRating()==-1){
                response = new DriverSetRatingResponse(false, Calendar.getInstance().getTime(), "No rating was set");
            }
            else
            {
                Driver driver=null;
                try {
                    driver = driverRepo.findById(request.getDriverID()).orElse(null);
                }catch(Exception e){
                    throw new DriverDoesNotExistException("User with ID does not exist in repository - could not get driver entity");
                }
                if(driver==null) {
                    throw new DriverDoesNotExistException("User with ID does not exist in repository - could not get driver entity");
                }

                double totalRatings= request.getRating()+driver.getTotalRatings();
                driver.setTotalRatings(totalRatings);
                int numRatings= driver.getNumberOfRatings()+1;
                driver.setNumberOfRatings(numRatings);
                double newRating= totalRatings/numRatings;
                driver.setRating(Math.round(newRating*100.0)/100.0);
                driverRepo.save(driver);

                response = new DriverSetRatingResponse(true, Calendar.getInstance().getTime(), "Rating complete");

            }
            return response;
        }
        else{
            throw new InvalidRequestException("DriverSetRating request is null - could not set rating");
        }
    }

    @Override
    public GetCustomerByEmailResponse getCustomerByEmail(GetCustomerByEmailRequest request) throws InvalidRequestException, CustomerDoesNotExistException {
        if(request.getEmail()==null){
            throw new InvalidRequestException("Email can't be null");
        }

        Customer customer=customerRepo.findByEmail(request.getEmail()).orElse(null);
        if(customer==null){
            throw new CustomerDoesNotExistException("Customer with email does not exist");
        }else {
            return new GetCustomerByEmailResponse(customer, true);
        }
    }

    @Override
    public GetShopperByEmailResponse getShopperByEmail(GetShopperByEmailRequest request) throws InvalidRequestException, ShopperDoesNotExistException {
        if(request.getEmail()==null){
            throw new InvalidRequestException("Email can't be null");
        }
        Shopper shopper= shopperRepo.findByEmail(request.getEmail()).orElse(null);
        if(shopper==null){
            throw new ShopperDoesNotExistException("Shopper with email does not exist");
        }else{
            return new GetShopperByEmailResponse(shopper,true);
        }
    }

    @Override
    public GetDriverByEmailResponse getDriverByEmail(GetDriverByEmailRequest request) throws InvalidRequestException, DriverDoesNotExistException {
        if(request.getEmail()==null){
            throw new InvalidRequestException("Email can't be null");
        }
        Driver driver=null;
        try {
            driver = driverRepo.findDriverByEmail(request.getEmail());
        }catch (NullPointerException e){
            driver=null;
        }
        if(driver==null){
            throw new DriverDoesNotExistException("Driver with email does not exist");
        }else{
            return new GetDriverByEmailResponse(driver,true);
        }

    }

    @Override
    public GetAdminByEmailResponse getAdminByEmail(GetAdminByEmailRequest request) throws InvalidRequestException, AdminDoesNotExistException {
        if(request.getEmail()==null){
            throw new InvalidRequestException("Email can't be null");
        }
        Admin admin=null;
        try {
            admin = adminRepo.findAdminByEmail(request.getEmail());
        }catch (NullPointerException e){
            admin=null;
        }
        if(admin==null){
            throw new AdminDoesNotExistException("Driver with email does not exist");
        }else{
            return new GetAdminByEmailResponse(admin,true);
        }
    }

    @Override
    public void saveDriver(SaveDriverToRepoRequest request) throws InvalidRequestException {
        if (request == null){
            throw new InvalidRequestException("Null request object.");
        }
        if (request.getDriver() == null){
            throw new InvalidRequestException("Null parameters.");
        }
        driverRepo.save(request.getDriver());
    }

    @Override
    public void saveShopper(SaveShopperToRepoRequest request) throws InvalidRequestException {
        if (request == null){
            throw new InvalidRequestException("Null request object.");
        }
        if (request.getShopper() == null){
            throw new InvalidRequestException("Null parameters.");
        }
        shopperRepo.save(request.getShopper());
    }
}