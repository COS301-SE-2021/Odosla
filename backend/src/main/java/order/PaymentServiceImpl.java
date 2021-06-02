package order;

import order.dataclass.Order;
import order.dataclass.OrderStatus;
import order.requests.*;
import order.responses.*;

import java.util.ArrayList;
import java.util.List;

public class PaymentServiceImpl implements PaymentService {

    // ORDER IMPLEMENTATION

    @Override
    public AddItemResponse addItem(AddItemRequest request) {
        return null;
    }

    @Override
    public RemoveItemResponse removeItem(RemoveItemRequest request) {
        return null;
    }

    @Override
    public SubmitOrderResponse submitOrder() {
        return null;
    }

    @Override
    public ResetOrderResponse resetOrder() {
        return null;
    }

    @Override
    public CancelOrderResponse cancelOrder(CancelOrderRequest req) {
        // get list of orders somewhere (mock or database)
        List<Order> orders = new ArrayList<Order>();
        Order order = null;
        double cancelationFee = 0;

        //find the order by id
        for (Order o: orders) {
            if(o.getOrderID() == req.getOrderID()){
                order = o;
                break;
            }
        }


        if(order == null || order.getStatus() == OrderStatus.DELIVERED ||
        order.getStatus() == OrderStatus.CUSTOMER_COLLECTED){
            return new CancelOrderResponse(false);
        }

        if(order.getStatus() != OrderStatus.AWAITING_PAYMENT ||
                order.getStatus() != OrderStatus.PURCHASED){
            cancelationFee = 1000;
        }

        // refund customers ordertotal - cancellation fee


        // remove Order from DB.
        orders.remove(order);

        return new CancelOrderResponse(true);
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