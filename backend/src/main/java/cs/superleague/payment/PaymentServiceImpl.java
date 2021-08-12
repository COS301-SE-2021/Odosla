package cs.superleague.payment;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import cs.superleague.payment.repos.InvoiceRepo;
import cs.superleague.payment.repos.TransactionRepo;
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

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service("paymentServiceImpl")
public class PaymentServiceImpl implements PaymentService {

    private final OrderRepo orderRepo;
    private final InvoiceRepo invoiceRepo;
    private final TransactionRepo transactionRepo;
    private final ShoppingService shoppingService;

    @Autowired
    public PaymentServiceImpl(OrderRepo orderRepo, InvoiceRepo invoiceRepo, TransactionRepo transactionRepo, ShoppingService shoppingService) {
        this.orderRepo = orderRepo;
        this.invoiceRepo = invoiceRepo;
        this.transactionRepo = transactionRepo;
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
     *               - Add up cost of items and subtract total for total order price
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
            UUID storeID=request.getStoreID();

            /* Get total cost of order*/
            AtomicReference<Double> finalTotalCost = totalCost;
            request.getListOfItems().stream().parallel().forEach(item -> {
                int quantity = item.getQuantity();
                double itemPrice = item.getPrice();
                for (int j = 0; j < quantity; j++) {
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
     *   3.3 Lastly if the order has changed to "collected", either by the driver or the customer the order
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
     * }
     *
     * @return
     * @throws InvalidRequestException
     * @throws OrderDoesNotExist
     */

    @Override
    public CancelOrderResponse cancelOrder(CancelOrderRequest req) throws InvalidRequestException, OrderDoesNotExist, NotAuthorisedException {
        CancelOrderResponse response;
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

        if(req != null){
            if (req.getUserID() == null) {
                throw new InvalidRequestException("UserID cannot be null in request object - order unsuccessfully updated.");
            }

            order = getOrder(new GetOrderRequest(req.getOrderID())).getOrder();

            if (!req.getUserID().equals(order.getUserID())) {
                throw new NotAuthorisedException("Not Authorised to update an order you did not place.");
            }

            List<Order> orders = orderRepo.findAll();

            if(order.getStatus() == OrderStatus.DELIVERED ||
                order.getStatus() == OrderStatus.CUSTOMER_COLLECTED ||
                    order.getStatus() == OrderStatus.DELIVERY_COLLECTED){
                message = "Cannot cancel an order that has been delivered/collected.";
                return new CancelOrderResponse(false,Calendar.getInstance().getTime(), message,orders);
            }

            // remove Order from DB.
            orderRepo.delete(order);
            orders = orderRepo.findAll();

            // refund customers order total - cancellation fee
            if(order.getStatus() != OrderStatus.AWAITING_PAYMENT || order.getStatus() != OrderStatus.PURCHASED){
                cancellationFee = 1000;
                message = "Order successfully cancelled. Customer has been charged " + cancellationFee;
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
     *            - check that the order id passed in exists in the database or not.
     *            - if the order is found in the database use its status to determine whether it can be updated or not, then proceed accordingly
     *              - e.g. if the order status say that the order has been delivered, the order cannot be updated.
     *
     * Request Object: (UpdateOrderRequest)
     * {
     *            "userID":"8b337604-b0f6-11eb-8529-0242ac130003"
     *            "userID":"8b337604-b0f6-11eb-8529-0242ac130003"
     *            "listOfItems": {{"ProductID":"12345","name":"item1"...}, ...}
     *            "discount": "30.
import java.util.List;50"
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
     * }
     *
     * @return
     * @throws InvalidRequestException
     * @throws OrderDoesNotExist
     * @throws NotAuthorisedException
     */

    @Override
    public UpdateOrderResponse updateOrder(UpdateOrderRequest request) throws InvalidRequestException, OrderDoesNotExist, NotAuthorisedException {
        String message;
        Order order;
        double discount = 0;
        double cost;

        if(request == null){
            order = getOrder(null).getOrder();
        }else {
            if (request.getUserID() == null) {
                throw new InvalidRequestException("UserID cannot be null in request object - order unsuccessfully updated.");
            }
            order = getOrder(new GetOrderRequest(request.getOrderID())).getOrder();
        }

        if (!request.getUserID().equals(order.getUserID())) {
            throw new NotAuthorisedException("Not Authorised to update an order you did not place.");
        }

        OrderStatus status = order.getStatus();

        // once the order has been paid for and sent to the  shoppers
        // if the order has not yet been delivered, collected and process by the shoppers
        if (status != OrderStatus.AWAITING_COLLECTION &&
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
                order.setItems(request.getListOfItems());
                cost = getCost(order.getItems());
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
    public GetOrderResponse getOrder(GetOrderRequest request) throws InvalidRequestException, OrderDoesNotExist{
        String message = null;
        Order order;

        if(request == null){
            throw new InvalidRequestException("Invalid order request received - cannot get order.");
        }

        if(request.getOrderID() == null){
            throw new InvalidRequestException("OrderID cannot be null in request object - cannot get order.");
        }

        order = orderRepo.findById(request.getOrderID()).orElse(null);
        if(order == null){
            throw new OrderDoesNotExist("Order doesn't exist in database - cannot get order.");
        }

        message = "Order retrieval successful.";
        return new GetOrderResponse(order, true, Calendar.getInstance().getTime(), message);
    }

    @Override
    public GetStatusResponse getStatus(GetStatusRequest request) throws PaymentException{
        String message;
        Order order;

        if(request == null){
            throw new InvalidRequestException("Invalid getStatusRequest received - could not get status.");
        }

        if(request.getOrderID() == null){
            throw new InvalidRequestException("OrderID cannot be null in request object - could not get status.");
        }

        order = orderRepo.findById(request.getOrderID()).orElse(null);
        if(order == null){
            throw new OrderDoesNotExist("Order doesn't exist in database - could not get status.");
        }

        if(!(order.getStatus() instanceof OrderStatus)){
            message = "Order does not have a valid Status";
            return new GetStatusResponse(null, false, new Date(), message);
        }

        message = "Status retrieval successful.";
        return new GetStatusResponse(order.getStatus().name(), true, new Date(), message);
    }

    /** WHAT TO DO: setStatus
     *
     * @param request is used to bring in:
     *            - order - order whose status is to be updated
     *            - orderStatus - the order status that we want the order to be changed to
     *
     * setStatus should:
     *            - check that the request object passed in is valid, and throw appropriate exceptions if it is not
     *            - it should then set the status of the order object passed in to that of the orderStatus passed in
     *
     * Request Object: (setStatusRequest)
     * {
     *
     * }
     *
     * Response object: (setStatusResponse)
     * {
     *    success: true // boolean
     *    timestamp: Thu Dec 05 09:29:39 UTC 1996 // Date
     *    message: "Order status successfully set"
     *    order:  // order object
     *
     * }
     *
     * @return
     * @throws PaymentException
     */
    @Override
    public SetStatusResponse setStatus(SetStatusRequest request) throws PaymentException{
        Order order;
        String message = "Order status successfully set";

        if(request == null){
            throw new InvalidRequestException("Invalid request received - request cannot be null");
        }

        if(request.getOrderStatus() == null){
            throw new InvalidRequestException("Invalid request received - order status cannot be null");
        }

        if(request.getOrder() == null){
            throw new InvalidRequestException("Invalid request received - order object cannot be null");
        }

        order = request.getOrder();
        order.setStatus(request.getOrderStatus());
        orderRepo.save(order);
        return new SetStatusResponse(order, true, Calendar.getInstance().getTime(), message);
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
    public GenerateInvoiceResponse generateInvoice(GenerateInvoiceRequest request)  throws InvalidRequestException{
        UUID invoiceID;
        UUID transactionID;
        Order order;
        Invoice invoice;
        Transaction transaction;

        if(request == null){
            throw new InvalidRequestException("Generate Invoice Request cannot be null - Invoice generation unsuccessful");
        }

        if(request.getTransactionID() == null){
            throw new InvalidRequestException("Transaction ID cannot be null - Invoice generation unsuccessful");
        }

        if(request.getCustomerID() == null){
            throw new InvalidRequestException("Customer ID cannot be null - Invoice generation unsuccessful");
        }

        transactionID = request.getTransactionID();

        invoiceID = UUID.randomUUID();

        Optional<Transaction> OptionalTransaction = transactionRepo.findById(transactionID);

        if(OptionalTransaction == null || !OptionalTransaction.isPresent()) {
            throw new InvalidRequestException("Invalid transactionID passed in - transaction does not exist.");
        }else {
            transaction = OptionalTransaction.get();
        }
        order = transaction.getOrder();
        Calendar timestamp = Calendar.getInstance();

        invoice = new Invoice(invoiceID, request.getCustomerID(), timestamp, "", order.getTotalCost(), order.getItems());
        invoiceRepo.save(invoice);

        byte[] PDF = PDF(invoiceID, invoice.getDate(), invoice.getDetails(), order.getItems(), invoice.getTotalCost());

        // send email
        // notificationService.sendEmail(PDF);

        return new GenerateInvoiceResponse(invoiceID, timestamp, "Invoice successfully generated.");
    }

    @Override
    public GetInvoiceResponse getInvoice(GetInvoiceRequest request) throws InvalidRequestException, NotAuthorisedException{
        UUID invoiceID;
        Invoice invoice;

        if(request == null){
            throw new InvalidRequestException("Get Invoice Request cannot be null - Invoice retrieval unsuccessful");
        }

        if(request.getInvoiceID() == null){
            throw new InvalidRequestException("Invoice ID cannot be null - Invoice retrieval unsuccessful.");
        }

        if(request.getUserID() == null){
            throw new InvalidRequestException("User ID cannot be null - Invoice retrieval unsuccessful.");
        }

        invoiceID = request.getInvoiceID();

        Optional<Invoice> optionalInvoice = invoiceRepo.findById(invoiceID);

        if(optionalInvoice == null || !optionalInvoice.isPresent()){
            throw new InvalidRequestException("Invalid invoiceID passed in - invoice does not exist.");
        }else{
            invoice = optionalInvoice.get();
        }

        if(!invoice.getCustomerID().equals(request.getUserID())){
            throw new NotAuthorisedException("Invalid customerID passed in - customer did not place this order.");
        }

        byte[] PDF = PDF(invoiceID, invoice.getDate(), invoice.getDetails(), invoice.getItem(), invoice.getTotalCost());

        // send email
        // notificationService.sendEmail(PDF);

        return new GetInvoiceResponse(invoiceID, PDF, invoice.getDate(), invoice.getDetails());
    }

    // Helper
    private double getCost(List<Item> items){
        double cost = 0;

        for (Item item : items) {
            cost += item.getPrice() * item.getQuantity();
        }

        return cost;
    }

    public byte[] PDF(UUID invoiceID, Calendar INVOICED_DATE, String DETAILS, List<Item> ITEM, double TOTAL_PRICE) {
        String home = System.getProperty("user.home");
        String file_name = home + "/Downloads/Odosla_Invoice_" + invoiceID + ".pdf";
        Document pdf = new Document();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(pdf, new FileOutputStream(file_name));
            pdf.open();
            com.itextpdf.text.Font header = FontFactory.getFont(FontFactory.COURIER, 24, Font.BOLD);
            com.itextpdf.text.Font body = FontFactory.getFont(FontFactory.COURIER, 16);
            pdf.add(new Paragraph("This is an invoice from Odosla", header));

            for (Item item: ITEM) {
                pdf.add(new Paragraph("Item: " + item.getName(), body));
                pdf.add(new Paragraph("Barcode: " + item.getBarcode(), body));
                pdf.add(new Paragraph("ItemID: " + item.getProductID(), body));
            }

            //pdf.add(new Paragraph("BuyerID: " + BuyerID, body));
            pdf.add(new Paragraph("Date: " + INVOICED_DATE, body));
            pdf.add(new Paragraph("Details: " + DETAILS, body));
            pdf.add(new Paragraph("Price: " + TOTAL_PRICE, body));
            //pdf.add(new Paragraph("ShippingID: " + SHIPMENT.getShipmentId(), body));
            pdf.add(new Paragraph("InvoiceID: " + invoiceID, body));
            pdf.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            System.out.println("PDF Error");
        }
        return output.toByteArray();
    }
}