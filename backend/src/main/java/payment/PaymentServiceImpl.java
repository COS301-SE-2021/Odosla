package payment;

import shopping.ShoppingService;
import shopping.dataclass.Item;
import payment.dataclass.*;
import payment.dataclass.Order;
import payment.dataclass.OrderStatus;
import payment.repos.OrderRepo;
import payment.requests.*;
import payment.responses.*;
import payment.dataclass.GeoPoint;
import payment.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shopping.exceptions.StoreClosedException;
import shopping.exceptions.StoreDoesNotExistException;
import shopping.requests.GetStoreByUUIDRequest;
import shopping.responses.GetStoreByUUIDResponse;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service("paymentServiceImpl")
public class PaymentServiceImpl implements PaymentService {

    private final OrderRepo orderRepo;
    private final ShoppingService shoppingService;

    @Autowired
    public PaymentServiceImpl(OrderRepo orderRepo, ShoppingService shoppingService) {
        this.orderRepo = orderRepo;
        this.shoppingService = shoppingService;
    }

    /** What to do
     *
     * @param request is used to bring in:
     *                - userID - the user ID creating the order
     *                - list of items - items order should hold
     *                - discount - discount for the order if applicable, if not the discount will be 0
     *                - storeID - the Store the order is being created with
     *                - OrderType - whether the order is for collection or delivery
     *                - DeliveryAddress - this is the address the order will be delivered to, the address of user
     *                - StoreAddress - address of store the order is created at
     *
     * SubmitOrder should do the following:
     *               - Check the request object is not null and all corresponding parameters else throw InvalidRequestException
     *               - Add up cost of items and subract total for total order price
     *               - If order with same ID already exists in database, then return object without success
     *               - If the order is created successfully, then order gets added to order repository and success
     *
     * Request object (SubmitOrderRequest)
     * {
     *                "userID":"8b337604-b0f6-11eb-8529-0242ac130003"
     *                "listOfItems": {{"ProductID":"12345","name":"item1"...}, ...}
     *                "discount": "30.50"
     *                "storeID": "8b337604-b0f6-11eb-8529-0242ac130023"
     *                "orderType": "OrderType.DELIVERY"
     *                "DeliveryAddress": {"geoID":"3847593","latitude":"30.49","longitude":"24.34"}
     *                "StoreAddress": {"geoID":"3847593","latitude":"30.49","longitude":"24.34"}
     * }
     *
     * Response object (SubmitOrderResponse)
     * {
     *    success: true // boolean
     *    timestamp: Thu Dec 05 09:29:39 UTC 1996 // Date
     *    message: "Order created successfully"
     *    order:  order / orderObject
     *
     * }
     * @return
     * @throws PaymentException
     * @throws InvalidRequestException
     */
    @Override
    public SubmitOrderResponse submitOrder(SubmitOrderRequest request) throws PaymentException, shopping.exceptions.InvalidRequestException, StoreDoesNotExistException, StoreClosedException {

        SubmitOrderResponse response = null;
        UUID orderID=UUID.randomUUID();
        AtomicReference<Double> totalCost= new AtomicReference<>((double) 0);

        boolean invalidReq = false;
        String invalidMessage = "";

        if (request!=null) {

            /* checking for invalid requests */
            if(request.getUserID()==null){
                invalidReq = true;
                invalidMessage = ("UserID cannot be null in request object - order unsuccessfully created.");
            }

            else if(request.getListOfItems()==null){
                invalidReq = true;
                invalidMessage = ("List of items cannot be null in request object - order unsuccessfully created.");
            }

            else if(request.getDiscount()==null){
                invalidReq = true;
                invalidMessage = ("Discount cannot be null in request object - order unsuccessfully created.");
            }

            else if(request.getStoreID()==null){
                invalidReq = true;
                invalidMessage = ("Store ID cannot be null in request object - order unsuccessfully created.");
            }

            else if(request.getOrderType()==null){
                invalidReq = true;
                invalidMessage = ("Order type cannot be null in request object - order unsuccessfully created.");
            }

            else if(request.getDeliveryAddress()==null){
                invalidReq = true;
                invalidMessage = ("Delivery Address GeoPoint cannot be null in request object - order unsuccessfully created.");
            }

            else if (request.getStoreAddress()==null){
                invalidReq = true;
                invalidMessage = ("Store Address GeoPoint cannot be null in request object - order unsuccessfully created.");
            }

            if (invalidReq) throw new InvalidRequestException(invalidMessage);


            double discount=request.getDiscount();
            List<Item> listOfItems=request.getListOfItems();
            UUID userID=request.getUserID();
            UUID storeID=request.getStoreID();

            /* Get total cost of order*/
            AtomicReference<Double> finalTotalCost = totalCost;
            listOfItems.stream().parallel().forEach(item -> {
                int quantiy = item.getQuantity();
                double itemPrice = item.getPrice();
                for (int j = 0; j < quantiy; j++) {
                    finalTotalCost.updateAndGet(v ->((double) (v + itemPrice)));
                }
            });

            /* Take off discount */
            finalTotalCost.updateAndGet(v -> ((double) (v - discount)));

            //meant to use assign order request in shop - Mock Data
            UUID shopperID = null;

            GeoPoint deliveryAddress = request.getDeliveryAddress();
            GeoPoint storeAddress = request.getStoreAddress();
            //Mock Data - still have to find out how this is going to work
            Boolean requiresPharmacy = false;

            OrderType orderType = request.getOrderType();

            BigDecimal bd = BigDecimal.valueOf(Double.parseDouble(totalCost.toString()));
            bd = bd.setScale(2, RoundingMode.HALF_UP);
            double totalC=bd.doubleValue();

            Order o = new Order(orderID, userID, storeID, shopperID, Calendar.getInstance(), null, totalC, orderType,OrderStatus.AWAITING_PAYMENT,listOfItems, discount, deliveryAddress, storeAddress, requiresPharmacy);

            Order alreadyExists=null;
            while (true) {
                try {
                    alreadyExists = orderRepo.findById(orderID).orElse(null);
                }
                catch (Exception e){}

                if(alreadyExists != null){
                    orderID=UUID.randomUUID();
                }
                else{
                    break;
                }
            }
            if (o != null) {
                GetStoreByUUIDRequest getShopRequest=new GetStoreByUUIDRequest(storeID);
                GetStoreByUUIDResponse shop=shoppingService.getStoreByUUID(getShopRequest);
                if (shop != null) {
                    if(shop.getStore().getOpen()==true) {
                        orderRepo.save(o);
                        System.out.println("Order has been created");
                        response = new SubmitOrderResponse(o, true, Calendar.getInstance().getTime(), "Order successfully created.");
                    }
                    else{
                        throw new StoreClosedException("Store is currently closed - could not create order");
                    }
                }
            }

        }else{
            throw new InvalidRequestException("Invalid submit order request received - order unsuccessfully created.");
        }
        return response;
    }

