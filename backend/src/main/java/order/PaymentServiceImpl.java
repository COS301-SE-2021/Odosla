package order;

import order.dataclass.*;
import order.repos.OrderRepo;
import order.requests.*;
import order.responses.*;
import order.dataclass.GeoPoint;
import order.exceptions.*;
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