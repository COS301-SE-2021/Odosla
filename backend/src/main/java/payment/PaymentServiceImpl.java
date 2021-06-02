package payment;

import payment.dataclass.*;
import payment.requests.*;
import payment.responses.*;
import payment.dataclass.GeoPoint;
import payment.exceptions.*;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class PaymentServiceImpl implements PaymentService {

    // ORDER IMPLEMENTATION
    @Override
    public SubmitOrderResponse submitOrder(SubmitOrderRequest request) throws InvalidRequestException {

        SubmitOrderResponse response = null;
        UUID orderID=UUID.randomUUID();
        AtomicReference<Double> totalCost= new AtomicReference<>((double) 0);

        if (request!=null) {

            /* checking for invalid requests */
            if(request.getUserID()==null){
                throw new InvalidRequestException("UserID cannot be null in request object");
            }
            else if(request.getListOfItems()==null){
                throw new InvalidRequestException("List of items cannot be null in request object");
            }
            else if(request.getDiscount()==null){
                throw new InvalidRequestException("Discount cannot be null in request object");
            }
            else if(request.getStoreID()==null){
                throw new InvalidRequestException("Store ID cannot be null in request object");
            }
            else if (request.getOrderType()==null){
                throw new InvalidRequestException("Order type cannot be null in request object");
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
            UUID shoperperID = UUID.randomUUID();
            //meant to get delivery address from current user logged in - Mock Data
            GeoPoint deliveryAddress = new GeoPoint(2.0, 2.0, "2616 Urban Quarters, Hatfield");
            //meant to get storeAddress from shop order id is - Mock Data
            GeoPoint storeAddress = new GeoPoint(3.0, 3.0, "Woolworthes, Hillcrest Boulevard");
            //Mock Data
            Boolean requiresPharmacy = false;

            OrderType orderType = request.getOrderType();
            double totalC = Double.parseDouble(totalCost.toString());
            Order o = new Order(UUID.randomUUID(), userID, storeID, shoperperID, Calendar.getInstance(), totalC, orderType, OrderStatus.AWAITING_PAYMENT, listOfItems, discount, deliveryAddress, storeAddress, requiresPharmacy);


            if (o != null) {
                System.out.println("Order has been created");
                response = new SubmitOrderResponse(orderID, OrderStatus.AWAITING_PAYMENT,Calendar.getInstance().getTime(), "Order successfully created.");
            }

        }else{
            throw new InvalidRequestException("Invalid order request received - order unsuccessfully created.");
        }
        return response;
    }

    @Override
    public ResetOrderResponse resetOrder() {
        return null;
    }

    @Override
    public CancelOrderResponse cancelOrder() {
        return null;
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