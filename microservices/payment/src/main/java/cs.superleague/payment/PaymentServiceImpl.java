package cs.superleague.payment;

import com.itextpdf.text.*;
import cs.superleague.delivery.dataclass.Delivery;
import cs.superleague.delivery.responses.CreateDeliveryResponse;
import cs.superleague.delivery.responses.GetDeliveryByUUIDResponse;
import cs.superleague.integration.dataclass.UserType;
import cs.superleague.integration.security.CurrentUser;
import cs.superleague.payment.dataclass.*;
import cs.superleague.payment.exceptions.InvalidRequestException;
import cs.superleague.payment.exceptions.NotAuthorisedException;
import cs.superleague.payment.exceptions.OrderDoesNotExist;
import cs.superleague.payment.exceptions.PaymentException;
import cs.superleague.payment.repos.CartItemRepo;
import cs.superleague.payment.repos.InvoiceRepo;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.payment.repos.TransactionRepo;
import cs.superleague.payment.requests.*;
import cs.superleague.payment.responses.*;
import cs.superleague.recommendation.requests.AddRecommendationRequest;
import cs.superleague.recommendation.requests.RemoveRecommendationRequest;
import cs.superleague.shopping.requests.AddToFrontOfQueueRequest;
import cs.superleague.shopping.requests.AddToQueueRequest;
import cs.superleague.shopping.responses.GetStoreByUUIDResponse;
import cs.superleague.shopping.responses.RemoveQueuedOrderResponse;
import cs.superleague.user.dataclass.Customer;
import cs.superleague.user.requests.RemoveProblemFromRepoRequest;
import cs.superleague.user.responses.GetCustomerByEmailResponse;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service("paymentServiceImpl")
public class PaymentServiceImpl implements PaymentService {
    private final OrderRepo orderRepo;
    private final InvoiceRepo invoiceRepo;
    private final TransactionRepo transactionRepo;
    private final RabbitTemplate rabbitTemplate;
    private final RestTemplate restTemplate;
    private final CartItemRepo cartItemRepo;
    @Value("${shoppingHost}")
    private String shoppingHost;
    @Value("${shoppingPort}")
    private String shoppingPort;
    @Value("${userHost}")
    private String userHost;
    @Value("${userPort}")
    private String userPort;
    @Value("${deliveryHost}")
    private String deliveryHost;
    @Value("${deliveryPort}")
    private String deliveryPort;

    @Autowired
    public PaymentServiceImpl(OrderRepo orderRepo, InvoiceRepo invoiceRepo, TransactionRepo transactionRepo, RabbitTemplate rabbitTemplate, RestTemplate restTemplate, CartItemRepo cartItemRepo) throws InvalidRequestException {
        this.orderRepo = orderRepo;
        this.invoiceRepo = invoiceRepo;
        this.transactionRepo = transactionRepo;
        this.rabbitTemplate = rabbitTemplate;
        this.restTemplate = restTemplate;
        this.cartItemRepo = cartItemRepo;
    }


