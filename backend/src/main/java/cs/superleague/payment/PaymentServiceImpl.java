package cs.superleague.payment;

import cs.superleague.shopping.ShoppingService;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.payment.dataclass.*;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.dataclass.OrderStatus;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.payment.requests.*;
import cs.superleague.payment.responses.*;
import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.payment.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cs.superleague.shopping.exceptions.StoreClosedException;
import cs.superleague.shopping.exceptions.StoreDoesNotExistException;
import cs.superleague.shopping.requests.GetStoreByUUIDRequest;
import cs.superleague.shopping.responses.GetStoreByUUIDResponse;

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
     *                - userID - the cs.superleague.user ID creating the order
     *                - list of items - items order should hold
     *                - discount - discount for the order if applicable, if not the discount will be 0
     *                - storeID - the Store the order is being created with
     *                - OrderType - whether the order is for collection or cs.superleague.delivery
     *                - DeliveryAddress - this is the address the order will be delivered to, the address of cs.superleague.user
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
    public SubmitOrderResponse submitOrder(SubmitOrderRequest request) throws PaymentException, cs.superleague.shopping.exceptions.InvalidRequestException, StoreDoesNotExistException, StoreClosedException {

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
            UUID userID=request.getUserID();
            UUID storeID=request.getStoreID();

            /* Get total cost of order*/
            AtomicReference<Double> finalTotalCost = totalCost;
            request.getListOfItems().stream().parallel().forEach(item -> {
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


            //Mock Data - still have to find out how this is going to work
            Boolean requiresPharmacy = false;

            OrderType orderType = request.getOrderType();

            BigDecimal bd = BigDecimal.valueOf(Double.parseDouble(totalCost.toString()));
            bd = bd.setScale(2, RoundingMode.HALF_UP);
            double totalC=bd.doubleValue();

            Order o = new Order(orderID, request.getUserID(), request.getStoreID(), shopperID, Calendar.getInstance(), null, totalC, orderType,OrderStatus.AWAITING_PAYMENT,request.getListOfItems(), request.getDiscount(), request.getDeliveryAddress(), request.getStoreAddress(), requiresPharmacy);

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

    /** WHAT TO DO: updateOrder
     *
     * @param request is used to bring in:
     *            - userID - the user ID of person who placed the order
     *            - orderID - the unique identifier of the order placed
     *            - listOfItems - list of items in the order of object type Item
     *            - discount - the amount of the discount
     *            - orderType - the type of order it is, whether it is a delivery or collection
     *            - deliveryAddress - the GeoPoint address of the store where order is going to be delivered to.
     *
     * updateOrder should:
     *            - check that the request object passed in is valid, and throw appropriate exceptions if it is not
     *            - check that the order id passed in in exists in the database or not.
     *            - if the order is found in the database use its status to determine whether it can be updated or not, then proceed accordingly
     *              - e.g if the order status say that the order has been delivered, the order cannot be updated.
     *
     * Request Object: (UpdateOrderRequest)
     * {
     *            "userID":"8b337604-b0f6-11eb-8529-0242ac130003"
     *            "userID":"8b337604-b0f6-11eb-8529-0242ac130003"
     *            "listOfItems": {{"ProductID":"12345","name":"item1"...}, ...}
     *            "discount": "30.50"
     *            "orderType": "OrderType.DELIVERY"
     *            "deliveryAddress": {"geoID":"3847593","latitude":"30.49","longitude":"24.34"}
     * }
     *
     * Response object: (UpdateOrderResponse)
     * {
     *    success: true // boolean
     *    timestamp: Thu Dec 05 09:29:39 UTC 1996 // Date
     *    message: "Order successfully updated"
     *    order:  // order object
     *
     * }
     *
     * @return
     * @throws InvalidRequestException
     * @throws OrderDoesNotExist
     * @throws NotAuthorisedException
     */

    @Override
    public UpdateOrderResponse updateOrder(UpdateOrderRequest request) throws InvalidRequestException, OrderDoesNotExist, NotAuthorisedException{
        String message = null;
        Order order = null;
        double discount = 0;
        double cost = 0;

        if(request == null){
            throw new InvalidRequestException("Invalid update order request received - cannot get order.");
        }

        if(request.getOrderID() == null){
            throw new InvalidRequestException("OrderID cannot be null in request object - cannot get order.");
        }

        if(request.getUserID() == null){
            throw new InvalidRequestException("UserID cannot be null in request object - order unsuccessfully updated.");
        }

        order = orderRepo.findById(request.getOrderID()).orElse(null);
        if(order == null){
            throw new OrderDoesNotExist("Order doesn't exist in database - cannot get order.");
        }

        if(request.getUserID() != order.getUserID()){
            throw new NotAuthorisedException("Not Authorised to update an order you did not place.");
        }

        OrderStatus status = order.getStatus();

        // once the order has been paid for and sent to the  shoppers
        // if the order has not yet been delivered, collected and process by the shoppers
        if(status != OrderStatus.AWAITING_COLLECTION &&
                status != OrderStatus.CUSTOMER_COLLECTED &&
                status != OrderStatus.DELIVERY_COLLECTED &&
                status != OrderStatus.DELIVERED &&
                status != OrderStatus.PACKING){ // statuses which do not allow for the updating of an order


            if(request.getDiscount() != 0){
                discount = request.getDiscount();
            }

            if(request.getListOfItems() != null){
                order.setItems(request.getListOfItems());

                for (Item item : order.getItems()) {
                    cost += item.getPrice();
                }

                order.setTotalCost(cost - discount);
            } // else refer them to cancel order

            if(request.getDiscount() != null){
                order.setDiscount(request.getDiscount());
            }

            if(request.getDeliveryAddress() != null){
                order.setDeliveryAddress(request.getDeliveryAddress());
            }

        }else{
            message = "Can no longer update the order - UpdateOrder Unsuccessful.";
            return new UpdateOrderResponse(order, false, Calendar.getInstance().getTime(), message );
        }

        orderRepo.save(order);
        // order has not been processed by shopper
        // nor has it been accepted by driver. customer can change type
        if(status == OrderStatus.AWAITING_PAYMENT){
            if(request.getOrderType() != null) {
                order.setType(request.getOrderType());
            }

            message = "Order successfully updated.";
        }else {
            message = "Delivery address and OrderType could not be updated. Other details updated successfully.";
        }

        return new UpdateOrderResponse(order, true, Calendar.getInstance().getTime(), message);
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