    /** WHAT TO DO: cancelOrder
     *
     * @param //request is used to bring in:
     *-   private UUID orderID // The order ID for the order that is to be cancelled
     *
     *
     * cancelOrder should: This function allows for an order to be cancelled based on the status of the particular order
     *   1.Check that the request object and the orderID are not null, if they are an invalidOrderRequest is thrown\
     *   2. Check if the orderID passed in exists in the DB if it does not, the OrderDoesNotExistException is thrown
     *   3. Checks the order status of the order
     *   3.1 if the order status is AWAITING_PAYMENT or PURCHASED the order can easily be cancelled without
     *       charging the customer.
     *   3.2 Once an order has reached the COLLECT status the customer will be charged a fee to cancel the order
     *   3.3 Lastly if the order has changed to collected, either by the driver or the customer the order
     *      can no longer be cancelled
     *
     * Request Object: (CancelOrderRequest)
     * {
     *    "orderID":"8b337604-b0f6-11eb-8529-0242ac130003"
     * }
     *
     * Response object: (CancelOrderResponse)
     * {
     *    success: true // boolean
     *    timestamp: Thu Dec 05 09:29:39 UTC 1996 // Date
     *    message: "Cannot cancel an order that has been delivered/collected."
     *    orders:  //List<Orders>
     *
     * }
     *
     * @return
     * @throws InvalidRequestException
     * @throws OrderDoesNotExist
     */

    @Override
    public CancelOrderResponse cancelOrder(CancelOrderRequest req) throws InvalidRequestException, OrderDoesNotExist {
        CancelOrderResponse response=null;
        Order order = null;
        double cancelationFee = 0;
        String message;

        /**
         * AWAITING_PAYMENT - cancel
         * PURCHASED - cancel
         * COLLECT - charge
         * DELIVERY_COLLECTED
         * CUSTOMER_COLLECTED
         * DELIVERED
         */

        if(req != null){

            if(req.getOrderID()==null){
                throw new InvalidRequestException("orderID in request object can't be null - couldn't cancel order ");
            }
            Order o=null;
            //find the order by id
            try {
                o=orderRepo.findById(req.getOrderID()).orElse(null);
            }
            catch (Exception e){
                throw new OrderDoesNotExist("Order doesn't exist in database - can't cancel order");
            }

            List<Order> orders=new ArrayList<>();
            orders=orderRepo.findAll();

            if(o.getStatus() == OrderStatus.DELIVERED || o.getStatus() == OrderStatus.CUSTOMER_COLLECTED || o.getStatus() == OrderStatus.DELIVERY_COLLECTED){
                message = "Cannot cancel an order that has been delivered/collected.";
                return new CancelOrderResponse(false,Calendar.getInstance().getTime(), message,orders);
            }

            // remove Order from DB.
            orders.remove(o);

            // refund customers ordertotal - cancellation fee
            if(o.getStatus() != OrderStatus.AWAITING_PAYMENT || o.getStatus() != OrderStatus.PURCHASED){
                cancelationFee = 1000;
                message = "Order successfully cancelled. Customer has been charged " + cancelationFee;
            }else {
                message = "Order has been successfully cancelled";
            }

            response= new CancelOrderResponse(true,Calendar.getInstance().getTime(), message,orders);
        }
        else{

            throw new InvalidRequestException("request object cannot be null - couldn't cancel order");
        }
        return response;
    }

    // TRANSACTION IMPLEMENTATION

    @Override
    public CreateTransactionResponse createTransaction(CreateTransactionRequest request) {
        return null;
    }

    @Override
    public VerifyPaymentResponse verifyPayment(VerifyPaymentRequest request) {
        return null;
    }

    @Override
    public ReverseTransactionResponse reverseTransaction(ReverseTransactionRequest request) {
        return null;
    }

    // INVOICE IMPLEMENTATION

    @Override
    public GenerateInvoiceResponse generateInvoice(GenerateInvoiceRequest request) {
        return null;
    }

    @Override
    public GetInvoiceResponse getInvoice(GetInvoiceRequest request) {
        return null;
    }
}