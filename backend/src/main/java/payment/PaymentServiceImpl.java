package payment;
import payment.dataclass.*;
import payment.dataclass.Order;
import payment.dataclass.OrderStatus;
import payment.mock.CancelOrdersMock;
import payment.repos.OrderRepo;
import payment.requests.*;
import payment.responses.*;
import payment.dataclass.GeoPoint;
import payment.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service("paymentServiceImpl")
public class PaymentServiceImpl implements PaymentService {

    private final OrderRepo orderRepo;

    @Autowired
    public PaymentServiceImpl(OrderRepo orderRepo) {
        this.orderRepo = orderRepo;
    }


    // ORDER IMPLEMENTATION
    @Override
    public SubmitOrderResponse submitOrder(SubmitOrderRequest request) throws InvalidRequestException {

        SubmitOrderResponse response = null;
        UUID orderID=UUID.randomUUID();
        AtomicReference<Double> totalCost= new AtomicReference<>((double) 0);

        if (request!=null) {

            /* checking for invalid requests */
            if(request.getUserID()==null){
                throw new InvalidRequestException("UserID cannot be null in request object - order unsuccessfully created.");
            }
            else if(request.getListOfItems()==null){
                throw new InvalidRequestException("List of items cannot be null in request object - order unsuccessfully created.");
            }
            else if(request.getDiscount()==null){
                throw new InvalidRequestException("Discount cannot be null in request object - order unsuccessfully created.");
            }
            else if(request.getStoreID()==null){
                throw new InvalidRequestException("Store ID cannot be null in request object - order unsuccessfully created.");
            }
            else if(request.getOrderType()==null){
                throw new InvalidRequestException("Order type cannot be null in request object - order unsuccessfully created.");
            }
            else if(request.getDeliveryAddress()==null){
                throw new InvalidRequestException("Delivery Address GeoPoint cannot be null in request object - order unsuccessfully created.");
            }
            else if (request.getStoreAddress()==null){
                throw new InvalidRequestException("Store Address GeoPoint cannot be null in request object - order unsuccessfully created.");
            }


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
                    finalTotalCost.updateAndGet(v -> new Double((double) (v + itemPrice)));
                }
            });

            /* Take off discount */
            finalTotalCost.updateAndGet(v -> new Double((double) (v - discount)));

            //meant to use assign order request in shop - Mock Data
            UUID shopperID = null;

            GeoPoint deliveryAddress = request.getDeliveryAddress();
            GeoPoint storeAddress = request.getStoreAddress();
            //Mock Data - still have to find out how this is going to work
            Boolean requiresPharmacy = false;

            OrderType orderType = request.getOrderType();
            double totalC = Double.parseDouble(totalCost.toString());
            Order o = new Order(orderID, userID, storeID, shopperID, Calendar.getInstance(), totalC, orderType,OrderStatus.AWAITING_PAYMENT,listOfItems, discount, deliveryAddress, storeAddress, requiresPharmacy);


            if (o != null) {
                orderRepo.save(o);
                System.out.println("Order has been created");
                response = new SubmitOrderResponse(orderID, OrderStatus.AWAITING_PAYMENT,Calendar.getInstance().getTime(), "Order successfully created.");
            }

        }else{
            throw new InvalidRequestException("Invalid submit order request received - order unsuccessfully created.");
        }
        return response;
    }


    @Override
    public CancelOrderResponse cancelOrder(CancelOrderRequest req) {
        List<Order> orders = new CancelOrdersMock().getOrders(); // temp placeholder
        Order order = null;
        double cancelationFee = 0;
        String message;

        /*
         *
         * AWAITING_PAYMENT - cancel
         * PURCHASED - cancel
         * COLLECT - charge
         * DELIVERY_COLLECTED
         * CUSTOMER_COLLECTED
         * DELIVERED
         *
         * */

        if(req == null){
            message = "request object cannot be null";
            return new CancelOrderResponse(false, orders, message);
        }

        //find the order by id
        for (Order o: orders) {
            if(o.getOrderID() == req.getOrderID()){
                order = o;
                break;
            }
        }

        if(order == null){
            message = "An order with id: " + req.getOrderID() + " does not exist";
            return new CancelOrderResponse(false, orders, message);
        }


        if(order.getStatus() == OrderStatus.DELIVERED ||
                order.getStatus() == OrderStatus.CUSTOMER_COLLECTED ||
                order.getStatus() == OrderStatus.DELIVERY_COLLECTED){
            message = "Cannot cancel an order that has been delivered/collected.";
            return new CancelOrderResponse(false, orders, message);
        }

        if(order.getStatus() != OrderStatus.AWAITING_PAYMENT ||
                order.getStatus() != OrderStatus.PURCHASED){
            cancelationFee = 1000;
            message = "Order successfully cancelled. Customer has been charged " + cancelationFee;
        }else
            message = "Order has been successfully cancelled";

        // refund customers ordertotal - cancellation fee


        // remove Order from DB.
        orders.remove(order);

        return new CancelOrderResponse(true, orders, message);
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