    /**
     * What to do
     *
     * @param request is used to bring in:
     *                - userID - the cs.superleague.user ID creating the order
     *                - list of items - items order should hold
     *                - discount - discount for the order if applicable, if not the discount will be 0
     *                - storeID - the Store the order is being created with
     *                - OrderType - whether the order is for collection or cs.superleague.delivery
     *                - DeliveryAddress - this is the address the order will be delivered to, the address of cs.superleague.user
     *                - StoreAddress - address of store the order is created at
     *                <p>
     *                SubmitOrder should do the following:
     *                - Check the request object is not null and all corresponding parameters else throw InvalidRequestException
     *                - Add up cost of items and subtract total for total order price
     *                - If order with same ID already exists in database, then return object without success
     *                - If the order is created successfully, then order gets added to order repository and success
     *                <p>
     *                Request object (SubmitOrderRequest)
     *                {
     *                "userID":"8b337604-b0f6-11eb-8529-0242ac130003"
     *                "listOfItems": {{"ProductID":"12345","name":"item1"...}, ...}
     *                "discount": "30.50"
     *                "storeID": "8b337604-b0f6-11eb-8529-0242ac130023"
     *                "orderType": "OrderType.DELIVERY"
     *                "DeliveryAddress": {"geoID":"3847593","latitude":"30.49","longitude":"24.34"}
     *                "StoreAddress": {"geoID":"3847593","latitude":"30.49","longitude":"24.34"}
     *                }
     *                <p>
     *                Response object (SubmitOrderResponse)
     *                {
     *                success: true // boolean
     *                timestamp: Thu Dec 05 09:29:39 UTC 1996 // Date
     *                message: "Order created successfully"
     *                order:  order / orderObject
     *                <p>
     *                }
     * @return
     * @throws PaymentException
     * @throws InvalidRequestException
     */
    @Override
    public SubmitOrderResponse submitOrder(SubmitOrderRequest request) throws PaymentException, InterruptedException, URISyntaxException {
        SubmitOrderResponse response = null;
        AtomicReference<Double> totalCost = new AtomicReference<>((double) 0);

        boolean invalidReq = false;
        String invalidMessage = "";

        UUID customerID = null;

        GetStoreByUUIDResponse shopOne = null;
        GetStoreByUUIDResponse shopTwo = null;
        GetStoreByUUIDResponse shopThree = null;
        if (request != null) {
            if (request.getListOfItems() == null) {
                invalidReq = true;
                invalidMessage = ("List of items cannot be null in request object - order unsuccessfully created.");
            } else if (request.getDiscount() == null) {
                invalidReq = true;
                invalidMessage = ("Discount cannot be null in request object - order unsuccessfully created.");
            } else if (request.getStoreIDOne() == null) {
                invalidReq = true;
                invalidMessage = ("Store ID cannot be null in request object - order unsuccessfully created.");
            } else if (request.getOrderType() == null) {
                invalidReq = true;
                invalidMessage = ("Order type cannot be null in request object - order unsuccessfully created.");
            } else if (request.getLongitude() == null) {
                invalidReq = true;
                invalidMessage = ("Longitude cannot be null in request object - order unsuccessfully created.");
            } else if (request.getLatitude() == null) {
                invalidReq = true;
                invalidMessage = ("Latitude cannot be null in request object - order unsuccessfully created.");
            } else if (request.getAddress() == null) {
                invalidReq = true;
                invalidMessage = ("Address cannot be null in request object - order unsuccessfully created.");
            }

            if (invalidReq) {
                throw new InvalidRequestException(invalidMessage);
            }

            String stringUri = "http://" + shoppingHost + ":" + shoppingPort + "/shopping/getStoreByUUID";
            URI uri = new URI(stringUri);

            Map<String, Object> parts = new HashMap<String, Object>();
            parts.put("StoreID", request.getStoreIDOne().toString());
            ResponseEntity<GetStoreByUUIDResponse> getStoreByUUIDResponseResponseEntity = restTemplate
                    .postForEntity(uri, parts, GetStoreByUUIDResponse.class);
            shopOne = getStoreByUUIDResponseResponseEntity.getBody();

            if (shopOne != null) {
                if (shopOne.getStore().getStoreLocation() == null) {
                    invalidReq = true;
                    invalidMessage = ("Store Address GeoPoint cannot be null in request object - order unsuccessfully created.");
                }

                if (!shopOne.getStore().getOpen()) {
                    invalidReq = true;
                    invalidMessage = ("Store is currently closed - could not create order");
                }
            }

            if (invalidReq) {
                throw new InvalidRequestException(invalidMessage);
            }
            if (request.getStoreIDTwo() != null){
                stringUri = "http://" + shoppingHost + ":" + shoppingPort + "/shopping/getStoreByUUID";
                uri = new URI(stringUri);

                parts = new HashMap<String, Object>();
                parts.put("StoreID", request.getStoreIDTwo().toString());
                getStoreByUUIDResponseResponseEntity = restTemplate
                        .postForEntity(uri, parts, GetStoreByUUIDResponse.class);
                shopTwo = getStoreByUUIDResponseResponseEntity.getBody();

                if (shopTwo != null) {
                    if (shopTwo.getStore().getStoreLocation() == null) {
                        invalidReq = true;
                        invalidMessage = ("Store Address GeoPoint cannot be null in request object - order unsuccessfully created.");
                    }

                    if (!shopTwo.getStore().getOpen()) {
                        invalidReq = true;
                        invalidMessage = ("Store is currently closed - could not create order");
                    }
                }

                if (invalidReq) {
                    throw new InvalidRequestException(invalidMessage);
                }
            }
            if (request.getStoreIDThree() != null){
                stringUri = "http://" + shoppingHost + ":" + shoppingPort + "/shopping/getStoreByUUID";
                uri = new URI(stringUri);

                parts = new HashMap<String, Object>();
                parts.put("StoreID", request.getStoreIDThree().toString());
                getStoreByUUIDResponseResponseEntity = restTemplate
                        .postForEntity(uri, parts, GetStoreByUUIDResponse.class);
                shopThree = getStoreByUUIDResponseResponseEntity.getBody();

                if (shopThree != null) {
                    if (shopThree.getStore().getStoreLocation() == null) {
                        invalidReq = true;
                        invalidMessage = ("Store Address GeoPoint cannot be null in request object - order unsuccessfully created.");
                    }

                    if (!shopThree.getStore().getOpen()) {
                        invalidReq = true;
                        invalidMessage = ("Store is currently closed - could not create order");
                    }
                }

                if (invalidReq) {
                    throw new InvalidRequestException(invalidMessage);
                }
            }
            CurrentUser currentUser = new CurrentUser();

            parts = new HashMap<>();
            parts.put("email", currentUser.getEmail());

            stringUri = "http://" + userHost + ":" + userPort + "/user/getCustomerByEmail";
            uri = new URI(stringUri);

            ResponseEntity<GetCustomerByEmailResponse> useCaseResponseEntity = restTemplate.postForEntity(
                    uri, parts, GetCustomerByEmailResponse.class);

            GetCustomerByEmailResponse getCustomerByEmailResponse = useCaseResponseEntity.getBody();
            Customer customer = getCustomerByEmailResponse.getCustomer();

            assert customer != null;

            customerID = customer.getCustomerID();

            double discount = request.getDiscount();

            /* Get total cost of order*/
            AtomicReference<Double> finalTotalCost = totalCost;
            request.getListOfItems().stream().parallel().forEach(item -> {
                int quantity = item.getQuantity();
                double itemPrice = item.getPrice();
                for (int j = 0; j < quantity; j++) {
                    finalTotalCost.updateAndGet(v -> v + itemPrice);
                }
            });

            /* Take off discount */
            finalTotalCost.updateAndGet(v -> v - discount);

            //meant to use assign order request in shop - Mock Data
            UUID shopperID = null;

            //Mock Data - still have to find out how this is going to work
            Boolean requiresPharmacy = false;

            OrderType orderType = request.getOrderType();

            BigDecimal bd = BigDecimal.valueOf(Double.parseDouble(totalCost.toString()));
            bd = bd.setScale(2, RoundingMode.HALF_UP);
            double totalC = bd.doubleValue();

            GeoPoint customerLocation = new GeoPoint(request.getLatitude(), request.getLongitude(), request.getAddress());

            Order alreadyExists = null;
            UUID orderOneID = UUID.randomUUID();
            UUID orderTwoID = UUID.randomUUID();
            UUID orderThreeID = UUID.randomUUID();
            while (true) {
                try {
                    alreadyExists = orderRepo.findById(orderOneID).orElse(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (alreadyExists != null) {
                    orderOneID = UUID.randomUUID();
                } else {
                    break;
                }
            }
            while (true) {
                try {
                    alreadyExists = orderRepo.findById(orderTwoID).orElse(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (alreadyExists != null) {
                    orderTwoID = UUID.randomUUID();
                } else {
                    break;
                }
            }
            while (true) {
                try {
                    alreadyExists = orderRepo.findById(orderThreeID).orElse(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (alreadyExists != null) {
                    orderThreeID = UUID.randomUUID();
                } else {
                    break;
                }
            }

            assert shopOne != null;

            //Order o = new Order(orderID, customerID, request.getStoreID(), shopperID, new Date(), null, totalC, orderType, OrderStatus.AWAITING_PAYMENT, request.getListOfItems(), request.getDiscount(), customerLocation, shop.getStore().getStoreLocation(), requiresPharmacy);
            Order orderOne = new Order();
            orderOne.setOrderID(orderOneID);
            orderOne.setUserID(customerID);
            orderOne.setStoreID(request.getStoreIDOne());
            orderOne.setShopperID(shopperID);
            orderOne.setCreateDate(new Date());
            orderOne.setProcessDate(null);
            orderOne.setTotalCost(totalC);
            orderOne.setType(orderType);
            orderOne.setStatus(OrderStatus.AWAITING_PAYMENT);
            orderOne.setDiscount(request.getDiscount());
            orderOne.setDeliveryAddress(customerLocation);
            orderOne.setStoreAddress(shopOne.getStore().getStoreLocation());
            orderOne.setRequiresPharmacy(requiresPharmacy);

            Order orderTwo = new Order();
            if (request.getStoreIDTwo() != null){
                orderTwo.setOrderID(orderTwoID);
                orderTwo.setUserID(customerID);
                orderTwo.setStoreID(request.getStoreIDTwo());
                orderTwo.setShopperID(shopperID);
                orderTwo.setCreateDate(new Date());
                orderTwo.setProcessDate(null);
                orderTwo.setTotalCost(totalC);
                orderTwo.setType(orderType);
                orderTwo.setStatus(OrderStatus.AWAITING_PAYMENT);
                //Not sure about discount across orders
                orderTwo.setDiscount(request.getDiscount());
                orderTwo.setDeliveryAddress(customerLocation);
                orderTwo.setStoreAddress(shopTwo.getStore().getStoreLocation());
                orderTwo.setRequiresPharmacy(requiresPharmacy);
            }
            Order orderThree = new Order();
            if (request.getStoreIDTwo() != null){
                orderThree.setOrderID(orderThreeID);
                orderThree.setUserID(customerID);
                orderThree.setStoreID(request.getStoreIDThree());
                orderThree.setShopperID(shopperID);
                orderThree.setCreateDate(new Date());
                orderThree.setProcessDate(null);
                orderThree.setTotalCost(totalC);
                orderThree.setType(orderType);
                orderThree.setStatus(OrderStatus.AWAITING_PAYMENT);
                //Not sure about discount across orders
                orderThree.setDiscount(request.getDiscount());
                orderThree.setDeliveryAddress(customerLocation);
                orderThree.setStoreAddress(shopThree.getStore().getStoreLocation());
                orderThree.setRequiresPharmacy(requiresPharmacy);
            }

            if (orderOne != null) {
                if (shopOne.getStore().getOpen() == true) {
                    UUID deliveryID = null;
                    if (orderRepo != null) {
                        orderRepo.save(orderOne);
                        if (request.getStoreIDTwo() != null){
                            orderRepo.save(orderTwo);
                        }
                        if (request.getStoreIDThree() != null){
                            orderRepo.save(orderThree);
                        }
                        //Setting total cost of each item
                        List<CartItem> cartItems = request.getListOfItems();
                        List<CartItem> orderOneCartItems = new ArrayList<>();
                        List<CartItem> orderTwoCartItems = new ArrayList<>();
                        List<CartItem> orderThreeCartItems = new ArrayList<>();
                        double orderOneCost = 0.0;
                        double orderTwoCost = 0.0;
                        double orderThreeCost = 0.0;
                        for (int k = 0; k < cartItems.size(); k++) {
                            UUID cartID = UUID.randomUUID();
                            while (cartItemRepo.existsById(cartID)) {
                                cartID = UUID.randomUUID();
                            }
//                            cartItems.get(k).setStoreID(request.getStoreIDOne());
                            if (cartItems.get(k).getStoreID().compareTo(request.getStoreIDOne()) == 0){
                                cartItems.get(k).setOrderID(orderOneID);
                                cartItems.get(k).setCartItemNo(cartID);
                                cartItems.get(k).setTotalCost(cartItems.get(k).getPrice() * cartItems.get(k).getQuantity());
                                orderOneCost = cartItems.get(k).getTotalCost() + orderOneCost;
                                orderOneCartItems.add(cartItems.get(k));
                            } else if (cartItems.get(k).getStoreID().compareTo(request.getStoreIDTwo()) == 0){
                                cartItems.get(k).setOrderID(orderTwoID);
                                cartItems.get(k).setCartItemNo(cartID);
                                cartItems.get(k).setTotalCost(cartItems.get(k).getPrice() * cartItems.get(k).getQuantity());
                                orderTwoCost = cartItems.get(k).getTotalCost() + orderTwoCost;
                                orderTwoCartItems.add(cartItems.get(k));
                            } else if (cartItems.get(k).getStoreID().compareTo(request.getStoreIDThree()) == 0){
                                cartItems.get(k).setOrderID(orderThreeID);
                                cartItems.get(k).setCartItemNo(cartID);
                                cartItems.get(k).setTotalCost(cartItems.get(k).getPrice() * cartItems.get(k).getQuantity());
                                orderThreeCost = cartItems.get(k).getTotalCost() + orderThreeCost;
                                orderThreeCartItems.add(cartItems.get(k));
                            }
                        }
                        orderOne.setTotalCost(orderOneCost);
                        orderOne.setCartItems(orderOneCartItems);
                        if (request.getStoreIDTwo() != null){
                            orderTwo.setTotalCost(orderTwoCost);
                            orderTwo.setCartItems(orderTwoCartItems);
                        }
                        if (request.getStoreIDThree() != null){
                            orderThree.setTotalCost(orderThreeCost);
                            orderThree.setCartItems(orderThreeCartItems);
                        }

                        if (cartItemRepo != null) {
                            cartItemRepo.saveAll(cartItems);
                        }
                        orderRepo.save(orderOne);
                        parts = new HashMap<>();
                        parts.put("orderID", orderOne.getOrderID().toString());
                        parts.put("customerID", orderOne.getUserID().toString());
                        parts.put("storeID", orderOne.getStoreID().toString());
                        parts.put("timeOfDelivery", null);
                        parts.put("placeOfDelivery", orderOne.getDeliveryAddress());
                        String strURL = "http://" + deliveryHost + ":" + deliveryPort + "/delivery/createDelivery";
                        URI uri2 = new URI(strURL);

                        ResponseEntity<CreateDeliveryResponse> createDeliveryResponseResponseEntity = restTemplate.postForEntity(uri2, parts, CreateDeliveryResponse.class);
                        CreateDeliveryResponse createDeliveryResponse = createDeliveryResponseResponseEntity.getBody();

                        if (createDeliveryResponse != null) {
                            deliveryID = createDeliveryResponse.getDeliveryID();
                        }
                        if (request.getStoreIDTwo() != null){
                            orderRepo.save(orderTwo);
                            parts = new HashMap<>();
                            parts.put("orderID", orderTwo.getOrderID().toString());
                            parts.put("customerID", orderTwo.getUserID().toString());
                            parts.put("storeID", orderTwo.getStoreID().toString());
                            parts.put("timeOfDelivery", null);
                            parts.put("placeOfDelivery", orderTwo.getDeliveryAddress());

                            createDeliveryResponseResponseEntity = restTemplate.postForEntity(uri2, parts, CreateDeliveryResponse.class);
                            createDeliveryResponse = createDeliveryResponseResponseEntity.getBody();
                        }
                        if (request.getStoreIDThree() != null){
                            orderRepo.save(orderThree);
                            parts = new HashMap<>();
                            parts.put("orderID", orderThree.getOrderID().toString());
                            parts.put("customerID", orderThree.getUserID().toString());
                            parts.put("storeID", orderThree.getStoreID().toString());
                            parts.put("timeOfDelivery", null);
                            parts.put("placeOfDelivery", orderThree.getDeliveryAddress());

                            createDeliveryResponseResponseEntity = restTemplate.postForEntity(uri2, parts, CreateDeliveryResponse.class);
                            createDeliveryResponse = createDeliveryResponseResponseEntity.getBody();
                        }
                        List<String> productIDs = new ArrayList<>();
                        for (CartItem item : request.getListOfItems()) {
                            productIDs.add(item.getProductID());
                        }

                        AddRecommendationRequest addRecommendationRequest = new AddRecommendationRequest(orderOne.getOrderID(), productIDs);
                        rabbitTemplate.convertAndSend("RecommendationEXCHANGE", "RK_AddRecommendation", addRecommendationRequest);
                    }
                    UUID finalOrderOneID = orderOneID;
                    UUID finalOrderTwoID = orderTwoID;
                    UUID finalOrderThreeID = orderThreeID;
                    if (request.getStoreIDTwo() == null){
                        orderTwo = null;
                    }
                    if (request.getStoreIDThree() == null){
                        orderThree = null;
                    }
                    Order finalOrderTwo = orderTwo;
                    Order finalOrderThree = orderThree;
                    new Thread(() -> {
                        CreateTransactionRequest createTransactionRequest1 = new CreateTransactionRequest(finalOrderOneID);
                        CreateTransactionRequest createTransactionRequest2 = null;
                        CreateTransactionRequest createTransactionRequest3 = null;
                        if (request.getStoreIDTwo() != null){
                            createTransactionRequest2 = new CreateTransactionRequest(finalOrderTwoID);
                        }
                        if (request.getStoreIDThree() != null){
                            createTransactionRequest3 = new CreateTransactionRequest(finalOrderThreeID);
                        }
                        try {
                            CreateTransactionResponse createTransactionResponse1 = createTransaction(createTransactionRequest1);
                            if (request.getStoreIDTwo() != null){
                                CreateTransactionResponse createTransactionResponse2 = createTransaction(createTransactionRequest2);
                            }
                            if (request.getStoreIDThree() != null){
                                CreateTransactionResponse createTransactionResponse3 = createTransaction(createTransactionRequest3);
                            }
                        } catch (PaymentException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        AddToQueueRequest addToQueueRequest = new AddToQueueRequest(orderOne);
                        rabbitTemplate.convertAndSend("ShoppingEXCHANGE", "RK_AddToQueue", addToQueueRequest);
                        if (request.getStoreIDTwo() != null){
                            addToQueueRequest = new AddToQueueRequest(finalOrderTwo);
                            rabbitTemplate.convertAndSend("ShoppingEXCHANGE", "RK_AddToQueue", addToQueueRequest);
                        }
                        if (request.getStoreIDThree() != null){
                            addToQueueRequest = new AddToQueueRequest(finalOrderThree);
                            rabbitTemplate.convertAndSend("ShoppingEXCHANGE", "RK_AddToQueue", addToQueueRequest);
                        }
                    }).start();
                    response = new SubmitOrderResponse(orderOne, orderTwo, orderThree, true, Calendar.getInstance().getTime(), "Order successfully created.", deliveryID);
                } else {
                    throw new InvalidRequestException("Store is currently closed - could not create order");
                }
            }
        } else {
            throw new InvalidRequestException("Invalid submit order request received - order unsuccessfully created.");
        }
        return response;
    }

    /**
     * WHAT TO DO: cancelOrder
     *
     * @param //request is used to bring in:
     *                  -   private UUID orderID // The order ID for the order that is to be cancelled
     *                  <p>
     *                  <p>
     *                  cancelOrder should: This function allows for an order to be cancelled based on the status of the particular order
     *                  1.Check that the request object and the orderID are not null, if they are an invalidOrderRequest is thrown\
     *                  2. Check if the orderID passed in exists in the DB if it does not, the OrderDoesNotExistException is thrown
     *                  3. Checks the order status of the order
     *                  3.1 if the order status is AWAITING_PAYMENT or PURCHASED the order can easily be cancelled without
     *                  charging the customer.
     *                  3.2 Once an order has reached the COLLECT status the customer will be charged a fee to cancel the order
     *                  3.3 Lastly if the order has changed to "collected", either by the driver or the customer the order
     *                  can no longer be cancelled
     *                  <p>
     *                  Request Object: (CancelOrderRequest)
     *                  {
     *                  "orderID":"8b337604-b0f6-11eb-8529-0242ac130003"
     *                  }
     *                  <p>
     *                  Response object: (CancelOrderResponse)
     *                  {
     *                  success: true // boolean
     *                  timestamp: Thu Dec 05 09:29:39 UTC 1996 // Date
     *                  message: "Cannot cancel an order that has been delivered/collected."
     *                  orders:  //List<Orders>
     *                  }
     * @return
     * @throws InvalidRequestException
     * @throws OrderDoesNotExist
     */

    @Override
    public CancelOrderResponse cancelOrder(CancelOrderRequest req) throws InvalidRequestException, OrderDoesNotExist, NotAuthorisedException, URISyntaxException {
        CancelOrderResponse response = null;
        Order order;
        double cancellationFee;
        String message;

        /*
         * AWAITING_PAYMENT - cancel
         * PURCHASED - cancel
         * COLLECT - charge
         * DELIVERY_COLLECTED
         * CUSTOMER_COLLECTED
         * DELIVERED
         */

        if (req != null) {

            order = getOrder(new GetOrderRequest(req.getOrderID())).getOrder();

            CurrentUser currentUser = new CurrentUser();

            List<Order> orders = orderRepo.findAll();

            if (order.getStatus() == OrderStatus.DELIVERED ||
                    order.getStatus() == OrderStatus.CUSTOMER_COLLECTED ||
                    order.getStatus() == OrderStatus.DELIVERY_COLLECTED) {
                message = "Cannot cancel an order that has been delivered/collected.";
                return new CancelOrderResponse(false, Calendar.getInstance().getTime(), message, orders);
            }

            if (currentUser.getUserType() == UserType.CUSTOMER) {
                if (order.getStatus().equals(OrderStatus.IN_QUEUE)) {
                    String stringURI = "http://" + shoppingHost + ":" + shoppingPort + "/shopping/removeQueuedOrder";
                    URI uri = new URI(stringURI);

                    Map<String, Object> parts = new HashMap<>();
                    parts.put("orderID", order.getOrderID());
                    parts.put("storeID", order.getStoreID());

                    ResponseEntity<RemoveQueuedOrderResponse> useCaseResponseEntity = restTemplate.postForEntity(
                            uri, parts, RemoveQueuedOrderResponse.class);
                }

                RemoveRecommendationRequest removeRecommendation = new RemoveRecommendationRequest(req.getOrderID());
                rabbitTemplate.convertAndSend("RecommendationEXCHANGE", "RK_RemoveRecommendation", removeRecommendation);
                if (order.getStatus() != OrderStatus.AWAITING_PAYMENT || order.getStatus() != OrderStatus.PURCHASED) {
                    cancellationFee = 1000;
                    message = "Order successfully cancelled. Customer has been charged " + cancellationFee;
                } else {
                    message = "Order has been successfully cancelled";
                }
                order.setStatus(OrderStatus.CANCELLED);
                orderRepo.save(order);

                response = new CancelOrderResponse(true, Calendar.getInstance().getTime(), message, orders);
            } else if (currentUser.getUserType() == UserType.SHOPPER) {
                cancellationFee = 1000;
                message = "Order successfully cancelled. Shopper has been charged " + cancellationFee;
                response = new CancelOrderResponse(true, Calendar.getInstance().getTime(), message, orders);
                order.setStatus(OrderStatus.IN_QUEUE);
                orderRepo.save(order);
                AddToFrontOfQueueRequest addToFrontOfQueueRequest = new AddToFrontOfQueueRequest(order);
                rabbitTemplate.convertAndSend("ShoppingEXCHANGE", "RK_AddToFrontOfQueue", addToFrontOfQueueRequest);
            }
//            String stringURI = "http://"+userHost+":"+userPort+"/user/getCustomerByEmail";
//            URI uri = new URI(stringURI);
//
//            Map<String, Object> parts = new HashMap<>();
//            parts.put("email", currentUser.getEmail());
//
//            ResponseEntity<GetCustomerByEmailResponse> useCaseResponseEntity = restTemplate.postForEntity(
//                    uri, parts, GetCustomerByEmailResponse.class);

//            GetCustomerByEmailResponse getCustomerByEmailResponse = useCaseResponseEntity.getBody();
//            Customer customer = getCustomerByEmailResponse.getCustomer();

//            if (customer == null || customer.getCustomerID() == null ||
//                    !customer.getCustomerID().equals(order.getUserID())) {
//                throw new NotAuthorisedException("Not Authorised to update an order you did not place.");
//            }


            // remove Order from DB.
//            orderRepo.delete(order);


            //orders = orderRepo.findAll();

            // refund customers order total - cancellation fee

        } else {
            throw new InvalidRequestException("request object cannot be null - couldn't cancel order");
        }
        return response;
    }

    /**
     * WHAT TO DO: updateOrder
     *
     * @param request is used to bring in:
     *                - userID - the user ID of person who placed the order
     *                - orderID - the unique identifier of the order placed
     *                - listOfItems - list of items in the order of object type Item
     *                - discount - the amount of the discount
     *                - orderType - the type of order it is, whether it is a delivery or collection
     *                - deliveryAddress - the GeoPoint address of the store where order is going to be delivered to.
     *                <p>
     *                updateOrder should:
     *                - check that the request object passed in is valid, and throw appropriate exceptions if it is not
     *                - check that the order id passed in exists in the database or not.
     *                - if the order is found in the database use its status to determine whether it can be updated or not, then proceed accordingly
     *                - e.g. if the order status say that the order has been delivered, the order cannot be updated.
     *                <p>
     *                Request Object: (UpdateOrderRequest)
     *                {
     *                "userID":"8b337604-b0f6-11eb-8529-0242ac130003"
     *                "userID":"8b337604-b0f6-11eb-8529-0242ac130003"
     *                "listOfItems": {{"ProductID":"12345","name":"item1"...}, ...}
     *                "discount": "30.
     *                import java.util.List;50"
     *                "orderType": "OrderType.DELIVERY"
     *                "deliveryAddress": {"geoID":"3847593","latitude":"30.49","longitude":"24.34"}
     *                }
     *                <p>
     *                Response object: (UpdateOrderResponse)
     *                {
     *                success: true // boolean
     *                timestamp: Thu Dec 05 09:29:39 UTC 1996 // Date
     *                message: "Order successfully updated"
     *                order:  // order object
     *                }
     * @return
     * @throws InvalidRequestException
     * @throws OrderDoesNotExist
     * @throws NotAuthorisedException
     */

    @Override
    public UpdateOrderResponse updateOrder(UpdateOrderRequest request) throws InvalidRequestException, OrderDoesNotExist, NotAuthorisedException, URISyntaxException {
        String message;
        Order order;
        double discount = 0;
        double cost;
        Customer customer;

        if (request == null) {
            throw new InvalidRequestException("Invalid order request received - cannot get order.");
        }

        if (request.getOrderID() == null) {
            throw new InvalidRequestException("OrderID cannot be null in request object - cannot get order.");
        }

        CurrentUser currentUser = new CurrentUser();

        String stringUri = "http://" + userHost + ":" + userPort + "/user/getCustomerByEmail";
        URI uri = new URI(stringUri);

        Map<String, Object> parts = new HashMap<String, Object>();
        parts.put("email", currentUser.getEmail());
        ResponseEntity<GetCustomerByEmailResponse> useCaseResponseEntity = restTemplate.postForEntity(
                uri, parts,
                GetCustomerByEmailResponse.class);

        GetCustomerByEmailResponse getCustomerByEmailResponse = useCaseResponseEntity.getBody();
        customer = getCustomerByEmailResponse.getCustomer();
        if (customer == null) {
            throw new InvalidRequestException("Incorrect email email given - customer does not exist");
        }

        GetOrderResponse getOrderResponse;
        getOrderResponse = getOrder(new GetOrderRequest(request.getOrderID()));
        order = getOrderResponse.getOrder();

        if (!customer.getCustomerID().equals(order.getUserID())) {
            throw new NotAuthorisedException("Not Authorised to update an order you did not place.");
        }

        OrderStatus status = order.getStatus();
        // once the order has been paid for and sent to the  shoppers
        // if the order has not yet been delivered, collected and process by the shoppers

        if (status != OrderStatus.AWAITING_COLLECTION &&
                status != OrderStatus.ASSIGNED_DRIVER &&
                status != OrderStatus.CUSTOMER_COLLECTED &&
                status != OrderStatus.DELIVERY_COLLECTED &&
                status != OrderStatus.DELIVERED &&
                status != OrderStatus.PACKING) { // statuses which do not allow for the updating of an order

            if (request.getOrderType() != null) {
                order.setType(request.getOrderType());
            }

            if (request.getDiscount() != 0) {
                discount = request.getDiscount();
            }

            if (request.getListOfItems() != null) {
                order.setCartItems(request.getListOfItems());
                cost = getCost(order.getCartItems());
                order.setTotalCost(cost - discount);
            } // else refer them to cancel order

            if (request.getDiscount() != null) {
                order.setDiscount(request.getDiscount());
            }

            if (request.getDeliveryAddress() != null) {
                order.setDeliveryAddress(request.getDeliveryAddress());
            }

            message = "Order successfully updated.";
        } else {
            message = "Can no longer update the order - UpdateOrder Unsuccessful.";
            return new UpdateOrderResponse(order, false, Calendar.getInstance().getTime(), message);
        }

        orderRepo.save(order);

        return new UpdateOrderResponse(order, true, Calendar.getInstance().getTime(), message);
    }

    @Override
    public GetOrderResponse getOrder(GetOrderRequest request) throws InvalidRequestException, OrderDoesNotExist {
        String message = null;
        Order order;

//        CurrentUser currentUser = new CurrentUser();

        if (request == null) {
            throw new InvalidRequestException("Invalid order request received - cannot get order.");
        }

        if (request.getOrderID() == null) {
            throw new InvalidRequestException("OrderID cannot be null in request object - cannot get order.");
        }

        order = orderRepo.findById(request.getOrderID()).orElse(null);
        //order.setOrderID(request.getOrderID());
        if (order == null) {
            throw new OrderDoesNotExist("Order doesn't exist in database - cannot get order.");
        }

        message = "Order retrieval successful.";
        return new GetOrderResponse(order, true, new Date(), message);
    }

    @Override
    public GetStatusResponse getStatus(GetStatusRequest request) throws PaymentException {
        String message;
        Order order;

        if (request == null) {
            throw new InvalidRequestException("Invalid getStatusRequest received - could not get status.");
        }

        if (request.getOrderID() == null) {
            throw new InvalidRequestException("OrderID cannot be null in request object - could not get status.");
        }

        order = orderRepo.findById(request.getOrderID()).orElse(null);
        if (order == null) {
            throw new OrderDoesNotExist("Order doesn't exist in database - could not get status.");
        }

        if (!(order.getStatus() instanceof OrderStatus)) {
            message = "Order does not have a valid Status";
            return new GetStatusResponse(null, false, new Date(), message);
        }

        message = "Status retrieval successful.";
        return new GetStatusResponse(order.getStatus().name(), true, new Date(), message);
    }

    /**
     * WHAT TO DO: setStatus
     *
     * @param request is used to bring in:
     *                - order - order whose status is to be updated
     *                - orderStatus - the order status that we want the order to be changed to
     *                <p>
     *                setStatus should:
     *                - check that the request object passed in is valid, and throw appropriate exceptions if it is not
     *                - it should then set the status of the order object passed in to that of the orderStatus passed in
     *                <p>
     *                Request Object: (setStatusRequest)
     *                {
     *                <p>
     *                }
     *                <p>
     *                Response object: (setStatusResponse)
     *                {
     *                success: true // boolean
     *                timestamp: Thu Dec 05 09:29:39 UTC 1996 // Date
     *                message: "Order status successfully set"
     *                order:  // order object
     *                <p>
     *                }
     * @return
     * @throws PaymentException
     */
    @Override
    public SetStatusResponse setStatus(SetStatusRequest request) throws PaymentException {
        Order order;
        String message = "Order status successfully set";

        if (request == null) {
            throw new InvalidRequestException("Invalid request received - request cannot be null");
        }

        if (request.getOrderStatus() == null) {
            throw new InvalidRequestException("Invalid request received - order status cannot be null");
        }

        if (request.getOrder() == null) {
            throw new InvalidRequestException("Invalid request received - order object cannot be null");
        }

        order = request.getOrder();
        order.setStatus(request.getOrderStatus());
        orderRepo.save(order);
        return new SetStatusResponse(order, true, Calendar.getInstance().getTime(), message);
    }

    // TRANSACTION IMPLEMENTATION

    @Override
    public CreateTransactionResponse createTransaction(CreateTransactionRequest request) throws PaymentException, InterruptedException {
        if (request == null) {
            throw new InvalidRequestException("Invalid request received - request cannot be null");
        }
        if (request.getOrderID() == null) {
            throw new InvalidRequestException("Invalid request received - orderID cannot be null");
        }
        Thread.sleep(2000);
        Order order = orderRepo.findById(request.getOrderID()).orElse(null);
        if (order == null) {
            throw new OrderDoesNotExist("Order doesn't exist in database - could not create transaction");
        }
        SetStatusRequest setStatusRequest = new SetStatusRequest(order, OrderStatus.VERIFYING);
        SetStatusResponse setStatusResponse = setStatus(setStatusRequest);
        VerifyPaymentRequest verifyPaymentRequest = new VerifyPaymentRequest(setStatusResponse.getOrder().getOrderID());
        VerifyPaymentResponse verifyPaymentResponse = verifyPayment(verifyPaymentRequest);
        CreateTransactionResponse createTransactionResponse;
        if (verifyPaymentResponse.isSuccess()) {
            createTransactionResponse = new CreateTransactionResponse(true, "Transaction successfully created.");
        } else {
            createTransactionResponse = new CreateTransactionResponse(false, "Transaction not created.");
        }
        return createTransactionResponse;
    }

    @Override
    public VerifyPaymentResponse verifyPayment(VerifyPaymentRequest request) throws PaymentException, InterruptedException {
        if (request == null) {
            throw new InvalidRequestException("Invalid request received - request cannot be null");
        }
        if (request.getOrderID() == null) {
            throw new InvalidRequestException("Invalid request received - orderID cannot be null");
        }
        Thread.sleep(3000);
        Order order = orderRepo.findById(request.getOrderID()).orElse(null);
        if (order == null) {
            throw new OrderDoesNotExist("Order doesn't exist in database - could not create transaction");
        }
        SetStatusRequest setStatusRequest = new SetStatusRequest(order, OrderStatus.PURCHASED);
        SetStatusResponse setStatusResponse = setStatus(setStatusRequest);
        Thread.sleep(2000);
        VerifyPaymentResponse verifyPaymentResponse;
        if (setStatusResponse.getSuccess() == true) {
            verifyPaymentResponse = new VerifyPaymentResponse(true, "Payment Successfully verified.");
        } else {
            verifyPaymentResponse = new VerifyPaymentResponse(false, "Payment was not verified.");
        }
        return verifyPaymentResponse;
    }

    @Override
    public ReverseTransactionResponse reverseTransaction(ReverseTransactionRequest request) {
        return null;
    }

    // INVOICE IMPLEMENTATION

    @Override
    public GenerateInvoiceResponse generateInvoice(GenerateInvoiceRequest request) throws InvalidRequestException {
        UUID invoiceID;
        UUID transactionID;
        Order order;
        Invoice invoice;
        Transaction transaction;

        if (request == null) {
            throw new InvalidRequestException("Generate Invoice Request cannot be null - Invoice generation unsuccessful");
        }

        if (request.getTransactionID() == null) {
            throw new InvalidRequestException("Transaction ID cannot be null - Invoice generation unsuccessful");
        }

        if (request.getCustomerID() == null) {
            throw new InvalidRequestException("Customer ID cannot be null - Invoice generation unsuccessful");
        }

        transactionID = request.getTransactionID();

        invoiceID = UUID.randomUUID();

        Optional<Transaction> OptionalTransaction = transactionRepo.findById(transactionID);

        if (OptionalTransaction == null || !OptionalTransaction.isPresent()) {
            throw new InvalidRequestException("Invalid transactionID passed in - transaction does not exist.");
        } else {
            transaction = OptionalTransaction.get();
        }
        order = transaction.getOrder();
        Calendar timestamp = Calendar.getInstance();

        invoice = new Invoice(invoiceID, request.getCustomerID(), timestamp, "", order.getTotalCost(), order.getCartItems());
        invoiceRepo.save(invoice);

        byte[] PDF = PDF(invoiceID, invoice.getDate(), invoice.getDetails(), order.getCartItems(), invoice.getTotalCost());

        // send email
        // notificationService.sendEmail(PDF);

        return new GenerateInvoiceResponse(invoiceID, timestamp, "Invoice successfully generated.");
    }

    @Override
    public GetInvoiceResponse getInvoice(GetInvoiceRequest request) throws InvalidRequestException, NotAuthorisedException {
        UUID invoiceID;
        Invoice invoice;

        if (request == null) {
            throw new InvalidRequestException("Get Invoice Request cannot be null - Invoice retrieval unsuccessful");
        }

        if (request.getInvoiceID() == null) {
            throw new InvalidRequestException("Invoice ID cannot be null - Invoice retrieval unsuccessful.");
        }

        if (request.getUserID() == null) {
            throw new InvalidRequestException("User ID cannot be null - Invoice retrieval unsuccessful.");
        }

        invoiceID = request.getInvoiceID();

        Optional<Invoice> optionalInvoice = invoiceRepo.findById(invoiceID);

        if (optionalInvoice == null || !optionalInvoice.isPresent()) {
            throw new InvalidRequestException("Invalid invoiceID passed in - invoice does not exist.");
        } else {
            invoice = optionalInvoice.get();
        }

        if (!invoice.getCustomerID().equals(request.getUserID())) {
            throw new NotAuthorisedException("Invalid customerID passed in - customer did not place this order.");
        }

        byte[] PDF = PDF(invoiceID, invoice.getDate(), invoice.getDetails(), invoice.getItem(), invoice.getTotalCost());

        // send email
        // notificationService.sendEmail(PDF);

        return new GetInvoiceResponse(invoiceID, PDF, invoice.getDate(), invoice.getDetails());
    }

    @Override
    public GetCustomersActiveOrdersResponse getCustomersActiveOrders(GetCustomersActiveOrdersRequest request) throws InvalidRequestException, OrderDoesNotExist, URISyntaxException {
        GetCustomersActiveOrdersResponse response;
        if (request == null) {
            throw new InvalidRequestException("Get Customers Active Orders Request cannot be null - Retrieval of Order unsuccessful");
        }

        CurrentUser currentUser = new CurrentUser();
        Customer customer = null;

        String stringURI = "http://" + userHost + ":" + userPort + "/user/getCustomerByEmail";
        URI uri = new URI(stringURI);

        Map<String, Object> parts = new HashMap<String, Object>();
        parts.put("email", currentUser.getEmail());
        ResponseEntity<GetCustomerByEmailResponse> useCaseResponseEntity = restTemplate.postForEntity(
                uri, parts,
                GetCustomerByEmailResponse.class);

        GetCustomerByEmailResponse getCustomerByEmailResponse = useCaseResponseEntity.getBody();
        customer = getCustomerByEmailResponse.getCustomer();
        List<Order> orders = orderRepo.findAllByUserID(customer.getCustomerID());
        if (orders == null) {
            throw new OrderDoesNotExist("No Orders found for this user in the database.");
        }
        for (Order o : orders) {
            if (o.getStatus().equals(OrderStatus.CUSTOMER_COLLECTED) || o.getStatus().equals(OrderStatus.DELIVERED)) {
                continue;
            } else {
                response = new GetCustomersActiveOrdersResponse(o.getOrderID(), true, "Order successfully returned to customer.");
                return response;
            }
        }
        response = new GetCustomersActiveOrdersResponse(null, false, "This customer has no active orders.");
        return response;
    }

    @Override
    public void saveOrderToRepo(SaveOrderToRepoRequest request) throws InvalidRequestException {
        if (request == null) {
            throw new InvalidRequestException("Null request object.");
        }
        if (request.getOrder() == null) {
            throw new InvalidRequestException("Null parameters");
        }

        if (orderRepo != null) {
            Order order = request.getOrder();
            order.setOrderID(request.getOrder().getOrderID());
            orderRepo.save(order);
        }

    }

    @Override
    public GetItemsResponse getItems(GetItemsRequest request) throws PaymentException {

        String message = "Items successfully retrieved";
        Order order = null;
        Optional<Order> orderOptional = null;

        if (request == null) {
            throw new InvalidRequestException("GetItemsRequest is null - could not get Items");
        }

        if (request.getOrderID() == null) {
            throw new InvalidRequestException("OrderID attribute is null - could not get Items");
        }

        orderOptional = orderRepo.findById(UUID.fromString(request.getOrderID()));

        if (orderOptional == null || !orderOptional.isPresent()) {
            throw new OrderDoesNotExist("Order with given ID does not exist - could not get Items");
        }

        order = orderOptional.get();

        if (order == null) {
            message = "order is null";
            return new GetItemsResponse(null, false, new Date(), message);
        }

        return new GetItemsResponse(order.getCartItems(), true, new Date(), message);
    }

    @Override
    public GetOrdersResponse getOrders(GetOrdersRequest request) throws PaymentException {

        String message = "Users successfully returned";
        List<Order> orders = new ArrayList<>();

        if (request == null) {
            throw new InvalidRequestException("GetOrders request is null - could not return orders");
        }

        orders.addAll(orderRepo.findAll());

        if (orders.isEmpty()) {
            message = "There no orders";
            return new GetOrdersResponse(orders, true, message, new Date());
        }

        return new GetOrdersResponse(orders, true, message, new Date());
    }

    // Helper
    private double getCost(List<CartItem> items) {
        double cost = 0;

        for (CartItem item : items) {
            cost += item.getPrice() * item.getQuantity();
        }

        return cost;
    }

    public byte[] PDF(UUID invoiceID, Calendar INVOICED_DATE, String DETAILS, List<CartItem> ITEM, double TOTAL_PRICE) {
        String home = System.getProperty("user.home");
        String file_name = home + "/Downloads/Odosla_Invoice_" + invoiceID + ".pdf";
        Document pdf = new Document();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
//            PdfWriter.getInstance(pdf, new FileOutputStream(file_name));
            pdf.open();
            com.itextpdf.text.Font header = FontFactory.getFont(FontFactory.COURIER, 24, Font.BOLD);
            com.itextpdf.text.Font body = FontFactory.getFont(FontFactory.COURIER, 16);
            pdf.add(new Paragraph("This is an invoice from Odosla", header));

            for (CartItem item : ITEM) {
                pdf.add(new Paragraph("Item: " + item.getName(), body));
                pdf.add(new Paragraph("Barcode: " + item.getBarcode(), body));
                pdf.add(new Paragraph("ItemID: " + item.getProductID(), body));
            }

            //pdf.add(new Paragraph("BuyerID: " + BuyerID, body));
            pdf.add(new Paragraph("Date: " + INVOICED_DATE.getTime(), body));
            pdf.add(new Paragraph("Details: " + DETAILS, body));
            pdf.add(new Paragraph("Price: " + TOTAL_PRICE, body));
            //pdf.add(new Paragraph("ShippingID: " + SHIPMENT.getShipmentId(), body));
            pdf.add(new Paragraph("InvoiceID: " + invoiceID, body));
            pdf.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("PDF Error");
        }
        return output.toByteArray();
    }

    @Override
    public GetAllCartItemsResponse getAllCartItems(GetAllCartItemsRequest request) throws InvalidRequestException {
        GetAllCartItemsResponse response = null;

        if (request != null) {

            List<CartItem> items;
            try {
                items = cartItemRepo.findAll();
            } catch (Exception e) {
                throw new InvalidRequestException("No items exist in repository");
            }

            if (items == null) {
                response = new GetAllCartItemsResponse(null, Calendar.getInstance().getTime(), "All items can't be retrieved");
            } else {
                response = new GetAllCartItemsResponse(items, Calendar.getInstance().getTime(), "All items have been retrieved");
            }
        } else {
            throw new InvalidRequestException("The GetAllCartItemsRequest parameter is null - Could not retrieve items");
        }
        return response;
    }

    @Override
    public GetOrderByUUIDResponse getOrderByUUID(GetOrderByUUIDRequest request) throws InvalidRequestException {
        GetOrderByUUIDResponse response = null;
        if (request != null) {

            if (request.getOrderID() == null) {
                throw new InvalidRequestException("OrderID is null in GetOrderByUUIDRequest request - could not return order entity");
            }

            Order order = null;
            try {
                order = orderRepo.findById(request.getOrderID()).orElse(null);
            } catch (NullPointerException e) {
                //Catching nullPointerException from mockito unit test, when(storeRepo.findById(mockito.any())) return null - which will return null pointer exception
            }

            if (order == null) {
                throw new InvalidRequestException("Order with ID does not exist in repository - could not get Order entity");
            }
            response = new GetOrderByUUIDResponse(order, Calendar.getInstance().getTime(), "Order entity with corresponding id was returned");
        } else {
            throw new InvalidRequestException("Order request is null - could not return order entity");
        }
        return response;
    }

    @Override
    public FixOrderProblemResponse fixOrderProblem(FixOrderProblemRequest request) throws InvalidRequestException, URISyntaxException {

        Order order;
        CartItem cartItem;
        List<CartItem> cartItems;
        String message = "Order successfully fixed";

        if (request == null) {
            throw new InvalidRequestException("FixOrderProblem request is null - could not fix order problem");
        }

        if (request.getCartItem() == null) {
            throw new InvalidRequestException("CartItem is null in fixOrderProblem request - could not fix order problem");
        }

        if (request.getCartItem().getCartItemNo() == null) {
            throw new InvalidRequestException("CartItemNo is null in fixOrderProblem request - could not fix order");
        }

        cartItem = cartItemRepo.findById(request.getCartItem().getCartItemNo()).orElse(null);

        if (cartItem == null) {
            message = "Could not find the cart item to replace";
            return new FixOrderProblemResponse(false, message, new Date());
        }

        cartItemRepo.delete(cartItem);

        if (request.getCartItems() != null)
        for (CartItem c : request.getCartItems()) {
            c.setTotalCost(c.getPrice() * c.getQuantity());
            c.setStoreID(cartItem.getStoreID());
            c.setCartItemNo(UUID.randomUUID());
            c.setOrderID(cartItem.getOrderID());
        }

        order = orderRepo.findById(cartItem.getOrderID()).orElse(null);

        if (order == null) {
            message = "Could not find the order the cartItem belongs to";
            return new FixOrderProblemResponse(false, message, new Date());
        }

        cartItems = order.getCartItems();

        if(cartItems.contains(cartItem))
            cartItems.remove(cartItem);

        if (request.getCartItems() != null)
            cartItems.addAll(request.getCartItems());

        order.setCartItems(cartItems);

        orderRepo.save(order);

        RemoveProblemFromRepoRequest removeProblemFromRepoRequest = new RemoveProblemFromRepoRequest(
                order.getOrderID(), cartItem.getBarcode());
        rabbitTemplate.convertAndSend("UserEXCHANGE", "RK_RemoveProblemFromRepo",
                removeProblemFromRepoRequest);

        return new FixOrderProblemResponse(true, message, new Date());
    }

    @Override
    public GetStatusOfMultipleOrdersResponse getStatusOfMultipleOrders(GetStatusOfMultipleOrdersRequest request) throws InvalidRequestException, URISyntaxException {
        if (request == null){
            throw new InvalidRequestException("Null request object.");
        }
        if (request.getDeliveryID() == null){
            throw new InvalidRequestException("Null deliveryID, cannot get status.");
        }
        String stringUri = "http://" + deliveryHost + ":" + deliveryPort + "/delivery/getDeliveryByUUID";
        URI uri = new URI(stringUri);

        Map<String, Object> parts = new HashMap<String, Object>();
        parts.put("deliveryID", request.getDeliveryID().toString());
        ResponseEntity<GetDeliveryByUUIDResponse> getDeliveryByUUIDResponseResponseEntity = restTemplate.postForEntity(uri, parts, GetDeliveryByUUIDResponse.class);
        if (getDeliveryByUUIDResponseResponseEntity == null || getDeliveryByUUIDResponseResponseEntity.getBody() == null){
            throw new InvalidRequestException("Delivery does not exist.");
        }
        System.out.println(getDeliveryByUUIDResponseResponseEntity.getBody().getMessage() + "Get delivery by UUID message");
        Delivery delivery = getDeliveryByUUIDResponseResponseEntity.getBody().getDelivery();
        if (delivery == null){
            throw new InvalidRequestException("Null delivery.");
        }
        Order orderOne = orderRepo.findById(delivery.getOrderIDOne()).orElse(null);
        Order orderTwo = null;
        Order orderThree = null;
        if (delivery.getOrderIDTwo() != null){
            orderTwo = orderRepo.findById(delivery.getOrderIDTwo()).orElse(null);
        }
        if (delivery.getOrderIDThree() != null){
            orderThree = orderRepo.findById(delivery.getOrderIDThree()).orElse(null);
        }
        if (orderOne == null){
            throw new InvalidRequestException("No order added to this delivery.");
        }
        if (orderTwo == null && orderThree == null){
            return new GetStatusOfMultipleOrdersResponse(orderOne.getStatus().name(), true, new Date(), "Order status returned.");
        }
        if (orderThree == null){
            OrderStatus orderOneStatus = orderOne.getStatus();
            OrderStatus orderTwoStatus = orderTwo.getStatus();
            if (orderOneStatus.ordinal() < orderTwoStatus.ordinal()){
                return new GetStatusOfMultipleOrdersResponse(orderOneStatus.name(), true, new Date(), "Order status returned.");
            } else{
                return new GetStatusOfMultipleOrdersResponse(orderTwoStatus.name(), true, new Date(), "Order status returned.");
            }
        }
        OrderStatus orderOneStatus = orderOne.getStatus();
        OrderStatus orderTwoStatus = orderTwo.getStatus();
        OrderStatus orderThreeStatus = orderThree.getStatus();
        if (orderOneStatus.ordinal() < orderTwoStatus.ordinal()){
            if (orderOneStatus.ordinal() < orderThreeStatus.ordinal()){
                return new GetStatusOfMultipleOrdersResponse(orderOneStatus.name(), true, new Date(), "Order status returned.");
            } else {
                return new GetStatusOfMultipleOrdersResponse(orderThreeStatus.name(), true, new Date(), "Order status returned.");
            }
        } else{
            if (orderTwoStatus.ordinal() < orderThreeStatus.ordinal()){
                return new GetStatusOfMultipleOrdersResponse(orderTwoStatus.name(), true, new Date(), "Order status returned.");
            } else {
                return new GetStatusOfMultipleOrdersResponse(orderThreeStatus.name(), true, new Date(), "Order status returned.");
            }
        }
    }
}